package com.bat.distributor.api.distributor.dto;

import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "DistributorCmd", description = "分销商信息")
public class DistributorCmd {

    @ApiModelProperty(value = "分销商id（修改时必填）", required = false)
    private Integer id;
    @NotBlank(message = "P_DISTRIBUTOR_USER_NAME_NULL")
    @ApiModelProperty(value = "用户名(登录名)", required = true, example = "bat")
    private String name;
    @NotBlank(message = "P_DISTRIBUTOR_PASSWORD_NULL")
    @ApiModelProperty(value = "用户登录密码(HD5加密)", required = true, example = "bat")
    private String password;
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人", required = false, example = "1")
    private Short companyType;
    @ApiModelProperty(value = "公司名", required = false, example = "bat")
    private String companyName;
    @NotNull(message = "P_DISTRIBUTOR_APPLY_STATUS_NULL")
    @ApiModelProperty(value = "资质申请状态 0未提交 1申请中 2申请通过 3申请失败（如果是申请通过或后台添加分销商请传2））", required = false, example = "2")
    private Short applyStatus;

    @NotNull(message = "P_DISTRIBUTOR_SALES_AREA_NULL")
    @ApiModelProperty(value = "销售区域id列表", required = true)
    private List<Integer> salesAreaIds;
    @Valid
    @NotNull(message = "P_DISTRIBUTOR_ADDRESS_NULL")
    @ApiModelProperty(value = "分销商地址", required = true)
    private DistributorAddressCmd address;
    @Valid
    @NotNull(message = "P_DISTRIBUTOR_BUSINESS_NULL")
    @ApiModelProperty(value = "分销商业务信息", required = true)
    private DistributorBusinessCmd business;
    @Valid
    @ApiModelProperty(value = "分销商价格等级")
    private List<DistributorOneScalePriceCmd> scalePrices;
    @Valid
//    @NotNull(message = "P_DISTRIBUTOR_CONTACTS_NULL")
    @ApiModelProperty(value = "分销商联系人信息列表", required = true)
    private List<DistributorContactsCmd> contacts;
    @Valid
    @NotNull(message = "P_DISTRIBUTOR_EXTEND_DATA_NULL")
    @ApiModelProperty(value = "分销商扩展信息", required = true)
    private DistributorExtendDataCmd extendData;
    @Valid
    @NotNull(message = "P_DISTRIBUTOR_FINANCIAL_NULL")
    @ApiModelProperty(value = "分销商财务信息", required = true)
    private DistributorFinancialCmd financial;
    @Valid
    @ApiModelProperty(value = "分销商特价商品信息", required = true)
    private List<DistributorSpecialGoodsCmd> specialGoods;

    @ApiModelProperty(value = "运营后台分账设置")
    private SubAccountAdminConfigCmd subAccountAdminConfigCmd;

}
