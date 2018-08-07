package com.bat.financial.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/3 21:35
 */
public interface Source {

    String FINANCIAL_OUTPUT = "financial-output";

    @Output(FINANCIAL_OUTPUT)
    MessageChannel financialOutput();

}
