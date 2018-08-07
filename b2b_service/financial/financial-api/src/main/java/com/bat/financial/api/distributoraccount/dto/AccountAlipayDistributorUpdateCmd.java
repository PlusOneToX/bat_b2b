package com.bat.financial.api.distributoraccount.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/20 9:56
 */
@Data
@ApiModel(value = "AccountAlipayDistributorUpdateCmd", description = "分销商收款账户(支付宝)更新")
public class AccountAlipayDistributorUpdateCmd {

    @NotNull(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    private List<DistributorInfo> distributorInfos;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_ACCOUNT_NAME_NULL")
    @ApiModelProperty(value = "收款账户名称", required = true, example = "")
    private String accountName;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_APP_ID_NULL")
    @ApiModelProperty(value = "微信应用appid", required = true, example = "wx63c410d5de384240")
    private String appId;

    @NotNull(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_APP_APP_PUBLIC_SECRET_NULL")
    @ApiModelProperty(value = "支付宝公钥", required = true, example = "1")
    private String appPublicSecret;

    @NotBlank(message = "P_ACCOUNT_ALIPAY_DISTRIBUTOR_APP_PRIVATE_SECRET_NULL")
    @ApiModelProperty(value = "支付宝私钥", required = true, example = "1341583701")
    private String appPrivateSecret;

}
