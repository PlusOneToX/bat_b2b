package com.bat.dubboapi.flexible.label.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LabelDTORpcQry implements Serializable {


    private static final long serialVersionUID = 3525221693032055354L;

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 模板文件
     */
    private String templateFile;

    /**
     * 类型: 1. 普通标签，2.定制标签
     */
    private Short type;

    /**
     * 状态: 1为启用，0为弃用
     */
    private Short openFlag;

    private BigDecimal barCodePositionX;

    private BigDecimal barCodePositionY;

    private BigDecimal barCodePositionWidth;

    private BigDecimal barCodePositionHeight;

    private BigDecimal productNamePositionX;

    private BigDecimal productNamePositionY;

    private BigDecimal productNamePositionWidth;

    private BigDecimal productNamePositionHeight;

    private Integer productNamePositionFont;

    private Integer productNamePositionFontSize;

    private BigDecimal modelPositionX;

    private BigDecimal modelPositionY;

    private BigDecimal modelPositionWidth;

    private BigDecimal modelPositionHeight;

    private Integer modelPositionFont;

    private Integer modelPositionFontSize;

    /**
     * 适用分销商范围 1为全部，2 为国内， 3为国外，4 为指定
     */
    private Short scope;

    /**
     * 产品类型id
     */
    private Integer categoryId;

    private BigDecimal enNamePositionX;

    private BigDecimal enNamePositionY;

    private BigDecimal enNamePositionWidth;

    private BigDecimal enNamePositionHeight;

    private Integer enNamePositionFont;

    private Integer enNamePositionFontSize;

    private BigDecimal stuffNamePositionX;

    private BigDecimal stuffNamePositionY;

    private BigDecimal stuffNamePositionWidth;

    private BigDecimal stuffNamePositionHeight;

    private Integer stuffNamePositionFont;

    private Integer stuffNamePositionFontSize;

    private BigDecimal stuffEnNamePositionX;

    private BigDecimal stuffEnNamePositionY;

    private BigDecimal stuffEnNamePositionWidth;

    private BigDecimal stuffEnNamePositionHeight;

    private Integer stuffEnNamePositionFont;

    private Integer stuffEnNamePositionFontSize;


    /**
     * 是否删除 1、是 0、否、默认是0
     */
    private Short delFlag;

    /**
     * 是否关联用户自己上传的图片 1、是 0、否 （定制标签才有值）
     */
    private Short relevanceUserUpload;

    private String thirdSkuNo;
}