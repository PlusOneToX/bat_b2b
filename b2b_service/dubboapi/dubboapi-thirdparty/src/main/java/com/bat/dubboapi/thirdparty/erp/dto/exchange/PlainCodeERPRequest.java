package com.bat.dubboapi.thirdparty.erp.dto.exchange;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlainCodeERPRequest implements Serializable {

    private static final long serialVersionUID = -3311732802666918323L;
    //明码
    private String F_MCode;

    /**
     * 材质明码排序号
     */
    private Integer sortNo;



}
