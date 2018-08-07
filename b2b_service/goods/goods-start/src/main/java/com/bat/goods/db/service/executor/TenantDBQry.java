package com.bat.goods.db.service.executor;

import javax.annotation.Resource;

import com.bat.goods.service.common.Constant;
import com.bat.goods.service.common.GoodsConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.goods.api.base.GoodsException;

@Component
public class TenantDBQry {

    @DubboReference(check = false, timeout = 30000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @Resource
    private GoodsConfig goodsConfig;

    /**
     * 根据租户编码获取租户数据库信息
     * 
     * @param tenantNo
     * @return
     */
    public PlatformTenantDBRpcDTO getTenantDB(String tenantNo) {
        Response<PlatformTenantDBRpcDTO> response =
            platformTenantServiceRpc.dbConfig(tenantNo, goodsConfig.getModelType());
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public PlatformTenantDBRpcDTO getTenantRedisDb(String tenantNo) {
        Response<PlatformTenantDBRpcDTO> response = platformTenantServiceRpc.dbConfig(tenantNo, Constant.REDISDB_TYPE);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }
}
