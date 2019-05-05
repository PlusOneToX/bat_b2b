package com.bat.thirdparty.xxljob.service.executor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.order.api.OrderGoodsDiyServiceRpc;
import com.bat.dubboapi.order.order.dto.info.OrderIdListQry;
import com.bat.dubboapi.platform.common.Response;
import com.bat.dubboapi.platform.tenant.api.PlatformTenantServiceRpc;
import com.bat.thirdparty.goods.api.GoodsServiceI;
import com.bat.thirdparty.order.service.AdminOrderServiceImpl;
import com.bat.thirdparty.tenant.TenantContext;

@Component
public class OrderGoodsJobExe {

    @Autowired
    private GoodsServiceI goodsServiceI;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsJobExe.class);

    @DubboReference(check = false, timeout = 10000)
    private PlatformTenantServiceRpc platformTenantServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private OrderGoodsDiyServiceRpc orderGoodsDiyServiceRpc;

    @Resource
    private AdminOrderServiceImpl adminOrderService;

    @Async
    public void autoUpdateSaleStatus(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        try {
            // 下架
            goodsServiceI.downSaleStatus();
        } catch (Exception e) {
            LOGGER.info("处理下架出现异常:{}", e);
        }
        try {
            // 上架
            goodsServiceI.upperSaleStatus();
        } catch (Exception e) {
            LOGGER.info("处理上架出现异常:{}", e);
        }
        TenantContext.removeTenantNo();
    }


}
