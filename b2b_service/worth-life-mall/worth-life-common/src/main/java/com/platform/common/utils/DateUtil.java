package com.platform.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 */
public class DateUtil {
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static String DATE_FORMAT_MONTH = "yyyy-MM";

    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_TIME_FORMAT_STR = "yyyyMMddHHmmss";

    public static String DATE_FORMAT_CHINESE = "yyyy年M月d日";

    public static final String DATE_TIME = "yyyy年MM月dd日 HH点mm分ss秒";

    public static final String DATE = "yyyy年MM月dd日";

    public static final String DATE_1 = "yyyy-MM-dd";

    public static final String DATE_TIME_2 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_2 = "yyyyMMdd";

    public static final String DATE_3 = "yyyyMM";

    public static Date getBeforeTimeAtminute(Integer minute) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -minute);
        Date beforeD = beforeTime.getTime();
        //   String time = sdf.format(beforeD);
        return beforeD;
    }


    /**
     * 获取时间差
     */

    public long dateCompare(Date firstDt, Date secentDt, ChronoUnit chronoUnit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDtStr = sdf.format(firstDt);
        String secentDtStr = sdf.format(secentDt);
        LocalDate localDate1 = LocalDate.parse(firstDtStr);
        LocalDate localDate2 = LocalDate.parse(secentDtStr);
        return localDate2.until(localDate1, chronoUnit);
    }

    /**
     * 获取24点前的时间差
     */

    public static long dateCompare24H(Date firstDt, ChronoUnit chronoUnit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDtStr = sdf.format(firstDt);
        String secentDtStr = sdf.format(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX));
        LocalDate localDate1 = LocalDate.parse(firstDtStr);
        LocalDate localDate2 = LocalDate.parse(secentDtStr);
        return localDate2.until(localDate1, chronoUnit);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getCurrentDateTime(String Dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(Dateformat);
        datestr = df.format(new Date());
        return datestr;
    }

    public static String dateToDateTime(Date date) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 将字符串日期转换为日期格式
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr) {

        if (datestr == null || datestr.equals("")) {
            return null;
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {
            date = DateUtil.stringToDate(datestr, DateUtil.DATE_FORMAT);
        }
        return date;
    }

    /**
     * 将字符串日期转换为日期格式
     * 自定義格式
     *
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr, String dateformat) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        try {
            date = df.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将日期格式日期转换为字符串格式
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 将日期格式日期转换为字符串格式 自定義格式
     *
     * @param date
     * @param dateformat
     * @return
     */
    public static String dateToString(Date date, String dateformat) {
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        datestr = df.format(date);
        return datestr;
    }

    public static boolean belongCalendar(Date nowTime, Date beginTime,
                                         Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        boolean after = date.after(begin);
        boolean before = date.before(end);
        if (after && before) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取日期的DAY值
     *
     * @param date 输入日期
     * @return
     */
    public static int getDayOfDate(Date date) {
        int d = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        d = cd.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    /**
     * 获取日期的MONTH值
     *
     * @param date 输入日期
     * @return
     */
    public static int getMonthOfDate(Date date) {
        int m = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        m = cd.get(Calendar.MONTH) + 1;
        return m;
    }

    /**
     * 获取日期的YEAR值
     *
     * @param date 输入日期
     * @return
     */
    public static int getYearOfDate(Date date) {
        int y = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        y = cd.get(Calendar.YEAR);
        return y;
    }

    /**
     * 获取星期几
     *
     * @param date 输入日期
     * @return
     */
    public static int getWeekOfDate(Date date) {
        int wd = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        wd = cd.get(Calendar.DAY_OF_WEEK) - 1;
        return wd;
    }

    /**
     * 获取输入日期的当月第一天
     *
     * @param date 输入日期
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.DAY_OF_MONTH, 1);
        return cd.getTime();
    }

    /**
     * 判断是否是闰年
     *
     * @param date 输入日期
     * @return 是true 否false
     */
    public static boolean isLeapYEAR(Date date) {

        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int year = cd.get(Calendar.YEAR);

        if (year % 4 == 0 && year % 100 != 0 | year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据整型数表示的年月日，生成日期类型格式
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    public static Date getDateByYMD(int year, int month, int day) {
        Calendar cd = Calendar.getInstance();
        cd.set(year, month - 1, day);
        return cd.getTime();
    }

    /**
     * 获取年周期对应日
     *
     * @param date  输入日期
     * @param iyear 年数  負數表示之前
     * @return
     */
    public static Date getYearCycleOfDate(Date date, int iyear) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        cd.add(Calendar.YEAR, iyear);

        return cd.getTime();
    }

    /**
     * 获取月周期对应日
     *
     * @param date 输入日期
     * @param i
     * @return
     */
    public static Date getMonthCycleOfDate(Date date, int i) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);

        cd.add(Calendar.MONTH, i);

        return cd.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static Integer daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            smdate = sdf.parse(sdf.format(smdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(smdate);

        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 字符串转日期
     *
     * @param dateTimeStr
     * @param format
     * @return
     */
    public static Date dateTimeStr2DateTime(String dateTimeStr, String format) {
        try {
            return new SimpleDateFormat(format).parse(dateTimeStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取后一天时间
     * 格式默认 yyyy-MM-dd HH:mm:ss
     *
     * @param dayNum 天数
     * @return
     */
    public static String getAfterDateTime(Integer dayNum) {
        return getAfterDateTime(dayNum, DateUtil.DATE_TIME_2);
    }

    /**
     * 获得后一天时间
     *
     * @param dayNum 天数
     * @param format 格式
     * @return
     */
    public static String getAfterDateTime(Integer dayNum, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        date = calendar.getTime();
        return sdf.format(date);
    }

    public static Date getAfterDateTimeAtDate(Date date, Integer dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        return calendar.getTime();
    }

    /**
     * 获取之前的时间
     *
     * @param dayNum 天数
     * @param format 格式
     * @return
     */
    public static String getBeforeDateTime(Integer dayNum, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -dayNum);
        date = calendar.getTime();
        return sdf.format(date);
    }


    public static String getBeforeDate(Integer dayNum, Date now, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -dayNum);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取之前的时间
     * 格式默认 yyyy-MM-dd HH:mm:ss
     *
     * @param dayNum 天数
     * @return
     */
    public static String getBeforeDateTime(Integer dayNum) {
        return getBeforeDateTime(dayNum, DateUtil.DATE_TIME_2);
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    public static Integer getSecondTimestampTwo() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return Integer.valueOf(timestamp);
    }

    /**
     * 时间戳转日期时间
     * 格式默认
     *
     * @param seconds
     * @return
     */
    public static String timeStamp2DateStr(Long seconds) {
        return timeStamp2DateStr(seconds, null);
    }

    /**
     * 时间戳转日期时间
     * 需要指定转换格式若不指定则转换为时间戳转日期时间
     *
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2DateStr(Long seconds, String format) {
        try {
            if (ObjectUtils.isEmpty(seconds)) {
                return null;
            }

            //默认格式
            if (StringUtils.isBlank(format)) {
                format = DateUtil.DATE_TIME;
            }
            return new SimpleDateFormat(format).format(new Date(seconds));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间转时间戳
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static Long date2Timestamp(String dateTime, String format) {
        try {
            if (ObjectUtils.isEmpty(dateTime)) {
                return null;
            }

            //默认格式
            if (StringUtils.isBlank(format)) {
                format = DateUtil.DATE_TIME;
            }
            Date date = new SimpleDateFormat(format).parse(dateTime);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取默认日期格式 yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */

    public static Date getDefaultFormat(Date time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_TIME_FORMAT);
            String format = sdf.format(time);
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.parse(format);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    public static Long timeToTimestampo(String str) {
        Long timestamp = null;
        try {
            str = str.replace("/Date(", "").replace(")/", "");
            String time = str.substring(0, str.length() - 5);
            Date date = new Date(Long.parseLong(time));
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            String format1 = format.format(date);
            timestamp = date2Timestamp(format1, "yyyy年MM月dd日");
            return timestamp;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        Date pledgeBeginTime = DateUtil.stringToDate("2020-10-10 00:00:00");
        Date pledgeEndTime = DateUtil.stringToDate("2020-11-1 00:00:00");
        pledgeBeginTime.setTime(pledgeBeginTime.getTime()-1000);
        pledgeEndTime.setTime(pledgeEndTime.getTime()+1000);
        System.out.println(DateUtil.dateToString(pledgeBeginTime,DateUtil.DATE_TIME_FORMAT));
        System.out.println(DateUtil.dateToString(pledgeEndTime,DateUtil.DATE_TIME_FORMAT));



    }
}
