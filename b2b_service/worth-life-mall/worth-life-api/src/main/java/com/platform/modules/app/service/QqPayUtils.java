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
package com.platform.modules.app.service;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.util.SignUtils;
import com.platform.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.*;

/**
 * @author 李鹏军
 */
@Slf4j
public class QqPayUtils {

    @SuppressWarnings("unchecked")
    public static <T> T createOrder(WxPayUnifiedOrderResult unifiedOrderResult, Map<Object, Object> request, String mchKey) {
        String prepayId = unifiedOrderResult.getPrepayId();
        if (StringUtils.isBlank(prepayId)) {
            throw new BusinessException(String.format("无法获取prepay id，错误代码： '%s'，信息：%s。",
                    unifiedOrderResult.getErrCode(), unifiedOrderResult.getErrCodeDes()));
        }

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = String.valueOf(System.currentTimeMillis());
        switch (request.get("trade_type").toString()) {
            case WxPayConstants.TradeType.MWEB: {
                return (T) new WxPayMwebOrderResult(unifiedOrderResult.getMwebUrl());
            }

            case WxPayConstants.TradeType.NATIVE: {
                return (T) new WxPayNativeOrderResult(unifiedOrderResult.getCodeURL());
            }

            case WxPayConstants.TradeType.APP: {
                // APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
                String appId = unifiedOrderResult.getAppid();
                if (StringUtils.isNotEmpty(unifiedOrderResult.getSubAppId())) {
                    appId = unifiedOrderResult.getSubAppId();
                }

                Map<String, String> configMap = new HashMap<>(8);
                // 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
                String partnerId = unifiedOrderResult.getMchId();
                if (StringUtils.isNotEmpty(unifiedOrderResult.getSubMchId())) {
                    partnerId = unifiedOrderResult.getSubMchId();
                }

                configMap.put("prepayid", prepayId);
                configMap.put("partnerid", partnerId);
                String packageValue = "Sign=QQPay";
                configMap.put("package", packageValue);
                configMap.put("timestamp", timestamp);
                configMap.put("noncestr", nonceStr);
                configMap.put("appid", appId);

                final WxPayAppOrderResult result = WxPayAppOrderResult.builder()
                        .sign(SignUtils.createSign(request, WxPayConstants.SignType.MD5, mchKey, null))
                        .prepayId(prepayId)
                        .partnerId(partnerId)
                        .appId(appId)
                        .packageValue(packageValue)
                        .timeStamp(timestamp)
                        .nonceStr(nonceStr)
                        .build();
                return (T) result;
            }

            case WxPayConstants.TradeType.JSAPI: {
                String signType = WxPayConstants.SignType.MD5;
                String appid = unifiedOrderResult.getAppid();
                if (StringUtils.isNotEmpty(unifiedOrderResult.getSubAppId())) {
                    appid = unifiedOrderResult.getSubAppId();
                }

                WxPayMpOrderResult payResult = WxPayMpOrderResult.builder()
                        .appId(appid)
                        .timeStamp(timestamp)
                        .nonceStr(nonceStr)
                        .packageValue("prepay_id=" + prepayId)
                        .signType(signType)
                        .build();

                payResult.setPaySign(SignUtils.createSign(request, WxPayConstants.SignType.MD5, mchKey, null));
                return (T) payResult;
            }

            default: {
                throw new BusinessException("该交易类型暂不支持");
            }
        }

    }

    /**
     * 方法描述：根据签名加密请求参数
     * 创建时间：2017年6月8日  上午11:28:52
     * 作者： xubo
     *
     * @param
     * @return
     */
    public static String arraySign(Map<Object, Object> params, String paySignKey) {
        Set<Object> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuilder temp = new StringBuilder();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            temp.append(valueString);
        }
        temp.append("&key=");
        temp.append(paySignKey);
        return getMessageDigest(temp.toString());
    }

    public static String getMessageDigest(String data) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(WxPayConstants.SignType.MD5);
            byte[] array = md.digest(data.getBytes("UTF-8"));

            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString().toUpperCase();
    }


    public static String convertMap2Xml(Map<Object, Object> paraMap) {
        StringBuilder xmlStr = new StringBuilder();
        if (paraMap != null) {
            xmlStr.append("<xml>");
            Set<Object> keySet = paraMap.keySet();
            Iterator<Object> keyIte = keySet.iterator();
            while (keyIte.hasNext()) {
                String key = (String) keyIte.next();
                String val = String.valueOf(paraMap.get(key));
                xmlStr.append("<");
                xmlStr.append(key);
                xmlStr.append(">");
                xmlStr.append(val);
                xmlStr.append("</");
                xmlStr.append(key);
                xmlStr.append(">");
            }
            xmlStr.append("</xml>");
        }
        return xmlStr.toString();
    }
}
