package com.bat.financial.pay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bat.financial.api.pay.constant.PayChannel;
import com.bat.financial.dao.pay.dataobject.ExpireTime;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/1/6 10:26
 */
public class DateUtils {
    public static final String ALI_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String WX_V3_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String WX_V2_PATTERN = "yyyyMMddHHmmss";

    public static Date getDateStr(String dateStr, String payChannel) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ALI_PATTERN);
        if (payChannel.startsWith("WX")) {
            simpleDateFormat = new SimpleDateFormat(WX_V3_PATTERN);
        }
        return simpleDateFormat.parse(dateStr);
    }

    public static String getTimeExpire(Date date) {
        return getTimeExpire(date, ALI_PATTERN);
    }

    public static String getTimeExpire(Date date, String payChannel) {
        Date expireDate = new Date(date.getTime() + ExpireTime.DEFAULT_EXPIRE_TIME * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ALI_PATTERN);
        if (payChannel.equalsIgnoreCase(PayChannel.WXPAY_V3.name()) || payChannel.startsWith("WXPAY_PARTNER")) {
            simpleDateFormat = new SimpleDateFormat(WX_V3_PATTERN);
        } else if (payChannel.equalsIgnoreCase(PayChannel.WXPAY_V2.name())) {
            simpleDateFormat = new SimpleDateFormat(WX_V2_PATTERN);
        }
        return simpleDateFormat.format(expireDate);
    }

    public static void main(String[] args) {
        System.out.println(getTimeExpire(new Date(), "WX2"));
    }
}
