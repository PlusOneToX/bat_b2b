package com.bat.distributor.dao.customer;

import java.util.List;

import com.bat.distributor.dao.customer.dataobject.CustomerAddressDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAddressDO record);

    CustomerAddressDO selectByPrimaryKey(Integer id);

    List<CustomerAddressDO> selectAll();

    int updateByPrimaryKey(CustomerAddressDO record);

    List<CustomerAddressDO> listByCustomerId(@Param("customerId") Integer customerId);

    List<CustomerAddressDO> listByCustomerIdAndDefaultFlag(@Param("customerId") Integer customerId,
        @Param("defaultFlag") Short defaultFlag);
}