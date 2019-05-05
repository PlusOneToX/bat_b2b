package com.bat.promotion.api.promotion.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/12/2 14:58
 */
@Data
@ApiModel(description = "活动导出接口")
public class PromotionExportQry {
    @ApiModelProperty(value = "促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束(多个中间用','号隔开)", required = false)
    private Short promoStatus;
    @ApiModelProperty(value = "活动开始时间", required = false, example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "活动结束时间", required = false, example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "活动来源，1 后台新增, 2 批量导入", required = false, example = "2")
    private Short promoSource;
    @ApiModelProperty(value = "活动类型，1 普通活动，2 阶梯活动", required = false, example = "2")
    private Short promoType;
}
