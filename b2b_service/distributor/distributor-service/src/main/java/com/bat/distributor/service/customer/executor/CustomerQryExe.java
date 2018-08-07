package com.bat.distributor.service.customer.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.customer.dto.CustomerListQry;
import com.bat.distributor.api.customer.dto.data.CustomerListDTO;
import com.bat.distributor.api.user.dto.customer.CustomerAddressListQry;
import com.bat.distributor.api.user.dto.customer.CustomerLoginQry;
import com.bat.distributor.api.user.dto.customer.data.CustomerAddressDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerLoginDTO;
import com.bat.distributor.dao.customer.CustomerAddressMapper;
import com.bat.distributor.dao.customer.CustomerMapper;
import com.bat.distributor.dao.customer.dataobject.CustomerAddressDO;
import com.bat.distributor.dao.customer.dataobject.CustomerDO;
import com.bat.distributor.service.customer.convertor.CustomerConvertor;
import com.bat.distributor.service.customer.validator.CustomerValidator;
import com.bat.distributor.service.distributor.validator.DistributorValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 14:39
 */
@Component
public class CustomerQryExe {

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private DistributorValidator distributorValidator;

    @Resource
    private CustomerValidator customerValidator;

    @Resource
    private CustomerAddressMapper customerAddressMapper;

    /**
     * C端客户密码登录
     * 
     * @param qry
     * @param distributorId
     * @return
     */
    public CustomerLoginDTO passwordLogin(CustomerLoginQry qry, String distributorId, String platform) {
        distributorValidator.checkDistributor(distributorId, platform);
        CustomerDO customerDO = customerValidator.checkCustomer(qry.getPhone(), Integer.valueOf(distributorId));
        if (StringUtils.isBlank(customerDO.getPassword())) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CUSTOMER_PASSWORD_NULL);
        }
        if (!customerDO.getPassword().equals(qry.getPassword())) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CUSTOMER_PASSWORD_ERROR);
        }
        return CustomerConvertor.toCustomerLoginDTO(customerDO, null);
    }

    /**
     * C端客户获取个人信息
     * 
     * @param qry
     * @return
     */
    public CustomerDTO getCustomer(BaseId qry) {
        CustomerDO customerDO = customerValidator.checkCustomer(qry.getId());
        return CustomerConvertor.toCustomerDTO(customerDO);
    }

    /**
     * 根据搜索条件查询C端客户列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<CustomerListDTO> listCustomer(CustomerListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CustomerDO> customerDOS = customerMapper.listCustomerDO(qryMap);
        PageInfo pageInfo = new PageInfo(customerDOS);
        // TODO 分销商平台数据
        List<CustomerListDTO> dtos = CustomerConvertor.toCustomerListDTOList(pageInfo.getList(), null);
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 查询分销商收货地址列表(分页)
     *
     * @param qry
     * @return
     */
    public PageInfo<CustomerAddressDTO> listAddress(CustomerAddressListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CustomerAddressDO> addressDOS = customerAddressMapper.listByCustomerId(qry.getId());
        PageInfo pageInfo = new PageInfo(addressDOS);
        List<CustomerAddressDTO> addressDTOS = CustomerConvertor.toCustomerAddressDTOList(pageInfo.getList());
        pageInfo.setList(addressDTOS);
        return pageInfo;
    }

}
