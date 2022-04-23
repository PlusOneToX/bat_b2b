/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.common.utils;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

/**
 * https://github.com/qcloudsms/qcloudsms_java/blob/master/src/test/java/QcloudSmsTest.java
 *
 * @author lipengjun
 */
public class SmsUtil {

    /**
     * 指定模板ID群发
     * 签名参数未提供或者为空时，会使用默认签名发送短信
     *
     * @param appid        appid
     * @param appkey       appkey
     * @param phoneNumbers 手机号
     * @param templateId   模板ID
     * @param params       参数
     * @param sign         签名
     * @return SmsMultiSenderResult
     */
    public static SmsMultiSenderResult crSendSms(int appid, String appkey, String[] phoneNumbers, int templateId, String[] params, String sign) {
        SmsMultiSenderResult result = new SmsMultiSenderResult();
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            result = msender.sendWithParam("86", phoneNumbers,
                    templateId, params, sign, "", "");
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 指定模板ID单发短信
     * 签名参数未提供或者为空时，会使用默认签名发送短信
     *
     * @param appid       appid
     * @param appkey      appkey
     * @param phoneNumber 手机号
     * @param templateId  模板ID
     * @param params      参数
     * @param smsSign     签名
     * @return SmsSingleSenderResult
     */
    public static SmsSingleSenderResult crSendSms(int appid, String appkey, String phoneNumber, int templateId, String[] params, String smsSign) {
        SmsSingleSenderResult result = new SmsSingleSenderResult();
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params, smsSign, "", "");
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 单发短信
     *
     * @param appid       appid
     * @param appkey      appkey
     * @param phoneNumber 手机号
     * @param msg         短信内容
     * @return SmsSingleSenderResult
     */
    public static SmsSingleSenderResult crSendSms(int appid, String appkey, String phoneNumber, String msg) {
        SmsSingleSenderResult result = new SmsSingleSenderResult();
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            result = ssender.send(0, "86", phoneNumber, msg, "", "");
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 群发短信
     *
     * @param appid        appid
     * @param appkey       appkey
     * @param phoneNumbers 手机号
     * @param msg          短信内容
     * @return SmsMultiSenderResult
     */
    public static SmsMultiSenderResult crSendSms(int appid, String appkey, String[] phoneNumbers, String msg) {
        SmsMultiSenderResult result = new SmsMultiSenderResult();
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            result = msender.send(0, "86", phoneNumbers, msg, "", "");
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return result;
    }
}
