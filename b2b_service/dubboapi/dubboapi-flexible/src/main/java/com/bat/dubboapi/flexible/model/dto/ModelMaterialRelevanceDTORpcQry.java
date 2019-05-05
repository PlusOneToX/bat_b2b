package com.bat.dubboapi.flexible.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelMaterialRelevanceDTORpcQry implements Serializable {

    private static final long serialVersionUID = 5202322741827275520L;
    /**
     * model_material_relevance关联表主键
     */
    private Integer id;

    /**
     * 型号id
     */
    private Integer modelId;

    /**
     * 材质id
     */
    private Integer materialId;

    /**
     * bom编码
     */
    private String bomCode;

    /**
     * 第三方sku(材质+型号)
     */
    private String thirdSku;
    /**
     * 第三方sku(材质)
     */
    private String materialSku;
    /**
     * 材质名称
     */
    private String materialName;
    /**
     * 第三方sku(型号)
     */
    private String modelSku;
    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 状态 1、启用 0、禁用
     */
    private Short openFlag;

    /**
     * 是否删除 1、是 0、否
     */
    private Short delFlag;


}
