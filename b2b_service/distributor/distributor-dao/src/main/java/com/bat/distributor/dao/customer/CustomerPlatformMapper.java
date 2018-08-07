package com.bat.distributor.dao.customer;

import com.bat.distributor.dao.customer.dataobject.CustomerPlatformDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerPlatformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerPlatformDO record);

    CustomerPlatformDO selectByPrimaryKey(Integer id);

    CustomerPlatformDO selectByOpenIdAndOtherIdAndPlatformAndCustomerId(@Param("customerId") Integer customerId,
        @Param("openId") String openId, @Param("otherId") String otherId, @Param("platform") String platform);

    List<CustomerPlatformDO> listByCustomerIdAndPlatform(@Param("customerId") Integer customerId,
        @Param("platform") String platform);

    List<CustomerPlatformDO> selectAll();

    int updateByPrimaryKey(CustomerPlatformDO record);

    CustomerPlatformDO selectOneByOpenIdAndPlatform( @Param("openId") String openId, @Param("platform") String platform);

    CustomerPlatformDO getByOpenIdAndPlatform(@Param("openId") String openId, @Param("platform") String platform);
}