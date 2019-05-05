package com.bat.dubboapi.order.type.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderTypeRpcQryDTO implements Serializable {
    private static final long serialVersionUID = -8791506917406605208L;
    private Integer id;

    private String name;

    private String erpType;

    private Short specialFlag;
    private Integer warehouseId;

    private String desc;

    private Short editable;

    private Short openFlag;


}