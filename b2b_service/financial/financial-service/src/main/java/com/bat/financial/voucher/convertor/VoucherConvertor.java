package com.bat.financial.voucher.convertor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.voucher.dto.data.VoucherDistributorDTO;
import com.bat.financial.dao.voucher.dataobject.VoucherDistributorDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/5 19:38
 */
public class VoucherConvertor {
    public static List<VoucherDistributorDTO> toVoucherDistributorDTOList(List<VoucherDistributorDO> list) {
        return list.stream().map(VoucherConvertor::toVoucherDistributorDTO).collect(Collectors.toList());
    }

    public static VoucherDistributorDTO toVoucherDistributorDTO(VoucherDistributorDO voucherDistributorDO) {
        if (voucherDistributorDO != null) {
            VoucherDistributorDTO dto = new VoucherDistributorDTO();
            BeanUtils.copyProperties(voucherDistributorDO, dto);
            return dto;
        }
        return null;
    }

}
