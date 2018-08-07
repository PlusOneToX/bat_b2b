package com.bat.financial.api.pay.dto.wechat;

import lombok.Data;

@Data
public class WxPayAmount {

    /**
     * 订单总金额，单位为分
     */
    private Integer total;

    /**
     * CNY：人民币，境内商户号仅支持人民币。
     */
    private String currency;
}

