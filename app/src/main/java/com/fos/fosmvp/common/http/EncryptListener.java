package com.fos.fosmvp.common.http;

import java.util.Map;

/**
 * 加解密监听
 */
public interface EncryptListener {
    String onEncrypt(Map<String, Object> formMap);
    String onDecrypt(String encryptStr);
}