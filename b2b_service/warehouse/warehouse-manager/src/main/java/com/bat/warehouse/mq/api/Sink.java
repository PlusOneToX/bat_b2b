package com.bat.warehouse.mq.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String WAREHOUSE_ORDER_INPUT="warehouse-order-input";

    String WAREHOUSE_THIRD_INPUT="warehouse-third-input";

    String WAREHOUSE_INPUT="warehouse-input";

    String WAREHOUSE_PLATFORM_INPUT = "warehouse-platform-input";

    @Input(Sink.WAREHOUSE_ORDER_INPUT)
    SubscribableChannel warehouseOrderInput();

    @Input(Sink.WAREHOUSE_THIRD_INPUT)
    SubscribableChannel warehouseThirdInput();

    @Input(Sink.WAREHOUSE_INPUT)
    SubscribableChannel warehouseInput();

    @Input(Sink.WAREHOUSE_PLATFORM_INPUT)
    SubscribableChannel warehousePlatformInput();

}
