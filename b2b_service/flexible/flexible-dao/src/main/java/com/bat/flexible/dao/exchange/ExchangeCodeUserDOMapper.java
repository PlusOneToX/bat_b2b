package com.bat.flexible.dao.exchange;

import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeUserDO;
import com.bat.flexible.dao.exchange.co.ExchangeCodeUserCO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExchangeCodeUserDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExchangeCodeUserDO record);

    int insertSelective(ExchangeCodeUserDO record);

    ExchangeCodeUserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExchangeCodeUserDO record);

    int updateByPrimaryKey(ExchangeCodeUserDO record);

    List<ExchangeCodeUserCO> listCOByUserIdAndStatus(@Param("userId") Integer userId,@Param("status") Short status);

    ExchangeCodeUserCO findByExchangeCodeId(@Param("exchangeCodeId")Integer exchangeCodeId);

    ExchangeCodeUserDO getByExchangeCodeId(@Param("exchangeCodeId") Integer exchangeCodeId);

    Integer countByCondition(@Param("userId") Integer userId,@Param("status") Short status,@Param("materialId")Integer materialId);


    /**
     * 前台用户进行兑换码解绑
     * @param record
     * @return
     */
    int unboundExchange(ExchangeCodeUserDO record);

}