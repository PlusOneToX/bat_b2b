package com.bat.promotion.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    public static String PROMOTION_ORDER_INPUT = "promotion-order-input";

    public static String PROMOTION_PLATFORM_INPUT = "promotion-platform-input";

    @Input(Sink.PROMOTION_ORDER_INPUT)
    SubscribableChannel promotionOrderInput();

    @Input(Sink.PROMOTION_PLATFORM_INPUT)
    SubscribableChannel promotionPlatformInput();
}
