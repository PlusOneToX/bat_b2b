package com.bat.promotion.service.common.rpc;

import static com.bat.promotion.service.common.Constant.*;
import static com.bat.promotion.service.rebatevoucher.executor.ErrorCode.B_PROMOTION_REBATE_VOUCHER_IS_UNAVAILABLE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.service.groupseckill.executor.GroupSeckillCmdExe;
import com.bat.promotion.service.groupseckill.executor.GroupSeckillRpcCmdExe;
import com.bat.promotion.service.promotion.executor.PromotionCmdExe;
import com.bat.promotion.service.promotion.executor.PromotionRpcCmdExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.OrderPromotionRpcQry;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.service.coupon.executor.CouponCmdExe;
import com.bat.promotion.service.coupon.executor.CouponRpcCmdExe;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherCmdExe;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherQryExe;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherRpcCmdExe;

import lombok.extern.slf4j.Slf4j;

/**
 * 促销活动service实现
 * 
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:10
 */
@DubboService
@Slf4j
public class PromotionServiceRpcImpl implements PromotionServiceDistributorRpc {

    @Resource
    private PromotionRpcQryExe rpcQryExe;

    @Resource
    private CouponCmdExe couponCmdExe;

    @Resource
    private GroupSeckillCmdExe groupSeckillCmdExe;

    @Resource
    private RebateVoucherCmdExe rebateVoucherCmdExe;

    @Resource
    private RebateVoucherQryExe rebateVoucherQryExe;

    @Resource
    private PromotionCmdExe promotionCmdExe;

    @Resource
    private PromotionRpcCmdExe promotionRpcCmdExe;

    @Resource
    private GroupSeckillRpcCmdExe groupSeckillRpcCmdExe;

    @Resource
    private CouponRpcCmdExe couponRpcCmdExe;

    @Resource
    private PromotionRpcQryExe promotionRpcQryExe;

    @Resource
    private RebateVoucherRpcCmdExe rebateVoucherRpcCmdExe;

    @Override
    public PromotionGroupSeckillGoodsRpcDTO getPromotionGroupSeckillGoods(List<Integer> promotionIds,
        List<Integer> groupSeckillIds) {
        return rpcQryExe.getPromotionGroupSeckillGoods(promotionIds, groupSeckillIds);
    }

    @Override
    public void promotionStart(List<Integer> ids) {
        promotionCmdExe.updateListPromotionStatus(ids, PROMO_STATUS_1);
    }

    @Override
    public void promotionStop(List<Integer> ids) {
        promotionCmdExe.updateListPromotionStatus(ids, PROMO_STATUS_2);
    }

    @Override
    public void promotionStartStop() {
        promotionRpcCmdExe.promotionStartStop();
    }

    @Override
    public void groupSeckillStart(List<Integer> ids) {
        groupSeckillCmdExe.updateListGroupSeckillStatus(ids, GROUP_SECKILL_STATUS_1);
    }

    @Override
    public void groupSeckillStop(List<Integer> ids) {
        groupSeckillCmdExe.updateListGroupSeckillStatus(ids, GROUP_SECKILL_STATUS_3);
    }

    @Override
    public void groupSeckillStartStop() {
        groupSeckillRpcCmdExe.groupSeckillStartStop();
    }

    @Override
    public void couponStart(List<Integer> ids) {
        couponCmdExe.updateListCouponStatus(ids, COUPON_STATUS_2, null);
    }

    @Override
    public void couponStop(List<Integer> ids) {
        couponCmdExe.updateListCouponStatus(ids, COUPON_STATUS_3, null);
    }

    @Override
    public void couponStartStop() {
        couponRpcCmdExe.couponStartStop();
    }

    @Override
    public void rebateVoucherStart(List<Integer> ids) {
        rebateVoucherCmdExe.updateListRebateVoucherStatus(ids, REBATE_VOUCHER_STATUS_5);
    }

    @Override
    public void rebateVoucherStop(List<Integer> ids) {
        rebateVoucherCmdExe.updateListRebateVoucherStatus(ids, REBATE_VOUCHER_STATUS_7);
    }

    @Override
    public void rebateVoucherStartStop() {
        rebateVoucherRpcCmdExe.rebateVoucherStartStop();
    }

    @Override
    public Response<List<GoodsItemPromotionPriceRpcDTO>> goodsItemPromotionPrice(GoodsItemPromotionPriceRpcQry qry) {
        return promotionRpcQryExe.goodsItemPromotionPrice(qry);
    }

    @Override
    public Response<List<GoodsItemPromotionRpcDTO>> goodsItemPromotion(List<GoodsItemRpcQry> goodsItems,
        Integer distributorId, Short presentFlag) {
        return promotionRpcQryExe.goodsItemPromotion(goodsItems, distributorId, presentFlag);
    }

    @Override
    public Response<PromotionGroupSeckillIdsByAllDistributorDTO> promotionGroupSeckillIdsByAllDistributor() {
        return promotionRpcQryExe.promotionGroupSeckillIdsByAllDistributor();
    }

    @Override
    public Response<OrderPromotionRpcDTO> promotionByOrderPromotionIds(OrderPromotionRpcQry qry) {
        return promotionRpcQryExe.promotionByOrderPromotionIds(qry);
    }

    @Override
    public Response<PromotionDistributorRpcDTO> getVisiblePromotionIdsByDistributor(Integer distributorId,
        Integer scalePriceId, Integer salesId, Integer departmentId) {
        PromotionDistributorRpcDTO rpcDTO =
            promotionRpcQryExe.getVisiblePromotionByDistributor(distributorId, scalePriceId, salesId, departmentId);
        return Response.of(rpcDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<List<RebateVoucherRpcDTO>> listRebateVoucher(Integer distributorId,
        List<Integer> rebateVoucherIds) {
        try {
            List<RebateVoucherDTO> rebateVoucherDTOS =
                rebateVoucherQryExe.listRebateVoucher(distributorId, rebateVoucherIds);

            if (CollectionUtils.isEmpty(rebateVoucherDTOS)) {
                return Response.of(new ArrayList<>());
            }
            List<RebateVoucherRpcDTO> collect = rebateVoucherDTOS.stream().map(rebateVoucherDTO -> {
                RebateVoucherRpcDTO rpcDTO = new RebateVoucherRpcDTO();
                BeanUtils.copyProperties(rebateVoucherDTO, rpcDTO);
                return rpcDTO;
            }).collect(Collectors.toList());
            return Response.of(collect);
        } catch (PromotionException e) {
            if (e.getCode().equals(B_PROMOTION_REBATE_VOUCHER_IS_UNAVAILABLE)) {
                return Response.buildFailure(e.getCode(), e.getMsg());
            } else {
                e.printStackTrace();
                log.error(e.getLocalizedMessage());
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            throw e;
        }
    }
}
