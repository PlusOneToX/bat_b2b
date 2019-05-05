package com.bat.platform.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/3 21:35
 */
public interface Source {

    public static String PLATFORM_OUTPUT = "platform-output";

    @Output(Source.PLATFORM_OUTPUT)
    MessageChannel platformOutput();

}
