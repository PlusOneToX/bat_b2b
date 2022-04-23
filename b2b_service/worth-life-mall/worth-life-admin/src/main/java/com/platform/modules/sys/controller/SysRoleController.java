/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.modules.sys.entity.SysRoleEntity;
import com.platform.modules.sys.service.SysRoleMenuService;
import com.platform.modules.sys.service.SysRoleOrgService;
import com.platform.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author 李鹏军
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleOrgService sysRoleOrgService;

    /**
     * 角色列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {

        //如需数据权限，在参数中添加DataScope
        params.put("dataScope", getDataScope());

        Page page = sysRoleService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 角色列表
     *
     * @return RestResponse
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public RestResponse select() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        List<SysRoleEntity> list = sysRoleService.selectListByMap(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 根据主键查询详情
     *
     * @param roleId 主键
     * @return RestResponse
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public RestResponse info(@PathVariable("roleId") String roleId) {
        SysRoleEntity role = sysRoleService.getById(roleId);

        //查询角色对应的菜单
        List<String> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        //查询角色对应的机构
        List<String> orgNoList = sysRoleOrgService.queryOrgNoList(roleId);
        role.setOrgNoList(orgNoList);

        return RestResponse.success().put("role", role);
    }

    /**
     * 保存角色
     *
     * @param role role
     * @return RestResponse
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public RestResponse save(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        role.setCreateUserOrgNo(getOrgNo());
        sysRoleService.add(role);

        return RestResponse.success();
    }

    /**
     * 修改角色
     *
     * @param role role
     * @return RestResponse
     */
    @SysLog("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public RestResponse update(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        role.setCreateUserOrgNo(getOrgNo());
        sysRoleService.update(role);

        return RestResponse.success();
    }

    /**
     * 删除角色
     *
     * @param roleIds roleIds
     * @return RestResponse
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public RestResponse delete(@RequestBody String[] roleIds) {
        sysRoleService.deleteBatch(roleIds);

        return RestResponse.success();
    }
}
