package com.bat.distributor.dao.distributor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.distributor.dao.distributor.dataobject.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.distributor.dao.distributor.dataobject.*;

@Mapper
public interface DistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorDO record);

    DistributorDO selectByPrimaryKey(Integer id);

    List<DistributorDO> listByIds(@Param("ids") List<Integer> ids);

    DistributorDO selectAncestorByDescendantId(Integer id);

    List<DistributorOneListDO> listOneDistributor(Map map);

    List<DistributorListDO> listDistributor(Map map);

    List<DistributorNextListDO> listNextDistributor(Map map);

    List<DistributorIdsDO> distributorIds(@Param("ids") List<Integer> ids);

    List<DistributorDO> selectAll();

    int updateByPrimaryKey(DistributorDO record);

    void applyDistributor(DistributorDO record);

    void freezeDistributor(@Param("id") Integer id, @Param("freezeStatus") Short freezeStatus,
        @Param("freezeTime") Date freezeTime);

    DistributorDO selectByName(@Param("name") String name);

    void updatePassword(@Param("id") Integer id, @Param("password") String password);

    List<UerNextListDO> listNextDistributorByDistributorId(Map map);

    DistributorNextDO selectNextDistributorById(Integer id);

    void updateApplyStatusById(@Param("id") Integer id, @Param("applyStatus") Short applyStatus,
        @Param("time") Date time);

    List<Integer> listDistributorIdBySalesId(@Param("salesId") Integer salesId);

    UpperDistributorDO getUpperDistributorId(@Param("distributorId")Integer distributorId);

    List<Integer> multiLevelDistributorById(@Param("id") Integer id);

    void freezeMultiLevelDistributors(@Param("id") Integer id, @Param("freezeStatus") Short freezeStatus,
                                      @Param("freezeTime") Date freezeTime);

    DistributorExtendDataDO distributorPaymentWayById(@Param("distributorId") Integer distributorId);

    List<Integer> queryAllSubDistributors(@Param("distributorId") Integer distributorId);

    void deleteSalesTerritoryByDistributorIdList(@Param("subDistributorIdList") List<Integer> subDistributorIdList);

    void inserDistributorSalesArea(@Param("subDistributorIdList") List<Integer> subDistributorIdList, @Param("areaId") Integer areaId);
}