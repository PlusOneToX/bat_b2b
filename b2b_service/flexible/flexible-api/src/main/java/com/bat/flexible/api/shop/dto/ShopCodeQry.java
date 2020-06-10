package com.bat.flexible.api.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShopCodeQry {

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "门店编码")
    @NotBlank(message = "S_SHOP_CODE_NULL")
    private String shopCode;
}
