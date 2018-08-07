package com.bat.order.dao.importOrder;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.order.dao.importOrder.dataobject.ImportOrderGoodsDO;

@Mapper
public interface ImportOrderGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImportOrderGoodsDO record);

    int insertSelective(ImportOrderGoodsDO record);

    ImportOrderGoodsDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImportOrderGoodsDO record);

    int updateByPrimaryKey(ImportOrderGoodsDO record);

    void insertBatch(@Param("dos") List<ImportOrderGoodsDO> importOrderGoodss);

    void updateBatch(@Param("dos") List<ImportOrderGoodsDO> changeImportOrderGoodsList);

    List<ImportOrderGoodsDO> findByImportOrderId(Integer importOrderId);

    List<ImportOrderGoodsDO> findByImportOrderIds(@Param("dos") List<Integer> importOrderIds);

    int deleteByImportOrderId(Integer id);

    void deleteByPrimaryKeys(@Param("dos") List<Integer> ids);

    void deleteByImportOrderIds(@Param("dos") List<Integer> importOrderIds);
}