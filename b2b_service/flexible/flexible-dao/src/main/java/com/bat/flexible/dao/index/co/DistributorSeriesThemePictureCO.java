package com.bat.flexible.dao.index.co;


import lombok.Data;

@Data
public class DistributorSeriesThemePictureCO {


    private Integer id;

    private Integer pictureId;

    /**
     * 产品图、缩略图
     */
    private String thumbnail;

    /**
     * 图片名称
     */
    private String pictureName;


    private String englishName;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 分类图片
     */
    private String categoryImage;


    private Integer seriesId;

    /**
     * 原图
     */
    private String originImage;



}
