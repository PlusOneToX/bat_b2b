package com.bat.financial.basesetting;

import javax.annotation.Resource;

import com.bat.financial.basesetting.executor.CurrencyQryExc;
import com.bat.financial.basesetting.executor.ErrorCode;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import com.bat.dubboapi.financial.basesetting.api.FinancialCurrencyServiceRpc;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRateRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;
import com.bat.dubboapi.financial.common.Response;
import com.bat.financial.api.base.MessageUtils;
import com.bat.financial.api.basesetting.dto.data.CurrencyDTO;
import com.bat.financial.api.basesetting.dto.data.CurrencyRateDTO;
import com.bat.financial.basesetting.executor.CurrencyRateQryExc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:00
 */
@DubboService
@Slf4j
public class CurrencyServiceRpcImpl implements FinancialCurrencyServiceRpc {

    @Resource
    private CurrencyQryExc currencyQryExc;

    @Resource
    private CurrencyRateQryExc currencyRateQryExc;

    @Override
    public Response<CurrencyRpcDO> getCurrencyByCurrencyCode(String CurrencyCode) {
        try {
            CurrencyDTO currencyByCurrencyCode = currencyQryExc.getCurrencyByCurrencyCode(CurrencyCode);
            if (currencyByCurrencyCode != null) {
                CurrencyRpcDO aDo = new CurrencyRpcDO();
                BeanUtils.copyProperties(currencyByCurrencyCode, aDo);
                return Response.of(aDo);
            }
            return Response.buildFailure(ErrorCode.B_CURRENCY_NOT_EXISTS,
                MessageUtils.get(ErrorCode.B_CURRENCY_NOT_EXISTS));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
    }

    @Override
    public Response<CurrencyRpcDO> getCurrencyByErpNo(String erpNo) {
        try {
            CurrencyDTO currencyByCurrencyCode = currencyQryExc.getCurrencyByErpNo(erpNo);
            if (currencyByCurrencyCode != null) {
                CurrencyRpcDO aDo = new CurrencyRpcDO();
                BeanUtils.copyProperties(currencyByCurrencyCode, aDo);
                return Response.of(aDo);
            }
            return Response.buildFailure(ErrorCode.B_CURRENCY_NOT_EXISTS,
                MessageUtils.get(ErrorCode.B_CURRENCY_NOT_EXISTS));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
    }

    @Override
    public Response<CurrencyRateRpcDTO> getCurrencyRate(String forCurrencyCode, String toCurrencyCode) {
        try {
            CurrencyRateDTO rateDTO =
                currencyRateQryExc.getCurrencyRateByForToCurrencyCode(forCurrencyCode, toCurrencyCode);
            if (rateDTO != null) {
                CurrencyRateRpcDTO dto = new CurrencyRateRpcDTO();
                BeanUtils.copyProperties(rateDTO, dto);
                return Response.of(dto);
            }
            return Response.buildFailure(ErrorCode.B_CURRENCY_RATE_NOT_EXISTS,
                MessageUtils.get(ErrorCode.B_CURRENCY_RATE_NOT_EXISTS));
        } catch (BeansException e) {
            e.printStackTrace();
            return Response.buildFailure(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(com.bat.financial.api.base.ErrorCode.SYSTEM_EXCEPTION));
        }
    }
}
