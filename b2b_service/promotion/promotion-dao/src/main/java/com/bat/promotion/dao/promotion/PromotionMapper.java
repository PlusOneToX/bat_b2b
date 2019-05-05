package com.bat.promotion.dao.promotion;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bat.promotion.dao.promotion.dataobject.PromotionStatusDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bat.promotion.dao.promotion.dataobject.PromotionDO;

@Mapper
public interface PromotionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromotionDO record);

    void insertList(List<PromotionDO> records);

    int insertSelective(PromotionDO record);

    PromotionDO selectByPrimaryKey(Integer id);

    List<PromotionDO> listPromotion(Map map);

    List<PromotionDO> listPromotionExport(Map map);

    List<PromotionDO> listPromotionByGoods(Map map);

    List<PromotionDO> listPromotionByRuleName(Map map);

    List<PromotionDO> listPromotionByConditionName(Map map);

    List<PromotionDO> listPromotionByDistributorName(Map map);

    int updateByPrimaryKeySelective(PromotionDO record);

    int updateByPrimaryKey(PromotionDO record);

    void updateListPromotionStatus(List<PromotionStatusDO> statusDOS);

    List<PromotionDO> listPromotionByTime(@Param("time") Date time);

    List<PromotionDO> listByIds(@Param("ids") List<Integer> ids);

    List<Integer> listIdByAllDistributor();

    List<Integer> listIdByDistributorId(@Param("distributorId") Integer distributorId);

    List<Integer> listIdByScalePriceId(@Param("scalePriceId") Integer scalePriceId);

    List<Integer> listIdBySalesId(@Param("salesId") Integer salesId);

    List<Integer> listIdByDepartmentId(@Param("departmentId") Integer DepartmentId);
}