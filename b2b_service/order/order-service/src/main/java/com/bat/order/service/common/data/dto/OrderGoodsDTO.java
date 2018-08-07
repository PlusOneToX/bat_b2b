package com.bat.order.service.common.data.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class OrderGoodsDTO {
    /**
     * 订单明细序号
     */
    private Integer serialNumber;

    /**
     * 货品编码
     */
    private String itemCode;

    /**
     * 是否赠品 1 非赠品 2 赠品
     */
    private Short itemType;
    /**
     * 是否预售 1 是 0 否
     */
    private Short mtoType;
    /**
     * 是否支持超卖 1、支持 0 不支持
     */
    private Short oversoldFlag;
    /**
     * 商品促销活动Id(活动条件id)
     */
    private Integer goodsPromotionId;
    /**
     * 订单促销活动Id(活动条件id)
     */
    private Integer orderPromotionId;
    /**
     * 拼团秒杀活动id
     */
    private Integer groupSeckillId;
    /**
     * 优惠券码
     */
    private String couponNo;
    /**
     * 货品购买数量
     */
    private Integer itemCount;
    /**
     * 货品在库数量
     */
    private Integer itemInCount;
    /**
     * 货品在途数量
     */
    private Integer itemOnWayCount;
    /**
     * 货品销售单价（如果有值，已此值为货品销售单价，没值，需通过计算获取货品销售单价）
     */
    private BigDecimal salePrice;
    /**
     * 货品预售数量
     */
    private Integer itemMtoCount;
    private OrderGoodsDiyDTO diy;
    /**
     * 兑换码
     */
    private List<String> codes;
    /**
     * 快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）
     */
    private Short mailSetting;

}