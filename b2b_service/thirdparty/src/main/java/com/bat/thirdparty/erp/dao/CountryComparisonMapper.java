package com.bat.thirdparty.erp.dao;

import java.util.List;

import com.bat.thirdparty.erp.dao.dataobject.CountryComparisonDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryComparisonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CountryComparisonDO record);

    CountryComparisonDO selectByPrimaryKey(Integer id);

    List<CountryComparisonDO> selectAll();

    int updateByPrimaryKey(CountryComparisonDO record);

    CountryComparisonDO selectByCountryId(Integer countryId);
}