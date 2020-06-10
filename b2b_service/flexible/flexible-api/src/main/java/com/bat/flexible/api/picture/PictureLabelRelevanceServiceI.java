package com.bat.flexible.api.picture;

import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.label.dto.LabelRelaSimpleDTO;

import java.util.List;

public interface PictureLabelRelevanceServiceI {

    /**
     * 报错图片和标签的关联关系
     * @param fromPicture true 来源图片详情 false 来源标签详情
     * @param pictureIdList 图片id
     * @param labelIdList 标签id列表
     * @param isAdd true 新增 false 修改
     * @param adminResponse
     */
    void saveRela(Boolean fromPicture, List<Integer> pictureIdList, List<Integer> labelIdList, Boolean isAdd, AdminResponse adminResponse);

    List<LabelRelaSimpleDTO> listLabelRelaByPictureIdAndDelFlag(Integer pictureId);

    List<PictureRelaSimpleDTO> listPictureRelaByLabelIdAndDelFlag(Integer labelId);

    /**
     * 删除图片和标签关联关系
     * @param labelId
     * @param pictureId
     * @param currentAdmin
     */
    void delete(Integer labelId, Integer pictureId, AdminResponse currentAdmin);
}
