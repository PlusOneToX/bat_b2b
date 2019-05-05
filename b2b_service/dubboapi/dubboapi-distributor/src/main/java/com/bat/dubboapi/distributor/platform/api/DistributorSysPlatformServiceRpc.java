package com.bat.dubboapi.distributor.platform.api;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;

public interface DistributorSysPlatformServiceRpc {

    /**
     * 根据分销商平台编码查询分销商系统平台配置
     * @param platform
     * @param distributorId
     * @return
     */
    Response<SysPlatformRpcDTO> getByPlatformAndDistributorId(String platform, Integer distributorId);




}
