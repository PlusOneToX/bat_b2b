package com.bat.distributor.dao.customer;

import java.util.List;
import java.util.Map;

import com.bat.distributor.dao.customer.dataobject.CustomerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerDO record);

    CustomerDO selectByPrimaryKey(Integer id);

    CustomerDO selectByPhoneAndDistributorId(@Param("phone") String phone,
        @Param("distributorId") Integer distributorId);

    CustomerDO selectByOpenIdAndOtherUidAndDistributorIdAndPlatform(@Param("openId") String openId,
        @Param("otherId") String otherId, @Param("distributorId") Integer distributorId,
        @Param("platform") String platform);

    List<CustomerDO> listByOtherUidAndDistributorIdAndPlatform(@Param("otherId") String otherId,
        @Param("distributorId") Integer distributorId, @Param("platform") String platform);

    List<CustomerDO> selectAll();

    int updateByPrimaryKey(CustomerDO record);

    List<CustomerDO> listCustomerDO(Map map);

    List<CustomerDO> listByIds(@Param("ids") List<Integer> ids);
}