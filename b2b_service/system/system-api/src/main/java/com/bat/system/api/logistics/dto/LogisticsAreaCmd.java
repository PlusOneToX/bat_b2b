package com.bat.system.api.logistics.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.bat.system.api.logistics.dto.data.LogisticsAreaDetailDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/19 14:40
 */
@Data
@ApiModel(value = "LogisticsCostUpdateCmd", description = "地区计费更新")
public class LogisticsAreaCmd {

    @ApiModelProperty(value = "id", example = "1")
    private Integer id;

    @NotNull(message = "P_LOGISTICS_FORMULA_FLAG_NULL")
    @ApiModelProperty(value = "是否使用公式, 0为否，1为是", required = true, example = "1")
    protected Short formulaFlag;

    @ApiModelProperty(value = "公式", example = "{{1000-p}-0.6}*(4+[(w-1000)/1000]*1.5)")
    protected String formula;

    @ApiModelProperty(value = "首重重量费用", example = "1")
    protected Double firstWeightCost;

    @ApiModelProperty(value = "首重体积费用", example = "1")
    protected Double firstVolumeCost;

    @ApiModelProperty(value = "续重体积费用", example = "1")
    protected Double additionalWeightCost;

    @ApiModelProperty(value = "续重体积费用", example = "1")
    protected Double additionalVolumeCost;

    @ApiModelProperty(value = "是否使用默认 0为否 1为是", required = true, example = "1")
    private Short defaultFlag;

    private List<LogisticsAreaDetailDTO> areaList;
}
