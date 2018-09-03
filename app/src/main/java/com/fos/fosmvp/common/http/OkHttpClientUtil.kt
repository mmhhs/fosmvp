package com.fos.fosmvp.common.http

import android.content.Context
import android.text.TextUtils
import okhttp3.OkHttpClient
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URLEncoder
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*


object OkHttpClientUtil {

    fun getSSLClientIgnoreExpire(client: OkHttpClient, context: Context, assetsSSLFileName: String): OkHttpClient {
        val inputStream = getStream(context, assetsSSLFileName)
        try {
            //Certificate
            val certificateFactory = CertificateFactory.getInstance("X.509")
            var certificate: Certificate? = null
            val pubSub: String
            val pubIssuer: String
            certificate = certificateFactory.generateCertificate(inputStream)
            val pubSubjectDN = (certificate as X509Certificate).subjectDN
            val pubIssuerDN = certificate.issuerDN
            pubSub = pubSubjectDN.name
            pubIssuer = pubIssuerDN.name

            //Log.e("sssss", "--"+pubSubjectDN.getName());
            //Log.e("sssss", "--"+pubIssuerDN.getName());

            // Create an SSLContext that uses our TrustManager
            val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    /**
                     * for (X509Certificate cert : chain) {
                     * // Make sure that it hasn't expired.
                     * cert.checkValidity();
                     * // Verify the certificate's public key chain.
                     * try {
                     * cert.verify(((X509Certificate) ca).getPublicKey());
                     * } catch (Exception e) {
                     * e.printStackTrace();
                     * }
                     * }
                     */
                    //1、判断证书是否是本地信任列表里颁发的证书
                    try {
                        val tmf = TrustManagerFactory.getInstance("X509")
                        tmf.init(null as KeyStore?)
                        for (trustManager in tmf.trustManagers) {
                            (trustManager as X509TrustManager).checkServerTrusted(chain, authType)
                        }
                    } catch (e: Exception) {
                        throw CertificateException(e)
                    }

                    //2、判断服务器证书 发布方的标识名  和 本地证书 发布方的标识名 是否一致
                    //3、判断服务器证书 主体的标识名  和 本地证书 主体的标识名 是否一致
                    //getIssuerDN()  获取证书的 issuer（发布方的标识名）值。
                    //getSubjectDN()  获取证书的 subject（主体的标识名）值。
                    //Log.e("sssss", "server--"+chain[0].getSubjectDN().getName());
                    //Log.e("sssss", "server--"+chain[0].getIssuerDN().getName());
                    if (chain[0].subjectDN.name != pubSub) {
                        throw CertificateException("server's SubjectDN is not equals to client's SubjectDN")
                    }
                    if (chain[0].issuerDN.name != pubIssuer) {
                        throw CertificateException("server's IssuerDN is not equals to client's IssuerDN")
                    }
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return  emptyArray()//new X509Certificate[0];
                }
            })

            //SSLContext  and SSLSocketFactory
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            //okhttpclient
            val builder = client.newBuilder()
            builder.sslSocketFactory(sslSocketFactory)
            return builder.build()

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return client
    }

    fun getTrustAllSSLClient(client: OkHttpClient): OkHttpClient {
        try {
            //Certificate

            //keystore

            // Create a trust manager that does not validate certificate chains
            val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            val builder = client.newBuilder()
            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { hostname, session -> true }

            return builder.build()
        } catch (e: Exception) {
            return client
        }

    }

    fun getSSLClient(client: OkHttpClient, context: Context, assetsSSLFileName: String): OkHttpClient {
        val inputStream = getStream(context, assetsSSLFileName)
        return getSSLClientByInputStream(client, inputStream)
    }

    fun getSSLClientByCertificateString(client: OkHttpClient, certificate: String): OkHttpClient {
        val inputStream = getStream(certificate)
        return getSSLClientByInputStream(client, inputStream)
    }

    private fun getStream(context: Context, assetsFileName: String): InputStream? {
        try {
            return context.assets.open(assetsFileName)
        } catch (var3: Exception) {
            return null
        }

    }

    private fun getStream(certificate: String): InputStream? {
        try {
            return ByteArrayInputStream(certificate.toByteArray(charset("UTF-8")))
        } catch (var3: Exception) {
            return null
        }

    }

    private fun getSSLClientByInputStream(client: OkHttpClient, inputStream: InputStream?): OkHttpClient {
        var client = client
        if (inputStream != null) {
            val sslSocketFactory = setCertificates(inputStream)
            if (sslSocketFactory != null) {
                client = client.newBuilder().sslSocketFactory(sslSocketFactory).build()
            }
        }
        return client
    }

    private fun setCertificates(vararg certificates: InputStream): SSLSocketFactory? {
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")

            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)

            var index = 0
            for (certificate in certificates) {
                val certificateAlias = Integer.toString(index++)
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate))
                try {
                    certificate?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())

            return sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 由于okhttp header 中的 value 不支持 null, \n 和 中文这样的特殊字符,所以encode字符串
     *
     * @param value
     * @return
     */
    fun getHeaderValueEncoded(value: String): String {
        if (TextUtils.isEmpty(value)) return " "
        var i = 0
        val length = value.length
        while (i < length) {
            val c = value[i]
            if (c <= '\u001f' && c != '\t' || c >= '\u007f') {//根据源码okhttp允许[0020-007E]+\t的字符
                try {
                    return URLEncoder.encode(value, "UTF-8")
                } catch (e: Exception) {
                    e.printStackTrace()
                    return " "
                }

            }
            i++
        }
        return value
    }

    /**
     * 由于okhttp header 中的 name 不支持 null,空格、\t、 \n 和 中文这样的特殊字符,所以encode字符串
     */
    fun getHeaderNameEncoded(name: String): String {
        if (TextUtils.isEmpty(name)) return "null"
        var i = 0
        val length = name.length
        while (i < length) {
            val c = name[i]
            if (c <= '\u0020' || c >= '\u007f') {//根据源码okhttp允许[0021-007E]的字符
                try {
                    return URLEncoder.encode(name, "UTF-8")
                } catch (e: Exception) {
                    e.printStackTrace()
                    return " "
                }

            }
            i++
        }
        return name
    }
}
