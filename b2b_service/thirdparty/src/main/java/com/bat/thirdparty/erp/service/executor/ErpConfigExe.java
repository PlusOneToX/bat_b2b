package com.bat.thirdparty.erp.service.executor;

import javax.annotation.Resource;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.thirdparty.erp.config.ErpConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ErpConfigExe {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private ErpConfig config;

    // public void getConfig() {
    // Response<PlatformTenantErpRpcDTO> platformTenantErpRpcDTOResponse =
    // platformTenantServiceRpc.erpConfig(TenantContext.getTenantNo(), ThirdCommonConstant.ERP_TYPE_JD);
    // if (!platformTenantErpRpcDTOResponse.isSuccess()) {
    // throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
    // }
    // PlatformTenantErpRpcDTO platformTenantErpRpcDTO = platformTenantErpRpcDTOResponse.getData();
    // config.setUsername(platformTenantErpRpcDTO.getUserName());
    // config.setPassword(platformTenantErpRpcDTO.getPassword());
    // config.setDbId(platformTenantErpRpcDTO.getDbId());
    // config.setLang(platformTenantErpRpcDTO.getLang());
    // config.setBaseUrl(platformTenantErpRpcDTO.getBaseUrl());
    // config.setBaseExtUrl(platformTenantErpRpcDTO.getBaseExtUrl());
    // config.setPlatform(platformTenantErpRpcDTO.getPlatform());
    // config.setErpSettleDefault(platformTenantErpRpcDTO.getSettleDefault());
    // config.setOnlinePayType(platformTenantErpRpcDTO.getSettleCashOnline());
    // config.setOfflinePayType(platformTenantErpRpcDTO.getSettleCashOffline());
    // config.setMonthPayType(platformTenantErpRpcDTO.getSettleMonth());
    // config.setSyncErpPurchaseAndOutboundOrderTime(config.getSyncErpPurchaseAndOutboundOrderTime());
    // }

    public PlatformTenantErpRpcDTO getPlatformTenantErpConfig() {
        Response<PlatformTenantErpRpcDTO> platformTenantErpRpcDTOResponse =
            platformTenantServiceRpc.erpConfig(TenantContext.getTenantNo(), ThirdCommonConstant.ERP_TYPE_JD);
        if (!platformTenantErpRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        return platformTenantErpRpcDTOResponse.getData();
    }

}
