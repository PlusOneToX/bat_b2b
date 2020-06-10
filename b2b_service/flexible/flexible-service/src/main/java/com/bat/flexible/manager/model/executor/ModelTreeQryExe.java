package com.bat.flexible.manager.model.executor;

import java.util.ArrayList;
import java.util.List;

import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.model.ModelDOMapper;
import com.bat.flexible.dao.model.co.ModelPageCO;
import com.bat.flexible.dao.model.co.ModelTreeCO;

@Component
public class ModelTreeQryExe {

    @Autowired
    private ModelDOMapper modelDOMapper;

    /**
     * 可用的树结构（按照产品类型）
     */
    @CreateCache(name = FlexibleKeyConstant.MODEL_TREE_USABLE_PRE)
    private Cache<String, List<ModelTreeCO>> usableCache;

    /**
     * 产品类型全部树结构(除了删除的)
     */
    @CreateCache(name = FlexibleKeyConstant.MODEL_TREE_ALL_PRE)
    private Cache<String, List<ModelPageCO>> allCache;

    @Cached(name = FlexibleKeyConstant.MODEL_TREE_USABLE_PRE, key = "#categoryId+'_'+#openFlag")
    public List<ModelTreeCO> treeByCategoryIdAndOpenFlag(Integer categoryId, Short openFlag) {
        List<ModelPageCO> allList = allCache.get(TenantContext.getTenantNo() + ":" + categoryId);
        // 判断全部树结构是否有
        if (allList != null && allList.size() > 0) {
            List<ModelPageCO> list = filterOpenFlagRecurise(allList, FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            if (list != null && list.size() > 0) {
                List<ModelTreeCO> treeCOList = new ArrayList<>();
                recuriseCopy(list, treeCOList);
                usableCache.put(TenantContext.getTenantNo() + ":" + categoryId + "_" + openFlag, treeCOList);
                return treeCOList;
            }
        }
        List<ModelTreeCO> treeCOList = modelDOMapper
            .treeByParentIdAndCategoryIdAndOpenFlag(FlexibleCommonConstant.COMMON_PARENT_ID, categoryId, openFlag);
        return treeCOList;
    }

    private void recuriseCopy(List<ModelPageCO> list, List<ModelTreeCO> treeCOList) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            ModelTreeCO modelTreeCO = new ModelTreeCO();
            modelTreeCO.setModelId(modelPageCO.getId());
            modelTreeCO.setParentId(modelPageCO.getParentId());
            modelTreeCO.setName(modelPageCO.getName());
            modelTreeCO.setEnglishName(modelPageCO.getEnglishName());
            modelTreeCO.setModelNo(modelPageCO.getModelNo());
            modelTreeCO.setCategoryId(modelPageCO.getCategoryId());
            modelTreeCO.setAtLastTrademark(modelPageCO.getAtLastTrademark());
            if (modelPageCO.getChildrenList() != null && modelPageCO.getChildrenList().size() > 0) {
                List<ModelTreeCO> sonList = new ArrayList<>();
                recuriseCopy(modelPageCO.getChildrenList(), sonList);
                modelTreeCO.setChildrenList(sonList);
            }
            treeCOList.add(modelTreeCO);
        }
    }

    @Cached(name = FlexibleKeyConstant.MODEL_TREE_ALL_PRE, key = "#categoryId")
    public List<ModelPageCO> treeAll(Integer categoryId) {
        List<ModelPageCO> list = modelDOMapper.treeByCondition(FlexibleCommonConstant.COMMON_PARENT_ID, categoryId);
        allCache.put(TenantContext.getTenantNo() + ":" + categoryId, list);
        return list;
    }

    // 异常产品类型全部树结构
    public void removeAll(Integer categoryId) {
        allCache.remove(TenantContext.getTenantNo() + ":" + categoryId);
        List<ModelPageCO> allList = treeAll(categoryId);
        // 判断全部树结构是否有
        if (allList != null && allList.size() > 0) {
            List<ModelPageCO> list = filterOpenFlagRecurise(allList, FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            if (list != null && list.size() > 0) {
                List<ModelTreeCO> treeCOList = new ArrayList<>();
                recuriseCopy(list, treeCOList);
                usableCache.put(
                    TenantContext.getTenantNo() + ":" + categoryId + "_" + FlexibleCommonConstant.COMMON_OPEN_FLAG_YES,
                    treeCOList);
            }
        }
    }

    private List<ModelPageCO> filterOpenFlagRecurise(List<ModelPageCO> list, Short openFlag) {
        if (openFlag == null) {
            return list;
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int x = 0; x < list.size(); x++) {
            ModelPageCO modelPageCO = list.get(x);
            List<ModelPageCO> childrenList = modelPageCO.getChildrenList();

            if (modelPageCO.getOpenFlag() - openFlag != 0) {
                // 不满足状态、删除
                list.remove(x);
                x--;
            } else {
                // 该分类id非父类id、查看子类列表是否存在
                if (childrenList != null && childrenList.size() > 0) {
                    // 过滤子类列表
                    childrenList = filterOpenFlagRecurise(childrenList, openFlag);
                    if (childrenList == null || childrenList.size() == 0) {
                        // 子类列表都不满足条件、剔除该节点
                        list.remove(x);
                        x--;
                    }
                }
            }

        }
        return list;
    }

}
