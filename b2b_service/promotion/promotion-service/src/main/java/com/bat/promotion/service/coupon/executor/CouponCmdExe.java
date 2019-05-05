package com.bat.promotion.service.coupon.executor;

import static com.bat.promotion.service.common.CommonErrorCode.B_PROMOTION_TIME_ERROR;
import static com.bat.promotion.service.common.CommonErrorCode.B_PROMOTION_UPDATE_ERROR;
import static com.bat.promotion.service.common.Constant.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.dao.coupon.*;
import com.bat.promotion.dao.coupon.dataobject.*;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;
import com.bat.promotion.service.coupon.convertor.CouponConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.coupon.dto.*;
import com.bat.promotion.dao.coupon.*;
import com.bat.promotion.dao.coupon.dataobject.*;
import com.bat.promotion.service.check.executor.PromotionCheckCmdExe;
import com.bat.promotion.service.common.PromotionConfig;
import com.bat.promotion.service.user.executor.UserCustomerCmdExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/24 10:43
 */
@Component
public class CouponCmdExe {
    @Resource
    private CouponMapper couponMapper;
    @Resource
    private CouponDistributorRelevanceMapper distributorRelevanceMapper;
    @Resource
    private CouponMaterialRelevanceMapper materialRelevanceMapper;
    @Resource
    private CouponModelRelevanceMapper modelRelevanceMapper;
    @Resource
    private CouponCustomerMapper couponCustomerMapper;
    @Resource
    private CouponRpcCmdExe rpcCmdExe;
    @Resource
    private PromotionConfig config;
    @Resource
    private PromotionRpcQryExe rpcQryExe;
    @Resource
    private PromotionCheckCmdExe checkCmdExe;

    @Resource
    private UserCustomerCmdExe userCustomerCmdExe;

    private static Logger logger = LoggerFactory.getLogger(CouponCmdExe.class);

