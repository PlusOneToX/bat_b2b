package com.bat.financial.api.pay.dto.wechat;

import lombok.Data;

@Data
public class WxPayPayer {

    //这两个值至少要一个

    /**
     * 用户在服务商appid下的唯一标识。 下单前需获取到用户的Openid，Openid获取详见
     */
    private String sp_openid;


    /**
     * 用户在子商户appid下的唯一标识。若传sub_openid，那sub_appid必填。下单前需获取到用户的Openid，Openid获取详见
      */
    private String sub_openid;
}
