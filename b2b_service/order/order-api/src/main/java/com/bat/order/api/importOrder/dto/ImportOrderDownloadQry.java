package com.bat.order.api.importOrder.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 14:22
 */
@Data
public class ImportOrderDownloadQry {

    @NotNull(message = "P_IMPORT_ORDER_ID_NULL")
    @ApiModelProperty(value = "导入订单id")
    private long importOrderId;

    @NotNull(message = "P_ORDER_FLAG_NULL")
    @ApiModelProperty(value = "下单状态：1-已下单 2-未下单")
    private short orderFlag;
}
