package com.bat.system.api.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.dto.RoleUpdateCmd;
import com.bat.system.api.user.dto.data.RoleDTO;
import com.bat.system.api.user.dto.data.UserInfoDTO;
import com.bat.system.api.user.dto.RoleCreateCmd;
import com.bat.system.api.user.dto.RoleQry;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface RoleService {
    /**
     * 添加角色
     *
     * @param cmd
     * @return
     */
    boolean createRole(RoleCreateCmd cmd);

    /**
     * 根据ID获取角色
     *
     * @param id
     * @return
     */
    RoleDTO getRoleById(Integer id);

    /**
     * 获取所有角色（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<RoleDTO> listRole(RoleQry qry);

    /**
     * 根据id删除角色
     *
     * @param id
     * @return
     */
    boolean deleteRoleById(Integer id);

    /**
     * 更新角色
     * 
     * @param cmd
     * @return
     */
    boolean updateRole(RoleUpdateCmd cmd);

    /**
     * 根据IDS获取角色 并合并
     * 
     * @param ids
     */
    UserInfoDTO getRoleByIds(List<Integer> ids);
}
