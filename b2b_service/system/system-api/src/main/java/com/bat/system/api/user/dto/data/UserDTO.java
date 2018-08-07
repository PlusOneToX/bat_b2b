package com.bat.system.api.user.dto.data;

import java.util.Date;
import java.util.List;

import com.bat.system.dao.user.dataobject.UserLoginDO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/13 18:43
 */
@Data
@ApiModel(value = "UserDTO")
public class UserDTO {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "钉钉头像")
    private String dingAvatar;
    @ApiModelProperty(value = "管理员类型, 1.超级管理员 2.普通用户")
    private Short adminType;
    @ApiModelProperty(value = "品牌范围,0无 1.全局 2.其他")
    private Short brandScope;
    @ApiModelProperty(value = "业务范围,1.全局 2.单业务范围 3.多业务范围")
    private Short saleScope;
    @ApiModelProperty(value = "状态, 1.启用 0.禁用")
    private Short status;
    @ApiModelProperty(value = "ERP_用户编码")
    private String erpUserNo;
    @ApiModelProperty(value = "组织id")
    private Integer organizationId;
    @ApiModelProperty(value = "部门id")
    private Integer departmentId;
    @ApiModelProperty(value = "用户id 钉钉id")
    private Long rockAccountId;
    @ApiModelProperty(value = "是否虚拟销售员，0否 1是")
    private Short fictitiousFlag;
    @ApiModelProperty(value = "是否销售员，0否 1是 ")
    private Short saleFlag;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "新增时间")
    private Date createTime;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "最后登录时间")
    private Date lastOnlineTime;
    @ApiModelProperty(value = "登录次数")
    private Integer times;
    @ApiModelProperty(value = "登录信息")
    private UserLoginDO userLoginDO;
    @ApiModelProperty(value = "品牌id")
    private List<Integer> brandIds;
    @ApiModelProperty(value = "角色id")
    private List<Integer> roleIds;
    @ApiModelProperty(value = "业务范围id")
    private List<Integer> saleIds;
}
