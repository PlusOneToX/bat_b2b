package com.bat.flexible.interceptor;

import java.lang.reflect.Method;
import java.util.List;

import com.bat.flexible.Tenant.TenantContext;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alicp.jetcache.anno.aop.CachePointcut;
import com.alicp.jetcache.anno.aop.JetCacheInterceptor;
import com.alicp.jetcache.anno.method.CacheHandler;
import com.alicp.jetcache.anno.method.CacheInvokeConfig;
import com.alicp.jetcache.anno.method.CacheInvokeContext;
import com.alicp.jetcache.anno.support.*;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/31 10:11
 */
@Aspect
@Component
@Primary
public class RedisJetCacheInterceptor extends JetCacheInterceptor {
    @Autowired
    private ConfigMap cacheConfigMap;
    private ApplicationContext applicationContext;
    private GlobalCacheConfig globalCacheConfig;
    ConfigProvider configProvider;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        if (this.configProvider == null) {
            this.configProvider = (ConfigProvider)this.applicationContext.getBean(ConfigProvider.class);
        }

        if (this.configProvider != null && this.globalCacheConfig == null) {
            this.globalCacheConfig = this.configProvider.getGlobalCacheConfig();
        }

        if (this.globalCacheConfig != null && this.globalCacheConfig.isEnableMethodCache()) {
            Method method = invocation.getMethod();
            Object obj = invocation.getThis();
            CacheInvokeConfig cac = null;
            if (obj != null) {
                String key = CachePointcut.getKey(method, obj.getClass());
                cac = this.cacheConfigMap.getByMethodInfo(key);
            }
            CachedAnnoConfig cachedAnnoConfig = cac.getCachedAnnoConfig();
            if (cachedAnnoConfig != null) {
                String key = cachedAnnoConfig.getKey();
                String[] split = key.split("\\+':'\\+");
                if (split.length > 1) {
                    key = split[split.length - 1];
                }
                key = TenantContext.getTenantNo() + "+':'+" + key;
                cachedAnnoConfig.setKey(key);
                cachedAnnoConfig.setKeyEvaluator(null);
            }
            List<CacheInvalidateAnnoConfig> invalidateAnnoConfigs = cac.getInvalidateAnnoConfigs();
            if (!CollectionUtils.isEmpty(invalidateAnnoConfigs)) {
                invalidateAnnoConfigs.forEach(invalidateAnnoConfig -> {
                    String key = invalidateAnnoConfig.getKey();
                    String[] split = key.split("\\+':'\\+");
                    if (split.length > 1) {
                        key = split[split.length - 1];
                    }
                    key = TenantContext.getTenantNo() + "+':'+" + key;
                    invalidateAnnoConfig.setKey(key);
                    invalidateAnnoConfig.setKeyEvaluator(null);
                });
            }
            if (cac != null && cac != CacheInvokeConfig.getNoCacheInvokeConfigInstance()) {
                CacheInvokeContext context =
                    this.configProvider.getCacheContext().createCacheInvokeContext(this.cacheConfigMap);
                context.setTargetObject(invocation.getThis());
                context.setInvoker(invocation::proceed);
                context.setMethod(method);
                context.setArgs(invocation.getArguments());
                context.setCacheInvokeConfig(cac);
                context.setHiddenPackages(this.globalCacheConfig.getHiddenPackages());
                return CacheHandler.invoke(context);
            } else {
                return invocation.proceed();
            }
        } else {
            return invocation.proceed();
        }
    }

    @Override
    public void setCacheConfigMap(ConfigMap cacheConfigMap) {
        this.cacheConfigMap = cacheConfigMap;
    }
}
