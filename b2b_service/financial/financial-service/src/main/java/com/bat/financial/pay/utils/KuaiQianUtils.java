package com.bat.financial.pay.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/24 9:18
 */
public class KuaiQianUtils {
    public static StringBuilder appendParam(StringBuilder returns, String paramId, String paramValue) {
        if (returns != null) {
            if (paramValue != "") {
                returns.append("&").append(paramId).append("=").append(paramValue);
            }
        } else {
            if (paramValue != "") {
                returns = new StringBuilder().append(paramId).append("=").append(paramValue);
            }
        }
        return returns;
    }

    public static String encodeParam(String returns, String paramId, String paramValue)
        throws UnsupportedEncodingException {
        if (!"".equals(returns)) {
            if (returns.contains(paramId + '=' + paramValue)) {
                return returns.replace((paramId + '=' + paramValue),
                    (paramId + '=' + URLEncoder.encode(paramValue, "utf-8")));
            }
        }
        return returns;
    }
}
