package com.bat.distributor.service.customer;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.distributor.service.customer.executor.CustomerQryRpcExe;
import com.bat.dubboapi.distributor.customer.dto.CustomerRpcQry;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.customer.api.CustomerServiceRpc;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;

import java.util.List;

@DubboService
public class CustomerServiceRpcImpl implements CustomerServiceRpc {

    @Resource
    CustomerQryRpcExe qryRpcExe;

    @Override
    public Response<CustomerRpcDTO> getCustomerByCustomerIdAndDistributorIdAndPlatform(Integer customerId,
        Integer distributorId, String platform) {
        CustomerRpcDTO rpcDTO =
            qryRpcExe.getCustomerByCustomerIdAndDistributorIdAndPlatform(customerId, distributorId, platform);
        return Response.of(rpcDTO);
    }

    @Override
    public Response<List<CustomerRpcDTO>> listByIds(List<Integer> ids) {
        List<CustomerRpcDTO>  customerRpcDTOS=qryRpcExe.listByIds(ids);
        return Response.of(customerRpcDTOS);
    }

    @Override
    public Response<List<CustomerRpcDTO>> listCustomer(CustomerRpcQry rpcQry) {
        List<CustomerRpcDTO> customerRpcDTOS = qryRpcExe.listCustomer(rpcQry);
        return Response.of(customerRpcDTOS);
    }
}