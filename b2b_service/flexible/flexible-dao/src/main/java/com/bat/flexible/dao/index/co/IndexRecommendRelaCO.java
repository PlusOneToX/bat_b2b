package com.bat.flexible.dao.index.co;

import lombok.Data;

@Data
public class IndexRecommendRelaCO {

    private Integer id;

    private Integer pictureId;

    private String code;

    private String pictureName;

    private String englishName;


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
     * 1、启用 0、关闭
     */
    private Short openFlag;


}
