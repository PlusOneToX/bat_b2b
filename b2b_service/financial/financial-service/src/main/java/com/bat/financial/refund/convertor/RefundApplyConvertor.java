package com.bat.financial.refund.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.refund.dto.RefundApplyCreateCmd;
import com.bat.financial.api.refund.dto.RefundApplyUpdateCmd;
import com.bat.financial.api.refund.dto.data.RefundCustomerApplyDTO;
import com.bat.financial.api.refund.dto.data.RefundDistributorApplyDTO;
import com.bat.financial.dao.refund.dataobject.RefundCustomerApplyDO;
import com.bat.financial.dao.refund.dataobject.RefundDistributorApplyDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 16:26
 */
public class RefundApplyConvertor {
    public static RefundDistributorApplyDO toRefundDistributorApplyDO(RefundApplyCreateCmd cmd) {
        RefundDistributorApplyDO aDo = new RefundDistributorApplyDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static RefundDistributorApplyDO toRefundDistributorApplyDO(RefundApplyUpdateCmd cmd) {
        RefundDistributorApplyDO aDo = new RefundDistributorApplyDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static List<RefundDistributorApplyDTO> toRefundDistributorApplyDTOList(List<RefundDistributorApplyDO> list) {
        return list.stream().map(refundDistributorApplyDO -> {
            RefundDistributorApplyDTO dto = new RefundDistributorApplyDTO();
            BeanUtils.copyProperties(refundDistributorApplyDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static List<RefundCustomerApplyDTO> toRefundCustomerApplyDTOList(List<RefundCustomerApplyDO> list) {
        return list.stream().map(refundCustomerApplyDO -> {
            RefundCustomerApplyDTO dto = new RefundCustomerApplyDTO();
            BeanUtils.copyProperties(refundCustomerApplyDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
