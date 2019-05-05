package com.bat.thirdparty.msgcenter.dao;


import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDepartmentRelevanceDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDistributorRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgCenterDistributorRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterDistributorRelevanceDO record);

    int insertSelective(MsgCenterDistributorRelevanceDO record);

    MsgCenterDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterDistributorRelevanceDO record);

    int updateByPrimaryKey(MsgCenterDistributorRelevanceDO record);

    void insertList(List<MsgCenterDepartmentRelevanceDO> list);

    List<Integer> listDistributorIdByCenterId(@Param("centerId")Integer centerId);
}