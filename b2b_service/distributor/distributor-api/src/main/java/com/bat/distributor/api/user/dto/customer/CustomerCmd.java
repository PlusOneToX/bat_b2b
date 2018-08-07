package com.bat.distributor.api.user.dto.customer;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 9:01
 */
@Data
@ApiModel(description = "C端客户信息修改")
public class CustomerCmd {
    @NotNull(message = "P_DISTRIBUTOR_CUSTOMER_ID_NULL")
    @ApiModelProperty(value = "客户id", required = true, example = "123444")
    private Integer id;
    @ApiModelProperty(value = "客户手机号", required = false, example = "18650811111")
    private String phone;
    @ApiModelProperty(value = "客户名称", required = false, example = "bat")
    private String name;
    @ApiModelProperty(value = "客户昵称", required = false, example = "bat")
    private String nikeName;
    @ApiModelProperty(value = "性别 1为男性，2为女性", required = false, example = "1")
    private Short sex;
    @ApiModelProperty(value = "客户生日", required = false, example = "2018-05-09")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    @ApiModelProperty(value = "头像", required = false, example = "http://bat")
    private String headPortrait;

}
