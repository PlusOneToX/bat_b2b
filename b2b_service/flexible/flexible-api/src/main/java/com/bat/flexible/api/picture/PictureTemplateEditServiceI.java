package com.bat.flexible.api.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;

import java.util.List;

public interface PictureTemplateEditServiceI {

    void saveTemplate(Integer pictureId, List<PictureTemplateEditCmd> templateEditList, Short type, Boolean isAdd, AdminResponse adminResponse);

    List<PictureTemplateEditCmd> listByPictureId(Integer id);


}
