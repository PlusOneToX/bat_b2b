package com.bat.thirdparty.oss.service.executor;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantOssRpcDTO;
import com.bat.thirdparty.oss.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OssConfigExe {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    public OssConfig getOssConfig() {
        Response<PlatformTenantOssRpcDTO> platformTenantOssRpcDTOResponse = platformTenantServiceRpc.ossConfig(TenantContext.getTenantNo(), ThirdCommonConstant.OSS_TYPE_ALI);
        if (!platformTenantOssRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        } else {
            PlatformTenantOssRpcDTO platformTenantOssRpcDTO = platformTenantOssRpcDTOResponse.getData();
            OssConfig ossConfig = new OssConfig();
            BeanUtils.copyProperties(platformTenantOssRpcDTO, ossConfig);
            return ossConfig;
        }
    }

}
