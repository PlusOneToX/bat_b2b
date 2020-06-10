package com.bat.flexible.manager.picture.executor;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.dao.picture.PictureCategoryDOMapper;
import com.bat.flexible.dao.picture.co.PictureCategoryTreeCO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.redis.FlexibleKeyConstant;
import com.bat.flexible.manager.common.utils.code.NumUtils;

@Component
public class PictureCatergoryCmdExe {

    @Autowired
    private PictureCategoryDOMapper pictureCategoryDOMapper;

    /**
     * 单个对象
     */
    @CreateCache(name = FlexibleKeyConstant.PICTURE_CATEGORY_DO_PRE)
    private Cache<String, PictureCategoryDO> simpleCache;

    @Autowired
    private PictureCategoryTreeQryExe pictureCategoryTreeQryExe;

    /**
     * 把这个父分类下面的所有子类都要下移
     * 
     * @param parentId
     * @param sequenceAdd
     * @param sequenceStart
     */
    public void updateSequenceAddByParentId(Integer parentId, BigDecimal sequenceAdd, BigDecimal sequenceStart) {
        List<PictureCategoryTreeCO> list = pictureCategoryDOMapper.treeByParentId(parentId);
        if (list == null || list.size() == 0) {
            return;
        }
        System.out.println(JSON.toJSONString(list));
        Map<Integer, BigDecimal> map = new HashMap<>();
        recursive(list, map);
        // 递归运算
        pictureCategoryDOMapper.updateSequenceAddByParentId(parentId, sequenceAdd, sequenceStart);
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<Integer, BigDecimal>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Integer, BigDecimal> entry = iterator.next();
                Integer parent_id = entry.getKey();
                BigDecimal sequence = entry.getValue();
                Integer length = NumUtils.getDecimalsLength(sequence);
                BigDecimal sonSequenceAdd = BigDecimal.ONE;

                if (length > 0) {
                    String str = "1.";
                    if (NumUtils.getDecimalsLength(sequenceAdd) > 0) {
                        str = "0.";
                    }
                    if (length > 2) {
                        for (int x = 0; x < length - 2; x++) {
                            str = str + "0";
                        }
                    }
                    str = str + "1";
                    sonSequenceAdd = new BigDecimal(str);
                }
                pictureCategoryDOMapper.updateSequenceAddByParentId(parent_id,
                    sequenceAdd.compareTo(BigDecimal.ZERO) == 1 ? sonSequenceAdd : sonSequenceAdd.negate(),
                    BigDecimal.ZERO);
            }
        }
    }

    public void create(PictureCategoryDO pictureCategoryDO) {
        pictureCategoryDOMapper.insert(pictureCategoryDO);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(pictureCategoryDO.getOpenFlag())) {
            // 可用
            pictureCategoryTreeQryExe.remove(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        }
        pictureCategoryTreeQryExe.removeAll(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        simpleCache.put(TenantContext.getTenantNo() + ":" + pictureCategoryDO.getId(), pictureCategoryDO);
    }

    public void update(PictureCategoryDO pictureCategoryDO) {
        pictureCategoryDOMapper.updateByPrimaryKey(pictureCategoryDO);
        simpleCache.remove(TenantContext.getTenantNo() + ":" + pictureCategoryDO.getId());
        pictureCategoryTreeQryExe.remove(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        pictureCategoryTreeQryExe.removeAll(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
    }

    public void recursive(List<PictureCategoryTreeCO> list, Map<Integer, BigDecimal> map) {
        if (list == null || list.size() == 0) {
            return;
        }
        List<PictureCategoryTreeCO> allList = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            List<PictureCategoryTreeCO> childrenList = list.get(x).getChildrenList();
            if (childrenList != null && childrenList.size() > 0) {
                if (map == null) {
                    map = new HashMap<>();
                }
                map.put(list.get(x).getId(), list.get(x).getSequence());
                allList.addAll(childrenList);
            }
        }
        if (allList != null && allList.size() > 0) {
            // 递归
            recursive(allList, map);
        } else {
            return;
        }
    }

    public static void main(String[] args) {
        BigDecimal aa = new BigDecimal("1");
        System.out.println(JSON.toJSONString(aa));
        aa = new BigDecimal("1.1");
        System.out.println(JSON.toJSONString(aa));
    }
}
