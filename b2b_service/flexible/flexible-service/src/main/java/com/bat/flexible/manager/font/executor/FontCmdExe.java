package com.bat.flexible.manager.font.executor;

import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.font.FontDOMapper;
import com.bat.flexible.dao.font.dataobject.FontDO;

@Component
public class FontCmdExe {

    @Autowired
    private FontDOMapper fontDOMapper;

    @CreateCache(name = FlexibleKeyConstant.FONT_DO_PRE)
    private Cache<String, FontDO> fontDOCache;

    public void create(FontDO fontDO) {
        fontDOMapper.insert(fontDO);
        fontDOCache.put(TenantContext.getTenantNo() + ":" + fontDO.getId(), fontDO);
    }

    public void update(FontDO fontDO) {
        fontDOMapper.updateByPrimaryKey(fontDO);
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(fontDO.getDelFlag())
            || FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(fontDO.getOpenFlag())) {
            // 移除缓存
            fontDOCache.remove(TenantContext.getTenantNo() + ":" + fontDO.getId());
        }
        fontDOCache.put(TenantContext.getTenantNo() + ":" + fontDO.getId(), fontDO);
    }
}
