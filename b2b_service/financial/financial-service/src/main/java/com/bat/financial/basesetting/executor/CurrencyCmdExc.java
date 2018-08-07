package com.bat.financial.basesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyErpRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.basesetting.dto.CurrencyCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyUpdateCmd;
import com.bat.financial.basesetting.convertor.CurrencyConvertor;
import com.bat.financial.dao.basesetting.CurrencyMapper;
import com.bat.financial.dao.basesetting.dataobject.CurrencyDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class CurrencyCmdExc {

    @Resource
    private CurrencyMapper currencyMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean syncCurrency(List<CurrencyErpRpcDTO> currencyErpRpcDTOS) {
        List<CurrencyDO> currencyDOS = CurrencyConvertor.toCurrencyDOList(currencyErpRpcDTOS);
        currencyDOS.forEach(currencyDO -> {
            CurrencyDO currencyDO1 = currencyMapper.getByErpNo(currencyDO.getErpNo());
            if (currencyDO1 == null) {
                currencyMapper.insert(currencyDO);
            } else {
                currencyDO.setCreateTime(currencyDO1.getCreateTime());
                currencyMapper.updateByPrimaryKeySelective(currencyDO);
            }
        });
        return true;
    }

    public boolean createCurrency(CurrencyCreateCmd cmd) {
        if (cmd.getErpNo() != null) {
            CurrencyDO currencyDO = currencyMapper.getByErpNo(cmd.getErpNo());
            if (currencyDO != null) {
                throw FinancialException.buildException(ErrorCode.B_CURRENCY_ERP_NO_EXISTS);
            }
        }
        CurrencyDO aDo = CurrencyConvertor.toCurrencyDO(cmd);
        currencyMapper.insert(aDo);
        return true;
    }

    public boolean updateCurrency(CurrencyUpdateCmd cmd) {
        CurrencyDO aDo = currencyMapper.selectByPrimaryKey(cmd.getId());
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_CURRENCY_ID_NOT_EXISTS);
        }
        CurrencyDO aDo1 = CurrencyConvertor.toCurrencyDO(cmd);
        currencyMapper.updateByPrimaryKeySelective(aDo1);
        return true;
    }

    public boolean deleteCurrencyById(Integer id) {
        CurrencyDO aDo = currencyMapper.selectByPrimaryKey(id);
        if (aDo == null) {
            throw FinancialException.buildException(ErrorCode.B_CURRENCY_ID_NOT_EXISTS);
        }
        currencyMapper.deleteByPrimaryKey(id);
        return true;
    }
}
