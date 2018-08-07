package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorErpIdRpcDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorInfoDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorNameDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorRpcDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorRpcMapper {
    List<DistributorErpIdRpcDO> listDistributorErpIdRpcByErpIds(@Param("erpIds") List<Integer> erpIds);

    List<DistributorErpIdRpcDO>
        listDistributorErpIdRpcByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorNameDO>
        listDistributorNameRpcByDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<DistributorNameDO> getAllDistributorNameOneTreeNode();

    List<DistributorRpcDO> listDistributorRpcByIds(@Param("ids") List<Integer> ids);

    DistributorRpcDO selectDistributorRpcById(Integer id);

    Integer selectOrderTypeIdByDistributorId(@Param("distributorId") Integer distributorId);

    DistributorInfoDO selectDistributorInfoDOByErpId(@Param("erpId") Integer erpId);
}