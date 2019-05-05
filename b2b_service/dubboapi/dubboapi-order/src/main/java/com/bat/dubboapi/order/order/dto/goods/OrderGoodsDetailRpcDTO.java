package com.bat.dubboapi.order.order.dto.goods;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class OrderGoodsDetailRpcDTO implements Serializable {

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
