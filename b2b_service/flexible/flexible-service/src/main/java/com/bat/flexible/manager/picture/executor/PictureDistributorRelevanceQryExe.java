package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureDistributorRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureDistributorRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureDistributorRelevanceQryExe {

    @Autowired
    private PictureDistributorRelevanceDOMapper pictureDistributorRelevanceDOMapper;

    public List<PictureDistributorRelevanceDO> listByPictureIdAAndDelFlag(Integer pictureId, Short delFlag) {
        return pictureDistributorRelevanceDOMapper.listByPictureIdAAndDelFlag(pictureId,delFlag);
    }

    public List<PictureDistributorRelevanceDO> listByCondition(Integer pictureId, List<Integer> distributorIds) {
        return pictureDistributorRelevanceDOMapper.listByCondition(pictureId,distributorIds);
    }
}
