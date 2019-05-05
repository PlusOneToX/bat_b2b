package com.bat.thirdparty.erp.service.executor;

import javax.annotation.Resource;

import com.bat.thirdparty.erp.manager.ErpDataManager;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.thirdparty.erp.api.request.CreateReceiveBillEntryRequest;
import com.bat.thirdparty.erp.api.request.CreateRefundBillRequest;
import com.bat.thirdparty.erp.api.response.CreateReceiveBillEntryResponse;
import com.bat.thirdparty.erp.api.response.CreateRefundBillResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/17 14:51
 */
@Component
@Slf4j
public class FinancialErpCmdExc {
    @Resource
    private ErpDataManager erpDataManager;

    public CreateReceiveBillEntryResponse syncReceiveBill(CreateReceiveBillEntryRequest request) {
        request.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        log.info("third party sync voucher request: {}", request);
        log.info("third party sync voucher request json: {}", JSON.toJSONString(request));
        CreateReceiveBillEntryResponse receiveBill = erpDataManager.createReceiveBill(request);
        log.info("third party sync voucher response: {}", receiveBill);
        if ("登陆令牌已失效！".equals(receiveBill.getMessage()) && "te".equals(receiveBill.getCode())) {
            erpDataManager.deleteCachedToken("AXI-token");
            return syncReceiveBill(request);
        } else {
            return receiveBill;
        }
    }

    public CreateRefundBillResponse syncRefundBill(CreateRefundBillRequest request) {
        request.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        log.info("third party sync refund request: {}", request);
        log.info("third party sync refund request json: {}", JSON.toJSONString(request));
        CreateRefundBillResponse receiveBill = erpDataManager.createRefundBill(request);
        log.info("third party sync voucher response: {}", receiveBill);
        if ("登陆令牌已失效！".equals(receiveBill.getMessage()) && "te".equals(receiveBill.getCode())) {
            erpDataManager.deleteCachedToken("AXI-token");
            return syncRefundBill(request);
        } else {
            return receiveBill;
        }
    }
}
