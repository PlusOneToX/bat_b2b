package com.bat.warehouse.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {

    public static String WAREHOUSE_OUTPUT = "warehouse-output";

    @Output(Source.WAREHOUSE_OUTPUT)
    MessageChannel warehouseOutput();

}
