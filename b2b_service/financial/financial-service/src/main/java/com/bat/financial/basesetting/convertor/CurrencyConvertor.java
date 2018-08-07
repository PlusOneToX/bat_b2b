package com.bat.financial.basesetting.convertor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyErpRpcDTO;
import com.bat.financial.api.basesetting.dto.CurrencyCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyDTO;
import com.bat.financial.dao.basesetting.dataobject.CurrencyDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:03
 */
public class CurrencyConvertor {
    public static List<CurrencyDTO> toCurrencyDTOList(List<CurrencyDO> list) {
        return list.stream().map(currencyDO -> {
            CurrencyDTO dto = new CurrencyDTO();
            BeanUtils.copyProperties(currencyDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public static List<CurrencyDO> toCurrencyDOList(List<CurrencyErpRpcDTO> list) {
        return list.stream().map(currencyDO -> {
            CurrencyDO dto = new CurrencyDO();
            BeanUtils.copyProperties(currencyDO, dto);
            Date date = new Date();
            dto.setCreateTime(date);
            dto.setUpdateTime(date);
            return dto;
        }).collect(Collectors.toList());
    }

    public static CurrencyDO toCurrencyDO(CurrencyCreateCmd cmd) {
        CurrencyDO aDo = new CurrencyDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setCreateTime(date);
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static CurrencyDO toCurrencyDO(CurrencyUpdateCmd cmd) {
        CurrencyDO aDo = new CurrencyDO();
        BeanUtils.copyProperties(cmd, aDo);
        Date date = new Date();
        aDo.setUpdateTime(date);
        return aDo;
    }

    public static CurrencyDTO toCurrencyDTO(CurrencyDO aDo) {
        if (aDo != null) {
            CurrencyDTO dto = new CurrencyDTO();
            BeanUtils.copyProperties(aDo, dto);
            return dto;
        }
        return null;
    }
}
