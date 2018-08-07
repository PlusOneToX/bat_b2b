package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorAddressDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorAddressMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByDistributorId(Integer distributorId);

    int insert(DistributorAddressDO record);

    DistributorAddressDO selectByPrimaryKey(Integer id);

    List<DistributorAddressDO> listByDistributorIdAndAddressType(@Param("distributorId") Integer distributorId,
        @Param("addressType") Short addressType);

    List<DistributorAddressDO> selectAll();

    int updateByPrimaryKey(DistributorAddressDO record);

    List<DistributorAddressDO> listByDistributorIdAndDefaultFlag(@Param("distributorId") Integer distributorId,
        @Param("defaultFlag") Short defaultFlag);
}