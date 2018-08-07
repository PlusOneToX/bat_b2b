package com.bat.system.api.storesetting.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/30 14:01
 */
@Data
@ApiModel(value = "MobileGoodsSortCmd", description = "栏目id")
public class MobileGoodsSortCmd {
    @NotNull(message = "P_MOBILE_ID_NULL")
    @ApiModelProperty(value = "移动端首页id", required = true, example = "1")
    private Integer id;

    List<ColumnGoodsItem> goodsIds;
}
