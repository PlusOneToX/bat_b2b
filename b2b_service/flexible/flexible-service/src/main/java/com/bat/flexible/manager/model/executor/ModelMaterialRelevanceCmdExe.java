package com.bat.flexible.manager.model.executor;

import com.alibaba.fastjson.JSONObject;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.model.ModelMaterialRelevanceDOMapper;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;

@Component
public class ModelMaterialRelevanceCmdExe {

    @Autowired
    private ModelMaterialRelevanceDOMapper modelMaterialRelevanceDOMapper;

    /**
     * 存储可用的对象、model_material
     */
    @CreateCache(name = FlexibleKeyConstant.PICTURE_MODEL_MATERIAL_RELEVANCE_DO_PRE)
    private Cache<String, ModelMaterialRelevanceDO> cache;

    private static final Logger logger = LoggerFactory.getLogger(ModelMaterialRelevanceCmdExe.class);

    /**
     * 删除必须要删除缓存、不能使用批量修改
     * 
     * @param modelMaterialRelevanceDO
     */
    public void update(ModelMaterialRelevanceDO modelMaterialRelevanceDO) {
        logger.info("即将修改的机型材料信息:{}", JSONObject.toJSONString(modelMaterialRelevanceDO));
        modelMaterialRelevanceDOMapper.updateByPrimaryKey(modelMaterialRelevanceDO);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(modelMaterialRelevanceDO.getOpenFlag())
            || FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(modelMaterialRelevanceDO.getDelFlag())) {
            // 删除或者禁用移除key
            cache.remove(TenantContext.getTenantNo() + ":" + modelMaterialRelevanceDO.getModelId() + "_"
                + modelMaterialRelevanceDO.getMaterialId());
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + modelMaterialRelevanceDO.getModelId() + "_"
                + modelMaterialRelevanceDO.getMaterialId(), modelMaterialRelevanceDO);
        }
        logger.info("修改成功");
    }

    public void create(ModelMaterialRelevanceDO relevanceDO) {
        modelMaterialRelevanceDOMapper.insert(relevanceDO);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(relevanceDO.getOpenFlag())) {
            // 启用的才传
            cache.put(TenantContext.getTenantNo() + ":" + relevanceDO.getModelId() + "_" + relevanceDO.getMaterialId(),
                relevanceDO);
        }
    }

}
