package com.bat.order.dao.order;

import java.util.List;

import com.bat.order.dao.order.dataobject.OrderTypeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderTypeDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderTypeDO record);

    int insertSelective(OrderTypeDO record);

    OrderTypeDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderTypeDO record);

    int updateByPrimaryKey(OrderTypeDO record);

    List<OrderTypeDO> listAll();

    List<OrderTypeDO> selectByParams(@Param("name") String name, @Param("erpType") String erpType);

    List<OrderTypeDO> listByCondition(@Param("openFlag") Short openFlag);

    List<OrderTypeDO> listBySpecialFlag(@Param("specialFlag") Short specialFlag);
}