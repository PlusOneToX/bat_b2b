package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商登录信息")
public class UserLoginDTO {
    @ApiModelProperty(value = "分销商id", example = "78445")
    private Integer id;
    @ApiModelProperty(value = "登录用户名", example = "bat")
    private String userName;
    @ApiModelProperty(value = "登录密码", example = "43546657652342ggdf")
    private String password;
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人", example = "1")
    private Short companyType;
    @ApiModelProperty(value = "公司名", example = "bat")
    private String companyName;
    @ApiModelProperty(value = "分销商编码", example = "R8948")
    private String erpNo;
    @ApiModelProperty(value = "来源类型 1.后台添加 2.前台注册申请 3.分销邀请", example = "1")
    private Short applyType;
    @ApiModelProperty(value = "资质申请状态 0未提交 1申请中 2申请通过 3申请失败", example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "资料审核状态 0未提交 1资料审核中  2资料审核通过  3资料审核失败", example = "2")
    private Short profileStatus;
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "多级分销级数", example = "1")
    private Short treeNode;
    @ApiModelProperty(value = "自动下推出库 1.是 0.否", example = "1")
    private Short autoDelivery;
    @ApiModelProperty(value = "是否能导出报价 1.能 0.不能", example = "1")
    private Short canExportPrice;
    @ApiModelProperty(value = "参与活动 0-不参与活动 1-全部活动 2-指定活动类型", example = "1")
    private Short promotionScope;
    @ApiModelProperty(value = "可参与活动类型 1-营销活动 2-阶梯活动 3-拼团活动", example = "1")
    private String promotionTypes;
    @ApiModelProperty(value = "是否支持在途库存 1是 0否 默认是1 ", example = "1")
    private Short onWayFlag;
    @ApiModelProperty(value = "国家编号", example = "1")
    private Integer countryId;
    @ApiModelProperty(value = "货币类型 1-人民币 2-美元", example = "1")
    private Short coinType;
    @ApiModelProperty(value = "登录账号类型：1 分销商主账号 2 分销商联系人账号", example = "1")
    private Short accountType;
    @ApiModelProperty(value = "是否开启分销模式 0 不开启, 1 开启", example = "0")
    private Short distributionFlag;
    @ApiModelProperty(value = "分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)", example = "1")
    private Short distributionPromotionFlag;
    @ApiModelProperty(value = "平台显示语言", example = "en")
    private String language;
    @ApiModelProperty(value = "平台显示币种", example = "USD")
    private String currencyType;

    @ApiModelProperty(value = "登录类型 1-分销商账号登录 2-分销商联系人", example = "1")
    private Short loginType;
    @ApiModelProperty(value = "分销商联系人登录信息（当登录类型为2时有值）")
    private ContactDTO contact;

    @ApiModelProperty(value = "是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值、其他情况不要传）",required = false,example = "0")
    private Short subAccountFlag;

}
