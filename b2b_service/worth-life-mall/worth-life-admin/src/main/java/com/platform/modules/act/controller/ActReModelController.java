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
package com.platform.modules.act.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.act.entity.ActReModelEntity;
import com.platform.modules.act.service.ActReModelService;
import com.platform.modules.sys.controller.AbstractController;
import org.activiti.engine.repository.Model;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2019-03-18 13:33:07
 */
@RestController
@RequestMapping("act/remodel")
public class ActReModelController extends AbstractController {
    @Autowired
    private ActReModelService actReModelService;

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("act:remodel:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = actReModelService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 新增
     *
     * @param actReModel actReModel
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("act:remodel:save")
    public RestResponse save(@RequestBody ActReModelEntity actReModel) {
        String modelId = "";
        try {
            Model model = actReModelService.add(actReModel);
            modelId = model.getId();
        } catch (Exception e) {
            RestResponse.error(e.getMessage());
        }
        return RestResponse.success().put("modelId", modelId);
    }

    /**
     * 根据Model部署流程
     *
     * @param id 标识
     * @return RestResponse
     */
    @SysLog("部署流程文件")
    @RequestMapping("/deploy")
    @RequiresPermissions("act:remodel:deploy")
    public RestResponse deploy(String id) {
        String msg = actReModelService.deploy(id);
        return RestResponse.success().put("msg", msg);
    }

    /**
     * 导出model的xml文件
     *
     * @param id       model标识
     * @param response 响应
     */
    @RequestMapping(value = "export")
    public void export(String id, HttpServletResponse response) {
        actReModelService.export(id, response);
    }

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("act:remodel:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        actReModelService.deleteBatch(ids);

        return RestResponse.success();
    }
}
