package com.bat.dubboapi.flexible.picture.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PictureDTORpcQry implements Serializable {

    private static final long serialVersionUID = 22190224700439329L;
    /**
     * 图片id
     */
    private Integer id;

    /**
     * 图片编码
     */
    private String code;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 英文
     */
    private String englishName;

    /**
     * 图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图
     */
    private Short type;

    /**
     * 分类id
     */
    private Integer categoryId;


    /**
     * 状态 1、启用 0、禁用
     */
    private Short openFlag;

    /**
     * 图片的适用范围(哪些型号可以适用这个图片信息): 1为全部可用，2为指定型号可用,3为指定类型可用
     */
    private Short modelScope;

    /**
     * 分销商适用范围 1为全部，2为国内，3为国外，4 指定
     */
    private Short resellerScope;



    /**
     * 版权费用
     */
    private BigDecimal copyrightCost;

    /**
     * 背景预览图
     */
    private String backgroundPreviewUrl;

    /**
     * 无相机空位预览图
     */
    private String noCameraVacancyPreview;
}
