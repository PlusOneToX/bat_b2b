package com.bat.financial.pay.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class WechatConfig {

    /**
     * 微信支付服务商JSAPI下单URL (V3)
     */
    @Value("${wechat.url.partner.v3.jsapi.create}")
    private String wxPayPartnerCreateJsapiUrl;

    // https://api.mch.weixin.qq.com/v3/profitsharing/orders
    /**
     * 微信支付服务商分账请求(V3版本)
     */
    @Value("${wechat.url.partner.v3.subaccount.create}")
    private String wxPayPartnerSubAccountCreateUrl;

}
