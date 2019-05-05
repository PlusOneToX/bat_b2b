package com.bat.promotion.service.check.executor;

import static com.bat.promotion.service.check.executor.ErrorCode.B_PROMOTION_CHECK_NULL;
import static com.bat.promotion.service.common.Constant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.promotion.service.coupon.executor.CouponQryExe;
import com.bat.promotion.service.groupseckill.executor.GroupSeckillQryExe;
import com.bat.promotion.service.promotion.executor.PromotionQryExe;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.check.dto.PromotionCheckListQry;
import com.bat.promotion.api.check.dto.data.PromotionCheckDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckListDTO;
import com.bat.promotion.api.coupon.dto.data.CouponDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.dao.check.PromotionCheckFlowMapper;
import com.bat.promotion.dao.check.PromotionCheckMapper;
import com.bat.promotion.dao.check.dataobject.PromotionCheckDO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckFlowDO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckListDO;
import com.bat.promotion.service.check.convertor.PromotionCheckConvertor;
import com.bat.promotion.service.rebatevoucher.executor.RebateVoucherQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/9/14 14:20
 */
@Component
public class PromotionCheckQryExe {

    @Resource
    private PromotionCheckMapper checkMapper;
    @Resource
    private PromotionCheckFlowMapper checkFlowMapper;
    @Resource
    private PromotionQryExe promotionQryExe;
    @Resource
    private GroupSeckillQryExe groupSeckillQryExe;
    @Resource
    private CouponQryExe couponQryExe;
    @Resource
    private RebateVoucherQryExe rebateVoucherQryExe;

    /**
     * 促销活动审批列表
     * 
     * @param qry
     * @param userId
     * @return
     */
    public PageInfo<PromotionCheckListDTO> listPromotionCheck(PromotionCheckListQry qry, String userId) {
        Map<String, Object> qryMap = new HashMap<>(BeanMap.create(qry));
        qryMap.put("userId", userId);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<PromotionCheckListDO> promotionCheckListDOS = null;
        // 标签类型：1,我发起的 2,待我审批 3,我审批的
        if (qry.getLabelType().equals(LABEL_TYPE_1)) {
            promotionCheckListDOS = checkMapper.listByUserId(qryMap);
        } else {
            promotionCheckListDOS = checkMapper.listByCheckUserId(qryMap);
        }
        PageInfo pageInfo = new PageInfo(promotionCheckListDOS);
        List<PromotionCheckListDTO> promotionCheckListDTOS =
            PromotionCheckConvertor.toPromotionCheckListDTOList(pageInfo.getList());
        pageInfo.setList(promotionCheckListDTOS);
        return pageInfo;
    }

    /**
     * 促销活动审批详情
     * 
     * @param qry
     * @return
     */
    public PromotionCheckDTO getPromotionCheck(BaseId qry) {
        PromotionCheckDO checkDO = checkMapper.selectByPrimaryKey(qry.getId());
        if (checkDO == null) {
            throw PromotionException.buildException(B_PROMOTION_CHECK_NULL);
        }
        List<PromotionCheckFlowDO> checkFlowDOS = checkFlowMapper.listByPromotionCheckId(qry.getId());
        PromotionCheckDTO checkDTO = PromotionCheckConvertor.toPromotionCheckDTO(checkDO, checkFlowDOS);
        if (checkDTO.getCheckType().equals(CHECK_TYPE_1)) {
            // 活动类型：1 促销活动 2 拼团秒杀活动 3 优惠券
            if (checkDTO.getPromotionType().equals(PROMOTION_CHECK_TYPE_1)) {
                PromotionDTO promotion = promotionQryExe.getPromotion(checkDO.getPromotionId());
                checkDTO.setPromotion(promotion);
            } else if (checkDTO.getPromotionType().equals(PROMOTION_CHECK_TYPE_2)) {
                GroupSeckillDTO groupSeckill = groupSeckillQryExe.getGroupSeckill(checkDO.getPromotionId());
                checkDTO.setGroupSeckill(groupSeckill);
            } else if (checkDTO.getPromotionType().equals(PROMOTION_CHECK_TYPE_3)) {
                CouponDTO coupon = couponQryExe.getCoupon(checkDO.getPromotionId());
                checkDTO.setCoupon(coupon);
            } else if (checkDTO.getPromotionType().equals(PROMOTION_CHECK_TYPE_4)) {
                List<RebateVoucherDTO> voucherDTOS =
                    rebateVoucherQryExe.listRebateVoucherByBatchId(checkDO.getPromotionId());
                checkDTO.setRebateVouchers(voucherDTOS);
            }
        }
        return checkDTO;
    }

}
