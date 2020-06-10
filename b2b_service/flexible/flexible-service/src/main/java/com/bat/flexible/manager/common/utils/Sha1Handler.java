package com.bat.flexible.manager.common.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * 对接SHA1加密解密的问题处理工具
 */
public class Sha1Handler {
    private static final Logger logger= LoggerFactory.getLogger(Sha1Handler.class);

    /**
     * 加密问题处理
     * @param value
     * @return
     */
    public static String encryption(String value)  {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(value.getBytes("UTF-8"));
            byte[] actual = md.digest();
            int actual_len = actual.length;
            StringBuilder actual_hex = new StringBuilder(actual.length * 2);
            for (int i = 0 ; i < actual_len ; i++) {
                actual_hex.append(String.format("%02X", actual[i]));
            }
            return actual_hex.toString();
        } catch (Exception e) {
           throw new RuntimeException("解密SIGN发生异常");
        }
    }
}
