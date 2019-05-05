package com.bat.thirdparty.message.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/4 13:34
 */
public interface Sink {

    String THIRDPARTY_ORDER_INPUT = "thirdparty-order-input";
    String THIRDPARTY_ORDER_ERP_INPUT = "thirdparty-order-erp-input";
    String THIRDPARTY_ORDER_ERP_PURCHASE_INPUT = "thirdparty-order-erp-purchase-input";
    String THIRDPARTY_ORDER_FACTORY_INPUT = "thirdparty-order-factory-input";
    String THIRDPARTY_ORDER_CUSTOMER_INPUT = "thirdparty-order-customer-input";
    String THIRDPARTY_ORDER_SUB_INPUT = "thirdparty-order-sub-input";
    String THIRDPARTY_FINANCIAL_INPUT = "thirdparty-financial-input";
    String THIRDPARTY_DISTRIBUTOR_INPUT = "thirdparty-distributor-input";
    String THIRDPARTY_FLEXIBLE_INPUT = "thirdparty-flexible-input";
    String THIRDPARTY_GOODS_INPUT = "thirdparty-goods-input";
    String THIRDPARTY_PROMOTION_INPUT = "thirdparty-promotion-input";
    String THIRDPARTY_SYSTEM_INPUT = "thirdparty-system-input";
    String THIRDPARTY_SELF_INPUT = "thirdparty-self-input";
    String THIRDPARTY_WAREHOUSE_INPUT = "thirdparty-warehouse-input";
    String THIRDPARTY_PLATFORM_INPUT = "thirdparty-platform-input";

    @Input(Sink.THIRDPARTY_ORDER_INPUT)
    SubscribableChannel thirdpartyOrderInput();

    @Input(Sink.THIRDPARTY_ORDER_ERP_INPUT)
    SubscribableChannel thirdpartyOrderErpInput();

    @Input(Sink.THIRDPARTY_ORDER_ERP_PURCHASE_INPUT)
    SubscribableChannel thirdpartyOrderErpPurchaseInput();

    @Input(Sink.THIRDPARTY_ORDER_FACTORY_INPUT)
    SubscribableChannel thirdpartyOrderFactoryInput();

    @Input(Sink.THIRDPARTY_ORDER_CUSTOMER_INPUT)
    SubscribableChannel thirdpartyOrderCustomerInput();

    @Input(Sink.THIRDPARTY_ORDER_SUB_INPUT)
    SubscribableChannel thirdpartyOrderSubInput();

    @Input(Sink.THIRDPARTY_FINANCIAL_INPUT)
    SubscribableChannel thirdpartyFinancialInput();

    @Input(Sink.THIRDPARTY_DISTRIBUTOR_INPUT)
    SubscribableChannel thirdpartyDistributorInput();

    @Input(Sink.THIRDPARTY_FLEXIBLE_INPUT)
    SubscribableChannel thirdpartyFlexibleInput();

    @Input(Sink.THIRDPARTY_GOODS_INPUT)
    SubscribableChannel thirdpartyGoodsInput();

    @Input(Sink.THIRDPARTY_PROMOTION_INPUT)
    SubscribableChannel thirdpartyPromotionInput();

    @Input(Sink.THIRDPARTY_SYSTEM_INPUT)
    SubscribableChannel thirdpartySystemInput();

    @Input(Sink.THIRDPARTY_SELF_INPUT)
    SubscribableChannel thirdpartySelfInput();

    @Input(Sink.THIRDPARTY_WAREHOUSE_INPUT)
    SubscribableChannel thirdpartyWarehouseInput();

    @Input(Sink.THIRDPARTY_PLATFORM_INPUT)
    SubscribableChannel thirdpartyPlatformInput();

}
