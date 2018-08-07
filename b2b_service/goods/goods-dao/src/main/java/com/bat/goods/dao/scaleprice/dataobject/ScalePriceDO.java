package com.bat.goods.dao.scaleprice.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class ScalePriceDO {
    private Integer id;
    private String name;
    private String description;
    private Short retailFlag;
    private Short openFlag;
    private Integer sort;
    private String erpField;
    private Date createTime;
    private Date updateTime;
}
