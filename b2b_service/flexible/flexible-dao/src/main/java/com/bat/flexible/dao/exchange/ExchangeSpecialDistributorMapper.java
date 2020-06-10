package com.bat.flexible.dao.exchange;


import com.bat.flexible.dao.exchange.co.ExchangeSpecialDistributorCO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeSpecialDistributorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeSpecialDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeSpecialDistributorDO record);

    int insertSelective(ExchangeSpecialDistributorDO record);

    ExchangeSpecialDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeSpecialDistributorDO record);

    int updateByPrimaryKey(ExchangeSpecialDistributorDO record);

    void insertList(List<ExchangeSpecialDistributorDO> list);

    List<ExchangeSpecialDistributorCO> listCOByCondition(@Param("exchangeSpecialId")Integer exchangeSpecialId, @Param("distributorId")Integer distributorId);
}