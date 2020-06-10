package com.bat.flexible.dao.label;

import com.bat.flexible.dao.label.dataobject.LabelDistributorRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LabelDistributorRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LabelDistributorRelevanceDO record);

    int insertSelective(LabelDistributorRelevanceDO record);

    LabelDistributorRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LabelDistributorRelevanceDO record);

    int updateByPrimaryKey(LabelDistributorRelevanceDO record);

    List<LabelDistributorRelevanceDO> listByLabelIdAndDistributorId(@Param("labelId") Integer labelId,@Param("distributorId")Integer distributorId);
}