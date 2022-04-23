package com.platform.common.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

public class BigDecimalUtil {

    private static Boolean checkType(Object... values) {
        Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        Boolean flag = false;
        for (Object value : values) {
            if (value instanceof Double) {
                flag = true;
            } else if (value instanceof String) {
                Matcher isNum = pattern.matcher(value.toString());
                if (isNum.matches()) {
                    flag = true;
                } else {
                    throw new RuntimeException("操作数值有误");
                }
            } else if (value instanceof Long) {
                flag = true;
            } else if (value instanceof Float) {
                flag = true;
            } else if (value instanceof Integer) {
                flag = true;
            } else if (value instanceof BigDecimal) {
                flag = true;
            } else if (value instanceof Short) {
                flag = true;
            }
        }
        return flag;
    }


    public static boolean greater(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).compareTo(new BigDecimal(decimal2.toString())) > 0;
    }

    public static boolean less(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).compareTo(new BigDecimal(decimal2.toString())) < 0;
    }

    public static boolean equal(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).compareTo(new BigDecimal(decimal2.toString())) == 0;
    }

    public static BigDecimal sub(Object decimal, Object decimal2) {
        checkType(decimal, decimal2);
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).subtract(new BigDecimal(decimal2.toString()));
    }

    public static BigDecimal add(Object decimal, Object decimal2) {
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).add(new BigDecimal(decimal2.toString()));
    }

    public static BigDecimal add(Object... decimal) {
        BigDecimal pre = BigDecimal.ZERO;
        for (Object o : decimal) {
            pre = add(pre, o);
        }
        return pre;
    }

    public static BigDecimal divide(Object decimal, Object decimal2) {
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2) || equal(BigDecimal.ZERO, decimal2)) {
            throw new RuntimeException("除法操作异常,除数不能为0");
        }
        return new BigDecimal(decimal.toString()).divide(new BigDecimal(decimal2.toString()), 8, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal multiply(Object decimal, Object decimal2) {
        if (ObjectUtils.isEmpty(decimal)) {
            decimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(decimal2)) {
            decimal2 = BigDecimal.ZERO;
        }
        return new BigDecimal(decimal.toString()).multiply(new BigDecimal(decimal2.toString()));
    }

    public static BigDecimal format(Object bigDecimal, Integer num) {
        checkType(bigDecimal);
        if (ObjectUtils.isEmpty(bigDecimal)) {
            return BigDecimal.ZERO;
        }
        StringBuffer pattern = new StringBuffer("#.");
        for (int i = 0; i < num; i++) {
            pattern.append("0");
        }
        DecimalFormat df2 = new DecimalFormat(pattern.toString());

        String str = df2.format(new BigDecimal(bigDecimal.toString()));

        return new BigDecimal(str);
        //String amount = new Formatter().format("%." + num + "f", new BigDecimal(bigDecimal.toString())).toString();
        //return new BigDecimal(amount);
    }

    public static BigDecimal percentage(Object bigDecimal) {
        checkType(bigDecimal);
        if (ObjectUtils.isEmpty(bigDecimal) || equal(bigDecimal, 0) || less(bigDecimal, 0)) {
            return BigDecimal.ZERO;
        }
        return BigDecimalUtil.divide(bigDecimal, 100);
    }

    public static BigDecimal max(Object bigDecimal, Object bigDecima2) {
        checkType(bigDecimal, bigDecima2);
        if (ObjectUtils.isEmpty(bigDecimal)) {
            bigDecimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(bigDecima2)) {
            bigDecima2 = BigDecimal.ZERO;
        }
        return new BigDecimal(bigDecimal.toString()).max(new BigDecimal(bigDecima2.toString()));
    }

    public static double maxToDubbo(Object bigDecimal, Object bigDecima2) {
        checkType(bigDecimal, bigDecima2);
        if (ObjectUtils.isEmpty(bigDecimal)) {
            bigDecimal = BigDecimal.ZERO;
        }
        if (ObjectUtils.isEmpty(bigDecima2)) {
            bigDecima2 = BigDecimal.ZERO;
        }
        BigDecimal max = new BigDecimal(bigDecimal.toString()).max(new BigDecimal(bigDecima2.toString()));
        return max.doubleValue();
    }

    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal(0);
        Object decimal = bigDecimal;
        System.out.println(decimal.toString());


    }

    public static BigDecimal toDecimal(Object available) {
        checkType(available);
        if (ObjectUtils.isEmpty(available)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(available.toString());
    }

    public static String toString(Object available) {
        checkType(available);
        if (ObjectUtils.isEmpty(available)) {
            return BigDecimal.ZERO.toPlainString();
        }
        return new BigDecimal(available.toString()).toPlainString();
    }


    public static BigDecimal setScale(Integer num, Object object) {
        checkType(object);
        if (ObjectUtils.isEmpty(object)) {
            return BigDecimal.ZERO.setScale(num, BigDecimal.ROUND_DOWN);
        }
        return new BigDecimal(object.toString()).setScale(num, BigDecimal.ROUND_DOWN);

    }

    public static BigDecimal setScale(Object object) {
        return setScale(2, object);
    }
}
