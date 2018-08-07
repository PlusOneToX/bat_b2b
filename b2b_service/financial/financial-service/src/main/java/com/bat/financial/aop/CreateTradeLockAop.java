package com.bat.financial.aop;

import java.util.concurrent.TimeUnit;

import com.bat.financial.common.constant.FinancialConstant;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.common.lock.LockConstant;
import com.bat.financial.api.pay.dto.CreateTradeCmd;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: 创建交易 分布式锁aop
 * @date 2018/1/7 18:03
 */
@Aspect
@Component
@Slf4j
public class CreateTradeLockAop {

    @Pointcut("execution(* com.bat.financial.pay.*.createTrade(com.bat.financial.api.pay.dto.CreateTradeCmd))")
    public void distributedLock() {}

    @CreateCache(name = LockConstant.CREATE_TRADE)
    private Cache<String, Integer> createTradeCache;

    /**
     * 创建交易 分布式锁切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("distributedLock()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String key;
        try {
            CreateTradeCmd tradeCmd = (CreateTradeCmd)joinPoint.getArgs()[0];
            String orderId = tradeCmd.getOrderId();
            if (StringUtils.isBlank(orderId)) {
                return joinPoint.proceed();
            }
            key = TenantContext.getTenantNo() + ":" + orderId;
        } catch (Exception e) {
            return joinPoint.proceed();
        }
        try (AutoReleaseLock autoReleaseLock = createTradeCache.tryLock(key, 1, TimeUnit.MINUTES)) {
            if (autoReleaseLock == null) {
                throw FinancialException.buildException(FinancialConstant.COMMON_OPERATE_REPEAT);
            }
            log.info("创建交易进入 分布式锁AOP");
            return joinPoint.proceed();
        }
    }
}
