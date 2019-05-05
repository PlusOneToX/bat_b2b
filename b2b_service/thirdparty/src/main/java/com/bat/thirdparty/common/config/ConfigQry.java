package com.bat.thirdparty.common.config;

import com.bat.thirdparty.tenant.TenantContext;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;
import com.bat.thirdparty.common.base.ThirdPartyException;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/12/9 17:47
 */
@Component
public class ConfigQry {

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    public PlatformTenantDiyFactoryRpcDTO getTenantFactoryConfig(String factoryNo) {
        Response<PlatformTenantDiyFactoryRpcDTO> response =
            platformTenantServiceRpc.diyFactoryConfig(TenantContext.getTenantNo(), factoryNo);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public PlatformTenantUrlRpcDTO getTenantUrlConfig(Short urlType) {
        Response<PlatformTenantUrlRpcDTO> response =
            platformTenantServiceRpc.urlConfig(TenantContext.getTenantNo(), urlType);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }
}
