package com.bat.warehouse.dao.warehouse.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WarehouseDO implements Serializable {
    private static final long serialVersionUID = -5408921723733223423L;

    private Integer id;

    private String name;

    private String warehouseNo;

    private Integer areaId;

    private Short syncType;

    private Short isPlatform;

    private Short openFlag;

    private Short delFlag;

    private Short calculationType;

    private Integer sortNo;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date deleteTime;

    private String remark;


}