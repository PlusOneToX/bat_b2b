package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorGoodsRelevanceNoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributorGoodsRelevanceNoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorGoodsRelevanceNoDO record);

    DistributorGoodsRelevanceNoDO selectByPrimaryKey(Integer id);

    List<DistributorGoodsRelevanceNoDO> selectAll();

    int updateByPrimaryKey(DistributorGoodsRelevanceNoDO record);

    DistributorGoodsRelevanceNoDO selectByDistributorId(Integer distributorId);

    void deleteByDistributorId(Integer distributorId);

    void insertList(List<DistributorGoodsRelevanceNoDO> record);
}