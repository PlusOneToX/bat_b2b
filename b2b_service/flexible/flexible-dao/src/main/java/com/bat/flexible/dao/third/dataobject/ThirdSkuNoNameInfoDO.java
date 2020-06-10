package com.bat.flexible.dao.third.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ThirdSkuNoNameInfoDO implements Serializable {
    private static final long serialVersionUID = -5287274406078745981L;

    private Integer id;

    private Integer distributorId;

    private String thirdNo;

    private String thirdExtName;

    private String thirdName;

    private Short type;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;


}