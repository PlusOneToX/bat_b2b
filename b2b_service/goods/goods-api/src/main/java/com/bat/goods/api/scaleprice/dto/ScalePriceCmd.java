package com.bat.goods.api.scaleprice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ScalePriceCmd", description = "价格等级信息")
public class ScalePriceCmd {

    @ApiModelProperty(value = "价格等级ID", required = false)
    private Integer id;
    @NotBlank(message = "P_GOODS_SCALE_PRICE_NAME_NULL")
    @ApiModelProperty(value = "价格等级名称,最长20个字符", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "对应erp价目表价格字段", required = false, example = "F_PAEZ_Price2")
    private String erpField;
    @ApiModelProperty(value = "价格等级描述", required = false, example = "价格等级描述")
    private String description;
    @NotNull(message = "P_GOODS_RETAIL_FLAG_NULL")
    @ApiModelProperty(value = "是否建议零售价:0 否 1 是", required = true, example = "0")
    private Short retailFlag;
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @NotNull(message = "P_GOODS_SORT_NULL")
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;

}
