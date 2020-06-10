package com.bat.flexible.dao.third;

import com.bat.flexible.dao.third.dataobject.ThirdCourierContrastDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ThirdCourierContrastDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdCourierContrastDO record);

    int insertSelective(ThirdCourierContrastDO record);

    ThirdCourierContrastDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThirdCourierContrastDO record);

    int updateByPrimaryKey(ThirdCourierContrastDO record);

    ThirdCourierContrastDO getByDistributorIdAndDistributionKdnCode(@Param("distributorId") Integer distributorId,@Param("distributionKdnCode") String distributionKdnCode);

}