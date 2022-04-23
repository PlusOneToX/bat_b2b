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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.Base64Util;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.entity.MallCommentEntity;
import com.platform.modules.mall.entity.MallCommentPictureEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallCommentPictureService;
import com.platform.modules.mall.service.MallCommentService;
import com.platform.modules.mall.service.MallUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: @author Lipengjun <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "ApiCommentController|商品评论管理控制器")
@RestController
@RequestMapping("/app/comment")
public class AppCommentController {
    @Autowired
    private MallCommentService commentService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallCommentPictureService commentPictureService;

    /**
     * 发表评论
     */
    @PostMapping("post")
    @ApiOperation(value = "发表评论", notes = "发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "orderId", value = "1"),
                    @ExampleProperty(mediaType = "evalLevel", value = "评价级别：int类型"),
                    @ExampleProperty(mediaType = "deliveryLevel", value = "配送质量：int类型"),
                    @ExampleProperty(mediaType = "goodsList", value = "json"),
                    @ExampleProperty(mediaType = "type", value = "0")
            }), required = true, dataType = "string")
    })
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public RestResponse post(@LoginUser MallUserEntity loginUser, @RequestBody JSONObject jsonParam) {
        String orderId = jsonParam.getString("orderId");
        Integer evalLevel = jsonParam.getInteger("evalLevel");
        Integer deliveryLevel = jsonParam.getInteger("deliveryLevel");
        Integer type = jsonParam.getInteger("type");
        List<Map<String, Object>> goodsList = jsonParam.getObject("goodsList", List.class);

        for (Map<String, Object> goods : goodsList) {
            List<String> pics = (List<String>) goods.get("pics");
            String goodsId = goods.get("goodsId").toString();
            String goodsSpecifitionNameValue = goods.get("goodsSpecifitionNameValue").toString();
            String goodsLevel = goods.getOrDefault("goodsLevel", 5).toString();
            String content = goods.get("comment").toString();

            MallCommentEntity commentEntity = new MallCommentEntity();
            commentEntity.setGoodsId(goodsId);
            commentEntity.setOrderId(orderId);
            commentEntity.setUserId(loginUser.getId());
            commentEntity.setContent(content);
            commentEntity.setGoodsSpecifitionNameValue(goodsSpecifitionNameValue);
            commentEntity.setStatus(0);
            commentEntity.setType(type);
            commentEntity.setAddTime(new Date());
            commentEntity.setEvalLevel(evalLevel);
            commentEntity.setDeliveryLevel(deliveryLevel);
            commentEntity.setGoodsLevel(Integer.parseInt(goodsLevel));
            commentEntity.setContent(Base64Util.encode(commentEntity.getContent()));

            boolean flag = commentService.save(commentEntity);
            if (flag && null != pics && pics.size() > 0) {
                int i = 0;
                for (String imgLink : pics) {
                    i++;
                    MallCommentPictureEntity pictureVo = new MallCommentPictureEntity();
                    pictureVo.setCommentId(commentEntity.getId());
                    pictureVo.setPicUrl(imgLink);
                    pictureVo.setSort(i);
                    commentPictureService.save(pictureVo);
                }
            }
        }
        return RestResponse.success();
    }

    /**
     * 评论总数
     */
    @IgnoreAuth
    @GetMapping("count")
    @ApiOperation(value = "评论总数", notes = "评论总数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "用户评论的类型;0评论的是商品,1评论的是文章", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "from", value = "查看来源", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品Id", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单Id", required = true, dataType = "string", example = "1")
    })
    public RestResponse count(String type,@RequestParam(defaultValue = "0") Integer from, String goodsId, String orderId) {
        Integer goodsCount = commentService.count(new QueryWrapper<MallCommentEntity>().eq(from != 1, "STATUS", 1).eq(StringUtils.isNotBlank(orderId), "ORDER_ID", orderId)
                .eq("TYPE", type).eq(StringUtils.isNotBlank(goodsId), "GOODS_ID", goodsId));

        Map<String, Object> param = new HashMap<>(2);
        param.put("goodsId", goodsId);
        param.put("orderId", orderId);
        param.put("type", type);
        param.put("from", from);
        param.put("status", 1);
        Integer picCount = commentService.queryHasPicTotal(param);

        return RestResponse.success().put("goodsCount", goodsCount).put("picCount", picCount);
    }

    /**
     * 根据商品ID获取评论列表
     *
     * @return
     */
    @IgnoreAuth
    @GetMapping("list")
    @ApiOperation(value = "评论列表", notes = "根据商品ID获取评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "用户评论的类型;0评论的是商品,1评论的是文章", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "from", value = "查看来源, 1 查看所有状态 否则查询审核通过的", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "orderId", value = "订单Id", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "goodsId", value = "商品Id", required = true, dataType = "string", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序字段", allowableValues = "RETAIL_PRICE,SALES", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "showType", value = "所有评价、有图评价", allowableValues = "0,1", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "sortType", value = "排序类型", allowableValues = "ASC,DESC", dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "current", value = "当前页码", example = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页条数", example = "10", dataType = "int")
    })
    public RestResponse list(String type,@RequestParam(defaultValue = "0") Integer from, String orderId, String goodsId, Integer showType, String order, String sortType,
                             @RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer limit) {

        Page<MallCommentEntity> page = new Page<>(current, limit);

        IPage<MallCommentEntity> data = commentService.page(page, new QueryWrapper<MallCommentEntity>().eq("TYPE", type).eq(StringUtils.isNotBlank(goodsId), "GOODS_ID", goodsId)
                .eq(StringUtils.isNotBlank(orderId), "ORDER_ID", orderId).eq(from != 1, "STATUS", 1).orderBy(StringUtils.isNotBlank(order), "ASC".equalsIgnoreCase(sortType), order)
                .exists(showType == 1, "SELECT 1 FROM MALL_COMMENT_PICTURE WHERE MALL_COMMENT.ID = MALL_COMMENT_PICTURE.COMMENT_ID"));

        List<MallCommentEntity> commentList = data.getRecords();

        for (MallCommentEntity commentItem : commentList) {
            commentItem.setContent(Base64Util.decode(commentItem.getContent()));
            commentItem.setUserInfo(userService.getById(commentItem.getUserId()));

            List<MallCommentPictureEntity> commentPictureEntities = commentPictureService.list(
                    new QueryWrapper<MallCommentPictureEntity>().eq("COMMENT_ID", commentItem.getId()));
            commentItem.setCommentPictureEntityList(commentPictureEntities);
        }

        return RestResponse.success().put("data", data);
    }
}
