package com.bat.distributor.api.user.dto.user.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分销商个人信息")
public class UserInfoDTO {
    @ApiModelProperty(value = "分销商id", example = "78445")
    private Integer id;
    @ApiModelProperty(value = "用户名", example = "bat")
    private String name;
    @ApiModelProperty(value = "公司名", example = "bat")
    private String companyName;
    @ApiModelProperty(value = "账户操作人", example = "bat")
    private String userName;
    @ApiModelProperty(value = "账户操作人联系方式", example = "5277587")
    private String phone;
    @ApiModelProperty(value = "电子邮件", example = "1244@163.com")
    private String email;
    @ApiModelProperty(value = "营业执照或身份证号", example = "R00343")
    private String certNo;
    @ApiModelProperty(value = "国家id", example = "1")
    private Integer countryId;
    @ApiModelProperty(value = "省份id", example = "1234")
    private Integer provinceId;
    @ApiModelProperty(value = "城市id", example = "1234")
    private Integer cityId;
    @ApiModelProperty(value = "区id", example = "1234")
    private Integer districtId;
    @ApiModelProperty(value = "地址(当地址类型为2时必填)", example = "地址")
    private String address;
    @ApiModelProperty(value = "邮编", example = "123455")
    private String zipCode;
    @ApiModelProperty(value = "公司类型 1-公司 2-个体商户 3-个人", example = "1")
    private Short companyType;
    @ApiModelProperty(value = "是否开启分销模式 0 不开启, 1 开启", example = "0")
    private Short distributionFlag;
    @ApiModelProperty(value = "分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)", example = "0")
    private Short distributionPromotionFlag;

    @ApiModelProperty(value = "账户相关信息")
    private UserFinancialDTO financial;

}
