package com.bat.financial.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 13:34
 */
public interface Sink {

    String FINANCIAL_INPUT = "financial-self-input";

    String FINANCIAL_PLATFORM_INPUT = "financial-platform-input";

    @Input(Sink.FINANCIAL_INPUT)
    SubscribableChannel financialInput();

    String FINANCIAL_DISTRIBUTOR_INPUT = "financial-distributor-input";

    @Input(Sink.FINANCIAL_DISTRIBUTOR_INPUT)
    SubscribableChannel financialDistributorInput();

    String FINANCIAL_ORDER_INPUT = "financial-order-input";

    @Input(Sink.FINANCIAL_ORDER_INPUT)
    SubscribableChannel financialOrderInput();

    @Input(Sink.FINANCIAL_PLATFORM_INPUT)
    SubscribableChannel financialPlatformInput();

    String FINANCIAL_THIRDPARTY_INPUT = "financial-thirdparty-input";

    @Input(Sink.FINANCIAL_THIRDPARTY_INPUT)
    SubscribableChannel financialThirdPartyInput();

}
