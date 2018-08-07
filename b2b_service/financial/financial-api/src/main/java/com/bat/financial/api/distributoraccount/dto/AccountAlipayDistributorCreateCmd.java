package com.bat.financial.api.distributoraccount.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:55
 */
@Data
@ApiModel(value = "AccountAlipayDistributorCreateCmd", description = "分销商收款账户(支付宝)新增")
public class AccountAlipayDistributorCreateCmd {

    private List<DistributorInfo> distributorInfos;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "收款账户名称", required = true, example = "")
    private String accountName;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_APP_ID_NULL")
    @ApiModelProperty(value = "微信应用appid", required = true, example = "wx63c410d5de384240")
    private String appId;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_APP_APP_PUBLIC_SECRET_NULL")
    @ApiModelProperty(value = "支付宝公钥", required = true, example = "1")
    private String appPublicSecret;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_APP_PRIVATE_SECRET_NULL")
    @ApiModelProperty(value = "支付宝私钥", required = true, example = "1341583701")
    private String appPrivateSecret;
}
