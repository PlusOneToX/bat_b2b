package com.bat.thirdparty.alibaba.taobao.dao;

import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaobaoTradeDOMapper {
    int deleteByPrimaryKey(Long tid);

    int insert(TaobaoTradeDO record);

    int insertSelective(TaobaoTradeDO record);

    TaobaoTradeDO selectByPrimaryKey(Long tid);

    int updateByPrimaryKeySelective(TaobaoTradeDO record);

    int updateByPrimaryKey(TaobaoTradeDO record);

    List<TaobaoTradeDO> findBySellerNickAndStatus(@Param("sellerNick")String sellerNick, @Param("name")String name);
}