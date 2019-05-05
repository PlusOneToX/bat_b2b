package com.bat.thirdparty.quanyi.api.dto;


import com.bat.thirdparty.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdQuanyiLogQry extends BasePageParamQry {

    @ApiModelProperty(value = "权益id")
    private Integer thirdQuanyiId;

}