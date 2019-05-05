package com.bat.dubboapi.flexible.material.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MaterialDTORpcQry implements Serializable {

    private static final long serialVersionUID = 1099671792460931881L;

    /**
     * 材质id
     */
    private Integer id;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 材质编码
     */
    private String materialNo;

    /**
     * 材质名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String englishName;

    /**
     * 产品类型id
     */
    private Integer categoryId;

    /**
     * 产品类型名称
     */
    private String categoryName;

    /**
     * 状态 1、启用 0、禁用
     */
    private Short openFlag;

    /**
     * 工厂编号
     */
    private String manufactors;

    /**
     * 是否最终材质 1、是 0、否
     */
    private Short atLastTrademark;

    private String stuffName;

    private String stuffEnName;

    private String subtitle;

    /**
     * 货品id
     */
    private Integer itemId;

    /**
     * 对应的货品编码
     */
    private String itemCode;

    /**
     * 是否允许上传图片 1、是 0、否
     */
    private Short allowUploadFlag;

    /**
     * 是否删除 1、是 0、正常
     */
    private Short delFlag;

}