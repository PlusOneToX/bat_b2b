package com.bat.thirdparty.erp.service;

import java.util.ArrayList;
import java.util.List;

import com.bat.thirdparty.common.error.WarehouseThirdErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyWarehouseServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInventoryQry;
import com.bat.thirdparty.erp.api.request.InquiryInventoryRequest;
import com.bat.thirdparty.erp.api.response.BaseResponse;
import com.bat.thirdparty.erp.api.response.InquiryInventoryResponse;
import com.bat.thirdparty.erp.manager.ErpDataManager;

@DubboService
public class ThirdPartyWarehouseServiceErpRpcImpl implements ThirdPartyWarehouseServiceErpRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyWarehouseServiceErpRpcImpl.class);

    @Autowired
    private ErpDataManager erpDataManager;

    /**
     * 分页条件查询ERP库存
     * 
     * @param inquiryInventoryQry
     * @return
     */
    @Override
    public Response<List<InquiryInfo>> syncStockFromERP(InquiryInventoryQry inquiryInventoryQry) {
        InquiryInventoryRequest inquiryInventoryRequest = new InquiryInventoryRequest();
        inquiryInventoryRequest.setDatetime(inquiryInventoryQry.getDatetime());
        inquiryInventoryRequest.setFMATERIALID(inquiryInventoryQry.getItemErpIdList());
        inquiryInventoryRequest.setPAGEINDEX(inquiryInventoryQry.getPage());
        inquiryInventoryRequest.setPAGECOUNT(inquiryInventoryQry.getSize());
        List<String> warehouseNoList = new ArrayList<>();
        warehouseNoList.add(inquiryInventoryQry.getWarehouseNo());
        inquiryInventoryRequest.setStokeId(warehouseNoList);
        inquiryInventoryRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
        // 定义失败计数器、失败再重试两次
        Integer failTime = 0;
        InquiryInventoryResponse inquiryInventoryResponse = null;
        while (failTime < 3) {
            LOGGER.info("请求ERP库存同步参数{}", JSON.toJSONString(inquiryInventoryRequest));
            inquiryInventoryResponse = erpDataManager.findErpStock(inquiryInventoryRequest);
            if (inquiryInventoryResponse != null
                && (BaseResponse.INVALID_TOKEN.equals(inquiryInventoryResponse.getCode())
                    || BaseResponse.INVALID_TOKEN_MSG.equals(inquiryInventoryResponse.getMessage()))) {
                // 令牌失效
                erpDataManager.deleteCachedToken("AXI-token");
                inquiryInventoryRequest.setACCESSTOKEN(erpDataManager.getToken("AXI-token"));
                LOGGER.info("同步ERP库存令牌失效、重新请求参数,{}", JSON.toJSONString(inquiryInventoryRequest));
                inquiryInventoryResponse = erpDataManager.findErpStock(inquiryInventoryRequest);
            }
            if (inquiryInventoryResponse == null || !inquiryInventoryResponse.isSuccess()) {
                failTime++;
                LOGGER.error("查询ERP库存、失败了{}次、参数为：{}", failTime,
                    JSON.toJSONString(inquiryInventoryQry) + ",响应：{}" + JSON.toJSONString(inquiryInventoryResponse));
            } else {
                break;
            }
        }
        if (inquiryInventoryResponse.isSuccess()) {
            return Response.of(inquiryInventoryResponse.getData());
        }
        LOGGER.info("同步ERP库存失败超过3次、不再重试、本次同步ERP库存失败");
        return Response.buildFailure(WarehouseThirdErrorCode.T_WAREHOUSE_SYNC_ERP_STOCK_ERROR,
            MessageUtils.get(WarehouseThirdErrorCode.T_WAREHOUSE_SYNC_ERP_STOCK_ERROR));
    }
}
