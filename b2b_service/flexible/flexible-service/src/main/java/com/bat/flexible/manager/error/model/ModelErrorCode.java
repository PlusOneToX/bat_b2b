package com.bat.flexible.manager.error.model;

public class ModelErrorCode {

    //型号错误名称提示
    public static final String MODEL_NAME_ERROR_MSG_CODE="MODEL_NAME_ERROR_MSG_CODE";

    /**
     * 删除父级型号、请先删除子类型号
     */
    public static final String M_MODEL_DELETE_SON_FIRST = "M_MODEL_DELETE_SON_FIRST";

    /**
     * 型号编码不能为空
     */
    public static final String M_MODEL_NO_NULL = "M_MODEL_NO_NULL";
    /**
     * 型号编码必须要唯一
     */
    public static final String M_MODEL_NO_ONLY = "M_MODEL_NO_ONLY";
    /**
     * 最终型号必须要关联材质
     */
    public static final String M_MODEL_MATERIAL_RELA_NULL = "M_MODEL_MATERIAL_RELA_NULL";

    /**
     * 型号非最终型号
     */
    public static final String M_MODEL_NOT_LAST_TRADEMARK ="M_MODEL_NOT_LAST_TRADEMARK" ;

    /**
     * 型号id和型号编码均为空
     *
     */
    public static final String M_MODEL_ID_AND_MODEL_NO_ALL_NULL="M_MODEL_ID_AND_MODEL_NO_ALL_NULL";

    /**
     * 入网型号不能为空
     *
     */
    public static final String M_MODEL_NETWORK_MODEL_NULL="M_MODEL_NETWORK_MODEL_NULL";

    /**
     * 型号名称不能为空
     */
    public static final String M_MODEL_NAME_NULL = "M_MODEL_NAME_NULL";
}
