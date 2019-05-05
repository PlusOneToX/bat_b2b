package com.bat.thirdparty.quanyi.dao.dataobject;

import lombok.Data;

import java.util.Date;
@Data
public class ThirdQuanyiLogDO {

    private Integer id;

    private Integer thirdQuanyiId;

    private Short direction;

    private String url;

    private Short operatorUserType;

    private Integer operatorId;

    private String operatorName;

    private Short operateType;

    private String remark;

    private Date createTime;

    private String header;

    private String param;

    private String response;


}