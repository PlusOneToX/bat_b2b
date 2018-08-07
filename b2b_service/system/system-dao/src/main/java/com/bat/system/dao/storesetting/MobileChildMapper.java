package com.bat.system.dao.storesetting;


import com.bat.system.dao.storesetting.dataobject.MobileChildDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MobileChildMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MobileChildDO record);

    int insertSelective(MobileChildDO record);

    MobileChildDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MobileChildDO record);

    int updateByPrimaryKey(MobileChildDO record);

    void deleteByParentId(@Param("parentId")Integer parentId);

    List<MobileChildDO> listByParentId(Integer parentId);
}