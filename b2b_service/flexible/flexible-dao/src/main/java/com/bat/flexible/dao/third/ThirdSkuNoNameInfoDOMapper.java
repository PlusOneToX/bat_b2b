package com.bat.flexible.dao.third;

import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThirdSkuNoNameInfoDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdSkuNoNameInfoDO record);

    int insertSelective(ThirdSkuNoNameInfoDO record);

    ThirdSkuNoNameInfoDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThirdSkuNoNameInfoDO record);

    int updateByPrimaryKey(ThirdSkuNoNameInfoDO record);

    List<ThirdSkuNoNameInfoDO> listByDistributorId(@Param("distributorId") Integer distributorId);

    void deleteByDistributorId(@Param("distributorId") Integer distributorId);
}