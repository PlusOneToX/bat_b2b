package com.bat.flexible.manager.picture.executor;


import com.bat.flexible.dao.picture.PictureMateriaRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureMaterialReferencesQryExe {

    @Autowired
    private PictureMateriaRelevanceDOMapper pictureMaterialReferencesDOMapper;


    public List<PictureMaterialRelevanceDO> listByPictureIdAndDelFlag(Integer pictureId, Short delFlag) {
        return pictureMaterialReferencesDOMapper.listByPictureIdAndDelFlag(pictureId,delFlag);
    }

    public List<PictureMaterialRelevanceDO> listByPictureIdAndMaterialId(Integer pictureId, Integer materialId) {
        return pictureMaterialReferencesDOMapper.listByPictureIdAndMaterialId(pictureId,materialId);
    }
}
