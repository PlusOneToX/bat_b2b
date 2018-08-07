package com.bat.system.api.storesetting.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/30 14:01
 */
@Data
@ApiModel(value = "ColumnGoodsSortCmd", description = "栏目id")
public class ColumnGoodsSortCmd {
    // @NotNull(message = "P_COLUMN_ID_NULL")
    // @ApiModelProperty(value = "栏目ID", required = true, example = "1")
    // private Integer id;
    //
    // List<ColumnGoodsItem> goodsIds;
    //
    // @NotNull(message = "P_COLUMN_OPERATION_TYPE_NULL")
    // @ApiModelProperty(value = "操作类型", required = true, example = "1")
    // private Short operationType;
}
