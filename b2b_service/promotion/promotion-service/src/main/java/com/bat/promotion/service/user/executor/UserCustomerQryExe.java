package com.bat.promotion.service.user.executor;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.service.common.CommonValidator;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.service.common.enumtype.ReceiveStatus;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;
import com.bat.promotion.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.user.dto.customer.*;
import com.bat.promotion.api.user.dto.customer.data.UserCustomerCouponDTO;
import com.bat.promotion.dao.coupon.CouponCustomerMapper;
import com.bat.promotion.dao.coupon.CouponMapper;
import com.bat.promotion.dao.coupon.CouponMaterialRelevanceMapper;
import com.bat.promotion.dao.coupon.CouponModelRelevanceMapper;
import com.bat.promotion.dao.coupon.dataobject.CouponMaterialRelevanceDO;
import com.bat.promotion.dao.coupon.dataobject.CouponModelRelevanceDO;
import com.bat.promotion.dao.coupon.dataobject.UserCustomerCouponDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 8:44
 */
@Component
@Slf4j
public class UserCustomerQryExe {

    @Resource
    private CommonValidator commonValidator;

    @Resource
    private CouponCustomerMapper couponCustomerMapper;
    @Resource
    private CouponMapper couponMapper;
    @Resource
    private CouponModelRelevanceMapper couponModelRelevanceMapper;
    @Resource
    private CouponMaterialRelevanceMapper couponMaterialRelevanceMapper;

    @Resource
    private PromotionRpcQryExe promotionRpcQryExe;

