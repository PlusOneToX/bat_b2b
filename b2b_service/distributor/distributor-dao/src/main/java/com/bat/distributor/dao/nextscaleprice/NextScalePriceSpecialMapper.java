package com.bat.distributor.dao.nextscaleprice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialDO;

@Mapper
public interface NextScalePriceSpecialMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NextScalePriceSpecialDO record);

    NextScalePriceSpecialDO selectByPrimaryKey(Integer id);

    List<NextScalePriceSpecialDO> selectAll();

    List<NextScalePriceSpecialDO> listByNextScalePriceId(Integer nextScalePriceId);

    void deleteByNextScalePriceId(Integer nextScalePriceId);

    int updateByPrimaryKey(NextScalePriceSpecialDO record);
}