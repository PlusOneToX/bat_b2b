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
package com.platform.modules.mp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.modules.mp.entity.WxMpMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信菜单Dao
 *
 * @author 李鹏军
 * @since 2019-08-23 16:45:18
 */
@Mapper
public interface WxMenuDao extends BaseMapper<WxMpMenu> {

    /**
     * 从数据库删除菜单
     *
     * @param appId appId
     * @return int
     */
    int menuDeleteByDb(String appId);
}
