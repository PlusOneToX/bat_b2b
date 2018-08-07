package com.bat.distributor.api.distributor.dto;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistributorListQry", description = "分销商列表查询(所有)")
public class DistributorListQry {
    @ApiModelProperty(value = "查询内容，分销商登录名/客户名/分销商ID/分销商ERP编码，名称支持模糊查询", required = false, example = "bat")
    private String content;
    @ApiModelProperty(value = "查询内容类型，1 分销商登录名 2 客户名 3 分销商ID 4 分销商ERP编码，(查询内容不为空是必填)", required = false,
        example = "bat")
    private Short contentType;
    @ApiModelProperty(value = "来源类型 1.后台添加 2.前台注册申请 3.分销邀请", required = false, example = "1")
    private Short applyType;
    @ApiModelProperty(value = "资质申请状态 0未提交 1申请中 2申请通过 3申请失败", required = false, example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "资料审核状态 0未提交 1资料审核中  2资料审核通过  3资料审核失败", required = false, example = "1")
    private Short profileStatus;
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", required = false, example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "信息是否同步到erp 1.是 0.否", required = false, example = "1")
    private Short erpFlag;
    @ApiModelProperty(value = "自动下推出库 1.是 0.否", required = false, example = "0")
    private Short autoDelivery;
    @ApiModelProperty(value = "多级分销级数", required = false, example = "1")
    private Integer treeNode;
    @ApiModelProperty(value = "是否启用柔性店铺开关，1启用 0 不启用")
    private Short rxShopSwitch;

    @NotNull(message = "P_DISTRIBUTOR_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_DISTRIBUTOR_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
