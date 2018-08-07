package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorFinancialDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributorFinancialMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorFinancialDO record);

    DistributorFinancialDO selectByPrimaryKey(Integer id);

    DistributorFinancialDO getByDistributorId(Integer distributorId);

    List<DistributorFinancialDO> selectAll();

    int updateByPrimaryKey(DistributorFinancialDO record);

    void deleteByDistributorId(Integer distributorId);
}