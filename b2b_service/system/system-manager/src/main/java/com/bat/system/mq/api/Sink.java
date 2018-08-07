package com.bat.system.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/4 13:34
 */
public interface Sink {

    String SYSTEM_PLATFORM_INPUT = "system-platform-input";

    @Input(Sink.SYSTEM_PLATFORM_INPUT)
    SubscribableChannel systemPlatformInput();

}
