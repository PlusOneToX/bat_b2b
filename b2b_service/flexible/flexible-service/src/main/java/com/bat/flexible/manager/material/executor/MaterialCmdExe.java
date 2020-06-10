package com.bat.flexible.manager.material.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.material.MaterialDOMapper;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;

@Component
public class MaterialCmdExe {

    @Autowired
    private MaterialDOMapper materialDOMapper;

    @Autowired
    private MaterialTreeQryExe materialTreeQryExe;

    @CreateCache(name = FlexibleKeyConstant.MATERIAL_DO_PRE)
    private Cache<String, MaterialDO> materialDOCache;

    public void update(MaterialDO materialDO) {
        materialDOMapper.updateByPrimaryKey(materialDO);
        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(materialDO.getDelFlag())) {
            materialDOCache.remove(TenantContext.getTenantNo() + ":" + materialDO.getId());
            return;
        }
        materialDOCache.put(TenantContext.getTenantNo() + ":" + materialDO.getId(), materialDO);
        materialTreeQryExe.remove(materialDO.getCategoryId());
    }

    public void create(MaterialDO materialDO) {
        materialDOMapper.insert(materialDO);
        materialDOCache.put(TenantContext.getTenantNo() + ":" + materialDO.getId(), materialDO);
        materialTreeQryExe.remove(materialDO.getCategoryId());
    }
}
