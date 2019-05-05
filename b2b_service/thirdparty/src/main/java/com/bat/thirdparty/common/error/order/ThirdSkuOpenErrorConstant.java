package com.bat.thirdparty.common.error.order;

/**
 * 三方对接外部异常枚举类
 */
public enum ThirdSkuOpenErrorConstant {

    MobileShellNullError("手机壳信息不能为空"),

    SkuNullError("sku不能为空"),

    SkuLengthError("sku长度非法"),

    SkuNoError("sku编码错误或者不存在"),

    SkuNoStatusDisableError("该SKU已被停用、无法下单"),

    MaterialAndModelRelaDeleteError("对应的材质和型号关系已过期、请速与客服联系"),

    MaterialAndModelRelaDisableError("对应的材质和型号关系已停用、请速与客服联系"),

    PrevImgUrlNullError("打印效果图不能为空"),

    ImgUrlNullError("图片URL不能为空"),

    MaterialDisableError("材质已被停用、请与客服沟通"),

    ModelDisableError("型号已被停用、请与客服沟通"),

    MaterialNoRelaPictureError("材质尚未绑定图片、请与客服沟通"),

    QuantityNullError("下单数量不能为空"),

    QuantityIllegalError("下单数量非法"),

    DistributorIdNullError("分销商id不能为空"),

    OrderSourceNullError("orderSource不能为空"),
    ;



    private String msg;

    ThirdSkuOpenErrorConstant(String msg) {
        this.msg = msg;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
