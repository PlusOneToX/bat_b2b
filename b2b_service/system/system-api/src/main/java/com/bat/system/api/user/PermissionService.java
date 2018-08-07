package com.bat.system.api.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.dto.PermissionCreateCmd;
import com.bat.system.api.user.dto.PermissionQry;
import com.bat.system.api.user.dto.PermissionUpdateCmd;
import com.bat.system.api.user.dto.data.PermissionDTO;
import com.bat.system.api.user.dto.data.PermissionSyncDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/11 15:48
 */
public interface PermissionService {
    /**
     * 添加权限
     *
     * @param cmd
     * @return
     */
    boolean createPermission(PermissionCreateCmd cmd);

    /**
     * 根据ID获取权限
     *
     * @param id
     * @return
     */
    PermissionDTO getPermissionById(Integer id);

    /**
     * 获取所有权限（分页）
     *
     * @param qry
     * @return
     */
    PageInfo<PermissionDTO> listPermission(PermissionQry qry);

    /**
     * 根据id删除权限
     *
     * @param id
     * @return
     */
    boolean deletePermissionById(Integer id);

    /**
     * 更新权限
     * 
     * @param cmd
     * @return
     */
    boolean updatePermission(PermissionUpdateCmd cmd);

    /**
     * 根据swagger信息同步权限
     * 
     * @param gateWaySwaggerUrl
     */
    void syncPermission(String gateWaySwaggerUrl);

    /**
     * 获取权限列表
     * 
     * @return
     */
    List<PermissionSyncDTO> listPermissionTree();

    /**
     * 更新缓存
     */
    void syncCache();

}
