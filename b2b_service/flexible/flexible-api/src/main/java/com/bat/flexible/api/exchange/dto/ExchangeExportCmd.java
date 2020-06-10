package com.bat.flexible.api.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ExchangeExportCmd {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "发卡分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "导出记录单名称")
    private String exportName;

    @ApiModelProperty(value = "兑换卡活动id")
    private Integer exchangeId;

    @ApiModelProperty(value = "出库数量")
    private Integer outNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否已审核 0否 1是")
    private Short examineFlag;

}
