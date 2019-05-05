package com.bat.dubboapi.flexible.exchange.dto;

import java.io.Serializable;
import java.util.List;

public class ExchangeBoxPlainCodeDTO implements Serializable {

    private static final long serialVersionUID = -8793845398500128747L;
    /**
     * 盒码
     */
    private String boxCode;

    /**
     * 明码列表、空表示整盒退
     */
    private List<String> plainCodeList;

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public List<String> getPlainCodeList() {
        return plainCodeList;
    }

    public void setPlainCodeList(List<String> plainCodeList) {
        this.plainCodeList = plainCodeList;
    }
}
