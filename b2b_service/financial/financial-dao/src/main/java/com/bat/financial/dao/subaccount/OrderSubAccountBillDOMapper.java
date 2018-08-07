package com.bat.financial.dao.subaccount;

import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderSubAccountBillDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSubAccountBillDO record);

    int insertSelective(OrderSubAccountBillDO record);

    OrderSubAccountBillDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSubAccountBillDO record);

    int updateByPrimaryKey(OrderSubAccountBillDO record);

    void batchCreate(@Param("list") List<OrderSubAccountBillDO> list);

    List<OrderSubAccountBillDO> listByCondition(@Param("orderSubAccountId") Integer orderSubAccountId,@Param("status")Short status);

    List<OrderSubAccountBillDO> listByIdList(@Param("idList") List<Integer> idList);

    void batchUpdate(@Param("billDOList") List<OrderSubAccountBillDO> billDOList);
}