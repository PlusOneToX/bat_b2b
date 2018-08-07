package com.bat.distributor.dao.customer.dataobject;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerDO {
    private Integer id;

    private Integer distributorId;

    private String no;

    private String phone;

    private String password;

    private String name;

    private String nikeName;

    private Short sex;

    private String platform;

    private Date birthday;

    private String headPortrait;

    private Short agreementFlag;

    private Short status;

    private Short customerType;

    private Integer parentId;

    private Date createTime;

    private Date updateTime;

}