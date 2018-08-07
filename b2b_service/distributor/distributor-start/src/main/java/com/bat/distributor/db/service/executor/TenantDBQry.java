package com.bat.distributor.db.service.executor;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.service.common.DistributorConfig;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;

@Component
public class TenantDBQry {

    @DubboReference(check = false, timeout = 30000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private DistributorConfig config;

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
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }
}
