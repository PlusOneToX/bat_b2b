package com.bat.financial.refund.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.refund.dto.RefundCreateCmd;
import com.bat.financial.api.refund.dto.RefundUpdateCmd;
import com.bat.financial.api.refund.dto.data.RefundDistributorDTO;
import com.bat.financial.dao.refund.dataobject.RefundDistributorDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/12 16:24
 */
public class RefundDistributorConvertor {
    public static RefundDistributorDO toRefundDistributorDO(RefundCreateCmd cmd) {
        RefundDistributorDO aDo = new RefundDistributorDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static RefundDistributorDO toRefundDistributorDO(RefundUpdateCmd cmd) {
        RefundDistributorDO aDo = new RefundDistributorDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static List<RefundDistributorDTO> toRefundDistributorDTOList(List<RefundDistributorDO> list) {
        return list.stream().map(RefundDistributorConvertor::toRefundDistributorDTO).collect(Collectors.toList());
    }

    public static RefundDistributorDTO toRefundDistributorDTO(RefundDistributorDO aDo) {
        if (aDo != null) {
            RefundDistributorDTO dto = new RefundDistributorDTO();
            BeanUtils.copyProperties(aDo, dto);
            return dto;
        }
        return null;
    }
}
