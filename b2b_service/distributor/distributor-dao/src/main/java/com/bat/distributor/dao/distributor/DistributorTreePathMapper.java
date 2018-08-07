package com.bat.distributor.dao.distributor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.distributor.dao.distributor.dataobject.DistributorTreePathDO;

@Mapper
public interface DistributorTreePathMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorTreePathDO record);

    void insertList(List<DistributorTreePathDO> treePathDOS);

    DistributorTreePathDO selectByPrimaryKey(Integer id);

    DistributorTreePathDO selectByDistributorDescendantIdAndTreeNode(
        @Param("distributorDescendantId") Integer distributorDescendantId, @Param("treeNode") Integer treeNode);

    List<DistributorTreePathDO> selectAll();

    int updateByPrimaryKey(DistributorTreePathDO record);

    List<DistributorTreePathDO> listByDistributorDescendantId(Integer distributorDescendantId);

    List<DistributorTreePathDO>
        listByDistributorDescendantIds(@Param("distributorDescendantIds") List<Integer> distributorDescendantIds);
}