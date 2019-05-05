package com.bat.thirdparty.msgcenter.api.dto.template;

import lombok.Data;

/**
 * 新订单提醒
 */
@Data
public class TemplateNewOrder {
    /**
     * 订单号
     */
    private TemplateValue character_string2;
    /**
     * 下单用户
     */
    private TemplateValue name1;
    /**
     * 订单金额
     */
    private TemplateValue amount3;

}
