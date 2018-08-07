package com.bat.order.api.order.dto.orderquery.admin;

import com.bat.order.api.basic.BaseSearchQry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 沙漠
 */
@Data
@ApiModel(value = "AdminOrderDeliverBillListQry", description = "订单发货")
public class AdminOrderDeliverBillListQry extends BaseSearchQry {

    public AdminOrderDeliverBillListQry() {
        super.attributeMapper.put((short)1, "setId");
        super.attributeMapper.put((short)2, "setDeliverErpNo");
        super.attributeMapper.put((short)3, "setOrderNo");
        super.attributeMapper.put((short)4, "setUserName");
    }

    @ApiModelProperty("是否需要推送第三方,1 为是需要推送，0为不需要推送")
    private Short push;

    @ApiModelProperty("同步给第三方成功与否: 1为同步成功，0为同步失败")
    private Short pushStatus;

    @ApiModelProperty("发货单流水号")
    private Integer id;

    @ApiModelProperty("erp出库单号")
    private String deliverErpNo;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("收货人")
    private String userName;

    @ApiModelProperty("是否已同步erp 0否1是")
    private Short synErpFlag;

}
