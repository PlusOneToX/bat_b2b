package com.bat.thirdparty.msgcenter.dao;


import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDepartmentRelevanceDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterScalePriceRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgCenterScalePriceRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterScalePriceRelevanceDO record);

    int insertSelective(MsgCenterScalePriceRelevanceDO record);

    MsgCenterScalePriceRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterScalePriceRelevanceDO record);

    int updateByPrimaryKey(MsgCenterScalePriceRelevanceDO record);

    void insertList(List<MsgCenterDepartmentRelevanceDO> list);

    List<Integer> listScalePriceIdByCenterId(@Param("centerId")Integer centerId);
}