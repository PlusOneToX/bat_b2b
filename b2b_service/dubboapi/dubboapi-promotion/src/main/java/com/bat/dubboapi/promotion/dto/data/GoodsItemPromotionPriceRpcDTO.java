package com.bat.dubboapi.promotion.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public class GoodsItemPromotionPriceRpcDTO implements Serializable {
    private Integer serialNumber;
    private Integer goodsId;
    private Integer itemId;
    private Integer materialId;
    private Integer modelId;
    /**
     * 会员价
     */
    private BigDecimal salePrice;
    /**
     * 实际价格
     */
    private BigDecimal actualPrice;
    /**
     * 货品总数量
     */
    private Integer itemCount;
    /**
     * 在库货品数量
     */
    private Integer itemInCount;
    /**
     * vmi货品数量
     */
    private Integer itemVmiCount;
    /**
     * 在途货品数量
     */
    private Integer itemOnWayCount;
    /**
     * 商品促销活动id(活动规则id)
     */
    private Integer goodsPromotionId;
    /**
     * 订单促销活动id(活动规则id)
     */
    private Integer orderPromotionId;
    /**
     * 拼团秒杀活动id
     */
    private Integer spellGroupId;
    /**
     * 优惠券码
     */
    private String couponNo;
    /**
     * 优惠形式，1满减  2满折 3兑换
     */
    private Short couponMethod;
    /**
     * 是否收取快递费 0否 1是
     */
    private Short deliveryFeeFlag;
    /**
     * 是否赠品 1 非赠品 2 赠品
     */
    private Short itemType;
    /**
     * 在途商品是否参与活动 1.参与，0.不参与
     */
    private Short onWayFlag;
    /**
     * 优先使用现有库存 1、是 0、否
     */
    private Short existFlag;
    /**
     * 是否支持超卖或预售（当先试用现有库存时为超卖，不先试用库存时为预售） 1、支持 0、不支持
     */
    private Short mtoFlag;
    /**
     * B_PROMOTION_SUCCESS=活动计算成功
     * B_PROMOTION_EXPIRE_ERROR=哎呀！活动已过期，商品将按原价计算，是否继续下单购买呢！
     * B_PROMOTION_GROUP_SECKILL_MIN_NUM_ERROR=哎呀！拼团秒杀数量未达到最小起批量，请您重新调整拼团数量！
     * B_PROMOTION_GROUP_MAX_NUM_ERROR=哎呀！拼团数量已超过可拼数量，请您重新调整拼团数量！
     * B_PROMOTION_SECKILL_MAX_NUM_ERROR=哎呀！秒杀数量已超过可秒数量，请您重新调整秒杀数量！
     * B_PROMOTION_CONDITION_ERROR=哎呀！购买的商品数量或金额未达到活动条件，是否继续下单购买呢！
     * B_PROMOTION_COUPON_ERROR=哎呀！优惠券不符合条件，请重新选择下单！
     */
    private String flag;
    /**
     * 活动提示
     */
    private String msg;

    public Short getExistFlag() {
        return existFlag;
    }

    public void setExistFlag(Short existFlag) {
        this.existFlag = existFlag;
    }

    public Short getMtoFlag() {
        return mtoFlag;
    }

    public void setMtoFlag(Short mtoFlag) {
        this.mtoFlag = mtoFlag;
    }

    public Short getCouponMethod() {
        return couponMethod;
    }

    public void setCouponMethod(Short couponMethod) {
        this.couponMethod = couponMethod;
    }

    public Short getDeliveryFeeFlag() {
        return deliveryFeeFlag;
    }

    public void setDeliveryFeeFlag(Short deliveryFeeFlag) {
        this.deliveryFeeFlag = deliveryFeeFlag;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Short getOnWayFlag() {
        return onWayFlag;
    }

    public void setOnWayFlag(Short onWayFlag) {
        this.onWayFlag = onWayFlag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getItemInCount() {
        return itemInCount;
    }

    public void setItemInCount(Integer itemInCount) {
        this.itemInCount = itemInCount;
    }

    public Integer getItemVmiCount() {
        return itemVmiCount;
    }

    public void setItemVmiCount(Integer itemVmiCount) {
        this.itemVmiCount = itemVmiCount;
    }

    public Integer getItemOnWayCount() {
        return itemOnWayCount;
    }

    public void setItemOnWayCount(Integer itemOnWayCount) {
        this.itemOnWayCount = itemOnWayCount;
    }

    public Integer getGoodsPromotionId() {
        return goodsPromotionId;
    }

    public void setGoodsPromotionId(Integer goodsPromotionId) {
        this.goodsPromotionId = goodsPromotionId;
    }

    public Integer getOrderPromotionId() {
        return orderPromotionId;
    }

    public void setOrderPromotionId(Integer orderPromotionId) {
        this.orderPromotionId = orderPromotionId;
    }

    public Integer getSpellGroupId() {
        return spellGroupId;
    }

    public void setSpellGroupId(Integer spellGroupId) {
        this.spellGroupId = spellGroupId;
    }

    public Short getItemType() {
        return itemType;
    }

    public void setItemType(Short itemType) {
        this.itemType = itemType;
    }
}
