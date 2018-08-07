package com.bat.financial.api.subaccount.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrderSubAccountPageQry {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "分账状态（前台展示的、与后台不一致、需要加入是否分账失败）0、不分账 1、待分账 2、部分分账 3、全部分账 4、分账失败 ")
    private Short status;

    @ApiModelProperty(value = "开始时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2018-02-23 05:39:38")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @NotNull(message = "P_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    protected Integer size;

    @NotNull(message = "P_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    protected Integer page;

    @ApiModelProperty(value = "分销商ID")
    private Integer distributorId;

    @ApiModelProperty(value = "查询类型 1、分销商用户名 2、店铺名称 ", example = "1")
    protected Short contentType;

    @ApiModelProperty(value = "查询内容", example = "1")
    protected String content;

    @ApiModelProperty(value = "是否存在分账失败 1、是 0、否")
    private Short subAccountFailFlag;

}
