package com.bat.promotion.dao.groupseckill;

import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.groupseckill.dataobject.UserGoodsItemGroupSeckillDO;
import com.bat.promotion.dao.groupseckill.dataobject.UserGroupSeckillGoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/27 15:18
 */
@Mapper
public interface UserGroupSeckillMapper {

    List<UserGroupSeckillGoodsDO> listGoodsItemIds(Map map);

    List<UserGoodsItemGroupSeckillDO> listGroupSeckillGoodsByGroupSeckillIdsAndGoodsId(
        @Param("groupSeckillIds") List<Integer> groupSeckillIds, @Param("goodsId") Integer goodsId);

}
