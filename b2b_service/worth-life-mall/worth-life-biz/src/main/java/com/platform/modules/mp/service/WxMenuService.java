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
package com.platform.modules.mp.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.common.utils.RestResponse;
import com.platform.modules.mp.entity.WxMpMenu;
import me.chanjar.weixin.common.bean.menu.WxMenu;

/**
 * 微信菜单业务
 *
 * @author lengleng
 * @since 2019-03-27 20:45:18
 */
public interface WxMenuService extends IService<WxMpMenu> {

    /**
     * 查询菜单信息
     *
     * @param appId appId
     * @return String
     */
    String getMenu(String appId);

    /**
     * 新增微信菜单
     *
     * @param wxMenus json
     * @return Boolean
     */
    Boolean menuCreateByDb(JSONObject wxMenus);

    /**
     * 发布到微信
     *
     * @param menu 菜单
     * @return boolean
     */
    boolean push(WxMenu menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return boolean
     */
    boolean delete(String menuId);

    /**
     * 从数据库删除菜单
     *
     * @param appId appId
     * @return boolean
     */
    boolean menuDeleteByDb(String appId);
}
