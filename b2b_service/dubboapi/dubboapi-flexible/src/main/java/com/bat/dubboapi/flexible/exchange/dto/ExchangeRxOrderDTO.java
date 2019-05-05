package com.bat.dubboapi.flexible.exchange.dto;

import java.io.Serializable;
import java.util.List;


public class ExchangeRxOrderDTO implements Serializable {


    private static final long serialVersionUID = -468445542483031338L;
    private List<ExchangeCodeOrderDTO> itemList;

    public List<ExchangeCodeOrderDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ExchangeCodeOrderDTO> itemList) {
        this.itemList = itemList;
    }

    public ExchangeRxOrderDTO() {
        super();
    }
}
