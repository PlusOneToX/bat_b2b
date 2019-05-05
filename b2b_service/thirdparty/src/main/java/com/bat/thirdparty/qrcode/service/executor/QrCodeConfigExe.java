package com.bat.thirdparty.qrcode.service.executor;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantOssRpcDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.qrcode.config.QrCodeConfig;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
public class QrCodeConfigExe {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    public QrCodeConfig getQrCodeConfig() {
        Response<PlatformTenantUrlRpcDTO> platformTenantUrlRpcDTOResponse = platformTenantServiceRpc.urlConfig(TenantContext.getTenantNo(),ThirdCommonConstant.URL_TYPE_DISTRIBUTOR_QRCODE);
        if (!platformTenantUrlRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        PlatformTenantUrlRpcDTO platformTenantUrlRpcDTO = platformTenantUrlRpcDTOResponse.getData();

        Response<PlatformTenantOssRpcDTO> platformTenantOssRpcDTOResponse = platformTenantServiceRpc.ossConfig(TenantContext.getTenantNo(), ThirdCommonConstant.OSS_TYPE_ALI);
        if (!platformTenantOssRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        PlatformTenantOssRpcDTO platformTenantOssRpcDTO = platformTenantOssRpcDTOResponse.getData();
        QrCodeConfig qrCodeConfig = new QrCodeConfig();
        qrCodeConfig.setDistributionUrl(platformTenantUrlRpcDTO.getUrl());
        qrCodeConfig.setDistributionOssFolder(platformTenantOssRpcDTO.getDistributorOssFolder());
        qrCodeConfig.setShopOssFolder(platformTenantOssRpcDTO.getDistributorOssFolder());
        return qrCodeConfig;
    }

}
