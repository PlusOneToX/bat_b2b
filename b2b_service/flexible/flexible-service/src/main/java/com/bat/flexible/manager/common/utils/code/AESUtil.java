package com.bat.flexible.manager.common.utils.code;


import com.bat.flexible.api.FlexibleCustomException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

@Component
public class AESUtil {

    private static String KEY ;

    @Value("${aes.key}")
    public void setKEY(String key){
        AESUtil.KEY = key;
    }

    /**
     * 根据密钥对指定的明文plainText进行加密.
     *
     * @param plainText 明文
     * @return 加密后的密文.
     */
    public static final String encrypt(String plainText) {
        if(KEY==null){
            throw new FlexibleCustomException("找不到KEY配置");
        }
        Key secretKey = getKey(KEY);
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] p = plainText.getBytes("UTF-8");
            byte[] result = cipher.doFinal(p);
          /*  BASE64Encoder encoder = new BASE64Encoder();
            String encoded = encoder.encode(result);*/
            String encoded = Base64.encodeBase64String(result);
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Key getKey(String keySeed) {
        if (keySeed == null) {
            keySeed = System.getenv("aes.key");
        }
        if (keySeed == null) {
            keySeed = System.getProperty("aes.key");
        }
        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = "abcd1234!@#$";// 默认种子
        }
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据密钥对指定的密文cipherText进行解密.
     *
     * @param cipherText 密文
     * @return 解密后的明文.
     */
    public static final String decrypt(String cipherText) {
        Key secretKey = getKey(KEY);
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
          /*  BASE64Decoder decoder = new BASE64Decoder();
            byte[] c = decoder.decodeBuffer(cipherText);*/
            byte[] c = Base64.decodeBase64(cipherText);

            byte[] result = cipher.doFinal(c);
            String plainText = new String(result, "UTF-8");
            return plainText;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String aa= encrypt("03511");
        System.out.println(aa);
        String bb = decrypt(aa);
        System.out.println(bb);
    }
}
