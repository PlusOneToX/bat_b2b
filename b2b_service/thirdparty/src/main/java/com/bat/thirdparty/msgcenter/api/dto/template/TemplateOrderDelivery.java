package com.bat.thirdparty.msgcenter.api.dto.template;

import lombok.Data;

/**
 * 订单发货提醒
 */
@Data
public class TemplateOrderDelivery {
    /**
     * 订单编号
     */
    private TemplateValue character_string1;
    /**
     * 快递公司
     */
    private TemplateValue name3;
    /**
     * 快递单号
     */
    private TemplateValue character_string4;
    /**
     * 发货时间
     */
    private TemplateValue date10;

    /**
     * 温馨提示
     */
    private TemplateValue thing7;

}
