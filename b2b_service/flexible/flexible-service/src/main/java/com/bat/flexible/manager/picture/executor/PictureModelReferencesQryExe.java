package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureModelRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureModelRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureModelReferencesQryExe {

    @Autowired
    private PictureModelRelevanceDOMapper pictureModelReferencesDOMapper;

    public List<PictureModelRelevanceDO> listByCondition(Integer pictureId, Integer modelId,List<Integer> pictureIdList) {
        return pictureModelReferencesDOMapper.listByCondition(pictureId,modelId,pictureIdList);
    }


}
