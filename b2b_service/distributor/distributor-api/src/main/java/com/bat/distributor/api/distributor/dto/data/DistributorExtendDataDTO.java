package com.bat.distributor.api.distributor.dto.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商扩展信息")
public class DistributorExtendDataDTO {

    private Integer id;
    @ApiModelProperty(value = "营业执照或身份证号", example = "R00343")
    private String certNo;
    @ApiModelProperty(value = "信息是否同步到erp 1.是 0.否", example = "1")
    private Short erpFlag;
    @ApiModelProperty(value = "ERP余额是否同步 1.是 0.否", example = "1")
    private Short erpBalanceFlag;
    @ApiModelProperty(value = "erp客户id", example = "1665432")
    private Integer erpId;
    @ApiModelProperty(value = "erp客户编号", example = "R00343")
    private String erpNo;
    @ApiModelProperty(value = "分销商备注", example = "R00343")
    private String comment;
    @ApiModelProperty(value = "是否开启分销模式 0 不开启, 1 开启", example = "0")
    private Short distributionFlag;
    @ApiModelProperty(value = "分销结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)", example = "1")
    private Short distributionMode;

    @ApiModelProperty(value = "分销商付款方式： 1支付宝, 2微信，3银行（当分销商结算方式为3的时候也就是自己收款的时候，这个分销商付款方式就生效了，分校层数据中上层分销商的付款方式就跟这个字段走）")
    private Short distributionPayWay;

    @ApiModelProperty(value = "分销订单是否自动审核： 1 是 2 否（注意：下级分销订单自动审核）", example = "1")
    private Short distributionAutoFlag;
    @ApiModelProperty(value = "是否开启C端模式 0 不开启, 1 开启", example = "0")
    private Short customerFlag;
    @ApiModelProperty(value = "C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)", example = "1")
    private Short customerMode;
    @ApiModelProperty(value = "分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)", example = "0")
    private Short distributionPromotionFlag;

    @ApiModelProperty(value = "是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值、其他情况不要传）",required = false,example = "0")
    private Short subAccountFlag;
}
