package com.bat.thirdparty.msgcenter.dao;

import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterAdminRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgCenterAdminRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterAdminRelevanceDO record);

    int insertSelective(MsgCenterAdminRelevanceDO record);

    MsgCenterAdminRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterAdminRelevanceDO record);

    int updateByPrimaryKey(MsgCenterAdminRelevanceDO record);

    void insertList(List<MsgCenterAdminRelevanceDO> list);

    List<Integer> listAdminIdsByCenterId(@Param("centerId")Integer centerId);
}