package com.bat.dubboapi.promotion.api;

import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.dubboapi.promotion.dto.OrderPromotionRpcQry;

import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/25 19:59
 */
public interface PromotionServiceDistributorRpc {
    /**
     * 获取活动商品id列表
     *
     * @param groupSeckillIds
     * @return
     */
    PromotionGroupSeckillGoodsRpcDTO getPromotionGroupSeckillGoods(List<Integer> promotionIds, List<Integer> groupSeckillIds);

    /**
     * 促销活动开始
     *
     * @param ids
     */
    void promotionStart(List<Integer> ids);

    /**
     * 促销活动结束
     *
     * @param ids
     */
    void promotionStop(List<Integer> ids);

    /**
     * 促销活动定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    void promotionStartStop();

    /**
     * 拼团秒杀开始
     *
     * @param ids
     */
    void groupSeckillStart(List<Integer> ids);

    /**
     * 拼团秒杀结束
     *
     * @param ids
     */
    void groupSeckillStop(List<Integer> ids);

    /**
     * 拼团秒杀定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    void groupSeckillStartStop();

    /**
     * 优惠券开始
     *
     * @param ids
     */
    void couponStart(List<Integer> ids);

    /**
     * 优惠券结束
     *
     * @param ids
     */
    void couponStop(List<Integer> ids);

    /**
     * 优惠券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    void couponStartStop();

    /**
     * 返利代金券开始
     *
     * @param ids
     */
    void rebateVoucherStart(List<Integer> ids);

    /**
     * 返利代金券结束
     *
     * @param ids
     */
    void rebateVoucherStop(List<Integer> ids);

    /**
     * 返利代金券定时执行器（每天执行一次，创建24小时内容开启或结束的定时任务）
     */
    void rebateVoucherStartStop();

    /**
     * 分销客户计算商品活动价格
     *
     * @param qry
     * @return
     */
    Response<List<GoodsItemPromotionPriceRpcDTO>> goodsItemPromotionPrice(GoodsItemPromotionPriceRpcQry qry);

    /**
     * 根据货品ids获取活动数据
     *
     * @param goodsItems
     * @return
     */
    Response<List<GoodsItemPromotionRpcDTO>> goodsItemPromotion(List<GoodsItemRpcQry> goodsItems, Integer distributorId, Short presentFlag);

    /**
     * 获取所有分销商可视活动ids
     *
     * @return
     */
    Response<PromotionGroupSeckillIdsByAllDistributorDTO> promotionGroupSeckillIdsByAllDistributor();

    /**
     * 根据订单活动条件ids获取活动信息
     *
     * @param qry
     * @return
     */
    Response<OrderPromotionRpcDTO> promotionByOrderPromotionIds(OrderPromotionRpcQry qry);

    /**
     * 根据分销商获取可视活动数据
     *
     * @param distributorId
     * @param scalePriceId
     * @param salesId
     * @param departmentId
     * @return
     */
    Response<PromotionDistributorRpcDTO> getVisiblePromotionIdsByDistributor(Integer distributorId, Integer scalePriceId, Integer salesId, Integer departmentId);

    /**
     * 根据代金券ids 分销商id 获取代金券
     * @param distributorId
     * @param rebateVoucherIds
     * @return
     */
    Response<List<RebateVoucherRpcDTO>> listRebateVoucher(Integer distributorId,List<Integer> rebateVoucherIds);

}
