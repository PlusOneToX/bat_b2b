package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureModelRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureModelRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureModelReferencesCmdExe {

    @Autowired
    private PictureModelRelevanceDOMapper pictureModelReferencesDOMapper;

    public void update(PictureModelRelevanceDO pictureModelRelevanceDO) {
        pictureModelReferencesDOMapper.updateByPrimaryKey(pictureModelRelevanceDO);
    }

    public void create(PictureModelRelevanceDO relevanceDO) {
        pictureModelReferencesDOMapper.insert(relevanceDO);
    }
}
