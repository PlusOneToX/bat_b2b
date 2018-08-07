package com.bat.distributor.dao.group;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.distributor.dao.group.dataobject.GroupDO;

@Mapper
public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupDO record);

    GroupDO selectByPrimaryKey(Integer id);

    List<GroupDO> selectAll();

    int updateByPrimaryKey(GroupDO record);

    List<Integer> getGroupDistributorIds(@Param("groupId") Integer groupId);

    void openGroup(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    List<GroupDO> listGroup(Map map);

    List<GroupDO> listByDistributorIds(@Param("distributorGroupIds") List<Integer> distributorGroupIds);
}