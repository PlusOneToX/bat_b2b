package com.bat.distributor.dao.nextscaleprice;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceFormulaDO;

@Mapper
public interface NextScalePriceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NextScalePriceDO record);

    NextScalePriceDO selectByPrimaryKey(Integer id);

    List<NextScalePriceDO> listByIds(@Param("ids") List<Integer> ids);

    NextScalePriceDO selectByDistributorId(Integer distributorId);

    List<NextScalePriceFormulaDO> listByNextDistributorIds(@Param("distributorIds") List<Integer> distributorIds);

    List<NextScalePriceDO> selectAll();

    List<NextScalePriceDO> listNextScalePrice(Map map);

    int updateByPrimaryKey(NextScalePriceDO record);
}