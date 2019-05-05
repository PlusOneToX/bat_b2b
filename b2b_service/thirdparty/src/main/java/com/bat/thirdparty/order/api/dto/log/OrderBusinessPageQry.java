package com.bat.thirdparty.order.api.dto.log;

import com.bat.thirdparty.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class OrderBusinessPageQry extends BasePageParamQry {

    @ApiModelProperty(value = "调用状态 1、成功 0、失败")
    private Short status;

    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "1、渠道订单号 2、收货人 3、手机号")
    private Short searchType=1;

    @ApiModelProperty(value = "搜索关键词")
    private String content;

    @ApiModelProperty(value = "平台来源")
    private String platform;

    private String otherOrderNo;

}
