package com.bat.flexible.manager.index.executor;

import com.bat.flexible.dao.index.IndexRecommendPictureRelevanceDOMapper;
import com.bat.flexible.dao.index.dataobject.IndexRecommendPictureRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexRecommendPictureRelevanceCmdExe {

    @Autowired
    private IndexRecommendPictureRelevanceDOMapper indexRecommendPictureRelevanceDOMapper;

    public void create(IndexRecommendPictureRelevanceDO pictureRela) {
        indexRecommendPictureRelevanceDOMapper.insert(pictureRela);
    }

    public void delete(Integer id) {
        indexRecommendPictureRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void update(IndexRecommendPictureRelevanceDO pictureRela) {
        indexRecommendPictureRelevanceDOMapper.updateByPrimaryKey(pictureRela);
    }
}
