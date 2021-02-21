package com.httpclient;

import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

/**
 * @author: ocean
 * @since: 2021-01-13
 **/
public class HttpConfig {

    public static final String PROTOCOL = "TLS";

    /**
     * 获取keystore
     *
     * @param keystorePath keystore路径
     * @param password     密码
     * @return 密钥库
     * @throws Exception Exception
     */
    private static KeyStore getKeyStore(String keystorePath, String password) throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
//        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream in = new FileInputStream(keystorePath);) {
            keystore.load(in, password.toCharArray());
            return keystore;
        }
    }

    /**
     * 获取 SSLSocketFactory
     *
     * @param keyManagerFactory 密钥库工厂
     * @param trustFactory      信任库工厂
     * @return SSLSocketFactory
     * @throws Exception Exception
     */
    public static SSLSocketFactory getSSLSocketFactory(KeyManagerFactory keyManagerFactory,
                                                       TrustManagerFactory trustFactory) throws Exception {

        // 实例化SSL上下文
        SSLContext context = SSLContext.getInstance(PROTOCOL);
        KeyManager[] keyManagers = Optional.ofNullable(keyManagerFactory)
                .map(KeyManagerFactory::getKeyManagers).orElse(null);
        TrustManager[] trustManagers = Optional.ofNullable(trustFactory)
                .map(TrustManagerFactory::getTrustManagers).orElse(null);
        context.init(keyManagers, trustManagers, new SecureRandom());
        return context.getSocketFactory();
    }

    public static TrustManagerFactory getTrustManagersFactory(String trustStorePath, String password) throws Exception {
        // 实例化信任库
        TrustManagerFactory trustFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = getKeyStore(trustStorePath, password);
        // 初始化信任库
        trustFactory.init(trustStore);

        return trustFactory;
    }

    public static TrustManagerFactory getTrustManagersFactory() throws Exception {
        // 实例化信任库
        TrustManagerFactory trustFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init((KeyStore) null);
        TrustManager[] managers = trustFactory.getTrustManagers();
        for (int i = 0; i < managers.length; i++) {
            final TrustManager tm = managers[i];
            if (tm instanceof X509TrustManager) {
                managers[i] = new TrustManager() {
                };
            }
        }
        return trustFactory;
    }

    public static KeyManagerFactory getKeyManagerFactory(String keystorePath, String password) throws Exception {
        KeyManagerFactory factory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获取密钥库
        KeyStore keyStore = getKeyStore(keystorePath, password);
        // 初始化密钥工厂
        factory.init(keyStore, password.toCharArray());
        return factory;
    }
}
