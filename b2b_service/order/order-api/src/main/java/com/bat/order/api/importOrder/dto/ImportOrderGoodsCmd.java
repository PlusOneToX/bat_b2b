package com.bat.order.api.importOrder.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/16 10:05
 */
@Data
public class ImportOrderGoodsCmd {

    private Integer importOderGoodsId;

    @ApiModelProperty("实际下单数量")
    private Integer actualOrderCount;
}
