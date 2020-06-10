package com.bat.flexible.manager.model.executor;

import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.model.ModelDOMapper;
import com.bat.flexible.dao.model.dataobject.ModelDO;

@Component
public class ModelCmdExe {

    @Autowired
    private ModelDOMapper modelDOMapper;

    @Autowired
    private ModelTreeQryExe modelTreeQryExe;

    @CreateCache(name = FlexibleKeyConstant.MODEL_DO_PRE)
    private Cache<String, ModelDO> cache;

    public void create(ModelDO modelDO) {
        modelDOMapper.insert(modelDO);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(modelDO.getOpenFlag())) {
            // 启用才放进去
            cache.put(TenantContext.getTenantNo() + ":" + modelDO.getId(), modelDO);
        }
        modelTreeQryExe.removeAll(modelDO.getCategoryId());
    }

    public void update(ModelDO modelDO, Boolean clearFlag) {
        modelDOMapper.updateByPrimaryKey(modelDO);

        if (FlexibleCommonConstant.COMMON_DEL_FLAG_YES.equals(modelDO.getDelFlag())
            || FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(modelDO.getOpenFlag())) {
            // 删除或者禁用移除key
            cache.remove(TenantContext.getTenantNo() + ":" + modelDO.getId());
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + modelDO.getId(), modelDO);
        }
        if (clearFlag != null && clearFlag) {
            modelTreeQryExe.removeAll(modelDO.getCategoryId());
        }
    }

    /**
     * 同一个分类id、父id下的型号序号++
     * 
     * @param categoryId
     * @param parentId
     * @param sortNoAdd
     *            序号变动值
     * @param sequenceStart
     *            开始序号值
     * @param sequenceEnd
     *            结束序号值
     */
    public void setSequenceAddByCategoryIdAndParentId(Integer categoryId, Integer parentId, Integer sortNoAdd,
        Integer sequenceStart, Integer sequenceEnd) {
        modelDOMapper.setSequenceAddByCategoryIdAndParentId(categoryId, parentId, sortNoAdd, sequenceStart,
            sequenceEnd);
    }
}
