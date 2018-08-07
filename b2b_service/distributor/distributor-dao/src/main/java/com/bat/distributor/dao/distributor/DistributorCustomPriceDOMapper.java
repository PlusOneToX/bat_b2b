package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorCustomPriceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorCustomPriceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorCustomPriceDO record);

    int insertSelective(DistributorCustomPriceDO record);

    DistributorCustomPriceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorCustomPriceDO record);

    int updateByPrimaryKey(DistributorCustomPriceDO record);

    List<DistributorCustomPriceDO> listByDistributorIdAndItemId(@Param("distributorId") Integer distributorId,
        @Param("itemId") Integer itemId);

    List<DistributorCustomPriceDO> listByDistributorIdAndItemIds(@Param("distributorId") Integer distributorId,
        @Param("itemIds") List<Integer> itemIds);
}