package com.bat.system.api.trainingdownloadcenter.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/10 15:42
 */
@Data
@ApiModel(value = "DownloadCenterId", description = "训练中心id")
public class TrainingCenterId {
    @NotNull(message = "P_DOWNLOAD_CENTER_ID_NULL")
    @ApiModelProperty(value = "训练中心ID", required = true, example = "1")
    private Integer id;
}
