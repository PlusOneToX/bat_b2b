package com.bat.flexible.manager.picture.validtor;

import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.diy.PictureModelMaterialDiyCmd;
import com.bat.flexible.manager.material.validator.MaterialValidtor;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.error.picture.PictureModelMaterialDiyErrorCode;
import com.bat.flexible.manager.model.validtor.ModelValidtor;
import com.bat.flexible.manager.picture.executor.PictureModelMaterialDiyQryExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PictureModelMaterialDiyValidator {



    @Autowired
    private PictureModelMaterialDiyQryExe pictureModelMaterialDiyQryExe;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private MaterialValidtor materialValidtor;

    @Autowired
    private ModelValidtor modelValidtor;

    public void valid(PictureModelMaterialDiyCmd modelMaterialDiyCmd) {
        PictureDO pictureDO = pictureServiceI.getById(modelMaterialDiyCmd.getPictureId());
        if(!PictureConstant.PICTURE_TYPE_IP.equals(pictureDO.getType())){
            throw new FlexibleCustomException(PictureModelMaterialDiyErrorCode.P_PICTURE_TYPE_MUST_BE_IP);
        }
        materialValidtor.validMaterialIsLast(modelMaterialDiyCmd.getMaterialId(),null);
        modelValidtor.validModelIsLast(modelMaterialDiyCmd.getModelId(),null);
    }
}
