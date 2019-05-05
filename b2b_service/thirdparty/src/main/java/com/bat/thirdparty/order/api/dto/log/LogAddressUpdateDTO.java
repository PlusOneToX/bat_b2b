package com.bat.thirdparty.order.api.dto.log;

import com.bat.thirdparty.order.api.dto.common.AddressQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class LogAddressUpdateDTO {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "COMMON_ID_NULL")
    private Integer id;


    @ApiModelProperty(value = "地址对象")
    @Valid
    private AddressQry address;
}
