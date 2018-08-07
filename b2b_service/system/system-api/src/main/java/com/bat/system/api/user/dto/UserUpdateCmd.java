package com.bat.system.api.user.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:50
 */
@Data
@ApiModel(value = "UserUpdateCmd", description = "后台用户更新")
public class UserUpdateCmd {

    @NotNull(message = "P_USER_ID_NULL")
    @ApiModelProperty(value = "id", required = true, example = "1")
    private Integer id;

    @NotBlank(message = "P_USER_NAME_NULL")
    @ApiModelProperty(value = "用户名", required = true, example = "张三")
    private String userName;

    @NotBlank(message = "P_REAL_NAME_NULL")
    @ApiModelProperty(value = "用户真实姓名", required = true, example = "李四")
    private String realName;

    // @NotBlank(message = "P_USER_PASSWORD_NULL")
    @ApiModelProperty(value = "密码", example = "123465")
    private String password;

    // @NotBlank(message = "P_USER_PHONE_NULL")
    @ApiModelProperty(value = "手机号", required = true, example = "18995912083")
    private String mobile;

    @ApiModelProperty(value = "邮箱", example = "11478375@qq.com")
    private String email;

    @ApiModelProperty(value = "钉钉头像",
        example = "https://i0.hdslb.com/bfs/face/24f5ca1b94dcf60f570f80ca2f6c8ae7daf2ca01.jpg@150w_150h.jpg")
    private String dingAvatar;

    @NotNull(message = "P_ADMIN_TYPE_NULL")
    @ApiModelProperty(value = "管理员类型 1.超级管理员 2.普通用户", required = true, example = "1")
    private Short adminType;

    // @NotNull(message = "P_BRAND_SCOPE_NULL")
    @ApiModelProperty(value = "品牌范围类型 0无 1.全局 2.其他", required = true, example = "1")
    private Short brandScope;

    @NotNull(message = "P_SALE_SCOPE_NULL")
    @ApiModelProperty(value = "业务范围,1.全局 2.单业务范围 3.多业务范围", required = true, example = "1")
    private Short saleScope;

    @NotNull(message = "P_USER_STATUS_NULL")
    @ApiModelProperty(value = "状态  1.启用 0.禁用", required = true, example = "1")
    private Short status;

    // @NotBlank(message = "P_ERP_USER_NO_NULL")
    @ApiModelProperty(value = "ERP_用户编码", required = true, example = "RQ000421")
    private String erpUserNo;

    @NotNull(message = "P_ORGANIZATION_ID_NULL")
    @ApiModelProperty(value = "组织id", required = true, example = "1")
    private Integer organizationId;

    // @NotNull(message = "P_DEPARTMENT_ID_NULL")
    @ApiModelProperty(value = "部门id", required = true, example = "1")
    private Integer departmentId;

    // @NotNull(message = "P_ROCK_ACCOUNT_ID_NULL")
    @ApiModelProperty(value = "账户中心id", required = true, example = "1")
    private Long rockAccountId;

    @NotNull(message = "P_FICTITIOUS_FLAG_NULL")
    @ApiModelProperty(value = "是否虚拟销售员，0否 1是", required = true, example = "0")
    private Short fictitiousFlag;

    @NotNull(message = "P_SALE_FLAG_NULL")
    @ApiModelProperty(value = "是否销售员，0否 1是 ", required = true, example = "1")
    private Short saleFlag;

    @ApiModelProperty(value = "备注", example = "test")
    private String remark;

    @ApiModelProperty(value = "角色Ids")
    private List<Integer> roleIds;

    @ApiModelProperty(value = "品牌Ids")
    private List<Integer> brandIds;

    @ApiModelProperty(value = "子销售员Ids")
    private List<Integer> saleIds;

}
