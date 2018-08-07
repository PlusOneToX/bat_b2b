package com.bat.system.dao.storesetting;

import java.util.List;
import java.util.Map;

import com.bat.system.dao.storesetting.dataobject.BannerDO;
import org.apache.ibatis.annotations.Param;

public interface BannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BannerDO record);

    int insertSelective(BannerDO record);

    BannerDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BannerDO record);

    int updateByPrimaryKey(BannerDO record);

    BannerDO getLastOne();

    BannerDO getBySort(Integer sort);

    BannerDO getPreOne(Integer sort);

    BannerDO getNextOne(Integer sort);

    void updateSort(Integer sort);

    List<BannerDO> listByParams(@Param("params") Map<String, Object> map);
}