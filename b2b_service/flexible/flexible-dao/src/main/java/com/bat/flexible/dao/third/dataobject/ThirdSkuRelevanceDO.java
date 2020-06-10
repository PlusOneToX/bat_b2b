package com.bat.flexible.dao.third.dataobject;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ThirdSkuRelevanceDO implements Serializable {
    private static final long serialVersionUID = -1328159173105909170L;
    private Integer id;

    private Integer distributorId;

    private Integer materialId;

    private Integer modelId;

    private String thirdMaterialCategoryNo;

    private String thirdBrandNo;

    private String thirdModelNo;

    private String thirdMaterialNo;

    private String thirdColourNo;

    private String thirdSkuNo;

    private Short openFlag;

    private Integer createUserId;

    private String createUserName;

    private Date createTime;

    private Integer updateUserId;

    private String updateUserName;

    private Date updateTime;




}