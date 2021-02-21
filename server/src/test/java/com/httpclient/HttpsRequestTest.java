package com.httpclient;

import org.junit.Assert;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: ocean
 * @since: 2021-02-06
 **/
public class HttpsRequestTest {
    private String password = "123456";
    private String trustStorePath = "D:\\ssl\\client.key.p12";
    private String keyStorePath = "D:\\ssl\\client.key.p12";
    private String httpUrl = "https://www.xuecheng.com:8443/ssl/";


    @Test
    public void oneWayAuthentication() throws Exception {
        URL url = new URL(httpUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // 打开输入输出流
        conn.setDoInput(true);
        //域名校验
        conn.setHostnameVerifier((k, t) -> true);
        // 单向认证
        TrustManagerFactory trustManagersFactory =
                HttpConfig.getTrustManagersFactory(trustStorePath, password);
        SSLSocketFactory sslSocketFactory = HttpConfig.getSSLSocketFactory(null, trustManagersFactory);
        conn.setSSLSocketFactory(sslSocketFactory);
        conn.connect();
        receiveData(conn);
        conn.disconnect();
    }

    @Test
    public void twoWayAuthentication() throws Exception {
        URL url = new URL(httpUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        // 打开输入输出流
        conn.setDoInput(true);
        //域名校验
        conn.setHostnameVerifier((k, t) -> true);
        // 双向认证
        TrustManagerFactory trustManagersFactory =
                HttpConfig.getTrustManagersFactory(trustStorePath, password);
        KeyManagerFactory keyManagerFactory = HttpConfig
                .getKeyManagerFactory(keyStorePath, password);
        SSLSocketFactory sslSocketFactory = HttpConfig
                .getSSLSocketFactory(keyManagerFactory, trustManagersFactory);
        conn.setSSLSocketFactory(sslSocketFactory);
        conn.connect();
        receiveData(conn);
        conn.disconnect();
    }

    private void receiveData(HttpsURLConnection conn) throws IOException {
        int length = conn.getContentLength();
        byte[] data = null;
        if (length != -1) {
            DataInputStream input = new DataInputStream(conn.getInputStream());
            data = new byte[length];
            input.readFully(data);
            input.close();
            System.out.println(new String(data));
        }
    }
}
