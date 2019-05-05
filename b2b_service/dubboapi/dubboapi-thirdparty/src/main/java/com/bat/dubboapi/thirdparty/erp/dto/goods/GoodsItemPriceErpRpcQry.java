package com.bat.dubboapi.thirdparty.erp.dto.goods;

import java.io.Serializable;
import java.util.List;

/**
 * erp接口请求参数
 */
public class GoodsItemPriceErpRpcQry implements Serializable {
    /**
     * 货品erp内码
     */
    private List<Integer> itemErpIds;
    /**
     * 价格等级erp对应列表
     */
    private List<ErpPriceFieldListRpcQry> erpPriceFields;

    public List<Integer> getItemErpIds() {
        return itemErpIds;
    }

    public void setItemErpIds(List<Integer> itemErpIds) {
        this.itemErpIds = itemErpIds;
    }

    public List<ErpPriceFieldListRpcQry> getErpPriceFields() {
        return erpPriceFields;
    }

    public void setErpPriceFields(List<ErpPriceFieldListRpcQry> erpPriceFields) {
        this.erpPriceFields = erpPriceFields;
    }
}
