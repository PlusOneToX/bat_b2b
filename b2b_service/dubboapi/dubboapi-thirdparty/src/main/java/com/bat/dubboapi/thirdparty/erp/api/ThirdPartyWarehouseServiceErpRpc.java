package com.bat.dubboapi.thirdparty.erp.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInfo;
import com.bat.dubboapi.thirdparty.erp.dto.warehouse.InquiryInventoryQry;

import java.util.List;

public interface ThirdPartyWarehouseServiceErpRpc {

    Response<List<InquiryInfo>> syncStockFromERP(InquiryInventoryQry inquiryInventoryQry);

}
