package com.bat.system.dao.storesetting;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.storesetting.dataobject.NoticeDO;
import org.apache.ibatis.annotations.Param;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NoticeDO record);

    int insertSelective(NoticeDO record);

    NoticeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NoticeDO record);

    int updateByPrimaryKeyWithBLOBs(NoticeDO record);

    int updateByPrimaryKey(NoticeDO record);

    List<NoticeDO> listByParams(@Param("params") Map<String, Object> map);
}