package com.bat.financial.api.distributoraccount.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/19 10:09
 */
@Data
@ApiModel(value = "AccountAppId", description = "平台账户id")
public class AccountAppId {

    @NotNull(message = "P_ACCOUNT_APP_ID_NULL")
    @ApiModelProperty(value = "appId", required = true, example = "1")
    private String appId;

    @ApiModelProperty(value = "subMchid", required = false, example = "1613278613")
    private String subMchid;

}
