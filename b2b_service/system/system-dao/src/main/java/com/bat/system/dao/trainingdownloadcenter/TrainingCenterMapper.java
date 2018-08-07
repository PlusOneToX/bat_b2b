package com.bat.system.dao.trainingdownloadcenter;

import java.util.List;

import com.bat.system.dao.trainingdownloadcenter.dataobject.TrainingCenterDO;
import org.apache.ibatis.annotations.Param;

public interface TrainingCenterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrainingCenterDO record);

    int insertSelective(TrainingCenterDO record);

    TrainingCenterDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrainingCenterDO record);

    int updateByPrimaryKey(TrainingCenterDO record);

    List<TrainingCenterDO> listByParentId(Integer parentId);

    TrainingCenterDO getPreOne(Integer sort);

    TrainingCenterDO getNextOne(Integer sort);

    void updateSort(@Param("parentId") Integer parentId, @Param("sort") Integer sort);

    List<TrainingCenterDO> listByParentIdRecursion(Integer parentId);
}