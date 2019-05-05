package com.bat.thirdparty.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.CommonErrorCode;
import com.bat.thirdparty.common.base.ThirdPartyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import sun.misc.BASE64Encoder;

public class CommonUtil {

    private static final char[] codeSequenceChar = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
        'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String getOrderNo(Long time) {
        // 自定义订单编号生成规则 由YYYYMMDD(年月日) + 时间戳的格式组成
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date(time));
        String orderNo = date + String.valueOf(time) + String.valueOf((int)(Math.random() * 10));
        return orderNo;
    }

    /**
     * 获取访问用户的IP
     *
     * @param request
     *            HttpServletRequest 访问请求
     * @return String
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") != -1) {
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }
        return ip;
    }

    /**
     * 日期格式化
     * 
     * @param time
     * @param format
     * @return
     */
    public static String formatDate(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * Base64
     *
     */
    public static String base64(String str) {
        byte[] bytes = str.getBytes();
        // Base64 加密
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
        // 随机产生，验证码由几个数字、几个字母组成
        int charNum = 12;
        for (int i = 0; i < charNum; i++) {
            // 得到随机产生的验证码字母。
            String strRand = String.valueOf(codeSequenceChar[random.nextInt(codeSequenceChar.length)]);
            // 将产生的六个随机数组合在一起。
            randomCodeRes.append(strRand);
        }
        return randomCodeRes.toString();
    }

    /**
     * set属性的值到Bean
     *
     * @param valMap
     *            是否过滤属性值为0
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> void copyFieldValue(T t, Map<String, String> valMap) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                String value = valMap.get(field.getName());
                if (StringUtils.isNotBlank(value)) {
                    setFieldValue(t, field, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(CommonErrorCode.B_THIRD_PARTY_FIELD_ERROR);
        }
    }

    /**
     * 根据属性设置值,仅支持基本类型
     *
     * @param bean
     * @param field
     * @param value
     * @throws Exception
     */
    public static void setFieldValue(Object bean, Field field, String value) throws Exception {
        Class<?> cls = bean.getClass();
        Method[] methods = cls.getDeclaredMethods();
        // 属性set方法名
        String fieldSetName = setMethodName(field.getName());
        if (checkSetMethod(methods, fieldSetName)) {
            // set方法
            Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
            if (value.equals("null")) {
                value = null;
            }
            if (validNullAndEmptyString(value)) {
                String fieldType = field.getType().getSimpleName();
                if ("String".equals(fieldType)) {
                    fieldSetMet.invoke(bean, value);
                } else if ("Date".equals(fieldType)) {
                    Date temp = DateUtils.parseDate(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Short".equalsIgnoreCase(fieldType)) {
                    Short temp = Short.parseShort(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                    Integer temp = Integer.parseInt(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Float".equalsIgnoreCase(fieldType)) {
                    Float temp = Float.parseFloat(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Long".equalsIgnoreCase(fieldType)) {
                    Long temp = Long.parseLong(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Double".equalsIgnoreCase(fieldType)) {
                    Double temp = Double.parseDouble(value);
                    fieldSetMet.invoke(bean, temp);
                } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                    Boolean temp = Boolean.parseBoolean(value);
                    fieldSetMet.invoke(bean, temp);
                } else {
                    System.out.println("not supper type" + fieldType);
                }
            } else {
                // value=null或者""
                fieldSetMet.invoke(bean, value);
            }
        } else {
            System.out.println("not this set method : " + fieldSetName);
        }
    }

    /**
     * 拼接在某属性的 set方法
     *
     * @param fieldName
     * @return
     */
    public static String setMethodName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * 判断是否存在某属性的 set方法
     *
     * @param methods
     * @param fieldSetMet
     * @return
     */
    public static boolean checkSetMethod(Method[] methods, String fieldSetMet) {
        for (Method met : methods) {
            if (fieldSetMet.equals(met.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证null/""
     *
     * @param value
     * @return
     */
    private static boolean validNullAndEmptyString(String value) {
        return (null != value && !"".equals(value) && !value.equals("null"));
    }


    /**
     * 对象转化为Map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }


    public static Date stringToDateTime(String time) {
        Date date = null;
        if (StringUtils.isNotEmpty(time)) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            } catch (ParseException e) {
                try {
                    date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
                } catch (ParseException e1) {
                    try {
                        date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(time);
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                        return null;
                    }
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return date;
    }

}
