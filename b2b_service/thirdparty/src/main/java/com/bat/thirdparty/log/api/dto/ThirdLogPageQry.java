package com.bat.thirdparty.log.api.dto;

import com.bat.thirdparty.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class ThirdLogPageQry  extends BasePageParamQry {

    @ApiModelProperty(value = "调用结果 调用状态 1、成功 0、失败")
    private Short status;

    @ApiModelProperty(value = "日志类型 1、推送定制信息给第三方 2、接收第三方订单（基于ID）3、接收第三方订单（基于编码） 4、推送销售单给ERP " +
            "5、B2B推单给工厂 6、工厂发货 7、推送物流信息给第三方 8、第三方取消订单 9、工厂取消订单")
    @NotNull(message = "API_TYPE_NULL")
    private Short logType;

    @ApiModelProperty(value = "搜索类型： 1、单据编号 2、调用平台名称 3、请求内容 4、返回结果（响应）" )
    private Short searchType=1;

    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "搜索关键词 、只会根据searchType匹配")
    private String content;


    private List<String> platformList;
}
