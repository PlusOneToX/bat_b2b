package com.bat.financial.api.pay.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2018/4/14.
 */
public class ObjectUtils {

    @SuppressWarnings("rawtypes")
    private static boolean isPrimitive(Object obj) {
        try {
            return ((Class)obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return false;
        }
    }

    private static void putItemToMap(Map<String, Object> map, String key, Object val) {
        if (val == null)
            return;
        map.put(key, val);
    }

    /**
     * 返回由对象的属性为key,值为map的value的Map集合
     *
     * @param obj
     *            Object
     * @return mapValue Map<String,String>
     * @throws Exception
     */
    public static Map<String, String> objToStrMap(Object obj) {
        try {
            Map<String, String> mapValue = new HashMap<>();
            Class<?> cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                Method methodGet = cls.getDeclaredMethod(strGet);
                Object object = methodGet.invoke(obj);
                String value = object != null ? object.toString() : "";
                mapValue.put(name, value);
            }
            return mapValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回由Map的key对属性，value对应值组成的对应
     *
     * @param map
     *            Map<String,String>
     * @param cls
     *            Class
     * @return obj Object
     * @throws Exception
     */
    public static Object strMapToObj(Map<String, String> map, Class<?> cls) throws Exception {
        Field[] fields = cls.getDeclaredFields();
        Object obj = cls.newInstance();
        for (Field field : fields) {
            Class<?> clsType = field.getType();
            String name = field.getName();
            String strSet = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            Method methodSet = cls.getDeclaredMethod(strSet, clsType);
            if (map.containsKey(name)) {
                Object objValue = typeConversion(clsType, map.get(name));
                methodSet.invoke(obj, objValue);
            }
        }
        return obj;
    }

    public static Object typeConversion(Class<?> cls, String str) {
        Object obj = null;
        String nameType = cls.getSimpleName();
        if ("Integer".equals(nameType)) {
            obj = Integer.valueOf(str);
        }
        if ("String".equals(nameType)) {
            obj = str;
        }
        if ("Float".equals(nameType)) {
            obj = Float.valueOf(str);
        }
        if ("Double".equals(nameType)) {
            obj = Double.valueOf(str);
        }

        if ("Boolean".equals(nameType)) {
            obj = Boolean.valueOf(str);
        }
        if ("Long".equals(nameType)) {
            obj = Long.valueOf(str);
        }

        if ("Short".equals(nameType)) {
            obj = Short.valueOf(str);
        }

        if ("Character".equals(nameType)) {
            obj = str.charAt(1);
        }

        return obj;
    }

    public static Map<String, Object> objectToMap(Object object) throws IllegalAccessException {
        Class<?> aClass = object.getClass();
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Object> data = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Object o = field.get(object);
            data.put(name, o);
        }
        return data;

    }
}
