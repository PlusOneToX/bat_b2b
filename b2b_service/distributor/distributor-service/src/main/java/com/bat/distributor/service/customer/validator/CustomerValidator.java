package com.bat.distributor.service.customer.validator;

import static com.bat.distributor.service.common.Constant.CUSTOMER_STATUS_2;
import static com.bat.distributor.service.customer.executor.ErrorCode.*;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.dao.customer.CustomerMapper;
import com.bat.distributor.dao.customer.dataobject.CustomerDO;
import org.springframework.stereotype.Component;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/9 14:38
 */
@Component
public class CustomerValidator {
    @Resource
    private CustomerMapper customerMapper;

    public CustomerDO checkCustomer(Integer id) {
        CustomerDO customerDO = customerMapper.selectByPrimaryKey(id);
        if (customerDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_NULL);
        }
        if (customerDO.getStatus().equals(CUSTOMER_STATUS_2)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_STATUS);
        }
        return customerDO;
    }

    public CustomerDO checkCustomer(String phone, Integer distributorId) {
        CustomerDO customerDO = customerMapper.selectByPhoneAndDistributorId(phone, distributorId);
        if (customerDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_NULL);
        }
        if (customerDO.getStatus().equals(CUSTOMER_STATUS_2)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_STATUS);
        }
        return customerDO;
    }

    public CustomerDO checkCustomer(Integer id, String phone) {
        CustomerDO customerDO = checkCustomer(id);
        if (customerDO.getPhone() == null || !customerDO.getPhone().equals(phone)) {
            throw DistributorException.buildException(B_DISTRIBUTOR_CUSTOMER_PHONE_ERROR);
        }
        return customerDO;
    }

}
