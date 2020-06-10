package com.bat.flexible.api.index.dto.page;


import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import lombok.Data;

@Data
public class SeriesThemePageQry extends BasePageParamQry {

    /**
     * 图片分类Id
     */
    private Integer pictureCategoryId;


    /**
     * 主题id
     */
    private Integer themeId;

    /**
     * 排序类型
     */
    private Short SortNoType;


    private String content;



}
