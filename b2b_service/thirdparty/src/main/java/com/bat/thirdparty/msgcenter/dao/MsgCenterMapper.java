package com.bat.thirdparty.msgcenter.dao;


import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MsgCenterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterDO record);

    int insertSelective(MsgCenterDO record);

    MsgCenterDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterDO record);

    int updateByPrimaryKeyWithBLOBs(MsgCenterDO record);

    int updateByPrimaryKey(MsgCenterDO record);

    List<MsgCenterDO> listByParams(@Param("params")Map<String, Object> map);
}