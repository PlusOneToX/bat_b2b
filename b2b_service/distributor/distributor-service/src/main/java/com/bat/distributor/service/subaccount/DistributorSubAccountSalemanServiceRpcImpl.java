package com.bat.distributor.service.subaccount;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountSalemanQryExe;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.subaccount.DistributorSubAccountSalemanServiceRpc;
import com.bat.dubboapi.distributor.subaccount.dto.DistributorSubAccountSalemanRpcDTOQry;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class DistributorSubAccountSalemanServiceRpcImpl implements DistributorSubAccountSalemanServiceRpc {


    @Autowired
    private DistributorSubAccountSalemanQryExe distributorSubAccountSalemanQryExe;

    @Override
    public Response<DistributorSubAccountSalemanRpcDTOQry> getSubAccountSalemanById(Integer id) {
        try {
            DistributorSubAccountSalemanDO accountSalemanDO = distributorSubAccountSalemanQryExe.getById(id);
            return Response.of(BeanUtils.copy(accountSalemanDO,DistributorSubAccountSalemanRpcDTOQry.class));
        } catch (DistributorException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(),e.getMsg());
        }
    }

    @Override
    public Response<List<DistributorSubAccountSalemanRpcDTOQry>> listAllSalemanByDistributorId(Integer distributorId) {
        List<DistributorSubAccountSalemanDO> list = distributorSubAccountSalemanQryExe.listByCondition(distributorId, null, null, null);
        return Response.of(BeanUtils.copyList(list,DistributorSubAccountSalemanRpcDTOQry.class));
    }
}
