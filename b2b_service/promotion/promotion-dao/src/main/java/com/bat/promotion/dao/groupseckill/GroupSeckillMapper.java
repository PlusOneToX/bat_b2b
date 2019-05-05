package com.bat.promotion.dao.groupseckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillSortDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillStatusDO;
import com.bat.promotion.dao.groupseckill.dataobject.GroupSeckillTypeGoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupSeckillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupSeckillDO record);

    int insertSelective(GroupSeckillDO record);

    GroupSeckillDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupSeckillDO record);

    int updateByPrimaryKey(GroupSeckillDO record);

    List<GroupSeckillDO> listGroupSeckill(Map map);

    List<GroupSeckillDO> listGroupSeckillByIds(@Param("ids") List<Integer> ids);

    List<GroupSeckillDO> listGroupSeckillByGoods(Map map);

    List<GroupSeckillDO> listGroupSeckillByDistributorName(Map map);

    List<GroupSeckillTypeGoodsDO>
        listGroupSeckillTypeGoodsByGroupSeckillIds(@Param("groupSeckillIds") List<Integer> groupSeckillIds);

    void updateListGroupSeckillStatus(List<GroupSeckillStatusDO> statusDOS);

    void updateGroupSeckillSort(List<GroupSeckillSortDO> sortDOS);

    List<GroupSeckillDO> listGroupSeckillByTime(@Param("time") Date time);

    List<Integer> listIdByAllDistributor();

    List<Integer> listIdByDistributorId(@Param("distributorId") Integer distributorId);

    List<Integer> listIdByScalePriceId(@Param("scalePriceId") Integer scalePriceId);

    List<Integer> listIdBySalesId(@Param("salesId") Integer salesId);

    List<Integer> listIdByDepartmentId(@Param("departmentId") Integer DepartmentId);
}