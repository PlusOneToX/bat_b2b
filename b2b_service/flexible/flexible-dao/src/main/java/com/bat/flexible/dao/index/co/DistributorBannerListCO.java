package com.bat.flexible.dao.index.co;

import lombok.Data;

@Data
public class DistributorBannerListCO {



    private Integer id;

    private String bannerUrl;

    /**
     * 运营类型 1、首页IP系列 2、活动页链接
     */
    private Short type;

    /**
     * 首页系列id
     */
    private Integer seriesId;

    /**
     * 外部链接
     */
    private String externalLink;


}
