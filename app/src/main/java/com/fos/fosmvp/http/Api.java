package com.fos.fosmvp.http;


import android.text.TextUtils;

import com.fos.fosmvp.base.BaseApplication;
import com.fos.fosmvp.start.FosMvpManager;
import com.fos.fosmvp.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * retorfit api
 *
 */
public class Api {
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 60 * 1000;
    //连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 60 * 1000;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    private static volatile Retrofit retrofit;

    private static CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static <T> T createApi(Class<T> paramClass) {
        return retrofit.create(paramClass);
    }

    public static void initialize() {
        initRetrofit(FosMvpManager.PREFIX_URL);
    }

    private static void initRetrofit(String baseUrl) {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            retrofit = new Retrofit
                    .Builder()
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
        }
    }

    public static Observable initObservable(Observable observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static void addSubscriber(Subscription subscription) {
        if ((subscription != null) && (mCompositeSubscription != null))
            mCompositeSubscription.add(subscription);
    }

    public void toSubscribe(Observable observable, Observer subscriber) {
        addSubscriber(observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public static void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 创建OkHttpClient
     *
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                })
                .build();
        return getTrustAllSSLClient(client);
    }


    /**
     * 拦截器
     */
    private static final Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request req = chain.request();
            String cacheControl = req.cacheControl().toString();
            //获取header参数
            String lock = req.header("lock");
            //TODO 对请求参数进行加密处理

            if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                req = req.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(req);

            if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                //TODO 只要加密的请求需要解密

                return  originalResponse;
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }

    };



    private static OkHttpClient getTrustAllSSLClient(OkHttpClient client) {
        client = OkHttpClientUtil.getTrustAllSSLClient(client);
        return client;
    }

    /**
     * 上传文件时使用
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("multipartFiles", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }
}
