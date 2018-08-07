package com.bat.order.service.deliver;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderStatusRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderDeliveryServiceRpcImplTest {

    @Resource
    private OrderDeliveryServiceRpcImpl orderDeliveryServiceRpc;

    @Test
    void changeDeliverOrderStatus() {
        String str = "{\"deliverOrderNo\":\"XSCKD1002203000050\",\"expressNo\":\" \",\"expressType\":\" \",\"status\":5}";
        ErpDeliverOrderStatusRequest request = JSON.parseObject(str,ErpDeliverOrderStatusRequest.class);
        orderDeliveryServiceRpc.changeDeliverOrderStatus(request);
    }
}