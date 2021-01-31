package com;

import com.utils.CipherUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author: ocean
 * @since: 2021-01-31
 **/
public class EncryptDataBasePassword {
    public static void main(String[] args) throws Exception {
        // 先获取AES的密钥
        String key = CipherUtils.initAESKeyHex();
        System.out.println("key: " + key);
        // 用上一步的密钥对数据库密码的明文进行AES128加密
        String encrypt = CipherUtils.encrypt("123456", key);
        System.out.println("encryptText: " + encrypt);
        // 将以上产生的encryptText放到配置文件中
    }
}
