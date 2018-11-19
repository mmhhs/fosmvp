package com.fos.fosmvp.ui.login.model;



import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES3 {
    // 密钥
    private final static String ServiceAPI_SecretKey = "fontone-trans@lx100$#365#$";
    private final static String AllWinOfCarOwner_SecretKey = "foton-trans@lx100$#365#$";
    // 向量
    private final static String iv = "20161201";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";
    private final static String Login_SecretKey = "fontone-exten@lx100$#365#$";
    private final static String login_iv = "20171201";
    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @param keyType 秘钥类型 默认为0  0：ServiceAPI_SecretKey 1：AllWinOfCarOwner_SecretKey
     * @return
     * @throws Exception
     */
    public static String encode(String plainText,int keyType) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(ServiceAPI_SecretKey.getBytes());
        if (keyType == 0){
            spec= new DESedeKeySpec(ServiceAPI_SecretKey.getBytes());
        }else if (keyType == 1){
            spec= new DESedeKeySpec(AllWinOfCarOwner_SecretKey.getBytes());
        }
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @param keyType 秘钥类型 默认为0  0：ServiceAPI_SecretKey 1：AllWinOfCarOwner_SecretKey
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText,int keyType) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(ServiceAPI_SecretKey.getBytes());
        if (keyType == 0){
            spec= new DESedeKeySpec(ServiceAPI_SecretKey.getBytes());
        }else if (keyType == 1){
            spec= new DESedeKeySpec(AllWinOfCarOwner_SecretKey.getBytes());
        }
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
        String str = new String(decryptData, encoding);
//        str = str.replace("\\\"","\"");
        return str;
    }

    public static String formatResultString(String resultString){
        String result = resultString;
        if (result.contains("\"{")&&result.contains("}\"")){
            result = result.replace("\"{","{");
            result = result.replace("}\"","}");
            if (result.contains("\\\"")){
                result = result.replace("\\\"","\"");
            }
        }
        if (result.contains("\"[")&&result.contains("]\"")){
            result = result.replace("\"[","[");
            result = result.replace("]\"","]");
            if (result.contains("\\\"")){
                result = result.replace("\\\"","\"");
            }
        }
        return result;
    }
    public static String encodeLogin(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(ServiceAPI_SecretKey.getBytes());
        spec= new DESedeKeySpec(Login_SecretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(login_iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }
}
