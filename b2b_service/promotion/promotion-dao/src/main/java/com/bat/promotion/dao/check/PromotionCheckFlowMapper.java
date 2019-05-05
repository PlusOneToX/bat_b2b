package com.bat.promotion.dao.check;

import java.util.List;

import com.bat.promotion.dao.check.dataobject.PromotionCheckFlowDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PromotionCheckFlowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionCheckFlowDO record);

    void insertList(List<PromotionCheckFlowDO> records);

    PromotionCheckFlowDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(PromotionCheckFlowDO record);

    List<PromotionCheckFlowDO> listByPromotionCheckId(@Param(value = "promotionCheckId") Integer promotionCheckId);

    void deleteByIds(@Param(value = "ids") List<Integer> ids);
}