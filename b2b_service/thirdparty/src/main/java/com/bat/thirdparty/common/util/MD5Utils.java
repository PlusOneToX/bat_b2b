package com.bat.thirdparty.common.util;

import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 */
public class MD5Utils {
    /**
     * 生成md5值
     * @param password
     * @return
     */
    public static String digest32(@NotNull String password){
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
//            System.out.println("MD5(" + password + ",32小写) = " + result);
//            System.out.println("MD5(" + password + ",32大写) = " + result.toUpperCase());
//            System.out.println("MD5(" + password + ",16小写) = " + buf.toString().substring(8, 24));
//            System.out.println("MD5(" + password + ",16大写) = " + buf.toString().substring(8, 24).toUpperCase());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 返回大寫數據
     * @param origin
     * @return
     */
    public static String digest32Upper(String origin){
        String digest = digest32(origin);
        return digest.toUpperCase();
    }
}
