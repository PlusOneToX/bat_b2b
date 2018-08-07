package com.bat.warehouse.manager.common.config;

import com.bat.warehouse.Tenant.TenantContext;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantErpRpcDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantRpcDTO;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.manager.common.constant.WarehouseCommonConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2018/05/9 17:47
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
            throw WarehouseException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public PlatformTenantUrlRpcDTO getTenantUrlConfig(Short urlType) {
        Response<PlatformTenantUrlRpcDTO> response =
            platformTenantServiceRpc.urlConfig(TenantContext.getTenantNo(), urlType);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw WarehouseException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public PlatformTenantErpRpcDTO getTenantErpConfig() {
        Response<PlatformTenantErpRpcDTO> response =
                platformTenantServiceRpc.erpConfig(TenantContext.getTenantNo(), WarehouseCommonConstant.ERP_TYPE_JD);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw WarehouseException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public PlatformTenantRpcDTO getTenantConfig() {
        Response<PlatformTenantRpcDTO> response =
                platformTenantServiceRpc.baseConfig(TenantContext.getTenantNo());
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw WarehouseException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }
}
