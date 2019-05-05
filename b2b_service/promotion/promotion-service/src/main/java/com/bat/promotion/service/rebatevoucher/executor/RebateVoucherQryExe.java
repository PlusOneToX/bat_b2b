package com.bat.promotion.service.rebatevoucher.executor;

import static com.bat.promotion.service.rebatevoucher.executor.ErrorCode.B_PROMOTION_REBATE_VOUCHER_IS_UNAVAILABLE;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.dao.check.PromotionCheckMapper;
import com.bat.promotion.dao.check.dataobject.PromotionCheckDO;
import com.bat.promotion.dao.rebatevoucher.RebateVoucherMapper;
import com.bat.promotion.dao.rebatevoucher.RebateVoucherUsageRecordMapper;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherDO;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherUsageRecordDO;
import com.bat.promotion.service.common.Constant;
import com.bat.promotion.service.rebatevoucher.convertor.RebateVoucherConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherListQry;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherUsageRecordListQry;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherUsageRecordDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:38
 */
@Component
@Slf4j
public class RebateVoucherQryExe {

    @Resource
    private RebateVoucherMapper rebateVoucherMapper;

    @Resource
    private RebateVoucherUsageRecordMapper rebateVoucherUsageRecordMapper;

    @Resource
    private PromotionCheckMapper checkMapper;

