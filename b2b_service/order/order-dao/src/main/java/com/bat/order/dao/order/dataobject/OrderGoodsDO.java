package com.bat.order.dao.order.dataobject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bat.order.dao.stock.dataobject.OrderGoodsStockDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;

import lombok.Data;

@Data
public class OrderGoodsDO {
    private Integer id;
    private Integer orderId;
    private Integer serialNumber;
    private Integer goodsId;
    private String goodsName;
    private String goodsNo;
    private Integer itemId;
    private String itemCode;
    private String barCode;
    private String itemName;
    private String imageUrl;
    private Short goodsType;
    private Short diyType;
    private String specsName;
    private String colorName;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private Integer warehouseId;
    private Integer itemCount;
    private Integer itemInCount;
    private Integer itemVmiCount;
    private Integer itemOnWayCount;
    private Integer deliverCount;
    private Integer unDeliverCount;
    private Date createTime;
    private Date updateTime;

    private Short itemType;
    /**
     * 定制信息
     */
    private OrderGoodsDiyDO orderGoodsDiy;
    /**
     * 订单锁库
     */
    private List<OrderGoodsStockDO> orderGoodsStocks;

    /**
     * 订单明细费用（分销商）
     */
    private OrderGoodsDistributorCostDO orderGoodsDistributorCost;

    /**
     * 订单明细费用（C端客户）
     */
    private OrderGoodsCustomerCostDO orderGoodsCustomerCost;
    /**
     * 订单明细兑换卡列表
     */
    private List<OrderGoodsExchangeCodeDO> exchangeCodes;
    /**
     * 订单明细第三方兑换码列表
     */
    private List<OrderGoodsThirdCodeDO> thirdCodes;
    /**
     * 在库数量变更
     */
    private Integer inChangeNum;
    /**
     * vmi数量变更
     */
    private Integer vmiChangeNum;
    /**
     * 在途数量变更
     */
    private Integer onWayChangeNum;

    /**
     * 货品销售单据（如果有值，已此值为货品销售单价，没值，需通过计算获取货品销售单价）
     */
    private BigDecimal salePrice;

}