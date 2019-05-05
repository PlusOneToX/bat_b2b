package com.bat.promotion.service.check.executor;

import static com.bat.promotion.service.check.executor.ErrorCode.*;
import static com.bat.promotion.service.common.Constant.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.service.coupon.executor.CouponCmdExe;
import com.bat.promotion.service.groupseckill.executor.GroupSeckillCmdExe;
import com.bat.promotion.service.promotion.executor.PromotionCmdExe;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.dubboapi.system.check.dto.data.CheckConfigRpcDTO;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.check.dto.PromotionCheckCmd;
import com.bat.promotion.dao.check.PromotionCheckFlowMapper;
import com.bat.promotion.dao.check.PromotionCheckMapper;
import com.bat.promotion.dao.check.dataobject.PromotionCheckDO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckFlowDO;
import com.bat.promotion.dao.coupon.CouponMapper;
import com.bat.promotion.dao.coupon.dataobject.CouponDO;
import com.bat.promotion.dao.groupseckill.GroupSeckillMapper;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.promotion.PromotionMapper;
import com.bat.promotion.dao.promotion.dataobject.PromotionDO;
import com.bat.promotion.dao.rebatevoucher.RebateVoucherMapper;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherDO;
import com.bat.promotion.service.check.convertor.PromotionCheckConvertor;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/9/14 14:20
 */
@Component
public class PromotionCheckCmdExe {

    @Resource
    private PromotionCheckMapper checkMapper;
    @Resource
    private PromotionCheckFlowMapper checkFlowMapper;

    @Resource
    private PromotionCmdExe promotionCmdExe;
    @Resource
    private GroupSeckillCmdExe groupSeckillCmdExe;
    @Resource
    private PromotionMapper promotionMapper;
    @Resource
    private GroupSeckillMapper groupSeckillMapper;
    @Resource
    private CouponMapper couponMapper;
    @Resource
    private RebateVoucherMapper rebateVoucherMapper;
    @Resource
    private CouponCmdExe couponCmdExe;

