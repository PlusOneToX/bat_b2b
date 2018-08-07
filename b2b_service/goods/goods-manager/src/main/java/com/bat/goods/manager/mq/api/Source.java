package com.bat.goods.manager.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/3 21:35
 */
public interface Source {

    public static String GOODS_OUTPUT = "goods-output";

    @Output(Source.GOODS_OUTPUT)
    MessageChannel goodsOutput();

}
