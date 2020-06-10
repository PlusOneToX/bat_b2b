package com.bat.flexible.api.picture.dto.theme;


import com.bat.flexible.dao.picture.dataobject.PictureCategoryThemeRelevanceDO;

import java.util.ArrayList;
import java.util.List;


public class PictureThemeRelaRequest {

    private Integer id;

    private List<PictureCategoryThemeRelevanceDO> pictureCategoryThemeRelas=new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PictureCategoryThemeRelevanceDO> getPictureCategoryThemeRelas() {
        return pictureCategoryThemeRelas;
    }

    public void setPictureCategoryThemeRelas(List<PictureCategoryThemeRelevanceDO> pictureCategoryThemeRelas) {
        this.pictureCategoryThemeRelas = pictureCategoryThemeRelas;
    }
}
