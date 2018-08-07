package com.bat.distributor.api.distributor.dto.data;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "一级分销商列表信息")
public class DistributorOneListDTO {

    private Integer id;
    @ApiModelProperty(value = "用户名(登录名)", example = "bat")
    private String name;
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人", example = "1")
    private Short companyType;
    @ApiModelProperty(value = "公司名", example = "bat")
    private String companyName;
    @ApiModelProperty(value = "来源类型 1.后台添加 2.前台注册申请 3.分销邀请", example = "1")
    private Short applyType;
    @ApiModelProperty(value = "资质申请状态 0未提交 1申请中 2申请通过 3申请失败", example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "资料审核状态 0未提交 1资料审核中  2资料审核通过  3资料审核失败", example = "1")
    private Short profileStatus;
    @ApiModelProperty(value = "申请时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    @ApiModelProperty(value = "申请审核时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "冻结时间", example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date freezeTime;
    @ApiModelProperty(value = "多级分销级数", example = "1")
    private Integer treeNode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "自动下推出库 1.是 0.否", example = "0")
    private Short autoDelivery;
}
