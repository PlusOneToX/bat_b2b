package com.bat.distributor.service.platform.sysplatform;

import com.bat.distributor.dao.platform.dataobject.SysPlatformDO;
import com.bat.distributor.service.platform.sysplatform.executor.SysPlatformQryExe;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.api.DistributorSysPlatformServiceRpc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class DistributorSysPlatformServiceRpcImpl implements DistributorSysPlatformServiceRpc {

    @Autowired
    private SysPlatformQryExe sysPlatformQryExe;

    @Override
    public Response getByPlatformAndDistributorId(String platform,Integer distributorId) {
        SysPlatformDO sysPlatformDO = sysPlatformQryExe.getByPlatformAndDistributorId(platform,distributorId);
        if(sysPlatformDO ==null){
            return Response.of(null);
        }
        return Response.of(PlatformConvertor.toSysPlatformRpcDTO(sysPlatformDO));
    }
}
