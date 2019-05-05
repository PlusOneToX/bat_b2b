package com.bat.platform;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.platform.dao.tenant.PlatformTenantUrlMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantUrlDO;
import com.bat.platform.service.common.Constant;

import static com.bat.platform.service.common.Constant.*;

/**
 * 启动后初始化租户信息
 * 
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/10/24 11:20
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Resource
    private PlatformTenantUrlMapper urlMapper;

    @CreateCache(name = Constant.TENANT_URL_CACHE_1)
    private Cache<String, List<String>> tenantUrlCache1;

    @CreateCache(name = Constant.TENANT_URL_CACHE_2)
    private Cache<String, List<String>> tenantUrlCache2;

    @CreateCache(name = Constant.TENANT_URL_CACHE_3)
    private Cache<String, List<String>> tenantUrlCache3;

    @CreateCache(name = Constant.TENANT_URL_CACHE_4)
    private Cache<String, List<String>> tenantUrlCache4;

    @CreateCache(name = Constant.TENANT_URL_CACHE_5)
    private Cache<String, List<String>> tenantUrlCache5;

    @CreateCache(name = Constant.TENANT_URL_CACHE_6)
    private Cache<String, List<String>> tenantUrlCache6;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化租户url缓存信息
        List<PlatformTenantUrlDO> tenantUrlDOS = urlMapper.all();
        if (!CollectionUtils.isEmpty(tenantUrlDOS)) {
            Map<String, List<PlatformTenantUrlDO>> TenantUrlDOSMap1 =
                tenantUrlDOS.stream().filter(tenantUrlDO -> tenantUrlDO.getUrlType().equals(URL_TYPE_1))
                    .collect(Collectors.groupingBy(PlatformTenantUrlDO::getHost));
            if (!CollectionUtils.isEmpty(TenantUrlDOSMap1)) {
                TenantUrlDOSMap1.forEach((key, values) -> {
                    List<String> tenantNos =
                        values.stream().map(PlatformTenantUrlDO::getTenantNo).collect(Collectors.toList());
                    tenantUrlCache1.put(key, tenantNos);
                });
            }
            Map<String, List<PlatformTenantUrlDO>> TenantUrlDOSMap2 =
                tenantUrlDOS.stream().filter(tenantUrlDO -> tenantUrlDO.getUrlType().equals(URL_TYPE_2))
                    .collect(Collectors.groupingBy(PlatformTenantUrlDO::getHost));
            if (!CollectionUtils.isEmpty(TenantUrlDOSMap2)) {
                TenantUrlDOSMap2.forEach((key, values) -> {
                    List<String> tenantNos =
                        values.stream().map(PlatformTenantUrlDO::getTenantNo).collect(Collectors.toList());
                    tenantUrlCache2.put(key, tenantNos);
                });
            }
            Map<String, List<PlatformTenantUrlDO>> TenantUrlDOSMap3 =
                tenantUrlDOS.stream().filter(tenantUrlDO -> tenantUrlDO.getUrlType().equals(URL_TYPE_3))
                    .collect(Collectors.groupingBy(PlatformTenantUrlDO::getHost));
            if (!CollectionUtils.isEmpty(TenantUrlDOSMap3)) {
                TenantUrlDOSMap3.forEach((key, values) -> {
                    List<String> tenantNos =
                        values.stream().map(PlatformTenantUrlDO::getTenantNo).collect(Collectors.toList());
                    tenantUrlCache3.put(key, tenantNos);
                });
            }
            Map<String, List<PlatformTenantUrlDO>> TenantUrlDOSMap4 =
                tenantUrlDOS.stream().filter(tenantUrlDO -> tenantUrlDO.getUrlType().equals(URL_TYPE_4))
                    .collect(Collectors.groupingBy(PlatformTenantUrlDO::getHost));
            if (!CollectionUtils.isEmpty(TenantUrlDOSMap4)) {
                TenantUrlDOSMap4.forEach((key, values) -> {
                    List<String> tenantNos =
                        values.stream().map(PlatformTenantUrlDO::getTenantNo).collect(Collectors.toList());
                    tenantUrlCache4.put(key, tenantNos);
                });
            }
            Map<String, List<PlatformTenantUrlDO>> TenantUrlDOSMap5 =
                tenantUrlDOS.stream().filter(tenantUrlDO -> tenantUrlDO.getUrlType().equals(URL_TYPE_5))
                    .collect(Collectors.groupingBy(PlatformTenantUrlDO::getHost));
            if (!CollectionUtils.isEmpty(TenantUrlDOSMap5)) {
                TenantUrlDOSMap5.forEach((key, values) -> {
                    List<String> tenantNos =
                        values.stream().map(PlatformTenantUrlDO::getTenantNo).collect(Collectors.toList());
                    tenantUrlCache5.put(key, tenantNos);
                });
            }
            Map<String, List<PlatformTenantUrlDO>> TenantUrlDOSMap6 =
                tenantUrlDOS.stream().filter(tenantUrlDO -> tenantUrlDO.getUrlType().equals(URL_TYPE_6))
                    .collect(Collectors.groupingBy(PlatformTenantUrlDO::getHost));
            if (!CollectionUtils.isEmpty(TenantUrlDOSMap6)) {
                TenantUrlDOSMap6.forEach((key, values) -> {
                    List<String> tenantNos =
                        values.stream().map(PlatformTenantUrlDO::getTenantNo).collect(Collectors.toList());
                    tenantUrlCache6.put(key, tenantNos);
                });
            }
        }
    }
}
