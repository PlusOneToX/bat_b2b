package com.bat.dubboapi.warehouse.warehouse.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WarehouseDTORpcQry implements Serializable {


    private static final long serialVersionUID = -1095839220273003424L;

    /**
     * 仓库id
     */
    private Integer id;

    /**
     * 仓库名称
     */
    private String name;

    private String warehouseNo;

    private Integer areaId;

    private Short syncType;

    private Short isPlatform;

    private Short openFlag;

    private Short delFlag;

    private Short calculationType;




}