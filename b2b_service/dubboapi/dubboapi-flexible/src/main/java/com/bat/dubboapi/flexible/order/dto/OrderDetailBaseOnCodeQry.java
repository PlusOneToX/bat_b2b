package com.bat.dubboapi.flexible.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDetailBaseOnCodeQry implements Serializable {
    private String image;
    private String generateImage;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    private String materialNo;
    private String modelNo;
    //图片id默认为0
    private String pictureNo;


}
