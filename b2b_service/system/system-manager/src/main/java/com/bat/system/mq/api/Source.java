package com.bat.system.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/3 21:35
 */
public interface Source {

    String SYSTEM_OUTPUT = "system-output";

    @Output(SYSTEM_OUTPUT)
    MessageChannel systemOutput();

}
