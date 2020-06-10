package com.bat.flexible.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {

    @Output("flexible-output")
    MessageChannel output();
}
