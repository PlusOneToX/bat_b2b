package com.bat.goods.dao.common;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bat(b2b_bat @ 163.com)
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
                if (objBefore == null && objAfter == null) {
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
}
