package com.bat.thirdparty.msgcenter.api.dto.template;

import lombok.Data;

/**
 * 分销商审核通知
 */
@Data
public class TemplateDistributorExamine {
    /**
     * 申请时间
     */
    private TemplateValue date1;
    /**
     * 分销商名称
     */
    private TemplateValue thing5;
    /**
     * 备注信息
     */
    private TemplateValue thing4;

}
