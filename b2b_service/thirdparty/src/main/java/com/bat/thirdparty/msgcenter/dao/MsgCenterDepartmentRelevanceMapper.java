package com.bat.thirdparty.msgcenter.dao;


import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDepartmentRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgCenterDepartmentRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterDepartmentRelevanceDO record);

    int insertSelective(MsgCenterDepartmentRelevanceDO record);

    MsgCenterDepartmentRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterDepartmentRelevanceDO record);

    int updateByPrimaryKey(MsgCenterDepartmentRelevanceDO record);

    void insertList(List<MsgCenterDepartmentRelevanceDO> list);

    List<Integer> listDepartmentIdByCenterId(@Param("centerId")Integer centerId);
}