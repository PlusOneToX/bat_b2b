package com.bat.financial.dao.pay.dataobject;

/**
 * @author: lim
 * @description: 平台凭证过期时间 单位秒
 * @date: 2018/5/25 19:26
 */
public class ExpireTime {
    public static final long WXPAY_JSAPI = 2 * 60 * 60;
    public static final long WXPAY_APP = 2 * 60 * 60;
    public static final long WXPAY_H5 = 5 * 60;
    public static final long WXPAY_NATIVE = 2 * 60 * 60;
    public static final long WXPAY_MINI_PROGRAM = 2 * 60 * 60;
    public static final long ALIPAY_FACE_TO_FACE = 2 * 60 * 60;

    public static final long DEFAULT_EXPIRE_TIME = 1 * 60;
}
