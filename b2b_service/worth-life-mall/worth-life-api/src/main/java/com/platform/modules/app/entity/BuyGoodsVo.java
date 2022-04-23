package com.platform.modules.app.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuyGoodsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String skuId;
    private String goodsId;
    private Integer number;
    private String selectedText;
}
