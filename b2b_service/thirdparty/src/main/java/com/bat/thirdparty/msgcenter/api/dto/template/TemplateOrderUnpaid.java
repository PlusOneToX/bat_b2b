package com.bat.thirdparty.msgcenter.api.dto.template;

import lombok.Data;

/**
 * 订单未支付提醒
 */
@Data
public class TemplateOrderUnpaid {
    /**
     * 订单编号
     */
    private TemplateValue character_string5;
    /**
     * 订单金额
     */
    private TemplateValue amount7;
    /**
     * 下单时间
     */
    private TemplateValue date6;
    /**
     * 温馨提醒
     */
    private TemplateValue thing4;

}
