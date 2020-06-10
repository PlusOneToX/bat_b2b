package com.bat.flexible.dao.picture.co;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CommonPicturePageCO {


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

    private String originImage;

    private  Short type;

    private String backgroundPreviewUrl;

    private String noCameraVacancyPreview;

    /**
     * 版权费
     */
    private BigDecimal copyrightCost;

    private Float templateCenterX;

    private Float templateCenterY;

    private List<PictureTemplateEditCmd> templateEditList;


}
