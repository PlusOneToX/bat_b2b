package com.bat.distributor.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 13:34
 */
public interface Sink {

    String DISTRIBUTOR_SYSTEM_INPUT = "distributor-system-input";
    String DISTRIBUTOR_INPUT = "distributor-input";

    public static String DISTRIBUTOR_PLATFORM_INPUT = "distributor-platform-input";

    @Input(Sink.DISTRIBUTOR_SYSTEM_INPUT)
    SubscribableChannel distributorSystemInput();

    @Input(Sink.DISTRIBUTOR_INPUT)
    SubscribableChannel distributorInput();

    @Input(Sink.DISTRIBUTOR_PLATFORM_INPUT)
    SubscribableChannel distributorPlatformInput();

}
