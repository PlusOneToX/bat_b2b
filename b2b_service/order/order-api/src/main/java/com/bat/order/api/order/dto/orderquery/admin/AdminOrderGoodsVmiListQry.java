package com.bat.order.api.order.dto.orderquery.admin;

import com.bat.order.api.basic.BaseSearchQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 沙漠
 */
@Data
@ApiModel(value = "AdminOrderGoodsVmiListQry", description = "VMI订单明细")
public class AdminOrderGoodsVmiListQry extends BaseSearchQry {

    public AdminOrderGoodsVmiListQry() {
        super.attributeMapper.put((short)1, "setItemCode");
        super.attributeMapper.put((short)2, "setOrderId");
        super.attributeMapper.put((short)3, "setOrderErpNo");
    }

    @ApiModelProperty("订单号")
    private Integer orderId;

    @ApiModelProperty("货品编号")
    private String itemCode;

    @ApiModelProperty("erp订单编号")
    private String orderErpNo;

    @ApiModelProperty("付款状态")
    private Short payStatus;

    @ApiModelProperty("订单状态")
    private Short orderStatus;

    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
