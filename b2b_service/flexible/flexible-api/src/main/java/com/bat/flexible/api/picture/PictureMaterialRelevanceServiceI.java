package com.bat.flexible.api.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;

import java.util.List;

public interface PictureMaterialRelevanceServiceI {

    /**
     * 保存关联关系
     * @param fromPicture true表示在图片详情处理关联关系 false表示在材质详情页面处理
     * @param pictureIdList 图片id列表
     * @param materialIdList 材质id列表
     * @param adminResponse
     * @param isAdd true表示新增 false表示修改
     */
    void saveRela(Boolean fromPicture,List<Integer> pictureIdList, List<Integer> materialIdList, AdminResponse adminResponse, Boolean isAdd);

    /**
     * 根据图片id喝状态查询材质关联列表
     * @param pictureId
     * @param delFlag
     * @return
     */
    List<MaterialRelaSimpleDTO> listSimpleByPictureIdAndDelFlag(Integer pictureId, Short delFlag);

    List<PictureMaterialRelevanceDO> listByPictureIdAndMaterialId(Integer pictureId,Integer materialId);

    /**
     * 删除关联关系
     * @param materialId
     * @param pictureId
     */
    void delete(Integer materialId, Integer pictureId, AdminResponse currentAdmin);
}
