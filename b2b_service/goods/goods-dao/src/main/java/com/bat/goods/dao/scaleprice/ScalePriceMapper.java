package com.bat.goods.dao.scaleprice;

import java.util.List;
import java.util.Map;

import com.bat.goods.dao.scaleprice.dataobject.ScalePriceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScalePriceMapper {

    Integer createScalePrice(ScalePriceDO scalePriceDO);

    List<ScalePriceDO> listScalePrice(Map map);

    List<ScalePriceDO> listScalePriceField(@Param("openFlag") Short openFlag);

    Integer listCount(@Param("content") String content);

    ScalePriceDO getById(@Param("id") Integer id);

    Integer updateScalePrice(ScalePriceDO scalePriceDO);

    void openScalePrice(@Param("id") Integer id, @Param("openFlag") Short openFlag);

    void deleteScalePrice(@Param("id") Integer id);

}
