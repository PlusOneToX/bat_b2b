package com.bat.promotion.service.common.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.promotion.Tenant.TenantContext;
import com.bat.promotion.api.base.MessageUtils;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.service.message.MessageSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.promotion.common.Response;
import com.bat.dubboapi.promotion.dto.CouponCustomerRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.CouponCustomerRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.promotion.dao.coupon.CouponCustomerMapper;
import com.bat.promotion.dao.coupon.CouponMaterialRelevanceMapper;
import com.bat.promotion.dao.coupon.CouponModelRelevanceMapper;
import com.bat.promotion.dao.coupon.dataobject.CouponMaterialRelevanceDO;
import com.bat.promotion.dao.coupon.dataobject.CouponModelRelevanceDO;
import com.bat.promotion.dao.coupon.dataobject.CouponStatusDO;
import com.bat.promotion.dao.coupon.dataobject.UserCustomerCouponDO;
import com.bat.promotion.service.coupon.convertor.CouponConvertor;

import lombok.extern.slf4j.Slf4j;

import static com.bat.promotion.service.common.CommonErrorCode.B_COUPON_USE_FLAG_ERROR;
import static com.bat.promotion.service.common.Constant.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:15
 */
@Slf4j
@Component
public class PromotionCustomerRpcQryExe {

    @Resource
    private CouponCustomerMapper couponCustomerMapper;

    @Resource
    private CouponMaterialRelevanceMapper couponMaterialRelevanceMapper;

    @Resource
    private CouponModelRelevanceMapper couponModelRelevanceMapper;

    @Resource
    private MessageSendService messageSendService;

    /**
     * 优惠券使用key
     */
    @CreateCache(name = Constant.COUPON_USE_FLAG_KEY)
    private Cache<String, Integer> couponCache;

    /**
     * C端客户计算商品活动价格
     * 
     * @param qry
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Response<List<GoodsItemPromotionPriceRpcDTO>> goodsItemPromotionPrice(GoodsItemPromotionPriceRpcQry qry) {
        List<AutoReleaseLock> couponNoLocks = new ArrayList<>();
        try {
            List<GoodsItemPriceRpcQry> goodsItems = qry.getGoodsItemPrices();
            List<String> couponNos =
                    goodsItems.stream().filter(goodsItem -> StringUtils.isNotBlank(goodsItem.getCouponNo()))
                            .map(GoodsItemPriceRpcQry::getCouponNo).distinct().collect(Collectors.toList());
            List<UserCustomerCouponDO> couponDOS = new ArrayList<>();
            List<CouponMaterialRelevanceDO> materialDOS = new ArrayList<>();
            List<CouponModelRelevanceDO> modelDOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(couponNos)) {
                couponDOS = couponCustomerMapper.listByCouponNosAndCustomerId(qry.getCustomerId(), couponNos);
                if (!CollectionUtils.isEmpty(couponDOS)) {
                    List<Integer> couponIds =
                            couponDOS.stream().map(UserCustomerCouponDO::getCouponId).distinct().collect(Collectors.toList());
                    materialDOS = couponMaterialRelevanceMapper.materialIdsByCouponIds(couponIds);
                    modelDOS = couponModelRelevanceMapper.modelIdsByCouponIds(couponIds);
                }
                // 使用优惠券时，更新优惠券状态
                if (qry.getUseFlag().equals(USE_FLAG_1)) {
                    // 判断优惠券是否正在使用，考虑并发问题
                    AtomicBoolean useFlag = new AtomicBoolean(false);
                    couponNos.forEach(couponNo -> {
                        AutoReleaseLock lock = couponCache.tryLock(TenantContext.getTenantNo() + ":" + couponNo, 10,
                                TimeUnit.SECONDS);
                        if (lock == null) {
                            useFlag.set(true);
                            return;
                        }
                        couponNoLocks.add(lock);
                    });
                    if (useFlag.get()) {
                        return Response.buildFailure(B_COUPON_USE_FLAG_ERROR, MessageUtils.get(B_COUPON_USE_FLAG_ERROR));
                    }
                    List<CouponStatusDO> couponStatusDOS =
                            CouponConvertor.toCustomerCouponStatusDOList(couponNos, COUPON_STATUS_6, null);
                    couponCustomerMapper.updateListCouponStatus(couponStatusDOS);
                    messageSendService.synCouponbat(qry.getCustomerId().toString());
                }
            }
            List<GoodsItemPromotionPriceRpcDTO> rpcDTOS = PromotionRpcConvertor
                    .toGoodsItemPromotionPriceRpcDTOList(goodsItems, couponDOS, materialDOS, modelDOS, qry.getOrderTime());
            return Response.of(rpcDTOS);
        }finally {
            if(!CollectionUtils.isEmpty(couponNoLocks)){
                couponNoLocks.forEach(lock ->{
                    if(lock != null){
                        lock.close();
                    }
                });
            }
        }
    }

    public Response<List<CouponCustomerRpcDTO>> listCouponCustomerByCustomerId(Integer customerId) {
        List<UserCustomerCouponDO> userCustomerCouponDOS = couponCustomerMapper.listByCustomerId(customerId);
        List<CouponCustomerRpcDTO> couponCustomerRpcDTOS = new ArrayList<>();
        for (UserCustomerCouponDO userCustomerCouponDO : userCustomerCouponDOS) {
            CouponCustomerRpcDTO couponCustomerRpcDTO = new CouponCustomerRpcDTO();
            BeanUtils.copyProperties(userCustomerCouponDO, couponCustomerRpcDTO);
            couponCustomerRpcDTOS.add(couponCustomerRpcDTO);
        }
        return Response.of(couponCustomerRpcDTOS);
    }

    public Response<List<CouponCustomerRpcDTO>> listCouponCustomer(CouponCustomerRpcQry rpcQry) {
        if(rpcQry.getCouponStatus()==null){
            rpcQry.setCouponStatus(COUPON_STATUS_2);
        }
        List<UserCustomerCouponDO> userCustomerCouponDOS =
            couponCustomerMapper.listCouponCustomerByCustomerIdsAndCouponCode(rpcQry.getCustomerIds(),
                rpcQry.getCouponCode(), rpcQry.getCouponStatus());
        List<CouponCustomerRpcDTO> couponCustomerRpcDTOS = new ArrayList<>();
        for (UserCustomerCouponDO userCustomerCouponDO : userCustomerCouponDOS) {
            CouponCustomerRpcDTO couponCustomerRpcDTO = new CouponCustomerRpcDTO();
            BeanUtils.copyProperties(userCustomerCouponDO, couponCustomerRpcDTO);
            couponCustomerRpcDTOS.add(couponCustomerRpcDTO);
        }
        return Response.of(couponCustomerRpcDTOS);
    }
}
