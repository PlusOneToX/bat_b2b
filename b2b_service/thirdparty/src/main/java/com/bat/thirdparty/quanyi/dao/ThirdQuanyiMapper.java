package com.bat.thirdparty.quanyi.dao;


import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThirdQuanyiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdQuanyiDO record);

    int insertSelective(ThirdQuanyiDO record);

    ThirdQuanyiDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThirdQuanyiDO record);

    int updateByPrimaryKeyWithBLOBs(ThirdQuanyiDO record);

    int updateByPrimaryKey(ThirdQuanyiDO record);

    ThirdQuanyiDO findByDistributorIdAndThirdCode(@Param("distributorId")Integer distributorId, @Param("thirdCode")String thirdCode);

    List<ThirdQuanyiDO> listByCondition(@Param("distributorId")Integer distributorId, @Param("distributorName")String distributorName, @Param("secretCode")String secretCode, @Param("thirdCode")String thirdCode, @Param("thirdPhone")String thirdPhone,@Param("status")Short status);

    ThirdQuanyiDO findByExchangeCodeId(@Param("exchangeCodeId")Integer exchangeCodeId);

    List<ThirdQuanyiDO> findByExchangeCodeIds(@Param("exchangeCodeIds")List<Integer> exchangeCodeIds);
}