    /**
     * 新增优惠券活动
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void createCoupon(CouponCmd cmd, String userId, String userName) {
        Date date = new Date(System.currentTimeMillis());
        CouponDO couponDO = CouponConvertor.toCouponDO(cmd, date);
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_14);
        couponApplyAndStatus(couponDO, detailRpcDTO);
        couponMapper.insert(couponDO);
        saveCouponScope(couponDO.getId(), cmd, OPERATION_TYPE_1);
        if (couponDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_3, couponDO.getId(), detailRpcDTO, date, userId, userName);
        }
    }

    /**
     * 保存优惠券关联关系数据
     * 
     * @param couponId
     * @param cmd
     * @param operationType
     */
    private void saveCouponScope(Integer couponId, CouponCmd cmd, Short operationType) {
        if (operationType.equals(OPERATION_TYPE_2) || operationType.equals(OPERATION_TYPE_3)) {
            deleteCouponScope(couponId);
        }
        if (cmd.getCouponScope().equals(SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(cmd.getDistributors())) {
            List<CouponDistributorRelevanceDO> distributorRelevanceDOS =
                CouponConvertor.toCouponDistributorRelevanceDOList(couponId, cmd.getDistributors());
            distributorRelevanceMapper.createDistributorRelevanceList(distributorRelevanceDOS);
        }
        List<CouponMaterialScopeCmd> materials = cmd.getMaterials();
        if (cmd.getMaterialScope().equals(MATERIAL_SCOPE_2) && !CollectionUtils.isEmpty(materials)) {
            List<CouponMaterialRelevanceDO> materialRelevanceDOS =
                CouponConvertor.toCouponMaterialRelevanceDOList(couponId, materials);
            materialRelevanceMapper.createMaterialRelevanceList(materialRelevanceDOS);
        }
        List<CouponModelScopeCmd> models = cmd.getModels();
        if (cmd.getModelScope().equals(MODEL_SCOPE_2) && !CollectionUtils.isEmpty(models)) {
            List<CouponModelRelevanceDO> modelRelevanceDOS =
                CouponConvertor.toCouponModelRelevanceDOList(couponId, models);
            modelRelevanceMapper.createModelRelevanceList(modelRelevanceDOS);
        }
    }

    /**
     * 删除优惠券关联关系数据
     * 
     * @param couponId
     */
    private void deleteCouponScope(Integer couponId) {
        distributorRelevanceMapper.deleteByCouponId(couponId);
        materialRelevanceMapper.deleteByCouponId(couponId);
        modelRelevanceMapper.deleteByCouponId(couponId);
    }

    /**
     * 优惠券的审批和状态处理
     *
     * @param couponDO
     */
    private void couponApplyAndStatus(CouponDO couponDO, CheckConfigDetailRpcDTO detailRpcDTO) {
        if (couponDO.getApplyStatus() != null && couponDO.getApplyStatus().equals(APPLY_STATUS_0)) {
            couponDO.setCouponStatus(COUPON_STATUS_0);
        } else {
            couponDO.setCouponStatus(COUPON_STATUS_0);
            if (detailRpcDTO != null && detailRpcDTO.getOpenFlag().equals(FLAG_1)) {
                couponDO.setApplyStatus(APPLY_STATUS_1);
            } else {
                couponDO.setApplyStatus(APPLY_STATUS_2);
            }
        }
    }

    /**
     * 修改优惠券活动(草稿状态的优惠券修改)
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCoupon(CouponCmd cmd, String userId, String userName) {
        CouponDO beforeCouponDO = couponMapper.selectByPrimaryKey(cmd.getId());
        if (beforeCouponDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_NULL);
        }
        if (!beforeCouponDO.getApplyStatus().equals(APPLY_STATUS_0)) {
            throw PromotionException.buildException(B_PROMOTION_UPDATE_ERROR);
        }
        Date date = new Date(System.currentTimeMillis());
        CouponDO afterCouponDO = CouponConvertor.toCouponDO(cmd, date);
        afterCouponDO.setId(beforeCouponDO.getId());
        afterCouponDO.setCreateTime(beforeCouponDO.getCreateTime());
        // 判断是否需要审批
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_14);
        couponApplyAndStatus(afterCouponDO, detailRpcDTO);
        couponMapper.updateByPrimaryKey(afterCouponDO);
        saveCouponScope(afterCouponDO.getId(), cmd, OPERATION_TYPE_2);
        if (afterCouponDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_3, afterCouponDO.getId(), detailRpcDTO, date, userId,
                userName);
        }
    }

    /**
     * 修改优惠券发放总数量和限购数量
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCouponCount(CouponCountCmd cmd) {
        CouponDO couponDO = couponMapper.selectByPrimaryKey(cmd.getId());
        if (couponDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_NULL);
        }
        if (!couponDO.getApplyStatus().equals(APPLY_STATUS_2)) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_CHECK_STATUS_ERROR);
        }
        if (cmd.getGenerateCount() != 0
            && cmd.getGenerateCount().intValue() < couponDO.getGeneratedCount().intValue()) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_COUNT_ERROR);
        }
        couponMapper.updateCount(cmd.getId(), cmd.getGenerateCount(), cmd.getLimitCount());
    }

    /**
     * 根据id删除优惠券
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCoupon(BaseId cmd) {
        CouponDO couponDO = couponMapper.selectByPrimaryKey(cmd.getId());
        if (couponDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_NULL);
        }
        // 只有草稿和申请失败的促销活动可以删除删除
        if (!couponDO.getApplyStatus().equals(APPLY_STATUS_0) && !couponDO.getApplyStatus().equals(APPLY_STATUS_3)
            && !couponDO.getCouponStatus().equals(COUPON_STATUS_0)) {
            throw PromotionException.buildException(com.bat.promotion.service.promotion.executor.ErrorCode.B_PROMOTION_DELETE_ERROR);
        }
        deleteCouponScope(cmd.getId());
        couponMapper.deleteByPrimaryKey(cmd.getId());
    }

    /**
     * 根据优惠券id变更状态(包括发布)
     * 
     * @param cmd
     */
    public void updateCouponStatus(CouponStatusCmd cmd) {
        List<Integer> ids = new ArrayList<>();
        ids.add(cmd.getId());
        CouponDO couponDO = couponMapper.selectByPrimaryKey(cmd.getId());
        if (!couponDO.getApplyStatus().equals(APPLY_STATUS_2)) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_CHECK_STATUS_ERROR);
        }
        // 发布优惠券
        if (cmd.getCouponStatus().equals(COUPON_STATUS_1)) {
            // 优惠券进行状态(如果开始时间与创建时间间隔10秒情况，活动直接开始)
            if (couponDO.getStartTime().getTime() - couponDO.getCreateTime().getTime() < config.getIntervalTime()
                * 1000) {
                cmd.setCouponStatus(COUPON_STATUS_2);
                couponDO.setCouponStatus(COUPON_STATUS_2);
            } else {
                cmd.setCouponStatus(COUPON_STATUS_1);
                couponDO.setCouponStatus(COUPON_STATUS_1);
            }
            updateListCouponStatus(ids, cmd.getCouponStatus(), cmd.getInvalidExplain());
            // 优惠券定时任务
            cronJobCoupon(couponDO);
        } else {
            updateListCouponStatus(ids, cmd.getCouponStatus(), cmd.getInvalidExplain());
        }
    }

    /**
     * 优惠券定时任务
     *
     * @param couponDO
     */
    private void cronJobCoupon(CouponDO couponDO) {
        try {
            rpcCmdExe.couponAddXxlJob(couponDO);
        } catch (ParseException e) {
            throw PromotionException.buildException(B_PROMOTION_TIME_ERROR);
        }
    }

    /**
     * 更新优惠券活动
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateListCouponStatus(List<Integer> ids, Short couponStatus, String invalidExplain) {
        List<CouponDO> couponDOS = couponMapper.listCouponByIds(ids);
        Map<Integer, CouponDO> couponDOMap =
            couponDOS.stream().collect(Collectors.toMap(CouponDO::getId, couponDO -> couponDO));
        List<CouponStatusDO> statusDOS = CouponConvertor.toCouponStatusDOList(couponDOS, couponStatus, invalidExplain);
        if (!CollectionUtils.isEmpty(statusDOS)) {
            List<CouponStatusDO> couponStatusDOS = new ArrayList<>();
            List<CouponStatusDO> userCouponStatusDOS = new ArrayList<>();
            statusDOS.forEach(statusDO -> {
                CouponDO couponDO = couponDOMap.get(statusDO.getId());
                // 优惠券提前结束情况，只需更新已领取的优惠券
                if (!couponDO.getCouponStatus().equals(COUPON_STATUS_4)) {
                    couponStatusDOS.add(statusDO);
                }
                if (!statusDO.getCouponStatus().equals(COUPON_STATUS_4)) {
                    userCouponStatusDOS.add(statusDO);
                }
            });
            if (!CollectionUtils.isEmpty(couponStatusDOS)) {
                couponMapper.updateListCouponStatus(couponStatusDOS);
            }
            if (!CollectionUtils.isEmpty(userCouponStatusDOS)) {
                couponCustomerMapper.updateListCouponStatus(userCouponStatusDOS);
            }
        }
    }

    /**
     * 根据券码变更客户优惠券状态
     * 
     * @param cmd
     */
    public void updateCustomerListCouponStatus(CouponCustomerStatusCmd cmd) {
        updateCustomerListCouponStatus(cmd.getCouponNos(), cmd.getCouponStatus(), cmd.getInvalidExplain());
    }

    /**
     * 更新客户优惠券活动状态
     * 
     * @param couponNos
     * @param couponStatus
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomerListCouponStatus(List<String> couponNos, Short couponStatus, String invalidExplain) {
        List<UserCustomerCouponDO> couponDOS = couponCustomerMapper.listByCouponNos(couponNos);
        List<CouponStatusDO> couponStatusDOS = null;
        if (couponStatus.equals(CONTENT_TYPE_6)) {
            couponStatusDOS = CouponConvertor.toCustomerCouponStatusDOList(couponNos, couponStatus, invalidExplain);
        } else {
            couponStatusDOS = CouponConvertor.toCouponStatusDOListByUserCouponDOS(couponDOS, couponStatus);
        }
        if (!CollectionUtils.isEmpty(couponStatusDOS)) {
            couponCustomerMapper.updateListCouponStatus(couponStatusDOS);
        }
        List<CouponCustomerDO> couponCustomerDOS = couponDOS.stream().map(a -> {
            CouponCustomerDO couponCustomerDO = new CouponCustomerDO();
            BeanUtils.copyProperties(a, couponCustomerDO);
            return couponCustomerDO;
        }).collect(Collectors.toList());
        userCustomerCmdExe.synCouponbat(couponCustomerDOS);
    }

}
