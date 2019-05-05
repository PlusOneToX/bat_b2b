package com.bat.promotion.service.rebatevoucher.convertor;

import static com.bat.promotion.service.rebatevoucher.executor.ErrorCode.B_PROMOTION_REBATE_VOUCHER_FACE_VALUE_MUST_BE_GREATER_THAN_ZERO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherDO;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherStatusDO;
import com.bat.promotion.dao.rebatevoucher.dataobject.RebateVoucherUsageRecordDO;
import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.rebatevoucher.dto.RebateVoucherCmd;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherDTO;
import com.bat.promotion.api.rebatevoucher.dto.data.RebateVoucherUsageRecordDTO;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/12/31 15:38
 */
public class RebateVoucherConvertor {

    public static RebateVoucherDO toRebateVoucherDO(RebateVoucherCmd cmd, Date date) {
        return toRebateVoucherDO(cmd, date, CommonUtils.getRebateVoucherNo(date), null, null);
    }

    public static RebateVoucherDO toRebateVoucherDO(RebateVoucherCmd cmd, Date date, String voucherNo, String userId,
        String userName) {
        RebateVoucherDO rebateVoucherDO = new RebateVoucherDO();
        BeanUtils.copyProperties(cmd, rebateVoucherDO);
        rebateVoucherDO.setCreateTime(date);
        rebateVoucherDO.setUpdateTime(date);
        if (rebateVoucherDO.getFaceValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw PromotionException.buildException(B_PROMOTION_REBATE_VOUCHER_FACE_VALUE_MUST_BE_GREATER_THAN_ZERO);
        }
        if (cmd.getId() == null && StringUtils.isNotBlank(voucherNo)) {
            // 新增
            if (rebateVoucherDO.getDistributorId() == null) {
                throw PromotionException.buildException("P_PROMOTION_REBATE_VOUCHER_DISTRIBUTOR_ID_NULL");
            }
            if (StringUtils.isBlank(rebateVoucherDO.getDistributorName())) {
                throw PromotionException.buildException("P_PROMOTION_REBATE_VOUCHER_DISTRIBUTOR_NAME_NULL");
            }
            rebateVoucherDO.setVoucherNo(voucherNo);
            rebateVoucherDO.setBalance(rebateVoucherDO.getFaceValue());
            rebateVoucherDO.setFreezeStatus(Constant.REBATE_VOUCHER_FREEZE_STATUS_10);
            if (StringUtils.isNoneBlank(userId, userName) && StringUtils.isNumeric(userId)) {
                rebateVoucherDO.setCreateUserId(Integer.valueOf(userId));
                rebateVoucherDO.setCreateUserName(userName);
            }
        }
        return rebateVoucherDO;
    }

    public static List<RebateVoucherDO> toRebateVoucherDOList(List<RebateVoucherCmd> cmds, Date date, String userId,
        String userName) {
        String dataStr = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        if (!CollectionUtils.isEmpty(cmds)) {
            List<RebateVoucherDO> list = new ArrayList<>();
            for (int i = 0; i < cmds.size(); i++) {
                RebateVoucherCmd cmd = cmds.get(i);
                RebateVoucherDO rebateVoucherDO =
                    toRebateVoucherDO(cmd, date, CommonUtils.getRebateVoucherNo(dataStr, i), userId, userName);
                list.add(rebateVoucherDO);
            }
            return list;
        }
        return new ArrayList<>();
    }

    public static RebateVoucherDTO toRebateVoucherDTO(RebateVoucherDO rebateVoucherDO) {
        if (rebateVoucherDO != null) {
            RebateVoucherDTO rebateVoucherDTO = new RebateVoucherDTO();
            BeanUtils.copyProperties(rebateVoucherDO, rebateVoucherDTO);
            return rebateVoucherDTO;
        }
        return null;
    }

    public static List<RebateVoucherDTO> toRebateVoucherDTOList(List<? extends RebateVoucherDO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(RebateVoucherConvertor::toRebateVoucherDTO).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static RebateVoucherUsageRecordDTO
        toRebateVoucherUsageRecordDTO(RebateVoucherUsageRecordDO rebateVoucherDO) {
        if (rebateVoucherDO != null) {
            RebateVoucherUsageRecordDTO rebateVoucherDTO = new RebateVoucherUsageRecordDTO();
            BeanUtils.copyProperties(rebateVoucherDO, rebateVoucherDTO);
            return rebateVoucherDTO;
        }
        return null;
    }

    public static List<RebateVoucherUsageRecordDTO>
        toRebateVoucherUsageRecordDTOList(List<? extends RebateVoucherUsageRecordDO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(RebateVoucherConvertor::toRebateVoucherUsageRecordDTO)
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static List<RebateVoucherStatusDO> toRebateVoucherStatusDOList(List<Integer> ids,
                                                                          Short rebateVoucherStatus) {
        List<RebateVoucherStatusDO> statusDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach(id -> {
                RebateVoucherStatusDO statusDO = new RebateVoucherStatusDO();
                statusDO.setUpdateTime(new Date());
                statusDO.setId(id);
                statusDO.setRebateVoucherStatus(rebateVoucherStatus);
                statusDOS.add(statusDO);
            });
        }
        return statusDOS;
    }
}
