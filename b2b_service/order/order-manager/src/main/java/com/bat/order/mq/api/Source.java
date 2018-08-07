package com.bat.order.mq.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {

    public static String ORDER_OUTPUT = "order-output";
    public static String ORDER_ERP_OUTPUT = "order-erp-output";
    public static String ORDER_ERP_PURCHASE_OUTPUT = "order-erp-purchase-output";
    public static String ORDER_FACTORY_OUTPUT = "order-factory-output";
    public static String ORDER_CUSTOMER_OUTPUT = "order-customer-output";
    public static String ORDER_SUB_OUTPUT = "order-sub-output";

    @Output(Source.ORDER_OUTPUT)
    MessageChannel orderOutput();

    @Output(Source.ORDER_ERP_OUTPUT)
    MessageChannel orderErpOutput();

    @Output(Source.ORDER_ERP_PURCHASE_OUTPUT)
    MessageChannel orderErpPurchaseOutput();

    @Output(Source.ORDER_FACTORY_OUTPUT)
    MessageChannel orderFactoryOutput();

    @Output(Source.ORDER_CUSTOMER_OUTPUT)
    MessageChannel orderCustomerOutput();

    @Output(Source.ORDER_SUB_OUTPUT)
    MessageChannel orderSubOutput();
}
