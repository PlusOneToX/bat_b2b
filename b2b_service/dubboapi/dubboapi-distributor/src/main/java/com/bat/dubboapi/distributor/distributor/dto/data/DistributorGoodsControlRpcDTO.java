package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorGoodsControlRpcDTO implements Serializable {
    private List<Integer> brandIds;
    private List<Integer> categoryIds;
    private List<Integer> goodsIds;
    private List<Integer> noBrandIds;
    private List<Integer> noCategoryIds;
    private List<Integer> noGoodsIds;
    private List<Integer> allVisibleProducts;
    private List<Integer> allVisibleBrands;

    public List<Integer> getAllVisibleBrands() {
        return allVisibleBrands;
    }

    public void setAllVisibleBrands(List<Integer> allVisibleBrands) {
        this.allVisibleBrands = allVisibleBrands;
    }

    public List<Integer> getAllVisibleProducts() {
        return allVisibleProducts;
    }

    public void setAllVisibleProducts(List<Integer> allVisibleProducts) {
        this.allVisibleProducts = allVisibleProducts;
    }

    public List<Integer> getNoBrandIds() {
        return noBrandIds;
    }

    public void setNoBrandIds(List<Integer> noBrandIds) {
        this.noBrandIds = noBrandIds;
    }

    public List<Integer> getNoCategoryIds() {
        return noCategoryIds;
    }

    public void setNoCategoryIds(List<Integer> noCategoryIds) {
        this.noCategoryIds = noCategoryIds;
    }

    public List<Integer> getNoGoodsIds() {
        return noGoodsIds;
    }

    public void setNoGoodsIds(List<Integer> noGoodsIds) {
        this.noGoodsIds = noGoodsIds;
    }

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

    public List<Integer> getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(List<Integer> goodsIds) {
        this.goodsIds = goodsIds;
    }
}
