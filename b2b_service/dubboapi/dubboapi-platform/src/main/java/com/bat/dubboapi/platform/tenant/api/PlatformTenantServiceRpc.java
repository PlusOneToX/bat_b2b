package com.bat.dubboapi.platform.tenant.api;

import java.util.List;

import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.dto.data.*;

public interface PlatformTenantServiceRpc {
    /**
     * 根据服务模块获取所有租户数据库信息
     *
     * @return
     */
    Response<List<PlatformTenantDBRpcDTO>> listTenantDBByModelType(Short modelType);

    /**
     * 根据租户id和服务模块获取租户数据库信息
     *
     * @param tenantNo
     * @param modelType
     */
    Response<PlatformTenantDBRpcDTO> dbConfig(String tenantNo, Short modelType);

    /**
     * 获取工厂配置
     *
     * @param tenantNo
     * @param factoryNo
     * @return
     */
    Response<PlatformTenantDiyFactoryRpcDTO> diyFactoryConfig(String tenantNo, String factoryNo);

    /**
     * 获取erp配置
     *
     * @param tenantNo
     * @param erpType
     * @return
     */
    Response<PlatformTenantErpRpcDTO> erpConfig(String tenantNo, Short erpType);

    /**
     * 获取oss配置
     *
     * @param tenantNo
     * @param ossType
     * @return
     */
    Response<PlatformTenantOssRpcDTO> ossConfig(String tenantNo, Short ossType);

    /**
     * 获取短信配置
     *
     * @param tenantNo
     * @param smsType
     * @return
     */
    Response<PlatformTenantSmsRpcDTO> smsConfig(String tenantNo, Short smsType);

    /**
     * 获取url配置
     *
     * @param tenantNo
     * @param urlType
     * @return
     */
    Response<PlatformTenantUrlRpcDTO> urlConfig(String tenantNo, Short urlType);

    /**
     * 获取租户公共配置
     *
     * @param tenantNo
     * @return
     */
    Response<PlatformTenantCommonRpcDTO> commonConfig(String tenantNo);

    /**
     * 获取基本信息配置
     * 
     * @param tenantNo
     * @return
     */
    Response<PlatformTenantRpcDTO> baseConfig(String tenantNo);

    /**
     * <h2>获取所有工厂标识factory_no</h2>
     */
    Response<List<String>> factoryNoList();
}
