package com.bat.thirdparty.msgcenter.dao;

import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDepartmentRelevanceDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDistributorGroupRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgCenterDistributorGroupRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterDistributorGroupRelevanceDO record);

    int insertSelective(MsgCenterDistributorGroupRelevanceDO record);

    MsgCenterDistributorGroupRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterDistributorGroupRelevanceDO record);

    int updateByPrimaryKey(MsgCenterDistributorGroupRelevanceDO record);

    void insertList(List<MsgCenterDepartmentRelevanceDO> list);

    List<Integer> listDistributorGroupIdByCenterId(@Param("centerId")Integer centerId);
}