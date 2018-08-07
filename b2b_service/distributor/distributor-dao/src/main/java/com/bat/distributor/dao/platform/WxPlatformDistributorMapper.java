package com.bat.distributor.dao.platform;

import java.util.List;

import com.bat.distributor.dao.platform.dataobject.WxPlatformDistributorDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxPlatformDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxPlatformDistributorDO record);

    WxPlatformDistributorDO selectByPrimaryKey(Integer id);

    List<WxPlatformDistributorDO> selectAll();

    int updateByPrimaryKey(WxPlatformDistributorDO record);

    void deleteByWxPlatformId(Integer wxPlatformId);

    void insertList(List<WxPlatformDistributorDO> records);

    List<WxPlatformDistributorDO> listByWxPlatformId(Integer wxPlatformId);
}