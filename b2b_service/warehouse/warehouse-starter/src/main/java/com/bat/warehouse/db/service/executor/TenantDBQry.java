package com.bat.warehouse.db.service.executor;

import com.bat.warehouse.manager.common.config.WarehouseConfig;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.warehouse.api.base.common.exception.WarehouseException;
import com.bat.warehouse.api.base.util.MessageUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TenantDBQry {

    @DubboReference(check = false, timeout = 30000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private WarehouseConfig config;

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
            throw new WarehouseException(MessageUtils.get(response.getErrCode()));
        }
    }
}
