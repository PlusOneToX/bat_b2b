package com.bat.flexible.manager.material.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.material.MaterialDOMapper;
import com.bat.flexible.dao.material.co.MaterialPageCO;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;

@Component
public class MaterialTreeQryExe {

    @Autowired
    private MaterialDOMapper materialDOMapper;

    @CreateCache(name = FlexibleKeyConstant.MATERIAL_TREE_USABLE_PRE)
    private Cache<String, List<MaterialTreeCO>> treeUsableCache;

    @CreateCache(name = FlexibleKeyConstant.MATERIAL_TREE_ALL_PRE)
    private Cache<String, List<MaterialPageCO>> treeAllCache;

    @Cached(name = FlexibleKeyConstant.MATERIAL_TREE_USABLE_PRE, key = "#categoryId")
    public List<MaterialTreeCO> treeUsableCOByCategoryId(Integer categoryId) {
        return materialDOMapper.treeUsableCOByParentIdAndCategoryId(FlexibleCommonConstant.COMMON_PARENT_ID,
            categoryId);
    }

    @Cached(name = FlexibleKeyConstant.MATERIAL_TREE_ALL_PRE, key = "#categoryId")
    public List<MaterialPageCO> treeAllByCategoryId(Integer categoryId) {
        // return
        // materialDOMapper.treeUsableCOByParentIdAndCategoryId(FlexibleCommonConstant.COMMON_PARENT_ID,categoryId);

        return materialDOMapper.treeByCondition(categoryId, FlexibleCommonConstant.COMMON_PARENT_ID);
    }

    public void remove(Integer categoryId) {
        treeUsableCache.remove(TenantContext.getTenantNo() + ":" + categoryId);
        treeAllCache.remove(TenantContext.getTenantNo() + ":" + categoryId);
        //空指针情况也删除
        treeUsableCache.remove(TenantContext.getTenantNo() + ":" + null);
        treeAllCache.remove(TenantContext.getTenantNo() + ":" + null);
        Map<Integer, MaterialDO> materialDOMap = new HashMap<>();
        List<MaterialDO> materialDOList = materialDOMapper.listAll();
        if (materialDOList != null && materialDOList.size() > 0) {
            materialDOMap =
                materialDOList.stream().collect(Collectors.toMap(MaterialDO::getId, materialDO -> materialDO));
        }
        List<MaterialPageCO> materialPageCOList =
            materialDOMapper.treeByCondition(categoryId, FlexibleCommonConstant.COMMON_PARENT_ID);
        treeAllCache.put(TenantContext.getTenantNo() + ":" + categoryId, materialPageCOList);
        if (materialPageCOList != null && materialDOList.size() > 0) {
            // 过滤禁用
            materialPageCOList =
                filterOpenFlagRecurise(materialPageCOList, FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
            if (materialPageCOList != null && materialPageCOList.size() > 0) {
                List<MaterialTreeCO> usableTreeCOList = new ArrayList<>();
                // 对象复制设置值
                recuriseCopy(materialPageCOList, materialDOMap, usableTreeCOList);
                treeUsableCache.put(TenantContext.getTenantNo() + ":" + categoryId, usableTreeCOList);
            }
        }
    }

    private void recuriseCopy(List<MaterialPageCO> materialPageCOList, Map<Integer, MaterialDO> materialDOMap,
        List<MaterialTreeCO> usableTreeCOList) {
        if (materialPageCOList == null || materialPageCOList.size() == 0) {
            return;
        }
        for (int x = 0; x < materialPageCOList.size(); x++) {
            MaterialPageCO materialPageCO = materialPageCOList.get(x);
            MaterialTreeCO materialTreeCO = new MaterialTreeCO();
            MaterialDO materialDO = materialDOMap.get(materialPageCO.getId());
            materialTreeCO.setMaterialId(materialPageCO.getId());
            materialTreeCO.setParentId(materialPageCO.getParentId());
            materialTreeCO.setName(materialPageCO.getName());
            materialTreeCO.setEnglishName(materialPageCO.getEnglishName());
            materialTreeCO.setMaterialNo(materialPageCO.getMaterialNo());
            materialTreeCO.setDescription(materialDO.getDescription());
            materialTreeCO.setImage(materialPageCO.getImage());
            materialTreeCO.setSubtitle(materialDO.getSubtitle());
            materialTreeCO.setColour(materialDO.getColour());
            materialTreeCO.setManufactor(materialDO.getManufactor());
            materialTreeCO.setItemId(materialDO.getItemId());
            materialTreeCO.setItemCode(materialDO.getItemCode());
            materialTreeCO.setAllowUploadFlag(materialDO.getAllowUploadFlag());
            materialTreeCO.setAtLastTrademark(materialDO.getAtLastTrademark());
            materialTreeCO.setMandatoryCoveredBleedFlag(materialDO.getMandatoryCoveredBleedFlag());
            if (materialPageCO.getChildrenList() != null && materialPageCO.getChildrenList().size() > 0) {
                List<MaterialTreeCO> sonList = new ArrayList<>();
                recuriseCopy(materialPageCO.getChildrenList(), materialDOMap, sonList);
                materialTreeCO.setChildrenList(sonList);
            }
            usableTreeCOList.add(materialTreeCO);
        }
    }

    public List<MaterialPageCO> filterOpenFlagRecurise(List<MaterialPageCO> materialPageCOList, Short openFlag) {
        if (openFlag == null) {
            return materialPageCOList;
        }
        if (materialPageCOList == null || materialPageCOList.size() == 0) {
            return null;
        }
        for (int x = 0; x < materialPageCOList.size(); x++) {
            MaterialPageCO materialPageCO = materialPageCOList.get(x);
            List<MaterialPageCO> childrenList = materialPageCO.getChildrenList();

            if (materialPageCO.getOpenFlag() - openFlag == 0) {
                // 按照状态 和类型来判断、都满足、不删除
                continue;
            }
            // 该分类id非父类id、查看子类列表是否存在
            if (childrenList == null || childrenList.size() == 0) {
                // 没有下一级
                materialPageCOList.remove(x);
                x--;
            } else {
                // 过滤子类列表
                childrenList = filterOpenFlagRecurise(childrenList, openFlag);
                if (childrenList == null || childrenList.size() == 0) {
                    // 子类列表都不满足条件、剔除该节点
                    materialPageCOList.remove(x);
                    x--;
                }
            }
        }
        return materialPageCOList;
    }
}
