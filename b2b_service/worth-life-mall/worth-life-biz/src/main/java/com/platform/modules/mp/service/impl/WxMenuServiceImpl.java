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
package com.platform.modules.mp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mp.dao.WxMenuDao;
import com.platform.modules.mp.entity.WxMpMenu;
import com.platform.modules.mp.service.WxMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信菜单业务
 *
 * @author lengleng
 * @since 2019-03-27 20:45:18
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxMenuServiceImpl extends ServiceImpl<WxMenuDao, WxMpMenu> implements WxMenuService {
    private static final String PUB_ED = "1";
    @Autowired
    private WxMpService wxMpService;

    /**
     * 查询菜单信息
     *
     * @return menu
     */
    @Override
    public String getMenu(String appId) {
        WxMpMenu wxMpMenu = this.getOne(new QueryWrapper<WxMpMenu>().eq("WX_APPID", appId));

        return wxMpMenu.getMenu();
    }

    /**
     * 新增修改微信菜单到数据库
     *
     * @param wxMenus json
     * @return boolean
     */
    @Override
    public Boolean menuCreateByDb(JSONObject wxMenus) {
        WxMpMenu wxMpMenu = this.getOne(new LambdaUpdateWrapper<WxMpMenu>().eq(WxMpMenu::getWxAppid, wxMpService.getWxMpConfigStorage().getAppId()), false);
        if (null == wxMpMenu) {
            throw new BusinessException("微信菜单配置未初始化，请联系管理员！");
        }
        wxMpMenu.setMenu(wxMenus.toJSONString());
        wxMpMenu.setPubFlag("0");
        baseMapper.updateById(wxMpMenu);
        return Boolean.TRUE;
    }

    /**
     * 发布到微信
     *
     * @return boolean
     */
    @Override
    public boolean push(WxMenu menu) {
        List<WxMpMenu> wxMpMenuList = this.list();

        if (StringUtils.isEmpty(wxMpMenuList) || wxMpMenuList.size() == 0) {
            throw new BusinessException("微信菜单配置未保存，请先保存菜单！");
        }
        WxMpMenu wxMpMenu = wxMpMenuList.get(0);
        // 判断是否发布
        if (PUB_ED.equals(wxMpMenu.getPubFlag())) {
            throw new BusinessException("微信菜单配置已发布，不要重复发布！");
        }
        WxMpMenuService menuService = wxMpService.getMenuService();

        // 给数据库保存的加一层
        try {
            menuService.menuCreate(menu);
            //更新菜单发布标志
            wxMpMenu.setPubFlag(PUB_ED);
        } catch (WxErrorException e) {
            log.error("发布微信菜单失败", e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        baseMapper.updateById(wxMpMenu);
        return true;
    }

    /**
     * 删除菜单
     * menuId 菜单ID
     *
     * @return boolean
     */
    @Override
    public boolean delete(String menuId) {
        WxMpMenuService menuService = wxMpService.getMenuService();
        try {
            menuService.menuDelete();
        } catch (WxErrorException e) {
            log.error("微信菜单删除失败", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean menuDeleteByDb(String appId) {
        baseMapper.menuDeleteByDb(appId);
        return true;
    }
}
