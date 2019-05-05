package com.bat.thirdparty.alibaba.taobao.dao;

import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaobaoOrderDOMapper {
    int deleteByPrimaryKey(Long oid);

    int insert(TaobaoOrderDO record);

    int insertSelective(TaobaoOrderDO record);

    TaobaoOrderDO selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(TaobaoOrderDO record);

    int updateByPrimaryKeyWithBLOBs(TaobaoOrderDO record);

    int updateByPrimaryKey(TaobaoOrderDO record);
}