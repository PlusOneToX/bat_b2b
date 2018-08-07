package com.bat.distributor.dao.distributor;

import java.util.List;

import com.bat.distributor.dao.distributor.dataobject.DistributorBusinessDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorNameDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DistributorBusinessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorBusinessDO record);

    DistributorBusinessDO selectByPrimaryKey(Integer id);

    DistributorBusinessDO getByDistributorId(Integer distributorId);

    List<DistributorBusinessDO> selectAll();

    int updateByPrimaryKey(DistributorBusinessDO record);

    List<Integer> listIdBySalesIdsAndOneTreeNode(@Param("salesIds") List<Integer> salesIds);

    List<DistributorNameDO> listDistributorNameBySalesIdAndOneTreeNode(@Param("salesIds") List<Integer> salesIds);

    List<Integer>
        listIdByDistributorGroupIdsAndOneTreeNode(@Param("distributorGroupIds") List<Integer> distributorGroupIds);

    List<DistributorNameDO> listNameByDistributorGroupIdsAndOneTreeNode(@Param("distributorGroupIds")List<Integer> distributorGroupIds);

    List<Integer> listIdBySalesIds(@Param("salesIds") List<Integer> salesIds);

    void deleteByDistributorId(Integer distributorId);


}