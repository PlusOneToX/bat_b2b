package com.bat.dubboapi.flexible.exchange.dto;

import java.io.Serializable;
import java.util.List;

public class ItemBoxCodeRequest implements Serializable {

    private static final long serialVersionUID = 1311295969872368795L;
    /**
     * 物料编号 货品编号
     */
    private String itemCode;

    /**
     * 销售订单号
     */
    private String orderNo;
    /**
     * 盒码列表
     */
    private List<String> boxCodeList;


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public List<String> getBoxCodeList() {
        return boxCodeList;
    }

    public void setBoxCodeList(List<String> boxCodeList) {
        this.boxCodeList = boxCodeList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
