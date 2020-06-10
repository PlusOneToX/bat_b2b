package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureDistributorRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureDistributorRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureDistributorRelevanceCmdExe {

    @Autowired
    private PictureDistributorRelevanceDOMapper pictureDistributorRelevanceDOMapper;

    public void create(PictureDistributorRelevanceDO relevanceDO) {
        pictureDistributorRelevanceDOMapper.insert(relevanceDO);
    }

    public void update(PictureDistributorRelevanceDO pictureDistributorRelevanceDO) {
        pictureDistributorRelevanceDOMapper.updateByPrimaryKey(pictureDistributorRelevanceDO);
    }
}
