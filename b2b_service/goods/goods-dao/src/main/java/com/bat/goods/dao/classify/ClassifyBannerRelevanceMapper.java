package com.bat.goods.dao.classify;

import com.bat.goods.dao.classify.dataobject.ClassifyBannerRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassifyBannerRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassifyBannerRelevanceDO record);

    int insertSelective(ClassifyBannerRelevanceDO record);

    ClassifyBannerRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassifyBannerRelevanceDO record);

    int updateByPrimaryKey(ClassifyBannerRelevanceDO record);

    void insertOrUpdateList(@Param("classifyBannerRelevanceDOS")List<ClassifyBannerRelevanceDO> classifyBannerRelevanceDOS);

    List<ClassifyBannerRelevanceDO> listRecommend();

    void deleteRecommend();

    List<ClassifyBannerRelevanceDO> listByClassifyIds(@Param("classifyIds")List<Integer> classifyIds);

    void deleteByClassifyId(@Param("classifyId")Integer classifyId);
}