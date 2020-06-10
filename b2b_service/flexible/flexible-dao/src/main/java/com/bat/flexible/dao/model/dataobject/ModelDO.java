package com.bat.flexible.dao.model.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class ModelDO implements Serializable {
    private static final long serialVersionUID = 8898031562247338275L;
    private Integer id;

    private String name;

    private String englishName;

    private Integer categoryId;

    private Integer parentId;

    private Integer sequence;

    private String image;

    private Short openFlag;

    private Short delFlag;

    private Short atLastTrademark;

    private String networkModel;

    private String modelNo;

    private Date createTime;

    private Integer createUserId;

    private String createUserName;

    private Date updateTime;

    private Integer updateUserId;

    private String updateUserName;

    private String depict;


}