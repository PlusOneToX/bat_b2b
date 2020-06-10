package com.bat.flexible.api.exchange.dto;

import java.io.Serializable;
import java.util.List;

public class GoodsItemBoxRequest implements Serializable {

    private static final long serialVersionUID = 5201969941743467981L;

    private String itemCode;

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
}
