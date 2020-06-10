package com.bat.flexible.api.picture;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.picture.dto.theme.PictureThemeDTO;
import com.bat.flexible.api.picture.dto.theme.PictureThemePageQry;
import com.bat.flexible.api.picture.dto.theme.PictureThemeRelaRequest;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.dto.ThemeDTO;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;

/**
 * 沙漠
 */
public interface PictureThemeServiceI {

    PictureThemeDO update(PictureThemeDO pictureTheme);

    Response delete(Integer id);

    PictureThemeDO create(PictureThemeDO pictureTheme);

    PageInfo<PictureThemeDO> page(PictureThemePageQry pictureThemePageQry);

    PictureThemeDTO getById(Integer id);

    Response relation(PictureThemeRelaRequest pictureThemeRelaRequest);

    Response pictureCategoryThemeList(ThemeDTO themeDTO);

    Response pictureCategoryThemeDetail(ThemeDTO themeDTO);
}
