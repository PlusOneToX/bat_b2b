package com.bat.thirdparty.common.util;


import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/15 14:38
 */
public class CommonUtils {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     * @return
     * @author SHANHY
     * @date   2015年11月27日
     */
    public static long getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            return ld;
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    /**
     * 判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * sha1加密
     * @param data
     * @return
     * @throws
     */
    public static String sha1(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(data.getBytes("UTF-8"));
        byte[] messageDigest = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte message : messageDigest) {
            String shaHex = Integer.toHexString(message & 0xFF);
            if (shaHex.length() < 2)
                hexString.append(0);
            hexString.append(shaHex);
        }
        return hexString.toString().toUpperCase();
    }

    public static boolean isUTF8(String key){
        try {
            key.getBytes("utf-8");
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String getCharset(String str){
        try {
            byte[] head = str.getBytes("ISO-8859-1");
            String code = "";
            code = "ISO-8859-1";
            if (head[0] == -1 && head[1] == -2 )
                code = "UTF-16";
            if (head[0] == -2 && head[1] == -1 )
                code = "Unicode";
            if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
                code = "UTF-8";
            return code;
        }catch (Exception e){
            return null;
        }
    }



    /**
     * 日期格式化
     * @param time
     * @param format
     * @return
     */
    private static String formatDate(Long time,String format) {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date=new Date(time);
        return sdf.format(date);
    }
}
