package com.bat.thirdparty.msgcenter.dao;

import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface MsgCenterLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterLogDO record);

    int insertSelective(MsgCenterLogDO record);

    MsgCenterLogDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterLogDO record);

    int updateByPrimaryKeyWithBLOBs(MsgCenterLogDO record);

    int updateByPrimaryKey(MsgCenterLogDO record);

    List<MsgCenterLogDO> listByParams(@Param("params")Map<String, Object> map);

    int countByParams(@Param("params")Map<String, Object> map);

    void readAll(@Param("userId") Integer userId, @Param("userType") short userType, @Param("date") Date date);

    void insertList(List<MsgCenterLogDO> list);
}