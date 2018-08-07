package com.bat.distributor.api.user.dto.user.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "下级分销商列表信息")
public class UserNextListDTO {
    @ApiModelProperty(value = "分销商id(编号)", example = "78445")
    private Integer id;
    @ApiModelProperty(value = "用户名", example = "bat")
    private String name;
    @ApiModelProperty(value = "公司名（商户名称）", example = "bat")
    private String companyName;
    @ApiModelProperty(value = "联系人名称", example = "bat")
    private String userName;
    @ApiModelProperty(value = "联系手机号", example = "18650811234")
    private String phone;
    @ApiModelProperty(value = "价格等级", example = "一级价")
    private String scalePriceName;
    @ApiModelProperty(value = "申请状态 1申请中 2申请通过 3申请失败", example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "申请时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

}
