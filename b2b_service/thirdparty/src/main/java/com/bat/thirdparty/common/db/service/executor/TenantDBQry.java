package com.bat.thirdparty.common.db.service.executor;

import static com.bat.thirdparty.common.ThirdCommonConstant.MONGODB_TYPE;

import javax.annotation.Resource;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ThirdPartyConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;

@Component
public class TenantDBQry {

    @DubboReference(check = false, timeout = 30000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private ThirdPartyConfig config;

    /**
     * 根据租户编码获取租户数据库信息
     * 
     * @param tenantNo
     * @return
     */
    public PlatformTenantDBRpcDTO getTenantDB(String tenantNo) {
        Response<PlatformTenantDBRpcDTO> response = platformTenantServiceRpc.dbConfig(tenantNo, config.getModelType());
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public PlatformTenantDBRpcDTO getTenantMongoDb(String tenantNo) {
        Response<PlatformTenantDBRpcDTO> response = platformTenantServiceRpc.dbConfig(tenantNo, MONGODB_TYPE);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

}
