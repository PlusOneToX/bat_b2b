package com.bat.flexible.dao.index.co;


import lombok.Data;

@Data
public class DistributorBannerPageCO {


    private Integer id;


    private String distributorName;


    private String companyName;

    /**
     * 状态
     */
    private Short status;
    /**
     * banner图片路径
     */
    private String bannerUrl;


    private Long startTime;

    private Long endTime;

    /**
     * 运营位置类型 1、首页主banner
     */
    private Short showLocation;

    /**
     * 备注
     */
    private String remark;


}
