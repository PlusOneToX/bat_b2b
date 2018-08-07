package com.bat.goods.manager.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 13:34
 */
public interface Sink {

    public static String GOODS_INPUT = "goods-input";
    public static String GOODS_ORDER_INPUT = "goods-order-input";
    public static String GOODS_PLATFORM_INPUT = "goods-platform-input";

    @Input(Sink.GOODS_INPUT)
    SubscribableChannel goodsInput();

    @Input(Sink.GOODS_ORDER_INPUT)
    SubscribableChannel goodsOrderInput();

    @Input(Sink.GOODS_PLATFORM_INPUT)
    SubscribableChannel goodsPlatformInput();

}
