package com.bat.thirdparty.msgcenter.api.dto;

import com.bat.thirdparty.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class MsgCenterQry extends BasePageParamQry {
    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "编号")
    private Integer id;

    @ApiModelProperty(value = "消息渠道 1.B2b 2.兑换商城 3.定制商城")
    private Short channel;

}
