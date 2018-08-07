package com.bat.financial.deposit.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.financial.api.deposit.dto.data.DepositDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorDTO;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.dao.deposit.dataobject.DepositDO;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorDO;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorSubsidiaryBookDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/27 15:02
 */
public class DepositConvertor {
    public static DepositDTO toDepositDTO(DepositDO depositDO) {
        DepositDTO dto = new DepositDTO();
        BeanUtils.copyProperties(depositDO, dto);
        return dto;
    }

    public static List<DepositDistributorDTO>
        toDepositDistributorDTOList(List<DepositDistributorDO> depositDistributorDOS) {
        return depositDistributorDOS.stream().map(depositDistributorDO -> {
            DepositDistributorDTO dto = new DepositDistributorDTO();
            BeanUtils.copyProperties(depositDistributorDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static List<DepositDistributorSubsidiaryBookDTO>
        toDepositDistributorSubsidiaryBookDOList(List<DepositDistributorSubsidiaryBookDO> list) {
        return list.stream().map(depositDistributorSubsidiaryBookDO -> {
            DepositDistributorSubsidiaryBookDTO dto = new DepositDistributorSubsidiaryBookDTO();
            BeanUtils.copyProperties(depositDistributorSubsidiaryBookDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static DepositDistributorDTO toDepositDistributorDTO(DepositDistributorDO distributorDO) {
        DepositDistributorDTO dto = new DepositDistributorDTO();
        BeanUtils.copyProperties(distributorDO, dto);
        return dto;
    }

    public static DepositDistributorDO toDepositDistributorDO(DepositDistributorDTO dto) {
        DepositDistributorDO aDo = new DepositDistributorDO();
        BeanUtils.copyProperties(dto, aDo);
        aDo.setUpdateTime(new Date());
        return aDo;
    }

    public static DepositDistributorSubsidiaryBookDO
        toDepositDistributorSubsidiaryBookDO(DepositDistributorSubsidiaryBookDTO aDo) {
        if (aDo != null) {
            DepositDistributorSubsidiaryBookDO dto = new DepositDistributorSubsidiaryBookDO();
            BeanUtils.copyProperties(aDo, dto);
            return dto;
        }
        return null;
    }
}
