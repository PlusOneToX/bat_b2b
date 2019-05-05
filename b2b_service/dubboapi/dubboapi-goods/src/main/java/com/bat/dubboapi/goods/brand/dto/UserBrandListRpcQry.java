package com.bat.dubboapi.goods.brand.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 19:49
 */
public class UserBrandListRpcQry implements Serializable {
    /**
     * 可视品牌ids（包括停用的，注意：不包括全部分销商可视的品牌id）
     */
    private List<Integer> brandIds;

    public List<Integer> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<Integer> brandIds) {
        this.brandIds = brandIds;
    }
}
