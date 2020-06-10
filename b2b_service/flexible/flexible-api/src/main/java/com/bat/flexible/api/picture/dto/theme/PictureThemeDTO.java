package com.bat.flexible.api.picture.dto.theme;


import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import lombok.Data;

import java.util.List;

@Data
public class PictureThemeDTO {

    private PictureThemeDO pictureTheme;

    private List<PictureCategoryDO> pictureCategorys;


}
