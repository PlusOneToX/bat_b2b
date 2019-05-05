package com.bat.thirdparty.quanyi.api.dto;


import com.bat.thirdparty.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdQuanyiQry extends BasePageParamQry {

    @ApiModelProperty(value = "第三方手机号")
    private String thirdPhone;

    @ApiModelProperty(value = "第三方验证码（核心，在苏宁表示订单号）")
    private String thirdCode;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "暗码")
    private String secretCode;

    @ApiModelProperty(value = "状态 1.未验证 2.未兑换 3.已兑换 4已作废")
    private Short status;

}