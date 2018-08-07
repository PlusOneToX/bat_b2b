package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 8:47
 */
@Data
@ApiModel(description = "C端客户第三方登录")
public class CustomerThirdPartyLoginCmd {
    @NotBlank(message = "P_DISTRIBUTOR_CUSTOMER_OPEN_ID_NULL")
    @ApiModelProperty(value = "第三方系统唯一标识码", required = true, example = "bat")
    private String openId;
    @ApiModelProperty(value = "第三方系统其他标识码(如的标识码)", required = false, example = "sfgregefqwew24356546523ffsf")
    private String otherUid;
    @ApiModelProperty(value = "手机号", required = false, example = "bat")
    private String phone;
}
