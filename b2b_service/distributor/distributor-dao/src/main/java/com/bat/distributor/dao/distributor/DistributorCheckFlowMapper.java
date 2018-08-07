package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorCheckFlowDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorCheckFlowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorCheckFlowDO record);

    void insertList(List<DistributorCheckFlowDO> records);

    DistributorCheckFlowDO selectByPrimaryKey(Integer id);

    List<DistributorCheckFlowDO>
        listByDistributorCheckId(@Param(value = "distributorCheckId") Integer distributorCheckId);

    List<DistributorCheckFlowDO> selectAll();

    int updateByPrimaryKey(DistributorCheckFlowDO record);

    void deleteByIds(@Param(value = "ids") List<Integer> ids);
}