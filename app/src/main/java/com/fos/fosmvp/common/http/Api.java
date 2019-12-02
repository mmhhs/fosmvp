package com.fos.fosmvp.common.http;


import android.text.TextUtils;

import com.fos.fosmvp.common.base.BaseApplication;
import com.fos.fosmvp.common.utils.LogUtils;
import com.fos.fosmvp.common.utils.NetWorkUtils;
import com.fos.fosmvp.common.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * retorfit api
 *
 */
public class Api {
    /** 读超时长，单位：毫秒 */
    public static int READ_TIME_OUT = 60 * 1000;
    /** 连接时长，单位：毫秒 */
    public static int CONNECT_TIME_OUT = 60 * 1000;
    /** 设缓存有效期为两天 */
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    private static volatile Retrofit retrofit;
    /** 加解密监听 */
    public static EncryptListener encryptListener;
    /** json key */
    public static String jsonKey = "param";


    public static <T> T createApi(Class<T> paramClass) {
        return retrofit.create(paramClass);
    }

    public static void initialize(String prefixUrl) {
        initRetrofit(prefixUrl);
    }

    private static void initRetrofit(String baseUrl) {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .setLenient()// json宽松
                    .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                    .serializeNulls() //智能null
                    .setPrettyPrinting()// 调教格式
                    .disableHtmlEscaping() //默认是GSON把HTML 转义的
                    .create();
            retrofit = new Retrofit
                    .Builder()
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
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
            RequestBody requestBody = req.body();
            Map<String, Object> formMap = new HashMap<>();
            //获取header参数
            String needEncrypt = req.header("encrypt");
            String needFormatGson = req.header("toGson");
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                // 从 formBody 中拿到请求参数，放入 formMap 中
                for (int i = 0; i < formBody.size(); i++) {
                    formMap.put(formBody.name(i), formBody.value(i));
                }
                LogUtils.e("request= "+req.toString()+"  param= "+formMap.toString());

                //对请求参数进行加密处理
                if (!StringUtils.isEmpty(needEncrypt)&&"yes".equals(needEncrypt)){
                    //加密
                    String jsonParam = encryptListener.onEncrypt(formMap);
                    // 重新修改 body 的内容
                    requestBody = new FormBody.Builder().add(jsonKey, jsonParam).build();
                    if (requestBody != null) {
                        req = req.newBuilder()
                                .post(requestBody)
                                .build();
                    }
                }
            }else {
                LogUtils.e("request= "+req.toString());
            }

            if (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                req = req.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }

            Response originalResponse = chain.proceed(req);
            String responseStr = originalResponse.toString();
            String response = originalResponse.body().string();
            if (NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {
                //只要加密的请求需要解密
                if (!StringUtils.isEmpty(needEncrypt)&&"yes".equals(needEncrypt)){
                    String newBody = encryptListener.onDecrypt(response);
                    LogUtils.e("response= "+responseStr+" return= "+newBody);
                    Response res = originalResponse.newBuilder().body(ResponseBody.create(null, newBody)).build();
                    return res;
                }else {
                    LogUtils.e("response= "+responseStr+" return= "+response);
                    Response res = originalResponse.newBuilder().body(ResponseBody.create(null, response)).build();
                    return res;
                }
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

    public static Observable initObservable(Observable observable) {
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable setObservable(Observable observable) {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 上传文件时使用
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (int i = 0;i<files.size();i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), files.get(i));
            MultipartBody.Part part = MultipartBody.Part.createFormData("multipartFiles"+i,  files.get(i).getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    public static void setJsonKey(String jsonKey) {
        Api.jsonKey = jsonKey;
    }

    public static void setReadTimeOut(int readTimeOut) {
        READ_TIME_OUT = readTimeOut;
    }

    public static void setConnectTimeOut(int connectTimeOut) {
        CONNECT_TIME_OUT = connectTimeOut;
    }

    public static void setEncryptListener(EncryptListener encryptListener) {
        Api.encryptListener = encryptListener;
    }

}
