package com.bat.system.api.logistics.dto.data;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/17 12:08
 */
@Data
@ApiModel(value = "LogisticsAreaDTO")
public class LogisticsAreaDTO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "物流id")
    private Integer logisticsId;

    @ApiModelProperty(value = "首重重量费用")
    private Double firstWeightCost;

    @ApiModelProperty(value = "首重体积费用")
    private Double firstVolumeCost;

    @ApiModelProperty(value = "续重重量费用")
    private Double additionalWeightCost;

    @ApiModelProperty(value = "续重体积费用")
    private Double additionalVolumeCost;

    @ApiModelProperty(value = "是否使用默认 0为否 1为是")
    private Short defaultFlag;

    @ApiModelProperty(value = "是否使用公式, 0为否，1为是")
    private Short formulaFlag;

    @ApiModelProperty(value = "公式")
    private String formula;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "相同配送配置分组编码")
    private String groupId;

    @ApiModelProperty(value = "id")
    private List<LogisticsAreaDetailDTO> areaList;
}
