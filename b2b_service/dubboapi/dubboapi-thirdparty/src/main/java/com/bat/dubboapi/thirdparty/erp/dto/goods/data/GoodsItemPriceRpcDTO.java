package com.bat.dubboapi.thirdparty.erp.dto.goods.data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GoodsItemPriceRpcDTO implements Serializable {
    /**
     * 货品erp内码
     */
    private Integer itemErpId;
    /**
     * 默认销售价
     */
    private BigDecimal salePrice = new BigDecimal(0);
    /**
     * 成本价
     */
    private BigDecimal costPrice = new BigDecimal(0);
    /**
     * 货品等级价格列表（按顺序）
     */
    private List<ErpPriceFieldRpcDTO> priceList;

    public Integer getItemErpId() {
        return itemErpId;
    }

    public void setItemErpId(Integer itemErpId) {
        this.itemErpId = itemErpId;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public List<ErpPriceFieldRpcDTO> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<ErpPriceFieldRpcDTO> priceList) {
        this.priceList = priceList;
    }
}
