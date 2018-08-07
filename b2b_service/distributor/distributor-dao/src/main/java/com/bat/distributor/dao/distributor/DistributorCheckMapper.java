package com.bat.distributor.dao.distributor;

import java.util.List;
import java.util.Map;

import com.bat.distributor.dao.distributor.dataobject.DistributorCheckDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorCheckListDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorCheckDO record);

    DistributorCheckDO selectByPrimaryKey(Integer id);

    List<DistributorCheckDO> selectAll();

    int updateByPrimaryKey(DistributorCheckDO record);

    List<DistributorCheckListDO> listByUserId(Map map);

    List<DistributorCheckListDO> listByCheckUserId(Map map);

    DistributorCheckDO selectCheckIngByDistributorId(@Param(value = "distributorId") Integer distributorId);
}