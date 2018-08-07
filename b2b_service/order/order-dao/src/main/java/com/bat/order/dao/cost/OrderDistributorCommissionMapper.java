package com.bat.order.dao.cost;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.order.dao.cost.dataobject.OrderDistributorCommissionDO;

@Mapper
public interface OrderDistributorCommissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDistributorCommissionDO record);

    void insertList(List<OrderDistributorCommissionDO> records);

    int insertSelective(OrderDistributorCommissionDO record);

    OrderDistributorCommissionDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDistributorCommissionDO record);

    int updateByPrimaryKey(OrderDistributorCommissionDO record);
}