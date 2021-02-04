package com.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @author: ocean
 * @since: 2021-02-03
 **/
public class HmacUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] bytes = initHmacSHA256Key();
        System.out.println(Hex.encodeHexString(bytes));

    }

    /**
     * 初始化密钥
     *
     * @return 密钥
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static byte[] initHmacSHA256Key() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    private static byte[] hmacSHA256(byte[] key, String valueToDigest) throws Exception {
        // 还原密钥
        SecretKeySpec sk = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance(sk.getAlgorithm());
        mac.init(sk);
        return mac.doFinal(valueToDigest.getBytes(StandardCharsets.UTF_8));
    }

    public static String hmacSHA256Str(byte[] key, String valueToDigest) throws Exception {
        // 还原密钥
        byte[] bytes = hmacSHA256(key, valueToDigest);
        return Hex.encodeHexString(bytes);
    }

    public static String hmacSHA256Str(String key, String valueToDigest) throws Exception {
        // 还原密钥
        byte[] bytes = hmacSHA256(Hex.decodeHex(key), valueToDigest);
        return Hex.encodeHexString(bytes);
    }
}