    @Transactional(rollbackFor = Exception.class)
    public PageInfo<RebateVoucherDTO> listRebateVoucher(RebateVoucherListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        // BeanMap 方式注入不了新值
        Map<String, Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        if (qry.getVoucherStatusStr() != null) {
            List<String> voucherStatusArr = Arrays.asList(qry.getVoucherStatusStr().split(","));
            if (!CollectionUtils.isEmpty(voucherStatusArr)
                && voucherStatusArr.stream().allMatch(StringUtils::isNumeric)) {
                map.put("voucherStatusArr", voucherStatusArr);
            }
        }
        List<RebateVoucherDO> rebateVoucherDOS = rebateVoucherMapper.selectByParams(map);
        // 冻结状态的代金券 超过有效期 自动变成已过期
        rebateVoucherDOS.forEach(rebateVoucherDO -> {
            boolean voucherStatus = rebateVoucherDO.getVoucherStatus() != null
                && rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_11);
            boolean timeStatus = rebateVoucherDO.getEndTime() != null && new Date().after(rebateVoucherDO.getEndTime());
            if (voucherStatus && timeStatus) {
                rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_7);
                rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
            }
        });
        PageInfo pageInfo = new PageInfo(rebateVoucherDOS);
        List<RebateVoucherDTO> toRebateVoucherDOList =
            RebateVoucherConvertor.toRebateVoucherDTOList(pageInfo.getList());
        List<Integer> promotionIds = toRebateVoucherDOList.stream().map(RebateVoucherDTO::getBatchId)
            .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (!promotionIds.isEmpty()) {
            // 填充审核人员信息
            List<PromotionCheckDO> promotionCheckDOS =
                checkMapper.listByPromotionIdsAndPromotionType(promotionIds, Constant.PROMOTION_CHECK_TYPE_4);
            Map<Integer, PromotionCheckDO> promotionCheckMap =
                promotionCheckDOS.stream().collect(Collectors.toMap(PromotionCheckDO::getPromotionId, k -> k));
            toRebateVoucherDOList.forEach(rebateVoucherDTO -> {
                PromotionCheckDO promotionCheckDO = promotionCheckMap.get(rebateVoucherDTO.getBatchId());
                if (promotionCheckDO != null) {
                    rebateVoucherDTO.setCheckUserId(promotionCheckDO.getCheckUserId());
                    rebateVoucherDTO.setCheckUserName(promotionCheckDO.getCheckUserName());
                    rebateVoucherDTO.setCheckTime(promotionCheckDO.getUpdateTime());
                }
            });
        }
        pageInfo.setList(toRebateVoucherDOList);
        return pageInfo;
    }

    public PageInfo<RebateVoucherUsageRecordDTO> listRebateVoucherUsageRecord(RebateVoucherUsageRecordListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<RebateVoucherUsageRecordDO> rebateVoucherUsageRecordDOS =
            rebateVoucherUsageRecordMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(rebateVoucherUsageRecordDOS);
        List<RebateVoucherUsageRecordDTO> toRebateVoucherDOList =
            RebateVoucherConvertor.toRebateVoucherUsageRecordDTOList(pageInfo.getList());
        pageInfo.setList(toRebateVoucherDOList);
        return pageInfo;
    }

    public BigDecimal getAvailableRebateVoucherBalanceSum(Integer distributorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("distributorId", distributorId);
        map.put("voucherStatus", Constant.REBATE_VOUCHER_STATUS_5);
        List<RebateVoucherDO> list = rebateVoucherMapper.selectByParams(map);
        return list.stream().map(RebateVoucherDO::getBalance).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public List<RebateVoucherDTO> listRebateVoucherByBatchId(Integer batchId) {
        List<RebateVoucherDO> list = rebateVoucherMapper.listRebateVoucherByBatchId(batchId);
        return RebateVoucherConvertor.toRebateVoucherDTOList(list);
    }

    /**
     * 获取用户可用的券 如果不可用 说明原因 如果状态不对，导致前端查出来了（更新状态）
     * 
     * @param distributorId
     * @param rebateVoucherIds
     * @return
     */
    public List<RebateVoucherDTO> listRebateVoucher(Integer distributorId, List<Integer> rebateVoucherIds) {
        Date date = new Date();
        List<RebateVoucherDO> rebateVoucherDOS =
            rebateVoucherMapper.listRebateVoucherByPrimaryKeysAndDistributorId(rebateVoucherIds, distributorId, date);
        List<Integer> ids = rebateVoucherDOS.stream().map(RebateVoucherDO::getId).collect(Collectors.toList());
        rebateVoucherIds.retainAll(ids);
        if (!rebateVoucherIds.isEmpty()) {
            log.info("用户使用的券与实际可用的券不一致，不一致的券号id：{}", rebateVoucherIds);
            rebateVoucherIds.forEach(id -> {
                RebateVoucherDO rebateVoucherDO = rebateVoucherMapper.selectByPrimaryKey(id);
                // 分销商归属
                if (!rebateVoucherDO.getDistributorId().equals(distributorId)) {
                    throwErrMsg("该券不属于当前分销商", rebateVoucherDO);
                }
                // 审核状态
                if (!rebateVoucherDO.getApplyStatus().equals(Constant.APPLY_STATUS_2)) {
                    if (!rebateVoucherDO.getApplyStatus().equals(rebateVoucherDO.getVoucherStatus())) {
                        rebateVoucherDO.setVoucherStatus(rebateVoucherDO.getApplyStatus());
                        rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
                    }
                    throwErrMsg("审核未通过", rebateVoucherDO);
                }
                // 冻结状态
                if (rebateVoucherDO.getFreezeStatus().equals(Constant.REBATE_VOUCHER_FREEZE_STATUS_11)) {
                    if (!rebateVoucherDO.getFreezeStatus().equals(rebateVoucherDO.getVoucherStatus())) {
                        rebateVoucherDO.setVoucherStatus(rebateVoucherDO.getFreezeStatus());
                        rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
                    }
                    throwErrMsg("该券被冻结", rebateVoucherDO);
                }
                // 已用完
                if (rebateVoucherDO.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                    if (!rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_9)) {
                        rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_9);
                        rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
                    }
                    throwErrMsg("该券已被用完", rebateVoucherDO);
                }
                // 有效期
                if (date.before(rebateVoucherDO.getStartTime())) {
                    if (!rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_4)) {
                        rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_4);
                        rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
                    }
                    throwErrMsg("该券尚未生效", rebateVoucherDO);
                } else if (date.after(rebateVoucherDO.getEndTime())) {
                    if (!rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_7)) {
                        rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_7);
                        rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
                    }
                    throwErrMsg("该券已过期", rebateVoucherDO);
                } else if (!rebateVoucherDO.getVoucherStatus().equals(Constant.REBATE_VOUCHER_STATUS_5)) {
                    rebateVoucherDO.setVoucherStatus(Constant.REBATE_VOUCHER_STATUS_5);
                    rebateVoucherMapper.updateByPrimaryKey(rebateVoucherDO);
                    rebateVoucherDOS.add(rebateVoucherDO);
                    throwErrMsg("该券原本不可用，其实可用", rebateVoucherDO, false);
                }
            });
        }
        return RebateVoucherConvertor.toRebateVoucherDTOList(rebateVoucherDOS);
    }

    private void throwErrMsg(String errDetailMsg, RebateVoucherDO rebateVoucherDO) {
        throwErrMsg(errDetailMsg, rebateVoucherDO, true);
    }

    private void throwErrMsg(String errDetailMsg, RebateVoucherDO rebateVoucherDO, boolean throwFlag) {
        String errMsg = MessageFormat.format("券id：{0}，券no：{1}，不可用原因：" + errDetailMsg, rebateVoucherDO.getId(),
            rebateVoucherDO.getVoucherNo());
        log.info(errMsg);
        if (throwFlag) {
            throw PromotionException.buildException(B_PROMOTION_REBATE_VOUCHER_IS_UNAVAILABLE, errMsg);
        }
    }
}
