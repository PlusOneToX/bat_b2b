package com.bat.flexible.manager.shop.executor;

import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.shop.ShopDOMapper;
import com.bat.flexible.dao.shop.dataobject.ShopDO;

@Component
public class ShopCmdExe {

    @Autowired
    private ShopDOMapper shopDOMapper;

    @CreateCache(name = FlexibleKeyConstant.SHOP_DO_PRE)
    private Cache<String, ShopDO> shopDOCache;

    public void create(ShopDO shopDO) {
        shopDOMapper.insert(shopDO);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(shopDO.getOpenFlag())) {
            shopDOCache.put(TenantContext.getTenantNo() + ":" + shopDO.getId(), shopDO);
        }
    }

    public void update(ShopDO shopDO) {
        shopDOMapper.updateByPrimaryKey(shopDO);
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(shopDO.getDelFlag())
            || FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(shopDO.getOpenFlag())) {
            shopDOCache.remove(TenantContext.getTenantNo() + ":" + shopDO.getId());
        } else {
            shopDOCache.put(TenantContext.getTenantNo() + ":" + shopDO.getId(), shopDO);
        }
    }

    public void restoreUserConfigIdNull(Integer userConfigId) {
        shopDOMapper.restoreUserConfigIdNull(userConfigId);
    }
}
