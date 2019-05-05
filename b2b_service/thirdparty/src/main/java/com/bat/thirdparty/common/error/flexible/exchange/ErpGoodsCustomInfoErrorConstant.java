package com.bat.thirdparty.common.error.flexible.exchange;

/**
 * //13101-13200 erp对材质型号
 */
public enum ErpGoodsCustomInfoErrorConstant {

    /**
     *   bom编码列表不能为空
     */
    BomCodeListNullError(13100,"T_FLEXIBLE_BOM_CODE_LIST_NULL"),

    /**
     *   操作人不能为空
     */
    OperateUserNameNullError(13100,"T_COMMON_OPERATE_USER_NAME_NULL"),
    /**
     * bom编码不能为空
     */
    BomCodeNullError(13101,"T_FLEXIBLE_BOM_CODE_NULL"),

    /**
     * 操作类型不能为空
     */
    OperateTypeNullError(13102,"T_FLEXIBLE_OPERATE_TYPE_NULL"),

    /**
     * 物料编码不能为空
     */
    ItemCodeNullError(13103,"物料编码不能为空"),

    StockOutStatusNullError(13104,"是否缺货不能为空"),

    /**
     * ERP同步是否缺货错误
     */
    ErpSyncStockOutStatusError(13105,"ERP同步是否缺货错误"),
    ;


    private Integer code;

    private String msg;

     ErpGoodsCustomInfoErrorConstant(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
