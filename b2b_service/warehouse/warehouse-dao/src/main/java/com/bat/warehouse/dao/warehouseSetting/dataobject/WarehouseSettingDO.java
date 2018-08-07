package com.bat.warehouse.dao.warehouseSetting.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class WarehouseSettingDO {
    private Integer id;

    private Short type;

    private Integer value;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;


}