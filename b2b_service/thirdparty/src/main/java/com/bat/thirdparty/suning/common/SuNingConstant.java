package com.bat.thirdparty.suning.common;

public class SuNingConstant {

    /**
     * ASES订单信息传输接口
     */
    public static final String createOrderSend="suning.fw.ffszlcreateorder.send";

    /**
     * ASES订单信息传输接口
     */
    public static final String oredrModifySend="suning.fw.ffszlmodifyorder.send";

    /**
     * 服务商反馈派工信息接口
     */
    public static final String orderDispatchCreate ="suning.fw.ffszlPgr.create";

    /**
     * 服务商反馈现场签到信息接口
     */
    public static final String orderSignInCreate ="suning.fw.ffszlSignIn.create";

    /**
     * 服务商反馈销单信息接口
     */
    public static final String orderDestroyCreate ="suning.fw.ffszlDestory.create";

    /**
     * 上门维度
     */
    public static final String smwd="22.61667";
    /**
     * 上门经度
     */
    public static final String smjd="114.06667";
    /**
     * 上门地址
     */
    public static final String smAddress="深圳";
    /**
     * 上门真实标识
     */
    public static final String smzsbs="N";

    /**
     * 来源系统
     */
    public static final String fromSys="bat";

    /**
     * 销单状态 E0004（另约）；E0008（取消）；E0006（完成）
     */
    public static final String userStatus3="E0006";

    /**
     * 调用成功
     */
    public static final String returnCodeS="S";
    /**
     * 调用失败
     */
    public static final String returnCodeF="F";

    /**
     * 调用成功 订单派工,订单核销返回
     */
    public static final String returnCodeY="Y";
    /**
     * 调用失败 订单派工，订单核销返回
     */
    public static final String returnCodeN="N";

    /**
     * 操作类型 01服务订单修改;02服务订单取消
     */
    public static final String modifyOperationType1="01";

    /**
     * 操作类型 01服务订单修改;02服务订单取消
     */
    public static final String modifyOperationType2="02";

    /**
     * 未知异常
     */
    public static final String returnUnknownError="未知异常";

    /**
     * 签名错误
     */
    public static final String returnSignError="签名错误!";

    /**
     * 请求的订单号不能为空
     */
    public static final String requestOrderNoCantNull="请求的订单号不能为空!";

    /**
     * 请求的订服务商品编码不能为空
     */
    public static final String requestSrvcmmdtyCodeCantNull="请求的订服务商品编码不能为空!";

    /**
     * 找不到对应订单信息
     */
    public static final String orderCantFind ="找不到对应订单信息!";

    /**
     * 订单已核销；无法取消
     */
    public static final String orderHAsDestroyCantCancel="订单已核销；无法取消!";

    /**
     * 取消失败；订单可能已经核销
     */
    public static final String cancelFailMayBeHAsDestroy="取消失败；订单可能已经核销!请联系客服";

}
