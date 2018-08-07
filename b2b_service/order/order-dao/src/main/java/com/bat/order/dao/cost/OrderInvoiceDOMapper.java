package com.bat.order.dao.cost;

import org.apache.ibatis.annotations.Mapper;

import com.bat.order.dao.cost.dataobject.OrderInvoiceDO;

@Mapper
public interface OrderInvoiceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInvoiceDO record);

    int insertSelective(OrderInvoiceDO record);

    OrderInvoiceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderInvoiceDO record);

    int updateByPrimaryKey(OrderInvoiceDO record);

    OrderInvoiceDO getByOrderId(Integer id);
}