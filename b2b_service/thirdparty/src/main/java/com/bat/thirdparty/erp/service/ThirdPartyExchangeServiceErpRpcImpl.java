package com.bat.thirdparty.erp.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyExchangeServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeInvalidCodeDTO;
import com.bat.dubboapi.thirdparty.erp.dto.exchange.ExchangeCodeSyncERPRequest;
import com.bat.thirdparty.erp.api.response.BaseResponse;
import com.bat.thirdparty.erp.api.response.ErpResponse;
import com.bat.thirdparty.erp.api.response.ErpSyncResponse;
import com.bat.thirdparty.erp.manager.ErpDataManager;

@DubboService
public class ThirdPartyExchangeServiceErpRpcImpl implements ThirdPartyExchangeServiceErpRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyExchangeServiceErpRpcImpl.class);

    @Autowired
    private ErpDataManager erpDataManager;

    @Override
    public Response syncExchangeCodeInvalidToErp(ExchangeCodeInvalidCodeDTO exchangeCodeInvalidCodeDTO) {
        exchangeCodeInvalidCodeDTO.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        LOGGER.info("同步作废明码盒码给ERP、参数{}", JSON.toJSONString(exchangeCodeInvalidCodeDTO));
        BaseResponse baseResponse = erpDataManager.syncExchangeCodeInvalid(exchangeCodeInvalidCodeDTO);
        LOGGER.info("同步作废明码盒码给ERP、响应{}", JSON.toJSONString(baseResponse));
        if (baseResponse == null) {
            return Response.buildFailure("同步盒码ERP失败", "同步ERP无响应");
        }
        if (baseResponse != null && (BaseResponse.INVALID_TOKEN.equals(baseResponse.getCode())
            || BaseResponse.INVALID_TOKEN_MSG.equals(baseResponse.getMessage()))) {
            LOGGER.info("erp登陆token失效，同步作废兑换码失败");
            erpDataManager.deleteCachedToken("AXI-token");
            exchangeCodeInvalidCodeDTO.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            LOGGER.info("同步作废明码盒码给ERP、重试参数{}", JSON.toJSONString(exchangeCodeInvalidCodeDTO));
            baseResponse = erpDataManager.syncExchangeCodeInvalid(exchangeCodeInvalidCodeDTO);
            LOGGER.info("同步作废明码盒码给ERP、重试响应{}", JSON.toJSONString(baseResponse));
            if (baseResponse == null) {
                return Response.buildFailure("同步盒码ERP失败", "同步ERP无响应");
            }
        }
        if (!"0".equals(baseResponse.getCode())) {
            return Response.buildFailure(baseResponse.getCode(), baseResponse.getMessage());
        }
        return Response.buildSuccess();
    }

    /**
     * 同步盒码明码到ERP
     * 
     * @param exchangeCodeSyncERPRequest
     * @return
     */
    @Override
    public Response syncBoxCodeAndPlainCodeToErp(ExchangeCodeSyncERPRequest exchangeCodeSyncERPRequest) {
        LOGGER.info("同步盒码到ERP,参数为{}", JSON.toJSONString(exchangeCodeSyncERPRequest));
        exchangeCodeSyncERPRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        ErpSyncResponse baseResponse = erpDataManager.syncBoxCodeAndPlainCodeToErp(exchangeCodeSyncERPRequest);
        LOGGER.info("同步盒码到ERP,响应为{}", JSON.toJSONString(baseResponse));
        Response response = new Response();
        if (baseResponse == null) {
            response.setSuccess(false);
            response.setErrCode("同步盒码ERP失败");
            response.setErrMessage("同步ERP无响应");
            return response;
        }
        if (baseResponse != null && (BaseResponse.INVALID_TOKEN.equals(baseResponse.getCode())
            || BaseResponse.INVALID_TOKEN_MSG.equals(baseResponse.getMessage()))) {
            LOGGER.info("erp登陆token失效，同步盒码失败");
            erpDataManager.deleteCachedToken("AXI-token");
            exchangeCodeSyncERPRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
            baseResponse = erpDataManager.syncBoxCodeAndPlainCodeToErp(exchangeCodeSyncERPRequest);
            if (baseResponse == null) {
                response.setSuccess(false);
                response.setErrCode("同步盒码ERP失败");
                response.setErrMessage("同步ERP无响应");
                return response;
            }
        }
        if (!"0".equals(baseResponse.getCode())) {

            return Response.buildFailure(baseResponse.getCode(), baseResponse.getMessage());
        }
        response.setSuccess(true);
        response.setErrMessage(baseResponse.getMessage());
        response.setData(baseResponse.getData());
        return response;
    }

    public static void main(String[] args) {
        ErpResponse erpResponse = null;
        System.out.println("0".equals(erpResponse.getCode()));
    }
}
