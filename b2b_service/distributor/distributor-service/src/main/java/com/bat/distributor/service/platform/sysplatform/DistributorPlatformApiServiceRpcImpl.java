package com.bat.distributor.service.platform.sysplatform;

import com.alibaba.fastjson.JSON;
import com.bat.distributor.dao.platform.dataobject.DistributorPlatformApiRpcDO;
import com.bat.distributor.service.platform.sysplatform.executor.SysPlatformQryExe;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.api.DistributorPlatformApiServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class DistributorPlatformApiServiceRpcImpl implements DistributorPlatformApiServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributorPlatformApiServiceRpcImpl.class);

    @Autowired
    private SysPlatformQryExe sysPlatformQryExe;

    /**
     * 根据分销商id和apiType类型、平台编码 查询分销商API接口url配置
     * @param distributorId 分销商id
     * @param apiType 事务类型
     * @param platform 分销商平台编码
     * @return
     */
    @Override
    public Response<DistributorPlatformApiRpcDTO> getByDistributorIdAndApiTypeAndPlatform(Integer distributorId, Short apiType, String platform) {
        DistributorPlatformApiRpcDO distributorPlatformApiRpcDO = sysPlatformQryExe.getByDistributorIdAndApiTypeAndPlatform(
                distributorId,apiType,platform);
        if(distributorPlatformApiRpcDO ==null){
            return Response.of(null);
        }
        DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO = PlatformConvertor.toDistributorPlatformApiRpcDTO(distributorPlatformApiRpcDO);
        LOGGER.info("返回分销商平台配置URL信息{}", JSON.toJSONString(distributorPlatformApiRpcDTO));
        return Response.of(distributorPlatformApiRpcDTO);
    }
}
