package com.bat.distributor.api.distributor.dto.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "分销商信息")
public class DistributorDTO {

    private Integer id;
    @ApiModelProperty(value = "上级分销商用户名", required = true, example = "bat")
    private String upName;
    @ApiModelProperty(value = "用户名(登录名)", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "用户登录密码(HD5加密)", required = true, example = "bat")
    private String password;
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人", required = false, example = "1")
    private Short companyType;
    @ApiModelProperty(value = "公司名", required = false, example = "bat")
    private String companyName;
    @ApiModelProperty(value = "来源类型 1.后台添加 2.前台注册申请 3.分销邀请", required = true, example = "1")
    private Short applyType;
    @ApiModelProperty(value = "资质申请状态 0未提交 1申请中 2申请通过 3申请失败", required = false, example = "1")
    private Short applyStatus;
    @ApiModelProperty(value = "资料审核状态 0未提交 1资料审核中  2资料审核通过  3资料审核失败", required = false, example = "1")
    private Short profileStatus;
    @ApiModelProperty(value = "申请时间", required = false, example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    @ApiModelProperty(value = "申请审核时间", required = false, example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    @ApiModelProperty(value = "冻结状态 1,未冻结 2,冻结", required = false, example = "1")
    private Short freezeStatus;
    @ApiModelProperty(value = "冻结时间", required = false, example = "2018-05-09 14:00:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date freezeTime;
    @ApiModelProperty(value = "多级分销级数", required = true, example = "1")
    private Integer treeNode;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "销售区域id列表")
    private List<Integer> salesAreaIds;
    @ApiModelProperty(value = "分销商地址")
    private DistributorAddressDTO address;
    @ApiModelProperty(value = "分销商业务信息")
    private DistributorBusinessDTO business;
    @ApiModelProperty(value = "分销商价格等级信息列表")
    private List<DistributorOneScalePriceDTO> scalePrices;
    @ApiModelProperty(value = "分销商联系人信息列表")
    private List<DistributorContactsDTO> contacts;
    @ApiModelProperty(value = "分销商扩展信息")
    private DistributorExtendDataDTO extendData;
    @ApiModelProperty(value = "分销商财务信息")
    private DistributorFinancialDTO financial;
    @ApiModelProperty(value = "分销商特价商品信息")
    private List<DistributorSpecialGoodsDTO> specialGoods;

    @ApiModelProperty(value = "分账参数配置")
    private SubAccountAdminConfigCmd subAccountAdminConfigCmd;
}
