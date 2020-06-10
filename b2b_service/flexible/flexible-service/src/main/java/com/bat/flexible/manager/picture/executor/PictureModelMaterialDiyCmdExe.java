package com.bat.flexible.manager.picture.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.picture.PictureModelMaterialDiyDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureModelMaterialDiyDO;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;

@Component
public class PictureModelMaterialDiyCmdExe {

    @Autowired
    private PictureModelMaterialDiyDOMapper pictureModelMaterialDiyDOMapper;

    @CreateCache(name = FlexibleKeyConstant.PICTURE_MODEL_MATERIAL_DIY_DO_PRE)
    private Cache<String, PictureModelMaterialDiyDO> cache;

    public void create(PictureModelMaterialDiyDO diyDO) {
        pictureModelMaterialDiyDOMapper.insert(diyDO);
    }

    public void removeCache(Integer materialId, Integer modelId, Integer pictureId) {
        cache.remove(TenantContext.getTenantNo() + ":" + materialId + "_" + modelId + "_" + pictureId);
    }

    public void delete(Integer id) {
        pictureModelMaterialDiyDOMapper.deleteByPrimaryKey(id);
    }
}
