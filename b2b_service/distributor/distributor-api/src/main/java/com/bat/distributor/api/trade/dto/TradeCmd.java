package com.bat.distributor.api.trade.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TradeAddCmd", description = "分销商收款条件信息")
public class TradeCmd {

    @ApiModelProperty(value = "分销商收款条件id", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_TRADE_NAME_NULL")
    @ApiModelProperty(value = "分销商收款条件名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商收款条件名称", required = false, example = "bat")
    private String nameEn;
    @NotNull(message = "P_DISTRIBUTOR_PAY_WAY")
    @ApiModelProperty(value = "结算方式，1为立即支付，2为期间结算", required = true, example = "1")
    private Short payWay;
    @ApiModelProperty(value = "结算时长", required = false, example = "20")
    private Integer settlingTime;
    @ApiModelProperty(value = "erp收款条件编号", required = false, example = "1224434")
    private String erpSettleAccountNo;
    @NotNull(message = "P_DISTRIBUTOR_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
}
