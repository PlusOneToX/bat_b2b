package com.bat.order.service.common.error;

public class FlexibleRpcErrorCode {

    /**
     * 通用id错误
     */
    public static final String FLEXIBLE_ID_ERROR = "FLEXIBLE_ID_ERROR";

    /**
     * 通用id为空
     */
    public static final String FLEXIBLE_ID_NULL = "FLEXIBLE_ID_NULL";

    /**
     * 通用id必须为空（新增）
     */
    public static final String FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD = "FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD";

    /**
     * id列表不能为空
     */
    public static final String FLEXIBLE_ID_LIST_NULL = "FLEXIBLE_ID_LIST_NULL";

    /**
     * 通用数据已被删除
     */
    public static final String FLEXIBLE_DATA_DEL_ALREADY = "FLEXIBLE_DATA_DEL_ALREADY";

    /**
     * 通用 父id错误
     */
    public static final String COMMON_PARENT_ID_ERROR = "COMMON_PARENT_ID_ERROR";

    /**
     * 通用 删除前请先下架
     */
    public static final String COMMON_DELETE_AFTER_DISABLE_ERROR = "COMMON_DELETE_AFTER_DISABLE_ERROR";

    /**
     * 长度为空
     */
    public static final String COMMON_LENGTH_NULL = "COMMON_LENGTH_NULL";
    /**
     * 长度小于0
     */
    public static final String COMMON_LENGTH_LESS_THEN_ZERO = "COMMON_LENGTH_LESS_THEN_ZERO";
    /**
     * 高度为空
     */
    public static final String COMMON_HEIGHT_NULL = "COMMON_HEIGHT_NULL";
    /**
     * 高度小于0
     */
    public static final String COMMON_HEIGHT_LESS_THEN_ZERO = "COMMON_HEIGHT_LESS_THEN_ZERO";

    /**
     * 宽度为空
     */
    public static final String COMMON_WIDTH_NULL = "COMMON_WIDTH_NULL";
    /**
     * 宽度小于0
     */
    public static final String COMMON_WIDTH_LESS_THEN_ZERO = "COMMON_WIDTH_LESS_THEN_ZERO";
    /**
     * 重量为空
     */
    public static final String COMMON_WEIGHT_NULL = "COMMON_WEIGHT_NULL";

    /**
     * 重量小于0
     */
    public static final String COMMON_WEIGHT_LESS_THEN_ZERO = "COMMON_WEIGHT_LESS_THEN_ZERO";
    /**
     * 状态不能为空
     */
    public static final String COMMON_OPEN_FLAG_NULL = "COMMON_OPEN_FLAG_NULL";

    /**
     * 状态未启用、不允许使用
     */
    public static final String COMMON_OPEN_FLAG_NO_FORBIN_USE = "COMMON_OPEN_FLAG_NO_FORBIN_USE";

    /**
     * 材质id不能为空
     */
    public static final String COMMON_MATERIAL_ID_NULL = "COMMON_MATERIAL_ID_NULL";
    /**
     * 型号不能为空
     */
    public static final String COMMON_MODEL_ID_NULL = "COMMON_MODEL_ID_NULL";

    /**
     * EXCEL导出异常
     */
    public static final String COMMON_EXCEL_EXPORT_ERROE = "COMMON_EXCEL_EXPORT_ERROE";

    /**
     * EXCEL导入异常
     */
    public static final String COMMON_EXCEL_IMPORT_ERROE = "COMMON_EXCEL_IMPORT_ERROE";

    /**
     * 非最终材质/最终型号
     */
    public static final String COMMON_AT_LAST_TRADEMARK_NOT_BELONG_YES = "COMMON_AT_LAST_TRADEMARK_NOT_BELONG_YES";

    /**
     * 材质不允许上传图片
     */
    public static final String FLEXIBLE_MATERIAL_NOT_ALLOW_UPLOAD_PICTURE =
        "FLEXIBLE_MATERIAL_NOT_ALLOW_UPLOAD_PICTURE";
    /**
     * 标签生成失败
     */
    public static final String FLEXIBLE_LABEL_CREATE_FAIL = "FLEXIBLE_LABEL_CREATE_FAIL";

}
