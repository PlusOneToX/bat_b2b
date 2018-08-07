package com.bat.financial.db.service.executor;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.common.FinancialConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TenantDBQry {

    @DubboReference(check = false, timeout = 30000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private FinancialConfig config;

    /**
     * 根据租户编码获取租户数据库信息
     * 
     * @param tenantNo
     * @return
     */
    public PlatformTenantDBRpcDTO getTenantDB(String tenantNo) {
        Response<PlatformTenantDBRpcDTO> response =
            platformTenantServiceRpc.dbConfig(tenantNo, config.getModelType());
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw FinancialException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }
}
