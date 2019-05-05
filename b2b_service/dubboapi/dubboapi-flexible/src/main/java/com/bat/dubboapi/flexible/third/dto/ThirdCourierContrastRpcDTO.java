package com.bat.dubboapi.flexible.third.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdCourierContrastRpcDTO implements Serializable {

    private static final long serialVersionUID = -8040944423598896028L;

    private Integer id;

    private Integer distributorId;

    private String distributionKdnCode;

    private String distributorShipperName;

    private String distributorShipperCode;

    private Short openFlag;

   
}