package com.bat.thirdparty.common.log;

public enum OrderBusinessLogEnum {

    //日志类型 1、推送定制信息给第三方 2、接收第三方订单（基于ID）3、接收第三方订单（基于编码） 4、推送销售单给ERP 5、B2B推单给工厂 6、工厂发货 7、推送物流信息给第三方 8、第三方取消订单 9、工厂取消订单
    // 10、推送核销信息给第三方 11.同步erp上的物流单号 12、接收摩乐吉的订单 13.拉取淘宝订单 14.推送信息到

    //推送给第三方
    PUSH_DIY_TO_THIRD(1,1),

    RECEIVE_DIY_ORDER_BASE_ID(2,2),

    RECEIVE_DIY_ORDER_BASE_CODE(3,2),

    SYNC_SALE_ORDER_ERP(4,3),

    SYNC_SALE_ORDER_FACTORY(5,6),

    FACTORY_DELIVERY_SYNC_B2B(6,5),

    PUSH_LOGISTICS_THIRD(7,1),

    CANCEL_ORDER_FROM_THIRD(8,2),

    CANCEL_ORDER_FROM_FACTORY(9,5),

    PUSH_WRITE_OFF_TO_THIRD(10,1),

    SYNC_EXPRESSNO_FROM_ERP(11,4),

    RECEIVE_ORDER_FROM_MOLEJI(12,2),

    GET_TAOBAO_ORDER(13,7),

    PUSH_TO_SUMSUNG(14,1),
    ;


    private Short logType;

    //1、B2B->第三方 2、第三方->B2B 3、B2B->ERP 4、ERP->B2B 5、工厂-->B2B 6、B2B->>工厂 7.天猫--> B2B
    private Short towardType;

    OrderBusinessLogEnum(Integer logType, Integer towardType) {
        this.logType = logType.shortValue();
        this.towardType = towardType.shortValue();
    }

    public Short getLogType() {
        return logType;
    }

    public Short getTowardType() {
        return towardType;
    }
}
