package com.bat.order.api.order.dto.orderquery.common;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * @author: lim
 * @description: 订单商品信息
 * @date: 2018/6/22 12:43
 */
@Data
public class OrderGoodsDTO implements Cloneable {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("订单id")
    private Integer orderId;
    @ApiModelProperty("行项目序号")
    private Integer serialNumber;
    @ApiModelProperty("商品(SPU)id")
    private Integer goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品编号")
    private String goodsNo;
    @ApiModelProperty("货品(SKU)id")
    private Integer itemId;
    @ApiModelProperty("货品编号")
    private String itemCode;
    @ApiModelProperty("条形码")
    private String barCode;
    @ApiModelProperty("货品名称")
    private String itemName;
    @ApiModelProperty("商品图片地址")
    private String imageUrl;
    @ApiModelProperty("商品类型 1-普通 2-定制")
    private Short goodsType;
    @ApiModelProperty("定制类型 0、标准定制 1、DIY定制")
    private Short diyType;
    @ApiModelProperty("规格值名称")
    private String specsName;
    @ApiModelProperty("颜色值名称")
    private String colorName;
    @ApiModelProperty("重量（克）")
    private BigDecimal weight;
    @ApiModelProperty("长度,mm")
    private BigDecimal length;
    @ApiModelProperty("宽(mm)")
    private BigDecimal width;
    @ApiModelProperty("高（mm）")
    private BigDecimal height;
    @ApiModelProperty("锁库仓库id")
    private Integer warehouseId;
    @ApiModelProperty("货品总数量")
    private Integer itemCount;
    @ApiModelProperty("在库货品数量")
    private Integer itemInCount;
    @ApiModelProperty("vmi货品数量")
    private Integer itemVmiCount;
    @ApiModelProperty("在途货品数量")
    private Integer itemOnWayCount;
    @ApiModelProperty("已发货数量")
    private Integer deliverCount;
    @ApiModelProperty("未发货数量")
    private Integer unDeliverCount;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @SneakyThrows
    @Override
    public OrderGoodsDTO clone() {
        return (OrderGoodsDTO)super.clone();
    }
}
