package com.bat.dubboapi.order.order.dto;

import java.io.Serializable;
import java.util.List;

public class ThirdCodeOrderCmd implements Serializable {

    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
