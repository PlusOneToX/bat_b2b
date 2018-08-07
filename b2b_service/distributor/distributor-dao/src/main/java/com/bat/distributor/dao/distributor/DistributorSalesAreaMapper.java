package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorSalesAreaDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributorSalesAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorSalesAreaDO record);

    void insertList(List<DistributorSalesAreaDO> doList);

    DistributorSalesAreaDO selectByPrimaryKey(Integer id);

    List<DistributorSalesAreaDO> selectAll();

    List<DistributorSalesAreaDO> listByDistributorId(Integer distributorId);

    int updateByPrimaryKey(DistributorSalesAreaDO record);

    int deleteByDistributorId(Integer distributorId);
}