package com.bat.distributor.api.subaccount.dto;

import com.bat.distributor.api.user.dto.user.UserPhoneVerifyCodeCmd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubAccountSalemanWechatCmd extends UserPhoneVerifyCodeCmd {

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "openId")
    @NotBlank(message = "D_COMMON_OPENID_NULL")
    private String openId;


}
