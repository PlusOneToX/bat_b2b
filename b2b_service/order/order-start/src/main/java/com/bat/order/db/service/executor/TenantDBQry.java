package com.bat.order.db.service.executor;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.service.common.config.OrderConfig;

@Component
@Slf4j
public class TenantDBQry {

    @DubboReference(check = false, timeout = 30000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private OrderConfig config;

    /**
     * 根据租户编码获取租户数据库信息
     * 
     * @param tenantNo
     * @return
     */
    public PlatformTenantDBRpcDTO getTenantDB(String tenantNo) {
//        log.info("租户：{},数据类型：{}",tenantNo,config.getModelType());
        Response<PlatformTenantDBRpcDTO> response = platformTenantServiceRpc.dbConfig(tenantNo, config.getModelType());
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }
}
