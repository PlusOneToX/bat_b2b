package com.bat.system.api.user.dto.data;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/17 14:57
 */
@Data
@ApiModel(value = "UserInfoDTO")
public class UserInfoDTO {
    private List<RoleDTO> roles;
    private List<MenuDTO> menus;
    private List<PermissionDTO> permissions;
}
