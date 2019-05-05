package com.bat.dubboapi.goods.goods.dto.data;

import java.io.Serializable;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2019/5/26 18:16
 */
public class GoodsAreVisibleRpcDTO implements Serializable {

    // 商品id
    private Integer goodsId;

    // 分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组
    private Integer distributorScope;

    // 品牌id
    private Integer brandId;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getDistributorScope() {
        return distributorScope;
    }

    public void setDistributorScope(Integer distributorScope) {
        this.distributorScope = distributorScope;
    }
}
