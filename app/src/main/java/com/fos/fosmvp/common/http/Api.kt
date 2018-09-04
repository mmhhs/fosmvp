package com.fos.fosmvp.common.http


import android.text.TextUtils
import com.fos.fosmvp.common.base.BaseApplication
import com.fos.fosmvp.common.utils.LogUtils
import com.fos.fosmvp.common.utils.NetWorkUtils
import com.fos.fosmvp.start.FosMvpManager
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * retorfit api
 *
 */
object Api {
    //读超时长，单位：毫秒
    val READ_TIME_OUT = 60 * 1000
    //连接时长，单位：毫秒
    val CONNECT_TIME_OUT = 60 * 1000
    /**
     * 设缓存有效期为两天
     */
    private val CACHE_STALE_SEC = (60 * 60 * 24 * 2).toLong()

    @Volatile private var retrofit: Retrofit? = null

    /**
     * 创建OkHttpClient
     *
     * @return
     */
    private val okHttpClient: OkHttpClient
        get() {
            val client = OkHttpClient.Builder()
                    .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                    .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
                    .addInterceptor(interceptor)
                    .hostnameVerifier { s, sslSession -> true }
                    .build()
            return getTrustAllSSLClient(client)
        }


    /**
     * 拦截器
     */
    private val interceptor = Interceptor { chain ->
        var req = chain.request()
        val cacheControl = req.cacheControl().toString()
        //获取header参数
        val lock = req.header("lock")
        //TODO 对请求参数进行加密处理

        if (!NetWorkUtils.isNetConnected(BaseApplication.appContext!!)) {
            req = req.newBuilder()
                    .cacheControl(if (TextUtils.isEmpty(cacheControl)) CacheControl.FORCE_NETWORK else CacheControl.FORCE_CACHE)
                    .build()
        }
        LogUtils.e("req= " + req.toString())
        val originalResponse = chain.proceed(req)
        LogUtils.e("response= " + originalResponse.body()!!.string())
        if (NetWorkUtils.isNetConnected(BaseApplication.appContext!!)) {
            //TODO 只要加密的请求需要解密

            originalResponse
        } else {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build()
        }
    }


    fun <T> createApi(paramClass: Class<T>): T {
        return retrofit!!.create(paramClass)
    }

    fun initialize() {
        initRetrofit(FosMvpManager.getPrefixUrl())
    }

    private fun initRetrofit(baseUrl: String) {
        if (retrofit == null) {
            val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create()
            retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
        }
    }


    private fun getTrustAllSSLClient(client: OkHttpClient): OkHttpClient {
        var client = client
        client = OkHttpClientUtil.getTrustAllSSLClient(client)
        return client
    }

    fun initObservable(observable: Observable<*>): Observable<*> {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 上传文件时使用
     * @param files
     * @return
     */
    fun filesToMultipartBodyParts(files: List<File>): List<MultipartBody.Part> {
        val parts = ArrayList<MultipartBody.Part>(files.size)
        for (file in files) {
            val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val part = MultipartBody.Part.createFormData("multipartFiles", file.name, requestBody)
            parts.add(part)
        }
        return parts
    }
}
