package com.bat.thirdparty.msgcenter.dao;


import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterWechatTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgCenterWechatTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgCenterWechatTemplateDO record);

    int insertSelective(MsgCenterWechatTemplateDO record);

    MsgCenterWechatTemplateDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgCenterWechatTemplateDO record);

    int updateByPrimaryKey(MsgCenterWechatTemplateDO record);

    List<MsgCenterWechatTemplateDO> list();

    void deleteAll();

    void insertList(List<MsgCenterWechatTemplateDO> list);

    MsgCenterWechatTemplateDO selectByType(@Param("type")short type);
}