package com.bat.thirdparty.message.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/3 21:35
 */
public interface Source {

    String OUTPUT = "thirdparty-output";

    @Output(OUTPUT)
    MessageChannel output();

}
