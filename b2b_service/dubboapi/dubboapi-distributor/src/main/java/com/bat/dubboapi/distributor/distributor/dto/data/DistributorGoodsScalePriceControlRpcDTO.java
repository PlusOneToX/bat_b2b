package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/8 8:48
 */
public class DistributorGoodsScalePriceControlRpcDTO implements Serializable {

    private Integer treeNode;
    private List<OneScalePriceRpcDTO> oneScalePrices;
    private List<NextScalePriceRpcDTO> nextScalePrices;
    private List<DistributorSpecialGoodsRpcDTO> specialPrices;

    public Integer getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(Integer treeNode) {
        this.treeNode = treeNode;
    }

    public List<OneScalePriceRpcDTO> getOneScalePrices() {
        return oneScalePrices;
    }

    public void setOneScalePrices(List<OneScalePriceRpcDTO> oneScalePrices) {
        this.oneScalePrices = oneScalePrices;
    }

    public List<NextScalePriceRpcDTO> getNextScalePrices() {
        return nextScalePrices;
    }

    public void setNextScalePrices(List<NextScalePriceRpcDTO> nextScalePrices) {
        this.nextScalePrices = nextScalePrices;
    }

    public List<DistributorSpecialGoodsRpcDTO> getSpecialPrices() {
        return specialPrices;
    }

    public void setSpecialPrices(List<DistributorSpecialGoodsRpcDTO> specialPrices) {
        this.specialPrices = specialPrices;
    }
}
