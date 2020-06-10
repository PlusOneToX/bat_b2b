package com.bat.flexible.manager.index.executor;

import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.index.IndexRecommendPictureRelevanceDOMapper;
import com.bat.flexible.dao.index.co.IndexRecommendRelaCO;
import com.bat.flexible.dao.index.dataobject.IndexRecommendPictureRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexRecommendPictureRelevanceQryExe {

    @Autowired
    private IndexRecommendPictureRelevanceDOMapper indexRecommendPictureRelevanceDOMapper;

    public IndexRecommendPictureRelevanceDO findMaxSortNo() {
        return indexRecommendPictureRelevanceDOMapper.findMaxSortNo();
    }

    public List<IndexRecommendPictureRelevanceDO> findByIndexRecommendId(Integer indexRecommendId) {
        return indexRecommendPictureRelevanceDOMapper.findByIndexRecommendId(indexRecommendId);
    }

    public IndexRecommendPictureRelevanceDO getById(Integer id) {
        if(id ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        IndexRecommendPictureRelevanceDO indexRecommendPictureRelevanceDO= indexRecommendPictureRelevanceDOMapper.selectByPrimaryKey(id);
        if(indexRecommendPictureRelevanceDO ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return indexRecommendPictureRelevanceDO;
    }

    public List<IndexRecommendRelaCO> listCOByIndexRecommendId(Integer indexRecommendId) {
        return indexRecommendPictureRelevanceDOMapper.listCOByIndexRecommendId(indexRecommendId);
    }

    public IndexRecommendPictureRelevanceDO findEffect(Integer sortNo, Boolean upFlag, Integer indexRecommendId) {
        return indexRecommendPictureRelevanceDOMapper.findEffect(sortNo,upFlag,indexRecommendId);
    }
}
