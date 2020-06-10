package com.bat.flexible.manager.common.utils;

import com.bat.flexible.api.FlexibleCustomException;
import org.apache.commons.codec.Charsets;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class CommonUtil {

    private static final char[] codeSequenceChar=new char[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public static String getOrderNo(Long time){
        //自定义订单编号生成规则   由YYYYMMDD(年月日) + 时间戳的格式组成
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date(time));
        String orderNo = date + String.valueOf(time) + String.valueOf((int)(Math.random()*10));
        return orderNo;
    }


    /**
     * 日期格式化
     * @param time
     * @param format
     * @return
     */
    public static String formatDate(Long time,String format) {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date=new Date(time);
        return sdf.format(date);
    }

    /**
     * Base64
     *
     */
    public static String base64(String str) {
        byte[] bytes = str.getBytes();
        //Base64 加密
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }
    /**
     * BASE64加密
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    public static String getRandom() {
        StringBuffer randomCodeRes = new StringBuffer();
        // 创建一个随机数生成器类
        Random random = new Random();
        //随机产生，验证码由几个数字、几个字母组成
        int charNum  =12;
        for (int i = 0; i < charNum; i++) {
            // 得到随机产生的验证码字母。
            String strRand = String.valueOf(codeSequenceChar[random.nextInt(codeSequenceChar.length)]);
            // 将产生的六个随机数组合在一起。
            randomCodeRes.append(strRand);
        }
        return randomCodeRes.toString();
    }

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
            throw new FlexibleCustomException("解密SIGN发生异常");
        }
    }

    /**
     *
     * @param apiKeyId
     * @param apiSecretKey
     * @param uuid
     * @return
     * @throws JoseException
     */
    public static String encodeJWT(String apiKeyId, String apiSecretKey, String uuid) throws JoseException {
        // current time measured in the number of seconds since the Unix Epoch (January 1 1970 00:00:00 GMT).
        long currTime = System.currentTimeMillis();
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(apiKeyId);
        claims.setSubject(uuid);
        claims.setIssuedAt(NumericDate.fromMilliseconds(currTime));
        // Expired in 1 hour
        claims.setExpirationTime(NumericDate.fromMilliseconds(currTime + 3600000));
        JsonWebSignature jws = new JsonWebSignature();
        jws.setHeader("typ", "JWT");
        jws.setPayload(claims.toJson());
        jws.setKey(new HmacKey(apiSecretKey.getBytes(Charsets.UTF_8)));
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        return jws.getCompactSerialization();
    }

}
