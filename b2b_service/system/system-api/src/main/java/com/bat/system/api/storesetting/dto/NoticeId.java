package com.bat.system.api.storesetting.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 18:44
 */
@Data
@ApiModel(value = "NoticeId", description = "公告id")
public class NoticeId {
    @NotNull(message = "P_NOTICE_ID_NULL")
    @ApiModelProperty(value = "公告id", required = true, example = "1")
    private Integer id;

}
