package com.bat.dubboapi.thirdparty.erp.dto.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderGoodsDetailDTO implements Serializable {

    private static final long serialVersionUID = -6913062962471482819L;

    private String itemCode;


    private Integer count;
}
