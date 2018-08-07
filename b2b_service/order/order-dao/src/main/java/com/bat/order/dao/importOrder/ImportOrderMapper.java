package com.bat.order.dao.importOrder;

import org.apache.ibatis.annotations.Mapper;

import com.bat.order.dao.importOrder.dataobject.ImportOrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ImportOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImportOrderDO record);

    int insertSelective(ImportOrderDO record);

    ImportOrderDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImportOrderDO record);

    int updateByPrimaryKeyWithBLOBs(ImportOrderDO record);

    int updateByPrimaryKey(ImportOrderDO record);

    void insertBatch(@Param("dos") List<ImportOrderDO> importOrders);

    List<ImportOrderDO> listImportOrderByParams(@Param("params") Map<String, Object> map);

    List<ImportOrderDO> listImportOrderByIds(@Param("dos") List<Integer> importOrderIds);

    void deleteByPrimaryKeys(@Param("dos") List<Integer> importOrderIds);

    void batchUpdate(@Param("dos") List<ImportOrderDO> importOrderList);
}