package com.bat.order.api.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/19 23:10
 */
@Data
@AllArgsConstructor
public class BusinessInfo {

    public static short PLATFORM_WEB = 1;

    public static short PLATFORM_ADMIN = 2;
    /**
     * 平台来源 web admin,前台管理后台
     */
    private short platform = PLATFORM_WEB;
    /**
     * 分销商id 业务员id
     */
    private Integer operatorId;
    /**
     * 业务名称 什么导出
     */
    private String businessName;
}
