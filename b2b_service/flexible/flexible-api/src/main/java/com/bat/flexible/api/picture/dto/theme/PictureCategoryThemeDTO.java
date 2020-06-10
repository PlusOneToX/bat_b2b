package com.bat.flexible.api.picture.dto.theme;


import com.github.pagehelper.PageInfo;
import lombok.Data;

@Data
public class PictureCategoryThemeDTO {

    private PageInfo pageInfo;

    private String themeName;

    private String themeImg;

    private Integer categoryId;

    //图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图
    private Short type;


}
