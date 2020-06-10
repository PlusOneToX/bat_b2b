package com.bat.flexible.api.exchange.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExchangeCodePageQry extends BasePageParamQry {


    //兑换方式
    private Short exchangeWay;

    //核销状态 1、未使用 2、已核销 3、已作废
    private Short status;

    //搜索词 兑换码/核销人/订单ID
    private String content;

    private Integer exchangeId;

    //批次ID
    private Integer exchangeFactoryId;

    /**
     * 是否将暗码解密
     */
    private Boolean encodeFlag=false;

    @ApiModelProperty(value = "导出记录id")
    private Integer exchangeExportId;


}
