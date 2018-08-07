package com.bat.order.service.order.executor.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.redis.OrderRedisKeyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.tenant.TenantContext;

@Component
public class OrderRedisKeyCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRedisKeyCmdExe.class);

    // 兑换码下单分布式锁
    @CreateCache(name = OrderRedisKeyConstant.EXCHANGE_ORDER_CREATE_PRE)
    private Cache<String, Integer> exchangeOrderCreateCache;

    public List<AutoReleaseLock> lockExchangeCodeList(List<String> secretCodeList) {
        List<AutoReleaseLock> autoReleaseLockList = new ArrayList<>();
        secretCodeList.stream().forEach(s -> {
            AutoReleaseLock autoReleaseLock =
                exchangeOrderCreateCache.tryLock(TenantContext.getTenantNo() + ":" + s, 1, TimeUnit.MINUTES);
            if (autoReleaseLock == null) {
                LOGGER.info("暗码[" + s + "]重复提交兑换");
                // 先解锁
                throw OrderException.buildException(OrderInfoErrorCode.ORDER_EXCHANGE_SECRET_CODE_EXCHANGING,
                    s + MessageUtils.get(OrderInfoErrorCode.ORDER_EXCHANGE_SECRET_CODE_EXCHANGING));
            }
            autoReleaseLockList.add(autoReleaseLock);
        });

        return autoReleaseLockList;
    }

    public static void releaseLock(List<AutoReleaseLock> autoReleaseLockList) {
        if (autoReleaseLockList == null || autoReleaseLockList.size() == 0) {
            return;
        }
        autoReleaseLockList.stream().forEach(autoReleaseLock -> {
            autoReleaseLock.close();
        });
    }

}
