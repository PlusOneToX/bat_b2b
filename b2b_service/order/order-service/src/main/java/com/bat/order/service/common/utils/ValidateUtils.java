package com.bat.order.service.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lim
 * @version 1.0
 * @description: 校验工具类
 * @date 2018/05/4 16:06
 */
public class ValidateUtils {
    private static final Pattern CHINA_PATTERN = Pattern
        .compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 145,147,149 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的) 166 17+3,5,6,7,8
     * 18+任意数 198,199
     */
    public static boolean isChinaPhoneLegal(String str) {
        Matcher m = CHINA_PATTERN.matcher(str);
        return m.matches();
    }
}
