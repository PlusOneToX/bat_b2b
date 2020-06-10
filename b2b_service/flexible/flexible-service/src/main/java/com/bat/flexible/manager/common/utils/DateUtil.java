package com.bat.flexible.manager.common.utils;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String PAY_TIME_PATTERN="yyyy-MM-dd HH:mm:ss";


    public static long getNextDayZero() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis();
    }

    /**
     * 获取过去第几天的日期
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取过去第几天的时间戳
     * @param past
     * @return
     */
    public static long getPastTimeStamp(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        long timeInMillis = calendar.getTimeInMillis();
        return timeInMillis;
    }

    public static long[] getGoodsItemPastTimeStamp(int past) {
        long[] timeStr = new long[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 23, 59, 59);
        long begin = calendar.getTime().getTime();
        timeStr[0] = begin;
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        long end = calendar.getTime().getTime();
        timeStr[1] = end;
        return timeStr;
    }

    public static void main(String[] args) {
//        String pastDate = getPastDate(7);
//        long pastTimeStamp = getPastTimeStamp(1);
//        System.out.print(pastTimeStamp);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 23, 59, 59);
        long tt = calendar.getTime().getTime();
        System.out.println(tt);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        long t2 = calendar.getTime().getTime();
        System.out.println(t2);
    }


    /**
     * 根據時間戳轉換成對應格式
     * @param time
     * @return
     */
    public static String getTime(@NotNull Long time,@NotNull String pattern){
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
        Date date = new Date(time);
        String format = dateFormat.format(date);
        return format;
    }

}
