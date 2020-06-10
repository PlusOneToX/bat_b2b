package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureProductCategoryRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureProductCategoryRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureProductCategoryReferencesQryExe {

    @Autowired
    private PictureProductCategoryRelevanceDOMapper pictureProductCategoryRelevanceDOMapper;

    public List<PictureProductCategoryRelevanceDO> listByPictureId(Integer pictureId) {
        return pictureProductCategoryRelevanceDOMapper.listByPictureId(pictureId);
    }

    public List<PictureProductCategoryRelevanceDO> listByCondition(Integer pictureId, Integer productCategoryId,List<Integer> pictureIdList) {
        return pictureProductCategoryRelevanceDOMapper.listByCondition(pictureId,productCategoryId,pictureIdList);
    }
}
