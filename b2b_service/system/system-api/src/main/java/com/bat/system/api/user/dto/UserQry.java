package com.bat.system.api.user.dto;

import java.util.HashMap;
import java.util.Map;

import com.bat.system.api.base.BaseSearchQry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:49
 */
@Data
@ApiModel(value = "UserQry", description = "后台用户查询")
public class UserQry extends BaseSearchQry {

    public UserQry() {
        super.attributeMapper.put((short)1, "setUserName");
        super.attributeMapper.put((short)2, "setErpUserNo");
        super.attributeMapper.put((short)3, "setRealName");
        super.attributeMapper.put((short)4, "setRoleName");
    }

    public static Map<String, String> map = new HashMap<>();

    @ApiModelProperty(value = "状态  1.启用 0.禁用", example = "1")
    private Short status;

    @ApiModelProperty(value = "是否销售员，0否 1是 ", example = "1")
    private Short saleFlag;

    @ApiModelProperty(value = "用户名", example = "1")
    private String userName;

    @ApiModelProperty(value = "用户真实名称", example = "1")
    private String realName;

    @ApiModelProperty(value = "用户erp编码", example = "1")
    private String erpUserNo;

    @ApiModelProperty(value = "管理员类型", example = "1")
    private String roleName;

}
