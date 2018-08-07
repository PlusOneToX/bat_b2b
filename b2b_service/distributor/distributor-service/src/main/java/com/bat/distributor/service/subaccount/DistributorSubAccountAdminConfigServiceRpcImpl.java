package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountAdminConfigDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountAdminConfigQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountAdminConfigServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountAdminConfigRpcDTOQry;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class DistributorSubAccountAdminConfigServiceRpcImpl implements DistributorSubAccountAdminConfigServiceRpc {

    @Autowired
    private DistributorSubAccountAdminConfigQryExe distributorSubAccountAdminConfigQryExe;


    @Override
    public Response<DistributorSubAccountAdminConfigRpcDTOQry> getAdminSubAccountConfigByDistributorId(Integer distributorId) {
        DistributorSubAccountAdminConfigDO adminConfigDO = distributorSubAccountAdminConfigQryExe.getByDistributorId(distributorId);
        return Response.of(BeanUtils.copy(adminConfigDO,DistributorSubAccountAdminConfigRpcDTOQry.class));
    }
}
