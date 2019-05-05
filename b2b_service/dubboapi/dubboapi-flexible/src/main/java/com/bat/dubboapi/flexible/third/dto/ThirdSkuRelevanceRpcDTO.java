package com.bat.dubboapi.flexible.third.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class ThirdSkuRelevanceRpcDTO implements Serializable {

    private Integer id;

    private Integer distributorId;

    private Integer materialId;

    private String materialName;

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
