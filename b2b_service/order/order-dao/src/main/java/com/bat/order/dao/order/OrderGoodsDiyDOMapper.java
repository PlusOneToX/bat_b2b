package com.bat.order.dao.order;

import java.util.Date;
import java.util.List;

import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyLabelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderGoodsDiyDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderGoodsDiyDO record);

    int insertList(List<OrderGoodsDiyDO> records);

    int insertSelective(OrderGoodsDiyDO record);

    OrderGoodsDiyDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderGoodsDiyDO record);

    int updateByPrimaryKey(OrderGoodsDiyDO record);

    List<OrderGoodsDiyDO> listByOrderId(@Param("orderId") Integer orderId);

    OrderGoodsDiyDO getByOrderGoodsId(Integer id);

    void updateList(@Param("updateList") List<OrderGoodsDiyDO> updateList);

    void updateListLabel(List<OrderGoodsDiyLabelDO> labelDOS);

    /**
     * 根据工厂编码查询未同步工厂的订单
     *
     * @param manufactors
     * @return
     */
    List<Integer> listUnSyncFactory(@Param("manufactors") String manufactors);

    /**
     * 根据时间查询未同步工厂的订单
     *
     * @param startTime
     * @return
     */
    List<Integer> listUnSyncFactoryByStartTime(@Param("startTime") Date startTime);

    List<OrderGoodsDiyDO> findLatelyNullLabel();

    List<Integer> needToSyncOrders(@Param("manufactors") String manufactors);
}