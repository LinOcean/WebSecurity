package com.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author: ocean
 * @since: 2021-01-31
 **/
public class CipherUtils {
    // 使用AES算法
    private static final String KEY_ALGORITHM = "AES";
    // 密钥长度
    private static int KEY_SIZE = 128;

    // iv长度
    private static int IV_SIZE = 16;
    /**
     * 加密/解密
     * <p>
     * 算法/工作模式/填充方式
     * ECB mode cannot use IV
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 使用AES算法生成密钥
     *
     * @return 16进制的密钥
     * @throws NoSuchAlgorithmException 指定算法不存在
     */
    public static String initAESKeyHex() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(KEY_SIZE);
        SecretKey secretKey = kg.generateKey();
        byte[] encoded = secretKey.getEncoded();
        // 装换成16进制
        return Hex.encodeHexString(encoded);
    }

    /**
     * 转换成密钥材料
     *
     * @param key 明文密钥
     * @return 密钥材料
     * @throws Exception Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data 明文
     * @param key  密钥
     * @return 密文
     * @throws Exception Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        // 将16进制的key转换回去
        byte[] keyByte = Hex.decodeHex(key);
        // 获取随机的安全IV值
        byte[] salt = secureRandom(IV_SIZE);
        String cipherText = encrypt(data.getBytes(StandardCharsets.UTF_8), salt, keyByte);
        // 将iv与加密后的数据拼接返回
        return Hex.encodeHexString(salt) + ":" + cipherText;
    }

    /**
     * 解密
     *
     * @param data 密文base64字符串
     * @param key  密钥
     * @return 明文
     * @throws Exception Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        if (!data.contains(":")) {
            throw  new IllegalArgumentException();
        }
        // 先获取iv值
        byte[] salt = Hex.decodeHex(data.substring(0, data.indexOf(":")));
        // 获取数据部分
        byte[] cipherText = Hex.decodeHex(data.substring(data.indexOf(":") + 1));
        return decrypt(cipherText, salt, Hex.decodeHex(key));
    }

    /**
     * 加密
     *
     * @param data 明文
     * @param salt iv值
     * @param key  密钥
     * @return 加密后16进制字符串
     * @throws Exception Exception
     */
    private static String encrypt(byte[] data, byte[] salt, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(salt));
        return Hex.encodeHexString(cipher.doFinal(data));
    }

    /**
     * 加密
     *
     * @param data 密文
     * @param salt iv值
     * @param key  密钥
     * @return
     * @throws Exception Exception
     */
    private static String decrypt(byte[] data, byte[] salt, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(salt));
        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }

    private static byte[] secureRandom(int len) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] bytes = new byte[len];
        random.nextBytes(bytes);
        return bytes;
    }
}
