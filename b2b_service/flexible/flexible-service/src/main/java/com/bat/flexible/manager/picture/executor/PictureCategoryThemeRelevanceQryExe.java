package com.bat.flexible.manager.picture.executor;

import com.bat.flexible.dao.picture.PictureCategoryThemeRelevanceDOMapper;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryThemeRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PictureCategoryThemeRelevanceQryExe {

    @Autowired
    private PictureCategoryThemeRelevanceDOMapper pictureCategoryThemeRelevanceDOMapper;

    public List<PictureCategoryThemeRelevanceDO> listByDistributorIdsAndCategoryType(List<Integer> distributorIds, Short type) {
        return pictureCategoryThemeRelevanceDOMapper.listByDistributorIdsAndCategoryType(distributorIds,type);
    }
}
