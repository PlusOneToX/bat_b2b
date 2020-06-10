package com.bat.flexible.dao.index.co;


import lombok.Data;

import java.io.Serializable;


@Data
public class SeriesThemePageCO implements Serializable {

    private static final long serialVersionUID = 3737988368934336824L;

    /**
     * 图片关联表id
     */

    private Integer id;

    private Integer pictureId;

    private String code;

    private String pictureName;

    private String englishName;

    private String distributorName;

    private String companyName;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 原图
     */
    private String originImage;

    /**
     * 适用货品编码 多个逗号隔开
     */
    private String itemCode;

    /**
     * 主题系列id
     */
    private Integer seriesId;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 分类名称
     */
    private String categoryName;


}
