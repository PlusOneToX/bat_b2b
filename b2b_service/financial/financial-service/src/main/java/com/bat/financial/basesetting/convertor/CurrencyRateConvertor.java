package com.bat.financial.basesetting.convertor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;
import com.bat.financial.api.basesetting.dto.CurrencyRateCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyRateUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyRateDTO;
import com.bat.financial.dao.basesetting.dataobject.CurrencyRateDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:03
 */
public class CurrencyRateConvertor {
    public static List<CurrencyRateDTO> toCurrencyRateDTOList(List<CurrencyRateDO> list) {
        return list.stream().map(currencyDO -> {
            CurrencyRateDTO dto = new CurrencyRateDTO();
            BeanUtils.copyProperties(currencyDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static CurrencyRateDO toCurrencyRateDO(CurrencyRateCreateCmd cmd) {
        CurrencyRateDO aDo = new CurrencyRateDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static CurrencyRateDO toCurrencyRateDO(CurrencyRateUpdateCmd cmd) {
        CurrencyRateDO aDo = new CurrencyRateDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static CurrencyRateDTO toCurrencyRateDTO(CurrencyRateDO aDo) {
        CurrencyRateDTO dto = new CurrencyRateDTO();
        BeanUtils.copyProperties(aDo, dto);
        return dto;
    }

    public static List<CurrencyRateDO> toCurrencyRateDOList(List<CurrencyRateErpRpcDTO> currencyErpRpcDTOS) {
        return currencyErpRpcDTOS.stream().map(currencyRateErpRpcDTO -> {
            CurrencyRateDO aDo = new CurrencyRateDO();
            BeanUtils.copyProperties(currencyRateErpRpcDTO, aDo);
            aDo.setExchangeRate(BigDecimal.valueOf(currencyRateErpRpcDTO.getExchangeRate()));
            aDo.setReverseExRate(BigDecimal.valueOf(currencyRateErpRpcDTO.getReverseExRate()));
            Date date = new Date();
            aDo.setCreateTime(date);
            aDo.setUpdateTime(date);
            return aDo;
        }).collect(Collectors.toList());
    }
}
