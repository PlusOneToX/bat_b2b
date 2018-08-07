package com.bat.distributor.service.customer.executor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.dao.customer.CustomerMapper;
import com.bat.distributor.dao.customer.CustomerPlatformMapper;
import com.bat.distributor.dao.customer.dataobject.CustomerDO;
import com.bat.distributor.dao.customer.dataobject.CustomerPlatformDO;
import com.bat.distributor.dao.platform.PlatformMapper;
import com.bat.distributor.dao.platform.dataobject.PlatformDO;
import com.bat.distributor.service.customer.convertor.CustomerConvertor;
import com.bat.distributor.service.distributor.executor.DistributorRpcQryExe;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.customer.dto.CustomerRpcQry;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 14:39
 */
@Component
public class CustomerQryRpcExe {

    @Resource
    private PlatformMapper platformMapper;
    @Resource
    private CustomerPlatformMapper customerPlatformMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private DistributorRpcQryExe distributorRpcQryExe;

    /**
     * 根据C端客户id、分销商id和平台获取客户信息
     * 
     * @param customerId
     * @param distributorId
     * @param platform
     * @return
     */
    public CustomerRpcDTO getCustomerByCustomerIdAndDistributorIdAndPlatform(Integer customerId, Integer distributorId,
        String platform) {
        CustomerDO customerDO = customerMapper.selectByPrimaryKey(customerId);
        PlatformDO platformDO = null;
        DistributorRpcDTO distributorRpcDTO = null;
        List<CustomerPlatformDO> customerPlatformDOS = null;
        if (customerDO != null) {
            platformDO = platformMapper.selectByPlatformNo(platform);
            customerPlatformDOS = customerPlatformMapper.listByCustomerIdAndPlatform(customerId, platform);
            distributorRpcDTO = distributorRpcQryExe.customerDistributorById(distributorId);
        }
        CustomerRpcDTO rpcDTO =
            CustomerConvertor.toCustomerRpcDTO(customerDO, platformDO, distributorRpcDTO, customerPlatformDOS);
        return rpcDTO;
    }

    public List<CustomerRpcDTO> listByIds(List<Integer> ids) {
        List<CustomerRpcDTO> customerRpcDTOS = new ArrayList<>();
        List<CustomerDO> customerDOS = customerMapper.listByIds(ids);
        PlatformDO platformDO = null;
        DistributorRpcDTO distributorRpcDTO = null;
        List<CustomerPlatformDO> customerPlatformDOS = null;
        for (CustomerDO customerDO : customerDOS) {
            platformDO = platformMapper.selectByPlatformNo(customerDO.getPlatform());
            customerPlatformDOS =
                customerPlatformMapper.listByCustomerIdAndPlatform(customerDO.getId(), customerDO.getPlatform());
            distributorRpcDTO = distributorRpcQryExe.customerDistributorById(customerDO.getDistributorId());
            CustomerRpcDTO rpcDTO =
                CustomerConvertor.toCustomerRpcDTO(customerDO, platformDO, distributorRpcDTO, customerPlatformDOS);
            customerRpcDTOS.add(rpcDTO);
        }
        return customerRpcDTOS;
    }

    public List<CustomerRpcDTO> listCustomer(CustomerRpcQry rpcQry) {
        List<CustomerRpcDTO> customerRpcDTOS = new ArrayList<>();
        List<CustomerDO> customerDOS = customerMapper.listByOtherUidAndDistributorIdAndPlatform(rpcQry.getOtherUid(),
            rpcQry.getDistributorId(), rpcQry.getPlatform());
        PlatformDO platformDO = null;
        DistributorRpcDTO distributorRpcDTO = null;
        List<CustomerPlatformDO> customerPlatformDOS = null;
        for (CustomerDO customerDO : customerDOS) {
            platformDO = platformMapper.selectByPlatformNo(customerDO.getPlatform());
            customerPlatformDOS =
                customerPlatformMapper.listByCustomerIdAndPlatform(customerDO.getId(), customerDO.getPlatform());
            distributorRpcDTO = distributorRpcQryExe.customerDistributorById(customerDO.getDistributorId());
            CustomerRpcDTO rpcDTO =
                CustomerConvertor.toCustomerRpcDTO(customerDO, platformDO, distributorRpcDTO, customerPlatformDOS);
            customerRpcDTOS.add(rpcDTO);
        }
        return customerRpcDTOS;
    }
}
