package com.bat.distributor.dao.electricity;

import com.bat.distributor.dao.electricity.dataobject.DistributorElectricityRelationMappingDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorElectricityRelationMappingDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorElectricityRelationMappingDO record);

    int insertSelective(DistributorElectricityRelationMappingDO record);

    DistributorElectricityRelationMappingDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorElectricityRelationMappingDO record);

    int updateByPrimaryKey(DistributorElectricityRelationMappingDO record);

    DistributorElectricityRelationMappingDO getBySellerNick(@Param("sellerNick") String sellerNick);

    DistributorElectricityRelationMappingDO getByDistributorIdAndSellerNick(@Param("distributorId") Integer distributorId,@Param("sellerNick") String sellerNick);

    List<DistributorElectricityRelationMappingDO> getAll();
}