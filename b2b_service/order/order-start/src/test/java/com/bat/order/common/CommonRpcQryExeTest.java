package com.bat.order.common;

import com.alibaba.fastjson.JSON;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.service.common.CommonRpcQryExe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class CommonRpcQryExeTest {

    @Resource
    private CommonRpcQryExe commonRpcQryExe;

    @Test
    void calcDiyWeight() {
        String json = "";
        List<OrderGoodsDO> orderGoodsDOS = JSON.parseArray(json, OrderGoodsDO.class);
        commonRpcQryExe.calcDiyWeight(orderGoodsDOS);
    }
}