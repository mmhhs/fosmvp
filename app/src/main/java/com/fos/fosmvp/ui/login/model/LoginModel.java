package com.fos.fosmvp.ui.login.model;


import com.fos.fosmvp.common.base.BaseResponse;
import com.fos.fosmvp.common.http.Api;
import com.fos.fosmvp.common.http.ApiService;
import com.fos.fosmvp.common.http.EncryptListener;
import com.fos.fosmvp.common.utils.LogUtils;
import com.fos.fosmvp.entity.login.UserEntity;
import com.fos.fosmvp.ui.login.contract.LoginContract;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;


public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<BaseResponse<UserEntity>> getLoginData(Map map) {
        Api.setEncryptListener(new EncryptListener() {
            @Override
            public String onEncrypt(Map<String, Object> formMap) {

                return lockData(formMap);
            }

            @Override
            public String onDecrypt(String encryptStr) {
                try {
                    encryptStr = DES3.formatResultString(DES3.decode(encryptStr, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return encryptStr;
            }
        });
        return Api.createApi(ApiService.class).login(map);
    }

    //数据加密进行传输
    public static String lockData(Map map) {
        String jsonParam = setHttpJsonParams(map);
        LogUtils.e("jsonParam= "+jsonParam);
        String encryptStr = "";
        try {
            encryptStr = DES3.encode(jsonParam,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }


    //map转json
    public static String setHttpJsonParams(Map<String, Object> argsMap){
        String jsonParams = "";
        LimitEntity limitEntity = new LimitEntity();
        limitEntity.setAuth("");
        limitEntity.setUid("");
        String limitStr = new Gson().toJson(limitEntity);
        String paramsStr = new JSONObject(argsMap).toString();
        jsonParams = "{"+"\"limit\":"+limitStr+",\"param\":"+paramsStr+"}";
        return jsonParams;
    }

}
