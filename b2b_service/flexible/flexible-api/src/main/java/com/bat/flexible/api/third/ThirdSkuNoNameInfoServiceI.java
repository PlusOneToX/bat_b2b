package com.bat.flexible.api.third;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.dao.third.dataobject.ThirdSkuNoNameInfoDO;

import java.util.List;
import java.util.Map;

public interface ThirdSkuNoNameInfoServiceI {
    List<ThirdSkuNoNameInfoDO> listByDistributorId(Integer distributorId);

    void deleteByDistributorId(Integer distributorId);

    void add(Map<String, String> materialCategoryMap, Map<String, String> brandMap, Map<String, String> thirdModelMap, Map<String, String> thirdMaterialMap, Map<String, String> colorMap, AdminResponse currentAdmin, Integer distributorId);

    void create(ThirdSkuNoNameInfoDO thirdSkuNoNameInfo);
}
