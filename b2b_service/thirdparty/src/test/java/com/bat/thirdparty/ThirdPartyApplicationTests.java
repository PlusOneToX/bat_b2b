package com.bat.thirdparty;

import javax.annotation.Resource;

import com.bat.thirdparty.order.service.AdminOrderServiceImpl;
import com.bat.thirdparty.xxljob.handler.OrderGoodsXxlJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThirdPartyApplicationTests {

    @Resource
    private AdminOrderServiceImpl adminOrderService;

    @Resource
    private OrderGoodsXxlJob orderGoodsXxlJob;

    @Test
    public void test() {
        // 根据订单Id同步物流信息
        adminOrderService.synchronizedLogisticsByOrderID(302440);
        // 定时任务自动同步物流信息
        // orderGoodsXxlJob.automaticallySynchronizeLogistics();
        // 同步到工厂
        // adminOrderService.syncOrderToFactory(222668);
    }

}
