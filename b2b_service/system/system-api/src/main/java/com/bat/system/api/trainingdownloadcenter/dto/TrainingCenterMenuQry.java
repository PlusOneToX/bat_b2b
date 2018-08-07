package com.bat.system.api.trainingdownloadcenter.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 16:19
 */
@Data
public class TrainingCenterMenuQry {
    @NotNull(message = "P_DOWNLOAD_CENTER_PARENT_ID_NULL")
    @ApiModelProperty(value = "训练中心父ID", required = true, example = "1")
    private Integer parentId;
}
