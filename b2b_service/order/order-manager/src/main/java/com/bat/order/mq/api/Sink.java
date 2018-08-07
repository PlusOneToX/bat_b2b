package com.bat.order.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    public static String ORDER_INPUT = "order-input";
    public static String ORDER_FINANCIAL_INPUT = "order-financial-input";
    public static String ORDER_DISTRIBUTOR_INPUT = "order-distributor-input";

    public static String ORDER_WAREHOUSE_INPUT = "order-warehouse-input";

    public static String ORDER_PLATFORM_INPUT = "order-platform-input";

    @Input(Sink.ORDER_INPUT)
    SubscribableChannel orderInput();

    @Input(Sink.ORDER_FINANCIAL_INPUT)
    SubscribableChannel orderFinancialInput();

    @Input(Sink.ORDER_DISTRIBUTOR_INPUT)
    SubscribableChannel orderDistributorInput();

    @Input(Sink.ORDER_WAREHOUSE_INPUT)
    SubscribableChannel orderWarehouseInput();

    @Input(Sink.ORDER_PLATFORM_INPUT)
    SubscribableChannel orderPlatformInput();
}
