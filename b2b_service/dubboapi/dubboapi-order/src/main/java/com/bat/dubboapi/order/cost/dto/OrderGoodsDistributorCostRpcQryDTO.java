package com.bat.dubboapi.order.cost.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderGoodsDistributorCostRpcQryDTO implements Serializable {
    private static final long serialVersionUID = -3804251395412966251L;
    private Integer id;
    private Integer orderId;
    private Integer orderGoodsId;

    private Integer distributorId;

    private Short itemType;

    private BigDecimal salePrice;

    private BigDecimal actualPrice;

    private BigDecimal platformPrice;

    private Integer goodsPromotionId;

    private Integer orderPromotionId;

    private Integer spellGroupId;

    private Date createTime;

    private Date updateTime;

    private BigDecimal rebateVoucherAmount;

}