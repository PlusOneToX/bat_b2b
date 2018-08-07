package com.bat.order.dao.importOrder;

import org.apache.ibatis.annotations.Mapper;

import com.bat.order.dao.importOrder.dataobject.ImportOrderDetailDO;

@Mapper
public interface ImportOrderDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImportOrderDetailDO record);

    int insertSelective(ImportOrderDetailDO record);

    ImportOrderDetailDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImportOrderDetailDO record);

    int updateByPrimaryKey(ImportOrderDetailDO record);
}