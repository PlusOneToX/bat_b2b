package com.bat.distributor.dao.salesarea;

import java.util.List;
import java.util.Map;

import com.bat.distributor.dao.salesarea.dataobject.SalesAreaDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SalesAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SalesAreaDO record);

    SalesAreaDO selectByPrimaryKey(Integer id);

    List<SalesAreaDO> selectAll();

    int updateByPrimaryKey(SalesAreaDO record);

    Integer getSalesAreaDistributorsCount(@Param("salesAreaId") Integer salesAreaId);

    List<SalesAreaDO> listSalesAreaByIds(@Param("ids") List<Integer> ids);

    void openSalesArea(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<SalesAreaDO> listSalesArea(Map map);
}