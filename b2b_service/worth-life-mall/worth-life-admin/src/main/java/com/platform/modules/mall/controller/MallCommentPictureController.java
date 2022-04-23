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
package com.platform.modules.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mall.entity.MallCommentPictureEntity;
import com.platform.modules.mall.service.MallCommentPictureService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 会员评价图片Controller
 *
 * @author 李鹏军
 * @since 2019-07-04 14:46:38
 */
@RestController
@RequestMapping("mall/commentpicture")
public class MallCommentPictureController extends AbstractController {
    @Autowired
    private MallCommentPictureService mallCommentPictureService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("mall:commentpicture:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallCommentPictureEntity> list = mallCommentPictureService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询会员评价图片
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("mall:commentpicture:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallCommentPictureService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("mall:commentpicture:info")
    public RestResponse info(@PathVariable("id") String id) {
        MallCommentPictureEntity mallCommentPicture = mallCommentPictureService.getById(id);

        return RestResponse.success().put("commentpicture", mallCommentPicture);
    }

    /**
     * 新增会员评价图片
     *
     * @param mallCommentPicture mallCommentPicture
     * @return RestResponse
     */
    @SysLog("新增会员评价图片")
    @RequestMapping("/save")
    @RequiresPermissions("mall:commentpicture:save")
    public RestResponse save(@RequestBody MallCommentPictureEntity mallCommentPicture) {

        mallCommentPictureService.add(mallCommentPicture);

        return RestResponse.success();
    }

    /**
     * 修改会员评价图片
     *
     * @param mallCommentPicture mallCommentPicture
     * @return RestResponse
     */
    @SysLog("修改会员评价图片")
    @RequestMapping("/update")
    @RequiresPermissions("mall:commentpicture:update")
    public RestResponse update(@RequestBody MallCommentPictureEntity mallCommentPicture) {

        mallCommentPictureService.update(mallCommentPicture);

        return RestResponse.success();
    }

    /**
     * 根据主键删除会员评价图片
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除会员评价图片")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:commentpicture:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        mallCommentPictureService.deleteBatch(ids);

        return RestResponse.success();
    }
}
