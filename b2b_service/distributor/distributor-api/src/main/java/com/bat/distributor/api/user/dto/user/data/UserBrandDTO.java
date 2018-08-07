package com.bat.distributor.api.user.dto.user.data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商品牌列表")
public class UserBrandDTO implements Serializable {

    private Integer id;
    @ApiModelProperty(value = "品牌名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "品牌英文名称", example = "bat")
    private String nameEn;

}
