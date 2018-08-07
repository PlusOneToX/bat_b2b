package com.bat.goods.manager.mq.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/7/8 20:44
 */
@Data
public class GoodsSaleDTO implements Serializable {
    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * ordergoodsId
     */
    private Integer orderGoodsId;

    /**
     * 货品id
     */
    private Integer itemId;
    /**
     * 销售数量
     */
    private Integer saleNum;
    /**
     * 数量变更类型：1 增加 2 减少
     */
    private Short changeType;

    /**
     * 商品促销活动
     */
    private Integer goodsPromotionId;
    /**
     * 订单促销活动
     */
    private Integer orderPromotionId;
    /**
     * 拼团秒杀活动
     */
    private Integer spellGroupId;

}
