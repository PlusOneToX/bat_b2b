package com.platform.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 随机工具类
 */
public class GenerateUtil {

    private static Random r = new Random(System.currentTimeMillis());

    private static String s[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * n位数字的随机号
     */
    public static String generateNumber(int n) {
        //随机生成n个数字放于数组内
        int[] nArray = new int[n * 100];
        for (int i = 0; i < n * 100; i++) {
            int ran = r.nextInt(10);
            nArray[i] = Integer.valueOf(s[ran]);
        }
        StringBuffer sbuffer = new StringBuffer();
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                int ran = r.nextInt(n * 10);
                sbuffer.append(Integer.toString(nArray[ran]));
            } else {
                int ran = r.nextInt(10);
                sbuffer.append(s[ran]);
            }
        }
        return sbuffer.toString();
    }

    /**
     * 生成随机字符串(含大小写数字)
     */
    public static String generateStrRecaptcha(int length) {
        StringBuffer sf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = r.nextInt(3);// 大写，小写，数字
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sf.append(String.valueOf((char) result));
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sf.append(String.valueOf((char) result));
                    break;
                case 2:
                    sf.append(String.valueOf(new Random().nextInt(10)));
                    break;

            }
        }
        return sf.toString();
    }



    /**
     * 生产随机字符串（小写）
     *
     * @param length 长度
     *               生成大写 65 生成小写 97 随机大小写 null
     */
    public static String generateLowStrRecaptcha(int length) {
        return generateStrRecaptcha(length, 97);
    }

    /**
     * 生产随机字符串（大写）
     *
     * @param length 长度
     */
    public static String generateUpStrRecaptcha(int length) {
        return generateStrRecaptcha(length, 65);
    }

    private static String generateStrRecaptcha(int length, int s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (s + r.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(GenerateUtil.generateStrRecaptcha(6));
        }
//        System.out.println(GenerateUtil.generateStrRecaptcha(6));
    }


    /**
     * 生成订单编号
     *
     * @return
     */
    private static long counter = 0;

    public static synchronized String getOrderNo(Integer dynamic, String fix, int suffixLen) {
        String suffix = generateNumber(suffixLen);
        String orderNo = fix + new SimpleDateFormat("yyMMddHHmmssSS").format(new Date()) + dynamic + suffix;
        return orderNo;
    }

    public static Integer getValue(Integer max, Integer min, Integer seed) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        s = s / seed * seed;
        return s;
    }

    /**
     * 生成邀请码
     *
     * @param customerId
     * @return
     */
    public static String generateInviteCode(Integer customerId, Integer length) {
        String customerIdString = customerId + "";
        int needLength = length - customerIdString.length();
        String random = generateNumber(needLength);
        return random + customerIdString;
    }


}
