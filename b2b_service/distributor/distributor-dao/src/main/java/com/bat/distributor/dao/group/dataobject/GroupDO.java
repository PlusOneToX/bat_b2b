package com.bat.distributor.dao.group.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class GroupDO {
    private Integer id;

    private String name;

    private String description;

    private String erpGroupNo;

    private Short openFlag;

    private Date createTime;

    private Date updateTime;
}