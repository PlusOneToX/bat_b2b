package com.bat.flexible.manager.error.model;

public class ModelMaterialRelevanceErrorCode {


    /**
     * 材质型号关系唯一
     */
    public static final String MATERIAL_MODEL_RELEVANCE_ALONE_ERROR="MATERIAL_MODEL_RELEVANCE_ALONE_ERROR";


    /**
     * 材质已关联这型号
     */
    public static final String MATERIAL_HAS_BEEN_ASSOCIATED_MODEL_ERROR="MATERIAL_HAS_BEEN_ASSOCIATED_MODEL_ERROR";

    /**
     * 型号已关联这材质
     */
    public static final String MODEL_HAS_BEEN_ASSOCIATED_MATERIAL_ERROR="MODEL_HAS_BEEN_ASSOCIATED_MATERIAL_ERROR";

    /**
     * 单个材质绑定型号列表时、材质id必须要一致
     */
    public static final String MATERIAL_ASSOCIATED_MODEL_MATERIAL_ID_NOT_ONLY_ERROR="MATERIAL_ASSOCIATED_MODEL_MATERIAL_ID_NOT_ONLY_ERROR";

    /**
     * 单个型号绑定材质列表时、型号id必须要一致
     */
    public static final String MODEL_ASSOCIATED_MATERIAL_MODELL_ID_NOT_ONLY_ERROR="MODEL_ASSOCIATED_MATERIAL_MODELL_ID_NOT_ONLY_ERROR";

    /**
     * 修改材质、型号材质关联表材质id与材质id不一致
     */
    public static final String MODEL_MATERIAL_RELA_MATERIAL_ID_ERROR="MODEL_MATERIAL_RELA_MATERIAL_ID_ERROR";

    /**
     * 修改型号、型号材质关联表型号id与型号id不一致
     */
    public static final String MODEL_MATERIAL_RELA_MODELL_ID_ERROR="MODEL_MATERIAL_RELA_MODELL_ID_ERROR";
    /**
     * 外框图不能为空
     */
    public static final String M_OUT_IMAGE_NULL = "M_OUT_IMAGE_NULL";
    /**
     * 内框图不能为空
     */
    public static final String M_FLOOR_IMAGE_NULL = "M_FLOOR_IMAGE_NULL";
    /**
     * 第三方编码不能为空
     */
    public static final String M_THIRD_SKU_NULL ="M_THIRD_SKU_NULL" ;
    /**
     * bom编码不能为空
     */
    public static final String M_BOM_CODE_NULL = "M_BOM_CODE_NULL";


    /**
     * 非最终型号不允许关联材质
     */
    public static final String M_MODEL_MATERIAL_RELA_FORBID_BY_NOT_LAST ="M_MODEL_MATERIAL_RELA_FORBID_BY_NOT_LAST" ;

    //非最终材质不能关联型号
    public static final String M_MATERIAL_MODEL_RELA_FORBID_BY_NOT_LAST ="M_MATERIAL_MODEL_RELA_FORBID_BY_NOT_LAST" ;

    //这个型号和材质没有关联关系
    public static final String M_MODEL_MATERIAL_NO_CORRELATION ="M_MODEL_MATERIAL_NO_CORRELATION" ;

    //这个型号和材质关联关系已下架
    public static final String M_MODEL_MATERIAL_STATUS_DISABLED ="M_MODEL_MATERIAL_STATUS_DISABLED" ;




}
