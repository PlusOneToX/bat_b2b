package com.bat.dubboapi.flexible.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelDTORpcQry implements Serializable {

    private static final long serialVersionUID = -3394419787525438820L;
    /**
     * 型号id
     */
    private Integer id;

    /**
     * 型号名称
     */
    private String name;

    /**
     * 型号编码
     */
    private String modelNo;

    /**
     * 型号英文
     */
    private String englishName;

    /**
     * 状态 1、启用 0、禁用
     */
    private Short openFlag;

    /**
     * 是否最终型号 1、是 0、否
     */
    private Short atLastTrademark;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 父名称
     */
    private String parentName;

    /**
     * 父名称英文
     */
    private String parentEnglishName;

    /**
     * 父型号编码
     */
    private String parentModelNo;

    /**
     * 是否删除 1、是 0、否
     */
    private Short delFlag;
}
