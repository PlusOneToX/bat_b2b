package com.bat.dubboapi.distributor.distributor.dto.data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/18 15:12
 */
public class DistributorCustomerPriceDTO implements Serializable {

    private Integer ItemId;

    private BigDecimal price;


    public Integer getItemId() {
        return ItemId;
    }

    public void setItemId(Integer itemId) {
        ItemId = itemId;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
