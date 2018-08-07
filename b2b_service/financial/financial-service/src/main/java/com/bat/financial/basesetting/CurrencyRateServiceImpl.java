package com.bat.financial.basesetting;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.basesetting.executor.CurrencyRateCmdExc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyFinancialServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.basesetting.CurrencyRateService;
import com.bat.financial.api.basesetting.dto.CurrencyRateCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyRateListQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyRateDTO;
import com.bat.financial.basesetting.executor.CurrencyRateQryExc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:00
 */
@Service
@Slf4j
public class CurrencyRateServiceImpl implements CurrencyRateService {

    @Resource
    private CurrencyRateQryExc currencyRateQryExc;

    @Resource
    private CurrencyRateCmdExc currencyRateCmdExc;

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartyFinancialServiceErpRpc thirdPartyFinancialServiceErpRpc;

    @Override
    public CurrencyRateDTO getCurrencyRateById(Integer id) {
        return currencyRateQryExc.getCurrencyRateById(id);
    }

    @Override
    public PageInfo<CurrencyRateDTO> listCurrencyRate(CurrencyRateListQry qry) {
        return currencyRateQryExc.listCurrencyRate(qry);
    }

    @Override
    public boolean createCurrencyRate(CurrencyRateCreateCmd cmd) {
        return currencyRateCmdExc.createCurrencyRate(cmd);
    }

    @Override
    public boolean updateCurrencyRate(CurrencyRateUpdateCmd cmd) {
        return currencyRateCmdExc.updateCurrencyRate(cmd);
    }

    @Override
    public boolean deleteCurrencyRateById(Integer id) {
        return currencyRateCmdExc.deleteCurrencyRateById(id);
    }

    @SneakyThrows
    @Override
    public boolean syncCurrencyRate() {
        Response<List<CurrencyRateErpRpcDTO>> listResponse = thirdPartyFinancialServiceErpRpc.listCurrencyRate();
        if (listResponse.isSuccess()) {
            return currencyRateCmdExc.syncCurrencyRate(listResponse.getData());
        } else {
            throw FinancialException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    @Override
    public CurrencyRateDTO getCurrencyRate(CurrencyRateQry qry) {
        return currencyRateQryExc.getCurrencyRate(qry);
    }

}
