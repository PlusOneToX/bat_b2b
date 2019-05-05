package com.bat.thirdparty.order.api.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderCommonCmd {

    @ApiModelProperty(value = "第三方订单号")
    private String orderNo;

    @ApiModelProperty(value = "地址对象")
    private AddressQry address;

    @ApiModelProperty(value = "用户信息对象")
    private UserInfoQry userInfo;

    @ApiModelProperty(value = "备注")
    private String remark;

}
