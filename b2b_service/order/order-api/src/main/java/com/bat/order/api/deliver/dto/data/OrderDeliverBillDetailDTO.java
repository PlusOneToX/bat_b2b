package com.bat.order.api.deliver.dto.data;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/7/14 22:33
 */
@Data
public class OrderDeliverBillDetailDTO {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单发货单id")
    private Integer orderDeliverBillId;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("订单明细id")
    private Integer orderGoodsId;
    @ApiModelProperty("行项目序号")
    private Integer serialNumber;
    @ApiModelProperty("商品id")
    private Integer goodsId;
    @ApiModelProperty("商品自定义编号")
    private String goodsNo;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("货品id")
    private Integer itemId;
    @ApiModelProperty("货品编号")
    private String itemCode;
    @ApiModelProperty("条形码")
    private String barCode;
    @ApiModelProperty("货品名称")
    private String itemName;
    @ApiModelProperty("发货数量")
    private Integer count;
    @ApiModelProperty("出库仓库名称")
    private Integer warehouseId;
    @ApiModelProperty("出库仓库名称")
    private String warehouseName;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
