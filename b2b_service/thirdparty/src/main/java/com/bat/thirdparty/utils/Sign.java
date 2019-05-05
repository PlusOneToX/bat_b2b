package com.bat.thirdparty.utils;

import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Sign {

    /**
     * HMACSHA256签名
     * @param data 要加密的数据
     * @param key 密钥
     * @return
     */
    public static String HMACSHA256(String data, String key){

        try {
            if(StringUtils.isBlank(key)){
                throw new ThirdPartyOpenApiException("密钥不能为空");
            }

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }catch (Exception e){
            throw new ThirdPartyOpenApiException("签名过程出错");
        }
    }

    public static void main(String args[]) throws Exception {
        System.out.println(HMACSHA256("aaa","sasas"));
    }
}
