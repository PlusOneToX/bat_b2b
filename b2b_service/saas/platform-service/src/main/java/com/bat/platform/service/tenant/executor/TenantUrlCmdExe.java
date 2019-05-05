package com.bat.platform.service.tenant.executor;

import static com.bat.platform.service.common.Constant.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.platform.api.base.PlatformException;
import com.bat.platform.api.tenant.dto.TenantUrlCmd;
import com.bat.platform.dao.tenant.PlatformTenantUrlMapper;
import com.bat.platform.dao.tenant.dataobject.PlatformTenantUrlDO;
import com.bat.platform.service.common.CommonErrorCode;
import com.bat.platform.service.common.Constant;

/**
 * 沙漠
 */
@Component
public class TenantUrlCmdExe {

    @Resource
    private PlatformTenantUrlMapper platformTenantUrlMapper;
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

    @Transactional(rollbackFor = Exception.class)
    public void add(TenantUrlCmd cmd) {
        PlatformTenantUrlDO platformTenantUrlDO = new PlatformTenantUrlDO();
        BeanUtils.copyProperties(cmd, platformTenantUrlDO);
        Date date = new Date();
        platformTenantUrlDO.setCreateTime(date);
        platformTenantUrlDO.setUpdateTime(date);
        platformTenantUrlMapper.insert(platformTenantUrlDO);
        // 更新租户url缓存数据
        updateTenantUrlCache(platformTenantUrlDO, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(TenantUrlCmd cmd) {
        if (cmd.getId() == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_ID_ERROR);
        }
        PlatformTenantUrlDO beforeUrlDO = platformTenantUrlMapper.selectByPrimaryKey(cmd.getId());
        if (beforeUrlDO == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
        }

        PlatformTenantUrlDO updateUrlDO = new PlatformTenantUrlDO();
        BeanUtils.copyProperties(cmd, updateUrlDO);
        platformTenantUrlMapper.updateByPrimaryKey(updateUrlDO);
        if (!beforeUrlDO.getHost().equals(updateUrlDO.getHost())) {
            // 更新租户url缓存数据
            updateTenantUrlCache(updateUrlDO, beforeUrlDO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        PlatformTenantUrlDO urlDO = platformTenantUrlMapper.selectByPrimaryKey(id);
        if (urlDO == null) {
            throw PlatformException.buildException(CommonErrorCode.D_COMMON_NO_RECORD_ERROR);
        }
        platformTenantUrlMapper.deleteByPrimaryKey(id);
        // 更新租户url缓存数据
        updateTenantUrlCache(null, urlDO);
    }

    /**
     * 更新租户url缓存数据
     * 
     * @param deleteTenantUrlDO
     */
    public void updateTenantUrlCache(PlatformTenantUrlDO addTenantUrlDO, PlatformTenantUrlDO deleteTenantUrlDO) {
        // 更新租户url缓存数据
        if (addTenantUrlDO != null) {
            List<String> tenantNos = null;
            if (addTenantUrlDO.getUrlType().equals(URL_TYPE_1)) {
                tenantNos = tenantUrlCache1.get(addTenantUrlDO.getHost());
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_2)) {
                tenantNos = tenantUrlCache2.get(addTenantUrlDO.getHost());
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_3)) {
                tenantNos = tenantUrlCache3.get(addTenantUrlDO.getHost());
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_4)) {
                tenantNos = tenantUrlCache4.get(addTenantUrlDO.getHost());
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_5)) {
                tenantNos = tenantUrlCache5.get(addTenantUrlDO.getHost());
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_6)) {
                tenantNos = tenantUrlCache6.get(addTenantUrlDO.getHost());
            }
            if (tenantNos == null) {
                tenantNos = new ArrayList<>();
            }
            tenantNos.add(addTenantUrlDO.getTenantNo());
            if (addTenantUrlDO.getUrlType().equals(URL_TYPE_1)) {
                tenantUrlCache1.put(addTenantUrlDO.getHost(), tenantNos);
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_2)) {
                tenantUrlCache2.put(addTenantUrlDO.getHost(), tenantNos);
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_3)) {
                tenantUrlCache3.put(addTenantUrlDO.getHost(), tenantNos);
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_4)) {
                tenantUrlCache4.put(addTenantUrlDO.getHost(), tenantNos);
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_5)) {
                tenantUrlCache5.put(addTenantUrlDO.getHost(), tenantNos);
            } else if (addTenantUrlDO.getUrlType().equals(URL_TYPE_6)) {
                tenantUrlCache6.put(addTenantUrlDO.getHost(), tenantNos);
            }
        }
        if (deleteTenantUrlDO != null) {
            List<String> tenantNos = null;
            if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_1)) {
                tenantNos = tenantUrlCache1.get(deleteTenantUrlDO.getHost());
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_2)) {
                tenantNos = tenantUrlCache2.get(deleteTenantUrlDO.getHost());
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_3)) {
                tenantNos = tenantUrlCache3.get(deleteTenantUrlDO.getHost());
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_4)) {
                tenantNos = tenantUrlCache4.get(deleteTenantUrlDO.getHost());
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_5)) {
                tenantNos = tenantUrlCache5.get(deleteTenantUrlDO.getHost());
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_6)) {
                tenantNos = tenantUrlCache6.get(deleteTenantUrlDO.getHost());
            }
            if (tenantNos != null) {
                tenantNos = tenantNos.stream().filter(tenantNo -> !tenantNo.equals(deleteTenantUrlDO.getTenantNo()))
                    .collect(Collectors.toList());
            }
            if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_1)) {
                if (CollectionUtils.isEmpty(tenantNos)) {
                    tenantUrlCache1.remove(deleteTenantUrlDO.getHost());
                } else {
                    tenantUrlCache1.put(deleteTenantUrlDO.getHost(), tenantNos);
                }
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_2)) {
                if (CollectionUtils.isEmpty(tenantNos)) {
                    tenantUrlCache2.remove(deleteTenantUrlDO.getHost());
                } else {
                    tenantUrlCache2.put(deleteTenantUrlDO.getHost(), tenantNos);
                }
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_3)) {
                if (CollectionUtils.isEmpty(tenantNos)) {
                    tenantUrlCache3.remove(deleteTenantUrlDO.getHost());
                } else {
                    tenantUrlCache3.put(deleteTenantUrlDO.getHost(), tenantNos);
                }
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_4)) {
                if (CollectionUtils.isEmpty(tenantNos)) {
                    tenantUrlCache4.remove(deleteTenantUrlDO.getHost());
                } else {
                    tenantUrlCache4.put(deleteTenantUrlDO.getHost(), tenantNos);
                }
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_5)) {
                if (CollectionUtils.isEmpty(tenantNos)) {
                    tenantUrlCache5.remove(deleteTenantUrlDO.getHost());
                } else {
                    tenantUrlCache5.put(deleteTenantUrlDO.getHost(), tenantNos);
                }
            } else if (deleteTenantUrlDO.getUrlType().equals(URL_TYPE_6)) {
                if (CollectionUtils.isEmpty(tenantNos)) {
                    tenantUrlCache6.remove(deleteTenantUrlDO.getHost());
                } else {
                    tenantUrlCache6.put(deleteTenantUrlDO.getHost(), tenantNos);
                }
            }
        }
    }
}
