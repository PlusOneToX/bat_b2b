package com.bat.dubboapi.distributor.customer.api;

import java.util.List;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.customer.dto.CustomerRpcQry;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 20:38
 */
public interface CustomerServiceRpc {
    /**
     * 根据C端客户id、分销商id和平台获取客户信息
     *
     * @param customerId
     * @param platform
     * @return
     */
    Response<CustomerRpcDTO> getCustomerByCustomerIdAndDistributorIdAndPlatform(Integer customerId,
                                                                                Integer distributorId, String platform);

    /**
     * 根据客户id列表获取客户信息
     * 
     * @param ids
     * @return
     */
    Response<List<CustomerRpcDTO>> listByIds(List<Integer> ids);

    /**
     * 
     * @param otherId
     * @return
     */
    Response<List<CustomerRpcDTO>> listCustomer(CustomerRpcQry rpcQry);
}
