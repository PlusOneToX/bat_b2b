package com.bat.distributor.service.common;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/8 14:56
 */
public class CommonUtils {
    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
     * 
     * @param objB
     *            进行属性比较的对象1
     * @param objA
     *            进行属性比较的对象2
     * @return 属性差异比较结果map
     */
    public static Map<String, List<Object>> compareFields(Object objB, Object objA) throws Exception {
        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        Field[] ignoreList = objB.getClass().getDeclaredFields();
        // 只有两个对象都是同一类型的才有可比性
        if (objB.getClass() == objA.getClass()) {
            Class clazz = objB.getClass();
            // 获取object的属性描述
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            // 这里就是所有的属性了
            for (PropertyDescriptor pd : pds) {
                // 属性名
                String name = pd.getName();
                // get方法
                Method readMethod = pd.getReadMethod();
                // 在obj1上调用get方法等同于获得obj1的属性值
                Object objBefore = readMethod.invoke(objB);
                // 在obj2上调用get方法等同于获得obj2的属性值
                Object objAfter = readMethod.invoke(objA);
                if ((objBefore == null || StringUtils.isBlank(objBefore.toString()))
                    && (objAfter == null || StringUtils.isBlank(objAfter.toString()))) {
                    continue;
                } else if (objBefore == null && objAfter != null) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(objBefore);
                    list.add(objAfter);
                    map.put(name, list);
                    continue;
                }
                // 比较这两个值是否相等,不等则放入map
                if (!objBefore.equals(objAfter)) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(objBefore);
                    list.add(objAfter);
                    map.put(name, list);
                }
            }
        }
        return map;
    }

    /**
     * 获取C端客户编码
     * 
     * @return
     */
    public static String getCustomerNo() {
        StringBuilder sb = new StringBuilder();
        sb.append("C");
        sb.append(String.valueOf(System.currentTimeMillis()));
        return sb.toString();
    }

    /**
     * @Description 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集）
     * @param str
     *            待转换字符串
     * @return 转换后字符串 exception
     */
    public static String emojiConvert(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, "[[" + URLEncoder.encode(matcher.group(1), "UTF-8") + "]]");
            } catch (UnsupportedEncodingException e) {
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串
     * @param str
     *            转换后的字符串
     * @return 转换前的字符串 exception
     */
    public static String emojiRecovery(String str) {
        String patternString = "\\[\\[(.*?)\\]\\]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * set属性的值到Bean
     * 
     * @param t
     * @param valMap
     *            是否过滤属性值为0
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T changeFieldValues(T t, Map<String, List<String>> valMap) {
        Field[] fields = t.getClass().getDeclaredFields();
        boolean changeBean = false;
        for (Field field : fields) {
            try {
                List<String> values = valMap.get(field.getName());
                if (values != null && !CollectionUtils.isEmpty(values)) {
                    changeBean = true;
                    setFieldValue(t, field, values.get(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_FIELD_ERROR);
            }
        }
        if (changeBean) {
            return t;
        } else {
            return null;
        }
    }

    /**
     * set属性的值到Bean
     *
     * @param cls
     * @param valMap
     *            是否过滤属性值为0
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> T copyFieldValue(Class<T> cls, Map<String, String> valMap) {
        try {
            Field[] fields = cls.getDeclaredFields();
            T t = cls.newInstance();
            boolean changeBean = false;
            for (Field field : fields) {
                String value = valMap.get(field.getName());
                if (StringUtils.isNotBlank(value)) {
                    changeBean = true;
                    setFieldValue(t, field, value);
                }
            }
            if (changeBean) {
                return t;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_FIELD_ERROR);
        }
    }

    /**
     * 判断数据类型
     * 
     * @param tarValue
     * @return
     */
    private static Boolean judgeValue(String tarValue) {
        return ("".equals(tarValue) || "0".equals(tarValue) || "0.0".equals(tarValue));
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
}
