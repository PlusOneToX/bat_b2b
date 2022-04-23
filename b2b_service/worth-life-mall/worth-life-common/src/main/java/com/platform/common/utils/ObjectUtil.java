package com.platform.common.utils;

public class ObjectUtil extends org.springframework.util.ObjectUtils {

    public static Boolean anyNotEmpty(Object... values) {
        if (!isEmpty(values)) {
            for (Object value : values) {
                boolean empty = isEmpty(value);
                if (!empty) {
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean anyEmpty(Object... values) {
        if (!isEmpty(values)) {
            for (Object value : values) {
                boolean empty = isEmpty(value);
                if (empty) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Boolean allEmpty(Object... values) {
        if (!isEmpty(values)) {
            for (Object value : values) {
                boolean empty = isEmpty(value);
                if (!empty) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }


}