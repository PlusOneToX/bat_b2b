package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeShareDistributorDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeShareDistributorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeShareDistributorDO record);

    int insertSelective(ExchangeShareDistributorDO record);

    ExchangeShareDistributorDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeShareDistributorDO record);

    int updateByPrimaryKey(ExchangeShareDistributorDO record);

    void insertList(List<ExchangeShareDistributorDO> list);

    List<ExchangeShareDistributorDO> listByExchangeShareIdAndType(@Param("exchangeShareId")Integer exchangeShareId, @Param("type")short type);

    void deleteByPrimaryKeys(List<Integer> ids);

    void deleteByExchangeShareId(@Param("exchangeShareId")Integer exchangeShareId);
}