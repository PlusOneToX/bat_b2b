package com.bat.thirdparty.suning.utils;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 苏宁加密
 */
public class SuNingEncryptUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuNingEncryptUtils.class);

    /**
     * 获取密文
     * @param str
     * @return
     */
    public static String getEncrypt(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes=md.digest(str.getBytes("UTF-8"));
            String encoded = Base64.getEncoder().encodeToString(bytes);
            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
    }

    /**
     * 获取经过URLEncode的密文
     * @param str
     * @return
     */
    public static String getUrlEncoderEncrypt(String str) {
        try {
            String encrypt = getEncrypt(str);
            LOGGER.info("计算出来的签名(未经过url转码):{}",encrypt);
            String urlEncoderEncrypt = URLEncoder.encode(encrypt, "UTF-8");
            return urlEncoderEncrypt;
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
    }



}