package com.bat.thirdparty.msgcenter.api.dto.template;

import lombok.Data;

/**
 * 订单状态通知
 */
@Data
public class TemplateOrderStatus {
    /**
     * 订单编号
     */
    private TemplateValue character_string1;

    /**
     * 订单状态
     */
    private TemplateValue phrase2;


    /**
     * 温馨提示
     */
    private TemplateValue thing11;

}
