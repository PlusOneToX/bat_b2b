package com.bat.flexible.manager.error.material;

public class MaterialErrorCode {

    //材质错误名称提示
    public static final String MATERIAL_NAME_ERROR_MSG_CODE="MATERIAL_NAME_ERROR_MSG_CODE";


    //材质编码不能为空（最低级）
    public static final String MATERIAL_NO_NULL="MATERIAL_NO_NULL";

    //材质工厂不能为空
    public static final String MANUFACTOR_NULL = "M_FACTORY_NULL";

    //最低级材质副标题不能为空
    public static final String SUBTITLE_NULL ="M_SUBTITLE_NULL" ;

    //最低级材质是否允许上传图片不能为空
    public static final String ALLOW_UPDATE_PICTURE_FLAG_NULL ="M_ALLOW_UPDATE_PICTURE_FLAG_NULL" ;

    //材质编码必须要唯一
    public static final String MATERIAL_NO_ONLY ="M_MATERIAL_NO_ONLY" ;

    //删除父类材质必须要先删除子类
    public static final String M_MATERIAL_DELETE_MUST_DELETE_SON_FIRST ="M_MATERIAL_DELETE_MUST_DELETE_SON_FIRST" ;

    //非最终材质
    public static final String M_MATERIAL_NOT_LAST_TRADEMARK="M_MATERIAL_NOT_LAST_TRADEMARK";

    //材质id、货品信息不能都为空
    public static final String M_MATERIAL_ID_AND_ITEM_NULL="M_MATERIAL_ID_AND_ITEM_NULL";

    //货品编码错误
    public static final String G_ITEM_CODE_ERROR="G_ITEM_CODE_ERROR";

    //货品ID错误
    public static final String G_ITEM_ID_ERROR="G_ITEM_ID_ERROR";

    //材质关联货品已被删除、材质不允许上架
    public static final String G_ITEM_HAS_DELETE_ERROR="G_ITEM_HAS_DELETE_ERROR";

    //材质关联货品尚未上架、材质不允许上架
    public static final String G_ITEM_HAS_NOT_SHELVES_ERROR="G_ITEM_HAS_NOT_SHELVES_ERROR";

    //该货品已关联其他材质、请勿重复关联
    public static final String M_ITEM_RELEVANCE_MATERIAL_REPEAT="M_ITEM_RELEVANCE_MATERIAL_REPEAT";

    //最终材质必须关联的货品ID不能为空
    public static final String M_MATERIAL_RELEVANCE_ITEM_ID_NULL="M_MATERIAL_RELEVANCE_ITEM_ID_NULL";

    //最终材质必须关联的货品编码不能为空
    public static final String M_MATERIAL_RELEVANCE_ITEM_CODE_NULL="M_MATERIAL_RELEVANCE_ITEM_CODE_NULL";

    //材质不允许用户上传自己图片
    public static final String M_MATERIAL_FORBIN_UPLOAD_ERROR="M_MATERIAL_FORBIN_UPLOAD_ERROR";
}
