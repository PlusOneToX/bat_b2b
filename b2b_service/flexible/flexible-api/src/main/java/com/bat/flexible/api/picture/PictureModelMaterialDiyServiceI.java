package com.bat.flexible.api.picture;

import com.bat.flexible.api.picture.dto.diy.PictureModelMaterialDiyCmd;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.picture.dataobject.PictureModelMaterialDiyDO;

public interface PictureModelMaterialDiyServiceI {


    Response create(PictureModelMaterialDiyCmd modelMaterialDiyCmd);

    /**
     * 根据材质id、型号id、图片id查询
     * @param materialId 材质id
     * @param modelId 型号id
     * @param pictureId 图片id
     * @return
     */
    Response<PictureModelMaterialDiyDO> getByMaterialIdAndModelIdAndPictureId(Integer materialId, Integer modelId, Integer pictureId);

    /**
     * 删除关联关系
     * @param materialId
     * @param modelId
     * @param pictureId
     */
    void delete(Integer materialId, Integer modelId, Integer pictureId);
}
