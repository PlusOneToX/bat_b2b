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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.*;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.AddGroup;
import com.platform.modules.mall.dao.MallUserDao;
import com.platform.modules.mall.dto.IntegralDto;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.impl.*;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.mall.service.MallShopAuditService;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.impl.SysUserServiceImpl;
import com.platform.modules.utils.WebHttpUtil;
import com.qcloud.cos.utils.Md5Utils;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 李鹏军
 * @since 2021-01-04 18:50:28
 */
@RestController
@RequestMapping("mall/shopaudit")
public class MallShopAuditController extends AbstractController {
    @Autowired
    private MallShopAuditService mallShopAuditService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private MallUserDao mallUserDao;
    @Autowired
    private MallUserServiceImpl mallUserService;
    @Autowired
    private MallShopsServiceImpl mallShopsService;


    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MallShopAuditEntity> list = mallShopAuditService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @ApiOperation(value = "用户店铺审核列表", notes = "用户店铺审核列表", response = MallShopAuditEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "pageNo", value = "页码"),
                    @ExampleProperty(mediaType = "pageSize", value = "页面大小"),
            }), required = false, dataType = "string")
    })
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = mallShopAuditService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    public RestResponse info(@PathVariable("id") Integer id) {
        MallShopAuditEntity mallShopAudit = mallShopAuditService.getById(id);

        return RestResponse.success().put("shopaudit", mallShopAudit);
    }

    /**
     * 新增
     *
     * @param mallShopAudit mallShopAudit
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody MallShopAuditEntity mallShopAudit) {

        mallShopAuditService.add(mallShopAudit);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param mallShopAudit mallShopAudit
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    // @RequiresPermissions("mall:shopaudit:update")
    @Transactional
    @ApiOperation(value = "用户店铺审核", notes = "用户店铺审核", response = MallShopAuditEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "queryParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userId", value = "用户id"),
                    @ExampleProperty(mediaType = "status", value = "审核状态  0 待审核 1审核通过 2拒绝"),
                    @ExampleProperty(mediaType = "id", value = "记录id"),
            }), required = false, dataType = "string")
    })
    public RestResponse update(@RequestBody MallShopAuditEntity mallShopAudit) {

        // mallShopAuditService.update(mallShopAudit);
        MallUserEntity mallUserEntity = mallUserDao.selectById(mallShopAudit.getUserId());
        if (ObjectUtil.isEmpty(mallUserEntity)) {
            return RestResponse.error("未找到用户");
        }
        MallShopAuditEntity mallShopAuditEntity = mallShopAuditService.getById(mallShopAudit.getId());
        if (0 != mallShopAuditEntity.getStatus()) {
            return RestResponse.error("记录状态不正确");
        }

      /*  if (StringUtils.isEmpty(mallUserEntity.getMobile())) {
            return RestResponse.error("用户未设置手机号");
        }*/

        List<MallShopsEntity> MallShopsEntityList = mallShopsService.lambdaQuery().eq(MallShopsEntity::getUserId, mallUserEntity.getId()).list();

        if (ObjectUtil.isNotEmpty(MallShopsEntityList)) {
            return RestResponse.error("该用户已用户店铺");
        }


        mallShopAudit.setAuditor(getUserId()).setAuditTime(new Date());


        // RestResponse audit = mallShopAuditService.audit(mallShopAudit);

        BeanUtils.copyNewPropertites(mallShopAudit, mallShopAuditEntity);
        mallShopAuditEntity.setStatus(mallShopAudit.getStatus());
        mallShopAuditService.saveOrUpdate(mallShopAuditEntity);

        // return RestResponse.success().put("date",mallShopAuditEntity).put("pid",mallShopAuditEntity.getId());


        if (1 == mallShopAudit.getStatus()) {



           /* SysUserEntity sysUserEntity = sysUserService.lambdaQuery().eq(SysUserEntity::getUserName, mallUserEntity.getMobile()).one();

            if (ObjectUtil.isEmpty(sysUserEntity)) {
                mallUserEntity.setRoleType(2);
                List<String> merchantRole = mallUserDao.getMerchantRole();

                sysUserEntity = new SysUserEntity().setMobile(mallUserEntity.getMobile()).setStatus(1).setSex(mallUserEntity.getGender()).setUserName(mallUserEntity.getMobile()).setRealName(mallUserEntity.getUserName()).setRoleIdList(merchantRole);
                ValidatorUtils.validateEntity(sysUserEntity, AddGroup.class);

                Map<String, Object> params = new HashMap<>(2);
                params.put("dataScope", getDataScope());

                sysUserEntity.setCreateUserId(getUserId());
                sysUserEntity.setCreateUserOrgNo(getOrgNo());
                sysUserService.add(sysUserEntity, params);
                mallUserService.saveOrUpdate(mallUserEntity);
            }*/


            MallShopsEntity mallShopsEntity = new MallShopsEntity().setName(mallShopAuditEntity.getShopName()).setShopsSn(GenerateUtil.generateStrRecaptcha(12)).setSort(0).setTelephone(mallUserEntity.getMobile()).setUserId(mallUserEntity.getId()).setDetails(mallShopAuditEntity.getShopAddress()).setWorkTime("9:00-18:00");

            mallShopsEntity.setCreateTime(new Date());
            mallShopsEntity.setCreateUserId(getUserId());
            mallShopsService.add(mallShopsEntity);

            MallUserEntity inviteUser = mallUserService.lambdaQuery().eq(MallUserEntity::getId, mallUserEntity.getInviteUserId()).one();
            if (ObjectUtil.isNotEmpty(inviteUser)) {
                inviteUser.setRoleType(3);
                mallUserService.saveOrUpdate(inviteUser);

                MallAwardConfEntity mallAwardConfEntity = mallAwardConfService.lambdaQuery().eq(MallAwardConfEntity::getType, 8).one();
                IntegralDto integralDto = new IntegralDto().setUpdateTime(new Date()).setUserId(inviteUser.getId()).setPid(mallShopAuditEntity.getId().toString()).setRemark(JSONObject.toJSONString(mallShopAuditEntity)).setAmount(mallAwardConfEntity.getAmount()).setLabel(mallAwardConfEntity.getRemark()).setType(mallAwardConfEntity.getType());
                mallUserIntegralService.exchange(integralDto);
            }

            mallUserEntity.setRoleType(2);
        } else {
            long count = mallUserService.lambdaQuery().eq(MallUserEntity::getInviteUserId, mallUserEntity.getId()).list().stream().filter(e -> 2 == e.getRoleType()).count();
            if (count > 0) {
                mallUserEntity.setRoleType(3);
            }
        }

        mallUserService.saveOrUpdate(mallUserEntity);

        jedisUtil.set(Constant.SHOP_CERTIFICATION + mallShopAudit.getUserId(), JSONObject.toJSONString(mallShopAuditEntity), -1);
        return RestResponse.success().put("data", mallShopAuditEntity);
    }


    @Autowired
    private MallAwardConfServiceImpl mallAwardConfService;

    @Autowired
    private MallUserIntegralServiceImpl mallUserIntegralService;

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("mall:shopaudit:delete")
    public RestResponse delete(@RequestBody Integer[] ids) {
        mallShopAuditService.deleteBatch(ids);

        return RestResponse.success();
    }
}