    /**
     * 促销活动审批
     * 
     * @param cmd
     * @param userId
     * @param userName
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkPromotion(PromotionCheckCmd cmd, String userId, String userName) {
        Date date = new Date(System.currentTimeMillis());
        PromotionCheckDO checkDO = checkMapper.selectByPrimaryKey(cmd.getId());
        if (checkDO == null) {
            throw PromotionException.buildException(B_PROMOTION_CHECK_NULL);
        }
        if (!checkDO.getCheckStatus().equals(CHECK_STATUS_0)) {
            throw PromotionException.buildException(B_PROMOTION_CHECK_FINISH);
        }
        if (!checkDO.getCheckUserId().equals(Integer.valueOf(userId))) {
            throw PromotionException.buildException(B_PROMOTION_CHECK_USER_ERROR);
        }
        PromotionDO promotionDO = null;
        GroupSeckillDO groupSeckillDO = null;
        CouponDO couponDO = null;
        List<RebateVoucherDO> rebateVoucherDOS = null;
        if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_1)) {
            promotionDO = promotionMapper.selectByPrimaryKey(checkDO.getPromotionId());
        } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_2)) {
            groupSeckillDO = groupSeckillMapper.selectByPrimaryKey(checkDO.getPromotionId());
        } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_3)) {
            couponDO = couponMapper.selectByPrimaryKey(checkDO.getPromotionId());
        } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_4)) {
            rebateVoucherDOS = rebateVoucherMapper.listRebateVoucherByBatchId(checkDO.getPromotionId());
        }
        // 更新当前审批流状态
        List<PromotionCheckFlowDO> checkFlowDOS = checkFlowMapper.listByPromotionCheckId(cmd.getId());
        PromotionCheckFlowDO promotionCheckFlowDO = checkFlowDOS.stream()
            .filter(checkFlowDO -> checkFlowDO.getUserId().equals(checkDO.getCheckUserId())).findFirst().get();
        promotionCheckFlowDO.setCheckStatus(cmd.getCheckStatus());
        promotionCheckFlowDO.setRemark(cmd.getRemark());
        promotionCheckFlowDO.setCheckTime(date);
        promotionCheckFlowDO.setUpdateTime(date);
        checkFlowMapper.updateByPrimaryKey(promotionCheckFlowDO);
        if (cmd.getCheckStatus().equals(CHECK_STATUS_2)) {
            // 审批拒绝时
            checkDO.setCheckStatus(cmd.getCheckStatus());
            checkDO.setUpdateTime(date);
            checkMapper.updateByPrimaryKey(checkDO);
            // 删除未完成的审批流
            List<PromotionCheckFlowDO> deleteCheckFlowDOS = checkFlowDOS.stream()
                .filter(checkFlowDO -> checkFlowDO.getCheckSort().intValue() > promotionCheckFlowDO.getCheckSort())
                .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deleteCheckFlowDOS)) {
                List<Integer> deleteIds =
                    deleteCheckFlowDOS.stream().map(PromotionCheckFlowDO::getId).collect(Collectors.toList());
                checkFlowMapper.deleteByIds(deleteIds);
            }
            // 处理审批数据
            if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_1)) {
                if (promotionDO != null) {
                    promotionDO.setApplyStatus(APPLY_STATUS_3);
                    promotionDO.setUpdateTime(date);
                    promotionMapper.updateByPrimaryKey(promotionDO);
                }
            } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_2)) {
                if (groupSeckillDO != null) {
                    groupSeckillDO.setApplyStatus(APPLY_STATUS_3);
                    groupSeckillDO.setUpdateTime(date);
                    groupSeckillMapper.updateByPrimaryKey(groupSeckillDO);
                }
            } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_3)) {
                if (couponDO != null) {
                    couponDO.setApplyStatus(APPLY_STATUS_3);
                    couponDO.setUpdateTime(date);
                    couponMapper.updateByPrimaryKey(couponDO);
                }
            } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_4)) {
                if (!CollectionUtils.isEmpty(rebateVoucherDOS)) {
                    rebateVoucherDOS.forEach(rebateVoucherDO -> {
                        rebateVoucherDO.setApplyStatus(APPLY_STATUS_3);
                        rebateVoucherDO.setVoucherStatus(REBATE_VOUCHER_STATUS_3);
                        rebateVoucherDO.setUpdateTime(date);
                    });
                    rebateVoucherMapper.batchUpdate(rebateVoucherDOS);
                }
            }
        } else {
            // 审批同意时，更新审批单（当是最后一个审批人审批时需更新审批结果）
            Optional<PromotionCheckFlowDO> optional = checkFlowDOS.stream()
                .filter(checkFlowDO -> checkFlowDO.getCheckSort().equals(promotionCheckFlowDO.getCheckSort() + 1))
                .findFirst();
            if (optional != null && optional.isPresent()) {
                PromotionCheckFlowDO checkingFlowDO = optional.get();
                // 非最后一个审批人审批情况，更新当前审批人
                checkDO.setCheckUserId(checkingFlowDO.getUserId());
                checkDO.setCheckUserName(checkingFlowDO.getUserName());
            } else {
                // 最后一个审批人审批时，审批单审批结束
                checkDO.setCheckStatus(cmd.getCheckStatus());
                if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_1)) {
                    if (promotionDO != null) {
                        promotionDO.setApplyStatus(APPLY_STATUS_2);
                        promotionDO.setUpdateTime(date);
                        promotionCmdExe.updatePromotion(promotionDO);
                    }
                } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_2)) {
                    if (groupSeckillDO != null) {
                        groupSeckillDO.setApplyStatus(APPLY_STATUS_2);
                        groupSeckillDO.setUpdateTime(date);
                        groupSeckillCmdExe.updateGroupSeckill(groupSeckillDO);
                    }
                } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_3)) {
                    if (couponDO != null) {
                        couponDO.setApplyStatus(APPLY_STATUS_2);
                        couponDO.setUpdateTime(date);
                        couponMapper.updateByPrimaryKey(couponDO);
                    }
                } else if (checkDO.getPromotionType().equals(PROMOTION_CHECK_TYPE_4)) {
                    if (!CollectionUtils.isEmpty(rebateVoucherDOS)) {
                        rebateVoucherDOS.forEach(rebateVoucherDO -> {
                            rebateVoucherDO.setApplyStatus(APPLY_STATUS_2);
                            if (date.before(rebateVoucherDO.getStartTime())) {
                                rebateVoucherDO.setVoucherStatus(REBATE_VOUCHER_STATUS_4);
                            } else if (date.after(rebateVoucherDO.getStartTime())
                                && date.before(rebateVoucherDO.getEndTime())) {
                                rebateVoucherDO.setVoucherStatus(REBATE_VOUCHER_STATUS_5);
                            } else if (date.after(rebateVoucherDO.getEndTime())) {
                                rebateVoucherDO.setVoucherStatus(REBATE_VOUCHER_STATUS_7);
                            }
                            rebateVoucherDO.setFreezeStatus(REBATE_VOUCHER_FREEZE_STATUS_10);
                            rebateVoucherDO.setUpdateTime(date);
                        });
                        rebateVoucherMapper.batchUpdate(rebateVoucherDOS);
                    }
                }
            }
            checkDO.setUpdateTime(date);
            checkMapper.updateByPrimaryKey(checkDO);
        }
    }

    /**
     * 活动新增，创建活动审批
     *
     * @param promotionId
     */
    public void promotionCheck(Short promotionType, Integer promotionId, CheckConfigDetailRpcDTO detailRpcDTO,
        Date date, String userId, String userName) {
        List<CheckConfigRpcDTO> checkFlows = detailRpcDTO.getCheckConfigs();
        if (CollectionUtils.isEmpty(checkFlows)) {
            throw PromotionException.buildException(B_PROMOTION_CHECK_FLOW_NULL);
        }
        PromotionCheckDO checkDO =
            PromotionCheckConvertor.toPromotionCheckDO(checkFlows, promotionType, promotionId, date, userId, userName);
        checkMapper.insert(checkDO);
        List<PromotionCheckFlowDO> checkFlowDOS =
            PromotionCheckConvertor.toPromotionCheckFlowDOList(checkFlows, checkDO.getId(), date);
        checkFlowMapper.insertList(checkFlowDOS);
    }

}
