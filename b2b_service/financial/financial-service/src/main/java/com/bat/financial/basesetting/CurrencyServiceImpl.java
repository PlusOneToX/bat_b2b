package com.bat.financial.basesetting;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.basesetting.executor.CurrencyCmdExc;
import com.bat.financial.basesetting.executor.CurrencyQryExc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyFinancialServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyErpRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.basesetting.CurrencyService;
import com.bat.financial.api.basesetting.dto.CurrencyCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyQry;
import com.bat.financial.api.basesetting.dto.CurrencyUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyDTO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:00
 */
@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    @Resource
    private CurrencyQryExc currencyQryExc;

    @Resource
    private CurrencyCmdExc currencyCmdExc;

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartyFinancialServiceErpRpc thirdPartyFinancialServiceErpRpc;

    @Override
    public CurrencyDTO getCurrencyById(Integer id) {
        return currencyQryExc.getCurrencyById(id);
    }

    @Override
    public PageInfo<CurrencyDTO> listCurrency(CurrencyQry qry) {
        return currencyQryExc.listCurrency(qry);
    }

    @SneakyThrows
    @Override
    public boolean syncCurrency() {
        Response<List<CurrencyErpRpcDTO>> listResponse = thirdPartyFinancialServiceErpRpc.listCurrency();
        if (listResponse.isSuccess()) {
            return currencyCmdExc.syncCurrency(listResponse.getData());
        } else {
            throw FinancialException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }

    }

    @Override
    public boolean createCurrency(CurrencyCreateCmd cmd) {
        return currencyCmdExc.createCurrency(cmd);
    }

    @Override
    public boolean updateCurrency(CurrencyUpdateCmd cmd) {
        return currencyCmdExc.updateCurrency(cmd);
    }

    @Override
    public boolean deleteCurrencyById(Integer id) {
        return currencyCmdExc.deleteCurrencyById(id);
    }
}
