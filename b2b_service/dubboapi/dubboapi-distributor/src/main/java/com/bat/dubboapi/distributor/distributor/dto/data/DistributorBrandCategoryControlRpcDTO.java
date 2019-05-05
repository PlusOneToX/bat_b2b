package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorBrandCategoryControlRpcDTO implements Serializable {
    private List<Integer> brandIds;
    private List<Integer> categoryIds;

    public List<Integer> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<Integer> brandIds) {
        this.brandIds = brandIds;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
