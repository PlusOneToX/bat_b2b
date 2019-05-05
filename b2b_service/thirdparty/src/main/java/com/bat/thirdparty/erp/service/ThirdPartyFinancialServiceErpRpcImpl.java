package com.bat.thirdparty.erp.service;

import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.service.executor.ErrorCode;
import com.bat.thirdparty.erp.service.executor.FinancialErpCmdExc;
import com.bat.thirdparty.erp.service.executor.FinancialErpQryExc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryReq;
import com.bat.dubboapi.financial.voucher.dto.data.CreateReceiveBillEntryResp;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyFinancialServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.financial.BalanceInfoRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.BalanceInfoDTO;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;
import com.bat.thirdparty.erp.api.request.CreateReceiveBillEntryRequest;
import com.bat.thirdparty.erp.api.request.CreateRefundBillRequest;
import com.bat.thirdparty.erp.api.response.CreateReceiveBillEntryResponse;
import com.bat.thirdparty.erp.api.response.CreateRefundBillResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/17 14:50
 */
@DubboService
@Slf4j
public class ThirdPartyFinancialServiceErpRpcImpl implements ThirdPartyFinancialServiceErpRpc {

    @Resource
    private FinancialErpQryExc financialErpQryExc;

    @Resource
    private FinancialErpCmdExc financialErpCmdExc;

    @DubboReference(check = false, timeout = 30000)
    private FinancialVoucherServiceRpc financialVoucherServiceRpc;

    /**
     * 获取币别列表
     * 
     * @return
     */
    @Override
    public Response<List<CurrencyErpRpcDTO>> listCurrency() {
        try {
            return Response.of(financialErpQryExc.listCurrency());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_CURRENCY_ERROR, MessageUtils.get(ErrorCode.B_ERP_CURRENCY_ERROR));
        }
    }

    /**
     * 获取汇率
     * 
     * @return
     */
    @Override
    public Response<List<CurrencyRateErpRpcDTO>> listCurrencyRate() {
        try {
            return Response.of(financialErpQryExc.listCurrencyRate());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_CURRENCY_RATE_ERROR, MessageUtils.get(ErrorCode.B_ERP_CURRENCY_RATE_ERROR));
        }
    }

    /**
     * 获取余额
     * 
     * @param qry
     * @return
     */
    @Override
    public Response<List<BalanceInfoDTO>> listBalance(BalanceInfoRpcQry qry) {
        try {
            return Response.of(financialErpQryExc.listBalance(qry));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_BALANCE_ERROR, MessageUtils.get(ErrorCode.B_ERP_BALANCE_ERROR));
        }
    }

    /**
     * 同步收款单
     *
     * @param req
     * @return
     */
    // @Override
    public Response<CreateReceiveBillEntryResp> syncVoucher(CreateReceiveBillEntryReq req) {
        try {
            CreateReceiveBillEntryRequest request = new CreateReceiveBillEntryRequest();
            BeanUtils.copyProperties(req, request);
            CreateReceiveBillEntryResponse response = financialErpCmdExc.syncReceiveBill(request);
            CreateReceiveBillEntryResp resp = new CreateReceiveBillEntryResp();
            BeanUtils.copyProperties(response, resp);
            return Response.of(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_SYNC_VOUCHER_RESPONSE_ERROR,
                MessageUtils.get(ErrorCode.B_ERP_SYNC_VOUCHER_RESPONSE_ERROR));
        }
    }

    /**
     * 同步退款
     *
     * @param req
     * @return
     */
    // @Override
    public Response<CreateRefundBillResponse>
        syncRefund(com.bat.dubboapi.financial.refund.dto.data.CreateRefundBillRequest req) {
        try {
            CreateRefundBillRequest request = new CreateRefundBillRequest();
            BeanUtils.copyProperties(req, request);
            CreateRefundBillResponse response = financialErpCmdExc.syncRefundBill(request);
            return Response.of(response);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure(ErrorCode.B_ERP_SYNC_VOUCHER_RESPONSE_ERROR,
                MessageUtils.get(ErrorCode.B_ERP_SYNC_VOUCHER_RESPONSE_ERROR));
        }
    }
}
