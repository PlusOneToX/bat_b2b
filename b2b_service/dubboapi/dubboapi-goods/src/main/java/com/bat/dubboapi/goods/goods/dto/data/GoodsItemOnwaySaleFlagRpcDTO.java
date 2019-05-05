package com.bat.dubboapi.goods.goods.dto.data;

import java.io.Serializable;

public class GoodsItemOnwaySaleFlagRpcDTO implements Serializable {
    private Integer id;
    /**
     * 直发客户是否支持在途：0-否 1-是
     */
    private Short onwaySaleFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getOnwaySaleFlag() {
        return onwaySaleFlag;
    }

    public void setOnwaySaleFlag(Short onwaySaleFlag) {
        this.onwaySaleFlag = onwaySaleFlag;
    }
}
