package com.bat.financial.api.pay.utils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 9:55
 */
public class UrlUtils {
    public static String mapToUrl(Map<String, String> params) {
        AtomicReference<String> url = new AtomicReference<>("");
        params.forEach((s, s2) -> {
            url.set(url.get() + "&" + s + "=" + s2);
        });
        return url.get().substring(1);
    }
}
