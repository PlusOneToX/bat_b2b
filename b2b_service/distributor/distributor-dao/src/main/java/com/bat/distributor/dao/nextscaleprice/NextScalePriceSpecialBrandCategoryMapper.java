package com.bat.distributor.dao.nextscaleprice;

import java.util.List;

import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialBrandCategoryDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialFormulaDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NextScalePriceSpecialBrandCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    void deleteByNextScalePriceSpecialId(Integer nextScalePriceSpecialId);

    int insert(NextScalePriceSpecialBrandCategoryDO record);

    int insertList(List<NextScalePriceSpecialBrandCategoryDO> records);

    NextScalePriceSpecialBrandCategoryDO selectByPrimaryKey(Integer id);

    List<NextScalePriceSpecialBrandCategoryDO> selectAll();

    List<NextScalePriceSpecialBrandCategoryDO> listByNextScalePriceSpecialId(Integer nextScalePriceSpecialId);

    List<NextScalePriceSpecialFormulaDO>
        listByNextScalePriceIds(@Param("nextScalePriceIds") List<Integer> nextScalePriceIds);

    void deleteByNextScalePriceSpecialIds(List<Integer> nextScalePriceSpecialIds);

    int updateByPrimaryKey(NextScalePriceSpecialBrandCategoryDO record);
}