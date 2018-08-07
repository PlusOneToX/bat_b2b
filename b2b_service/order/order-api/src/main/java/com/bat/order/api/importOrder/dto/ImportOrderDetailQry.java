package com.bat.order.api.importOrder.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/12 14:22
 */
@Data
public class ImportOrderDetailQry {
    @ApiModelProperty(value = "导入编号")
    private Integer id;
    @ApiModelProperty(value = "暂不知")
    private Short accessType;
    @ApiModelProperty(value = "暂不知")
    private String groupName;
    @ApiModelProperty(value = "物流编号")
    private String logisticsNo;
    @ApiModelProperty(value = "暂不知")
    private Short isShowFee;
}
