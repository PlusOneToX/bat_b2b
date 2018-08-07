package com.bat.system.dao.trainingdownloadcenter;

import java.util.List;

import com.bat.system.dao.trainingdownloadcenter.dataobject.DownloadCenterDO;
import org.apache.ibatis.annotations.Param;

public interface DownloadCenterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DownloadCenterDO record);

    int insertSelective(DownloadCenterDO record);

    DownloadCenterDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DownloadCenterDO record);

    int updateByPrimaryKey(DownloadCenterDO record);

    List<DownloadCenterDO> listByParentId(Integer parentId);

    List<DownloadCenterDO> listByParentIdRecursion(Integer parentId);

    void updateSort(@Param("parentId") Integer parentId, @Param("sort") Integer sort);

    DownloadCenterDO getPreOne(Integer sort);

    DownloadCenterDO getNextOne(Integer sort);
}