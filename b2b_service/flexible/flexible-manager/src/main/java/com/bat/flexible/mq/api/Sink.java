package com.bat.flexible.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String FLEXIBLE_ORDER_INPUT="flexible-order-input";

    String FLEXIBLE_PLATFORM_INPUT = "flexible-platform-input";

    @Input(Sink.FLEXIBLE_ORDER_INPUT)
    SubscribableChannel flexibleOrderInput();

    @Input(Sink.FLEXIBLE_PLATFORM_INPUT)
    SubscribableChannel flexiblePlatformInput();
}
