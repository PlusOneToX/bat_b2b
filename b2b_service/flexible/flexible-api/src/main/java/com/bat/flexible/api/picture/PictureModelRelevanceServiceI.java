package com.bat.flexible.api.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.dao.picture.dataobject.PictureModelRelevanceDO;

import java.util.List;

public interface PictureModelRelevanceServiceI {
    void saveRela(Integer pictureId, Short modelScope, Boolean isAdd, AdminResponse adminResponse, List<Integer> modelIdApplyList);

    List<ModelRelaSimpleDTO> listModelRelaByPictureIdAndDelFlag(Integer pictureId, Short commonDelFlagNo);

    List<PictureModelRelevanceDO> listByCondition(Integer pictureId, Integer modelId,List<Integer> pictureIdList);

    /**
     * 删除关联关系
     * @param modelId
     * @param pictureId
     * @param currentAdmin
     */
    void delete(Integer modelId, Integer pictureId, AdminResponse currentAdmin);
}
