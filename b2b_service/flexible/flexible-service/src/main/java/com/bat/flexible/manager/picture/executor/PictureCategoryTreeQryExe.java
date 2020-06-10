package com.bat.flexible.manager.picture.executor;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.picture.PictureCategoryDOMapper;
import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.dao.picture.co.PictureCategorySimpleTreeCO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureCategoryTreeQryExe {

    @Autowired
    private PictureCategoryDOMapper pictureCategoryDOMapper;

    @CreateCache(name = FlexibleKeyConstant.PICTURE_CATEGORY_TREE_USABLE_PRE)
    private Cache<String, List<PictureCategorySimpleTreeCO>> treeUsableCache;

    @CreateCache(name = FlexibleKeyConstant.PICTURE_CATEGORY_TREE_ALL_PRE)
    private Cache<String, List<PictureCategoryPageCO>> treeAllCache;

    @Cached(name = FlexibleKeyConstant.PICTURE_CATEGORY_TREE_USABLE_PRE, key = "#openFlag")
    public List<PictureCategorySimpleTreeCO> treeSimple(Short openFlag) {
        List<PictureCategorySimpleTreeCO> treeCOList =
                pictureCategoryDOMapper.treeSimple(openFlag, FlexibleCommonConstant.COMMON_PARENT_ID);
        return treeCOList;
    }

    /**
     * 删除树
     *
     * @param openFlag
     */
    public void remove(Short openFlag) {
        treeUsableCache.remove(TenantContext.getTenantNo() + ":" + openFlag);
        // 重新加载
        treeSimple(openFlag);
    }

    @Cached(name = FlexibleKeyConstant.PICTURE_CATEGORY_TREE_ALL_PRE, key = "#delFlag")
    public List<PictureCategoryPageCO> treeAll(Short delFlag) {
        return pictureCategoryDOMapper.treeCOByParentId(FlexibleCommonConstant.COMMON_PARENT_ID);
    }

    /**
     * 删除树
     *
     * @param delFlag
     */
    public void removeAll(Short delFlag) {
        treeAllCache.remove(TenantContext.getTenantNo() + ":" + delFlag);
        // 重新加载
        treeAll(delFlag);
    }
}
