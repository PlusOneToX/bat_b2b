package com.bat.flexible.dao.index;

import com.bat.flexible.dao.index.co.IndexRecommendPageCO;
import com.bat.flexible.dao.index.dataobject.DistributorIndexRecommendRelevanceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DistributorIndexRecommendRelevanceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DistributorIndexRecommendRelevanceDO record);

    int insertSelective(DistributorIndexRecommendRelevanceDO record);

    DistributorIndexRecommendRelevanceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DistributorIndexRecommendRelevanceDO record);

    int updateByPrimaryKey(DistributorIndexRecommendRelevanceDO record);

    List<DistributorIndexRecommendRelevanceDO> listByIndexRecommendId(@Param("indexRecommendId") Integer indexRecommendId);

    void deleteByIndexRecommendId(@Param("indexRecommendId")Integer indexRecommendId);

    List<DistributorIndexRecommendRelevanceDO> listByDistributorIdList(@Param("distributorIdList") List<Integer> distributorIdList);

    List<IndexRecommendPageCO> listCOByContent(@Param("content") String content);
}