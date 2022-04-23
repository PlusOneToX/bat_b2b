package com.platform.common.utils;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static void copyNewPropertites(Object source, Object target) {

        Class<?> targetClazz = target.getClass();
        Class<?> sourceClazz = source.getClass();
        Field[] sourceFiles = sourceClazz.getDeclaredFields();
        for (int i = 0; i < sourceFiles.length; i++) {
            String fieldName = sourceFiles[i].getName();
            try {
                Field targetField = targetClazz.getDeclaredField(fieldName);
                if (targetField.getType() == sourceFiles[i].getType()) {
                    String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method sourceGetMethod = sourceClazz.getDeclaredMethod(getMethodName, new Class[]{});
                    Method targetGetMethod = targetClazz.getDeclaredMethod(getMethodName, new Class[]{});
                    Object value = sourceGetMethod.invoke(source, new Object[]{});
                    Object oldValue = targetGetMethod.invoke(target, new Object[]{});
                    if (!ObjectUtils.isEmpty(oldValue) || ObjectUtils.isEmpty(value)) {
                        continue;
                    }
                    Method setMethod = targetClazz.getDeclaredMethod(setMethodName, sourceFiles[i].getType());
                    setMethod.invoke(target, value);
                } else {
                    throw new NoSuchFieldException("同名属性类型不匹配");
                }
            } catch (Exception e) {
                continue;
            }
        }
        System.out.println(target);
    }
    /**
     *
     * @Title: mapToObject
     * @Description: TODO(map转换为bean)
     * @return T    返回类型
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }

        T obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }

    /**
     *
     * @Title: objectToMap
     * @Description: TODO(bean转换为Map)
     * @return Map<?,?>    返回类型
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if(obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}