    /**
     * 优惠券列表(分页)
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    public PageInfo<UserCustomerCouponDTO> couponList(UserCustomerCouponListQry qry, String userId,
                                                      String distributorId) {
        commonValidator.checkCustomer(userId, distributorId);
        Map<String, Object> qryMap = getQryMap(qry.getStatuss(), userId, distributorId);
        List<Integer> distributorIds = promotionRpcQryExe.getDistributorTreePaths(Integer.valueOf(distributorId));
        qryMap.put("distributorIds", distributorIds);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<UserCustomerCouponDO> userCustomerCouponDOS = couponCustomerMapper.listUserCustomerCouponByMap(qryMap);
        PageInfo pageInfo = new PageInfo(userCustomerCouponDOS);
        List<UserCustomerCouponDTO> dtos = UserConvertor.toUserCustomerCouponDTOList(pageInfo.getList(), null, null);
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 优惠券总数获取
     *
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    public Integer couponCount(UserCustomerCouponListQry qry, String userId, String distributorId) {
        commonValidator.checkCustomer(userId, distributorId);
        Map<String, Object> qryMap = getQryMap(qry.getStatuss(), userId, distributorId);
        List<Integer> distributorIds = promotionRpcQryExe.getDistributorTreePaths(Integer.valueOf(distributorId));
        qryMap.put("distributorIds", distributorIds);
        int count = couponCustomerMapper.countUserCustomerCouponByMap(qryMap);
        return count;
    }

    /**
     * 根据商品列表获取优惠券列表(分页)
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    public PageInfo<UserCustomerCouponDTO> couponGoodsList(UserCustomerGoodsCouponListQry qry, String userId,
                                                           String distributorId) {
        commonValidator.checkCustomer(userId, distributorId);
        Map<String, Object> qryMap = getQryMap(qry.getStatuss(), userId, distributorId);
        List<UserCustomerGoodsItemQry> goodss = qry.getGoodss();
        // 先获取商品可视优惠券ids
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));
        List<Integer> materialIds = new ArrayList<>();
        List<Integer> modelIds = new ArrayList<>();
        goodss.forEach(goods -> {
            totalAmount.set(totalAmount.get().add(new BigDecimal(goods.getPrice().doubleValue() * goods.getCount()))
                .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (!modelIds.contains(goods.getModelId())) {
                modelIds.add(goods.getModelId());
            }
            if (!materialIds.contains(goods.getMaterialId())) {
                materialIds.add(goods.getMaterialId());
            }
        });
        qryMap.put("totalAmount", totalAmount.get());
        qryMap.put("modelIds", modelIds);
        qryMap.put("materialIds", materialIds);
        List<Integer> couponIds = couponMapper.couponIdsByMaterialIdsAndModelIds(qryMap);
        qryMap.put("couponIds", couponIds);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<UserCustomerCouponDO> userCustomerCouponDOS =
            couponCustomerMapper.listUserCustomerGoodsCouponByMap(qryMap);
        PageInfo pageInfo = new PageInfo(userCustomerCouponDOS);
        List<CouponModelRelevanceDO> modelRelevanceDOS = new ArrayList<>();
        List<CouponMaterialRelevanceDO> materialRelevanceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userCustomerCouponDOS)) {
            couponIds = userCustomerCouponDOS.stream()
                .filter(
                    dto -> dto.getGoodsEnable().equals(Constant.GOODS_ENABLE_1) && dto.getCouponStatus().equals(Constant.COUPON_STATUS_2))
                .map(UserCustomerCouponDO::getCouponId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(couponIds)) {
                modelRelevanceDOS = couponModelRelevanceMapper.modelIdsByCouponIds(couponIds);
                materialRelevanceDOS = couponMaterialRelevanceMapper.materialIdsByCouponIds(couponIds);
            }
        }
        List<UserCustomerCouponDTO> dtos =
            UserConvertor.toUserCustomerCouponDTOList(pageInfo.getList(), materialRelevanceDOS, modelRelevanceDOS);
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据优惠券类型获取优惠券列表(不分页)
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    public List<UserCustomerCouponDTO> couponSpecialList(UserCustomerSpecialCouponListQry qry, String userId,
        String distributorId) {
        commonValidator.checkCustomer(userId, distributorId);
        Map<String, Object> qryMap = getQryMap(qry.getStatuss(), userId, distributorId);
        List<Integer> couponTypes = Arrays.asList(qry.getCouponTypes().split(",")).stream()
            .map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        qryMap.put("couponTypes", couponTypes);
        List<UserCustomerCouponDO> userCustomerCouponDOS =
            couponCustomerMapper.listUserCustomerSpecialCouponByMap(qryMap);
        List<UserCustomerCouponDTO> dtos = UserConvertor.toUserCustomerCouponDTOList(userCustomerCouponDOS, null, null);
        return dtos;
    }

    /**
     * 根据状态获取查询map
     * 
     * @param statuss
     * @param userId
     * @param distributorId
     */
    private Map<String, Object> getQryMap(String statuss, String userId, String distributorId) {
        Map<String, Object> qryMap = new HashMap();
        qryMap.put("customerId", Integer.valueOf(userId));
        qryMap.put("distributorId", Integer.valueOf(distributorId));
        List<Integer> distributorIds = promotionRpcQryExe.getDistributorTreePaths(Integer.valueOf(distributorId));
        qryMap.put("distributorIds", distributorIds);
        List<Integer> couponStatuss = new ArrayList<>();
        if (StringUtils.isNotBlank(statuss)) {
            couponStatuss = Arrays.asList(statuss.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
                .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(couponStatuss) || couponStatuss.contains(ReceiveStatus.ALL.getValue())) {
            qryMap.put("all", ReceiveStatus.ALL.getValue());
        } else {
            if (couponStatuss.contains(ReceiveStatus.UN_RECEIVE.getValue())) {
                qryMap.put("unReceive", ReceiveStatus.UN_RECEIVE.getValue());
            }
            if (couponStatuss.contains(ReceiveStatus.RECEIVED.getValue())) {
                qryMap.put("received", ReceiveStatus.RECEIVED.getValue());
            }
            if (couponStatuss.contains(ReceiveStatus.UN_USED.getValue())) {
                qryMap.put("unUsed", ReceiveStatus.UN_RECEIVE.getValue());
            }
            if (couponStatuss.contains(ReceiveStatus.USED.getValue())) {
                qryMap.put("used", ReceiveStatus.USED.getValue());
            }
            if (couponStatuss.contains(ReceiveStatus.EXPIRED.getValue())) {
                qryMap.put("expired", ReceiveStatus.EXPIRED.getValue());
            }
        }
        return qryMap;
    }

    /**
     * 根据商品列表获取可用优惠券列表数量
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    public Integer couponGoodsEnableCount(UserCustomerGoodsEnableCouponQry qry, String userId, String distributorId) {
        commonValidator.checkCustomer(userId, distributorId);
        Map<String, Object> qryMap = new HashMap();
        qryMap.put("customerId", Integer.valueOf(userId));
        qryMap.put("distributorId", Integer.valueOf(distributorId));
        // 先获取商品可视优惠券ids
        List<Integer> couponIds = getCouponIdsEnableByGoodss(qry.getGoodss(), qryMap);
        Integer count = 0;
        if (!CollectionUtils.isEmpty(couponIds)) {
            qryMap.put("couponIds", couponIds);
            count = couponCustomerMapper.listUserCustomerGoodsEnableCountCouponByMap(qryMap);
        }
        return count;
    }

    /**
     * 根据商品获取可用的优惠券列表
     * 
     * @param goodss
     * @param qryMap
     * @return
     */
    private List<Integer> getCouponIdsEnableByGoodss(List<UserCustomerGoodsItemQry> goodss,
        Map<String, Object> qryMap) {
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));
        List<Integer> materialIds = new ArrayList<>();
        List<Integer> modelIds = new ArrayList<>();
        goodss.forEach(goods -> {
            totalAmount.set(totalAmount.get().add(new BigDecimal(goods.getPrice().doubleValue() * goods.getCount()))
                .setScale(3, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (!modelIds.contains(goods.getModelId())) {
                modelIds.add(goods.getModelId());
            }
            if (!materialIds.contains(goods.getMaterialId())) {
                materialIds.add(goods.getMaterialId());
            }
        });
        qryMap.put("totalAmount", totalAmount.get());
        qryMap.put("modelIds", modelIds);
        qryMap.put("materialIds", materialIds);
        List<Integer> couponIds = couponMapper.couponIdsEnableByMaterialIdsAndModelIds(qryMap);
        return couponIds;
    }

    /**
     * 根据商品列表获取最优优惠券
     * 
     * @param qry
     * @param userId
     * @param distributorId
     * @return
     */
    public UserCustomerCouponDTO couponGoodsOptimal(UserCustomerGoodsOptimalCouponQry qry, String userId,
                                                    String distributorId) {
        commonValidator.checkCustomer(userId, distributorId);
        Map<String, Object> qryMap = new HashMap();
        qryMap.put("customerId", Integer.valueOf(userId));
        qryMap.put("distributorId", Integer.valueOf(distributorId));
        // 先获取商品可视优惠券ids
        List<Integer> couponIds = getCouponIdsEnableByGoodss(qry.getGoodss(), qryMap);
        List<UserCustomerCouponDO> userCustomerCouponDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(couponIds)) {
            qryMap.put("couponIds", couponIds);
            userCustomerCouponDOS = couponCustomerMapper.listUserCustomerGoodsEnableCouponByMap(qryMap);
        }
        // 获取最优优惠券
        BigDecimal totalAmount = new BigDecimal(qryMap.get("totalAmount").toString());
        UserCustomerCouponDTO dto = UserConvertor.toOptimalUserCustomerCouponDTO(userCustomerCouponDOS, totalAmount);
        if (dto != null) {
            List<CouponMaterialRelevanceDO> materials =
                couponMaterialRelevanceMapper.listMaterialRelevanceByCouponId(dto.getCouponId());
            if (!CollectionUtils.isEmpty(materials)) {
                dto.setMaterialIds(
                    materials.stream().map(CouponMaterialRelevanceDO::getMaterialId).collect(Collectors.toList()));
            }
            List<CouponModelRelevanceDO> models =
                couponModelRelevanceMapper.listModelRelevanceByCouponId(dto.getCouponId());
            if (!CollectionUtils.isEmpty(models)) {
                dto.setModelIds(models.stream().map(CouponModelRelevanceDO::getModelId).collect(Collectors.toList()));
            }
        }
        return dto;
    }

}
