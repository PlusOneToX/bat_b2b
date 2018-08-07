package com.bat.order.api.importOrder.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/2 13:51
 */
@Data
public class TempDownLoadQry {
    @ApiModelProperty(value = "1 普通订单导入 2定制订单导入")
    private Integer fileType;
}
