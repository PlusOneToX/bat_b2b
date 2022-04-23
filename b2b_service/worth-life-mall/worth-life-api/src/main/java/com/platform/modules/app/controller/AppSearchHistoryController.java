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
package com.platform.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallGoodsService;
import com.platform.modules.mall.service.MallKeywordsService;
import com.platform.modules.mall.service.MallSearchHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppSearchHistoryController
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @since 2017-03-23 15:31
 */
@Api(tags = "AppSearchHistoryController|搜索管理控制器")
@RestController
@RequestMapping("/app/search")
public class AppSearchHistoryController extends AppBaseController {
    @Autowired
    private MallKeywordsService keywordsService;
    @Autowired
    private MallSearchHistoryService searchHistoryService;
    @Autowired
    private MallGoodsService goodsService;

    /**
     * 热搜
     */
    @IgnoreAuth
    @GetMapping("hotKeyword")
    @ApiOperation(value = "热搜", notes = "热搜")
    public RestResponse hotKeyword() {
        Map<String, Object> resultObj = new HashMap<>(2);
        //取出热闹关键词
        List<Map<String, Object>> hotKeywordList = keywordsService.listMaps(new QueryWrapper<MallKeywordsEntity>()
                .select("DISTINCT KEYWORD,IS_HOT"));

        resultObj.put("hotKeywordList", hotKeywordList);
        return RestResponse.success(resultObj);
    }

    /**
     * 搜索历史记录
     */
    @GetMapping("history")
    @ApiOperation(value = "搜索历史记录", notes = "获取会员搜索历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse index(@LoginUser MallUserEntity loginUser) {
        Map<String, Object> resultObj = new HashMap<>(8);
        List<MallKeywordsEntity> keywordsEntityList = keywordsService.list(new QueryWrapper<MallKeywordsEntity>().eq("IS_DEFAULT", 1));
        //取出输入框默认的关键词
        MallKeywordsEntity defaultKeyword = null != keywordsEntityList && keywordsEntityList.size() > 0 ? keywordsEntityList.get(0) : null;
        //取出热闹关键词
        List<Map<String, Object>> hotKeywordList = keywordsService.listMaps(new QueryWrapper<MallKeywordsEntity>()
                .select("DISTINCT KEYWORD,IS_HOT"));

        List<MallSearchHistoryEntity> historyVoList = searchHistoryService.list(new QueryWrapper<MallSearchHistoryEntity>().select("distinct keyword").eq("USER_ID", loginUser.getId()));
        String[] historyKeywordList = null;
        if (null != historyVoList && historyVoList.size() > 0) {
            historyKeywordList = new String[historyVoList.size()];
            int i = 0;
            for (MallSearchHistoryEntity historyVo : historyVoList) {
                historyKeywordList[i] = historyVo.getKeyword();
                i++;
            }
        }
        resultObj.put("defaultKeyword", defaultKeyword);
        resultObj.put("historyKeywordList", historyKeywordList);
        resultObj.put("hotKeywordList", hotKeywordList);
        return RestResponse.success(resultObj);
    }

    /**
     * 根据用户输入获取系统已有的关键字
     */
    @IgnoreAuth
    @GetMapping("helper")
    @ApiOperation(value = "搜索关键字", notes = "根据用户输入获取系统已有的关键字")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字", required = true, dataType = "string")
    })
    public RestResponse helper(String keyword) {
        List<MallKeywordsEntity> keywords = keywordsService.list(new QueryWrapper<MallKeywordsEntity>()
                .select("DISTINCT KEYWORD").like(StringUtils.isNotBlank(keyword), "KEYWORD", keyword));
        String[] keys = new String[keywords.size()];
        if (null != keywords) {
            int i = 0;
            for (MallKeywordsEntity keywordsVo : keywords) {
                keys[i] = keywordsVo.getKeyword();
                i++;
            }
        }
        return RestResponse.success().put("data", keys);
    }

    /**
     * 搜索历史记录
     */
    @GetMapping("searchGoods")
    @ApiOperation(value = "搜索历史记录", notes = "获取会员搜索历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键词", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "searchFrom", value = "1:微信小程序 2:微信公众号 3:app 4:H5 5:支付宝小程序 6:QQ小程序", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "品牌制造商ID", example = "1", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse searchGoods(@LoginUser MallUserEntity loginUser, String keyword, Integer searchFrom, String categoryId, String order, String sortType,
                                    @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {
        MallSearchHistoryEntity searchHistoryEntity = new MallSearchHistoryEntity();
        searchHistoryEntity.setAddTime(new Date());
        searchHistoryEntity.setKeyword(keyword);
        searchHistoryEntity.setUserId(loginUser.getId());
        searchHistoryEntity.setSearchFrom(searchFrom);
        searchHistoryService.save(searchHistoryEntity);

        Page<MallGoodsEntity> page = new Page<>(current, limit);

        IPage<MallGoodsEntity> data = goodsService.page(page, new QueryWrapper<MallGoodsEntity>().eq(StringUtils.isNotBlank(categoryId), "CATEGORY_ID", categoryId)
                .like(StringUtils.isNotBlank(keyword), "name", keyword).eq("IS_ON_SALE", 1)
                .orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order).orderByDesc("SORT"));

        //筛选的分类
        List<MallCategoryEntity> filterCategory = getCategoryByData(data.getRecords());
        return RestResponse.success().put("data", data).put("filterCategory", filterCategory);
    }

    /**
     * 清空搜索记录
     */
    @PostMapping("clearHistory")
    @ApiOperation(value = "清空搜索记录", notes = "清空用户搜索记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string")
    })
    public RestResponse clearHistory(@LoginUser MallUserEntity loginUser) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("USER_ID", loginUser.getId());
        searchHistoryService.removeByMap(params);
        return RestResponse.success();
    }
}
