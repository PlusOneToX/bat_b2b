package com.bat.distributor.api.trade.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商收款条件信息")
public class TradeDTO {

    private Integer id;
    @ApiModelProperty(value = "分销商收款条件名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "分销商收款条件名称", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "结算方式，1为立即支付，2为期间结算", required = true, example = "1")
    private Short payWay;
    @ApiModelProperty(value = "结算时长", required = true, example = "20")
    private Integer settlingTime;
    @ApiModelProperty(value = "erp收款条件编号", required = false, example = "1224434")
    private String erpSettleAccountNo;
    @ApiModelProperty(value = "状态, 1启用0停用", required = true, example = "0")
    private Short openFlag;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
