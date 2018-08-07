package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorBrandRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributorBrandRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorBrandRelevanceNoDO record);

    DistributorBrandRelevanceNoDO selectByPrimaryKey(Integer id);

    List<DistributorBrandRelevanceNoDO> selectAll();

    int updateByPrimaryKey(DistributorBrandRelevanceNoDO record);

    DistributorBrandRelevanceNoDO selectByDistributorId(Integer distributorId);

    void deleteByDistributorId(Integer distributorId);

    int insertList(List<DistributorBrandRelevanceNoDO> record);
}