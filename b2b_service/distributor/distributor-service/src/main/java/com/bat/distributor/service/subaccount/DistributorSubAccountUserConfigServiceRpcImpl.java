package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountUserConfigQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountUserConfigServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountUserConfigRpcDTOQry;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class DistributorSubAccountUserConfigServiceRpcImpl implements DistributorSubAccountUserConfigServiceRpc {

    @Autowired
    private DistributorSubAccountUserConfigQryExe distributorSubAccountUserConfigQryExe;

    @Override
    public Response<DistributorSubAccountUserConfigRpcDTOQry> getUserSubAccountConfigById(Integer id) {
        try {
            DistributorSubAccountUserConfigDO configDO = distributorSubAccountUserConfigQryExe.getById(id);
            return Response.of(BeanUtils.copy(configDO,DistributorSubAccountUserConfigRpcDTOQry.class));
        } catch (DistributorException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(),e.getMsg());
        }
    }
}
