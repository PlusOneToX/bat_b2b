package com.bat.flexible.api.exchange.dto;


import java.util.List;

public class ExchangeBoxCodeOpenRequest  {

    private List<ExchangeBoxCodeRequest> boxCodeList;


    public List<ExchangeBoxCodeRequest> getBoxCodeList() {
        return boxCodeList;
    }

    public void setBoxCodeList(List<ExchangeBoxCodeRequest> boxCodeList) {
        this.boxCodeList = boxCodeList;
    }
}
