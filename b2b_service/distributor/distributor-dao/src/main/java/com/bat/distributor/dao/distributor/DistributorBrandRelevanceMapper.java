package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorBrandRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorBrandRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorBrandRelevanceDO record);

    DistributorBrandRelevanceDO selectByPrimaryKey(Integer id);

    DistributorBrandRelevanceDO selectByDistributorId(Integer distributorId);

    List<DistributorBrandRelevanceDO> selectAll();

    int updateByPrimaryKey(DistributorBrandRelevanceDO record);

    List<DistributorBrandRelevanceDO> listByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorBrandRelevanceDO> listByBrandId(String brandId);

    List<Integer> listDistributorIdsByBrandId(String brandId);

    void deleteByDistributorId(Integer distributorId);

    void updateList(List<DistributorBrandRelevanceDO> relevanceDOS);

    void insertList(List<DistributorBrandRelevanceDO> relevanceDOS);

    int deleteByIds(@Param("ids") List<Integer> ids);

}