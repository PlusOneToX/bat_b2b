package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureProductCategoryRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureProductCategoryRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureProductCategoryReferencesCmdExe {

    @Autowired
    private PictureProductCategoryRelevanceDOMapper pictureProductCategoryRelevanceDOMapper;

    public void deleteById(Integer id) {
        pictureProductCategoryRelevanceDOMapper.deleteByPrimaryKey(id);
    }

    public void create(PictureProductCategoryRelevanceDO referencesDO) {
        pictureProductCategoryRelevanceDOMapper.insert(referencesDO);
    }
}
