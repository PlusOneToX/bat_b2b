package com.bat.dubboapi.distributor.platform.api;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;

public interface DistributorPlatformApiServiceRpc {

    /**
     * 根据分销商id和apiType类型、平台编码 查询分销商API接口url配置
     * @param distributorId 分销商id
     * @param apiType 事务类型
     * @param platform 分销商平台编码
     * @return
     */
    Response<DistributorPlatformApiRpcDTO> getByDistributorIdAndApiTypeAndPlatform(Integer distributorId, Short apiType, String platform);
}
