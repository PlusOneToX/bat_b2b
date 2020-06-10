package com.bat.flexible.api.index.dto.banner;


import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class DistributorBannerDTO {

    private Integer id;



    /**
     * banner图片路径
     */
    @NotBlank(message = "图片不能为空")
    private String bannerUrl;

    @NotNull(message = "开始时间不能为空")
    private Long startTime;

    @NotNull(message = "结束时间不能为空")
    private Long endTime;

    /**
     * 运营位置类型 1、首页主banner
     */
    @NotNull(message = "运营位置不能为空")
    private Short showLocation;

    /**
     * 备注
     */
    private String remark;

    /**
     * 运营类型 1、首页IP系列 2、活动页链接
     */
    @NotNull(message = "运营类型不能为空")
    private Short type;

    /**
     * 主题系列id
     */
    private Integer seriesId;

    /**
     * 外部链接
     */
    private String externalLink;


    private Set<Integer> pictureIdList;

    @NotEmpty(message = "必须要选中分销商")
    private List<DistributorSimpleRelaQry> distributorList;



}
