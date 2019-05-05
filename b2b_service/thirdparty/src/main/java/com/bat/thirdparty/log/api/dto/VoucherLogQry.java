package com.bat.thirdparty.log.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "收款单日志查询")
public class VoucherLogQry {

    @ApiModelProperty(value = "3:操作来源")
    private String operateSource;

    @ApiModelProperty(value = "4:操作人名称")
    private String operator;

    @ApiModelProperty(value = "5:操作类型")
    private String operateType;

    @ApiModelProperty(value = "6:操作说明")
    private String operateDes;

    @ApiModelProperty(value = "7操作数据")
    private String operateData;

    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @NotNull(message = "P_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    protected Integer size;

    @NotNull(message = "P_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    protected Integer page;

    @ApiModelProperty(value = "查询类型", example = "1")
    protected Short contentType;

    @ApiModelProperty(value = "查询内容", example = "1")
    protected String content;

    @ApiModelProperty(value = "收款单id")
    private Integer voucherId;

    @ApiModelProperty(value = "订单id")
    private Integer orderId;

}
