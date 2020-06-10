package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.IndexRecommendPictureRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IndexRecommendPictureRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IndexRecommendPictureRelevanceDO record);

    int insertSelective(IndexRecommendPictureRelevanceDO record);

    IndexRecommendPictureRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IndexRecommendPictureRelevanceDO record);

    int updateByPrimaryKey(IndexRecommendPictureRelevanceDO record);

    IndexRecommendPictureRelevanceDO findMaxSortNo();

    List<IndexRecommendPictureRelevanceDO> findByIndexRecommendId(@Param("indexRecommendId") Integer indexRecommendId);

    List<IndexRecommendRelaCO> listCOByIndexRecommendId(@Param("indexRecommendId") Integer indexRecommendId);

    IndexRecommendPictureRelevanceDO findEffect(@Param("sortNo") Integer sortNo,@Param("upFlag") Boolean upFlag,@Param("indexRecommendId") Integer indexRecommendId);
}