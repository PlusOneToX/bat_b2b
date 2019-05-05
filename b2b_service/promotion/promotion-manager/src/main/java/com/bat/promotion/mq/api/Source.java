package com.bat.promotion.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {

    public static String PROMOTION_OUTPUT = "promotion-output";

    @Output(Source.PROMOTION_OUTPUT)
    MessageChannel promotionOutput();

}
