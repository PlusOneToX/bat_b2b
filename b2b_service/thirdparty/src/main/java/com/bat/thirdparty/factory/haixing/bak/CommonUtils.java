package com.bat.thirdparty.factory.haixing.bak;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
/**
 * 订单同步到海星（脚本类-放同一个包方便管理）
 */
public class CommonUtils {
    public static Map objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        Map map = new HashMap();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(),field.get(obj) );
        }
        return map;
    }
}
