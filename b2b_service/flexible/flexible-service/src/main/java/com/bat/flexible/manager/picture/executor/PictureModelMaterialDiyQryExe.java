package com.bat.flexible.manager.picture.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.anno.Cached;
import com.bat.flexible.dao.picture.PictureModelMaterialDiyDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureModelMaterialDiyDO;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;

@Component
public class PictureModelMaterialDiyQryExe {

    @Autowired
    private PictureModelMaterialDiyDOMapper pictureModelMaterialDiyDOMapper;

    @Cached(name = FlexibleKeyConstant.PICTURE_MODEL_MATERIAL_DIY_DO_PRE,
        key = "#materialId+'_'+#modelId+'_'+#pictureId")
    public PictureModelMaterialDiyDO getByMaterialIdAndModelIdAndPictureId(Integer materialId, Integer modelId,
        Integer pictureId) {
        PictureModelMaterialDiyDO pictureModelMaterialDiyDO =
            pictureModelMaterialDiyDOMapper.getByMaterialIdAndModelIdAndPictureId(materialId, modelId, pictureId);
        return pictureModelMaterialDiyDO;
    }

    public List<PictureModelMaterialDiyDO> listByCondition(Integer materialId, Integer modelId, Integer pictureId) {
        return pictureModelMaterialDiyDOMapper.listByCondition(materialId, modelId, pictureId);
    }
}
