package com.bat.thirdparty.quanyi.dao.co;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ThirdQuanyiPageCO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "分销商id")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商名称")
    private String distributorName;

    @ApiModelProperty(value = "第三方手机号")
    private String thirdPhone;

    @ApiModelProperty(value = "第三方验证码（核心，在苏宁表示订单号）")
    private String thirdCode;

    @ApiModelProperty(value = "第三方最终sku")
    private String thirdSkuNo;

    @ApiModelProperty(value = "第三方用户备注")
    private String thirdUserRemark;

    @ApiModelProperty(value = "兑换活动id")
    private Integer exchangeId;

    @ApiModelProperty(value = "兑换活动名称")
    private String exchangeName;

    @ApiModelProperty(value = "兑换码id")
    private Integer exchangeCodeId;

    @ApiModelProperty(value = "明码")
    private String plainCode;

    @ApiModelProperty(value = "暗码")
    private String secretCode;

    @ApiModelProperty(value = "材质id")
    private Integer materialId;

    @ApiModelProperty(value = "材质名称")
    private String materialName;

    @ApiModelProperty(value = "c端客户id")
    private Integer customerId;

    @ApiModelProperty(value = "c端客户编号")
    private Integer customerNo;

    @ApiModelProperty(value = "b2b系统订单id")
    private Integer orderId;

    @ApiModelProperty(value = "状态 1.未验证 2.未兑换 3.已兑换 4已作废")
    private Short status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
