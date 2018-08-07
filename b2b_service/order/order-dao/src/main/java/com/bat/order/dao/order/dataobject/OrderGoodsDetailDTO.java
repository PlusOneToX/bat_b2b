package com.bat.order.dao.order.dataobject;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderGoodsDetailDTO {

    private Integer id;
    private Integer orderId;
    private Integer goodsId;
    private Integer itemId;
    private String itemName;
    private Integer itemCount;

    private String generateImage;
    private String previewImage;

    private BigDecimal actualPrice;
}
