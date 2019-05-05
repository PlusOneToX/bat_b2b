package com.bat.thirdparty.quanyi.dao.co;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ThirdQuanyiLogPageCO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "对应的权益id")
    private Integer thirdQuanyiId;

    @ApiModelProperty(value = "请求方向 1第三方访问-本系统 2.本系统访问第三方")
    private Short direction;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "操作人分类 1、苏宁 2、b2b系统 3、系统管理员 4、c端用户")
    private Short operatorUserType;

    @ApiModelProperty(value = "操作人id")
    private Integer operatorId;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作类型 1 苏宁传输订单  2 系统对苏宁订单进行派工 3 c端客户领取权益码 4 系统对苏宁订单进行签到")
    private Short operateType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "请求头")
    private String header;

    @ApiModelProperty(value = "请求参数")
    private String param;

    @ApiModelProperty(value = "响应")
    private String response;

}
