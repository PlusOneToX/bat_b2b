package com.bat.financial.basesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.basesetting.dto.CurrencyRateCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyRateUpdateCmd;
import com.bat.financial.basesetting.convertor.CurrencyRateConvertor;
import com.bat.financial.dao.basesetting.CurrencyRateMapper;
import com.bat.financial.dao.basesetting.dataobject.CurrencyRateDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class CurrencyRateCmdExc {

    @Resource
    private CurrencyRateMapper currencyRateMapper;

    public boolean createCurrencyRate(CurrencyRateCreateCmd cmd) {
        CurrencyRateDO aDo = CurrencyRateConvertor.toCurrencyRateDO(cmd);
        currencyRateMapper.insert(aDo);
        return true;
    }

    public boolean updateCurrencyRate(CurrencyRateUpdateCmd cmd) {
        CurrencyRateDO aDo = currencyRateMapper.selectByPrimaryKey(cmd.getId());
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_CURRENCY_RATE_ID_NOT_EXISTS);
        }
        CurrencyRateDO aDo1 = CurrencyRateConvertor.toCurrencyRateDO(cmd);
        currencyRateMapper.updateByPrimaryKeySelective(aDo1);
        return true;
    }

    public boolean deleteCurrencyRateById(Integer id) {
        CurrencyRateDO aDo = currencyRateMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_CURRENCY_ID_NOT_EXISTS);
        }
        currencyRateMapper.deleteByPrimaryKey(id);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean syncCurrencyRate(List<CurrencyRateErpRpcDTO> currencyErpRpcDTOS) {
        List<CurrencyRateDO> currencyRateDOS = CurrencyRateConvertor.toCurrencyRateDOList(currencyErpRpcDTOS);
        currencyRateDOS.forEach(currencyRateDO -> {
            CurrencyRateDO aDo = currencyRateMapper.getByBegDate(currencyRateDO.getBegDate());
            if (aDo == null) {
                currencyRateMapper.insert(currencyRateDO);
            } else {
                currencyRateDO.setId(aDo.getId());
                currencyRateDO.setCreateTime(aDo.getCreateTime());
                currencyRateMapper.updateByPrimaryKeySelective(currencyRateDO);
            }
        });
        return true;
    }
}
