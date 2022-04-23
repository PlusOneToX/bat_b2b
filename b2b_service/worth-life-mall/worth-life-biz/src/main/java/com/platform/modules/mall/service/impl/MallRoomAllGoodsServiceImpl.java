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
package com.platform.modules.mall.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaLiveInfo;
import cn.binarywang.wx.miniapp.bean.WxMaLiveResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.HttpUtils;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallRoomAllGoodsDao;
import com.platform.modules.mall.entity.MallRoomAllGoodsEntity;
import com.platform.modules.mall.service.MallRoomAllGoodsService;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 直播接口所有商品信息Service实现类
 *
 * @author 李鹏军
 * @since 2020-07-06 16:35:39
 */
@Service("mallRoomAllGoodsService")
public class MallRoomAllGoodsServiceImpl extends ServiceImpl<MallRoomAllGoodsDao, MallRoomAllGoodsEntity> implements MallRoomAllGoodsService {
    @Autowired
    private WxMaService maService;

    @Override
    public List<MallRoomAllGoodsEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.GOODS_ID");
        params.put("asc", false);
        Page<MallRoomAllGoodsEntity> page = new Query<MallRoomAllGoodsEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallRoomAllGoodsPage(page, params));
    }

    @Override
    public boolean add(MallRoomAllGoodsEntity mallRoomAllGoods) {
        WxMaLiveInfo.Goods goods = new WxMaLiveInfo.Goods();
        try {
            // 上传临时素材
            WxMediaUploadResult mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                    HttpUtils.getNetResource(mallRoomAllGoods.getCoverImgUrl()));
            goods.setCoverImgUrl(mediaUploadResult.getMediaId());
            goods.setName(mallRoomAllGoods.getName());
            goods.setPriceType(mallRoomAllGoods.getPriceType());
            goods.setPrice(mallRoomAllGoods.getPrice());
            goods.setPrice2(mallRoomAllGoods.getPrice2());
            goods.setUrl(mallRoomAllGoods.getUrl());

            WxMaLiveResult result = maService.getLiveGoodsService().addGoods(goods);
            mallRoomAllGoods.setAuditId(result.getAuditId());
            mallRoomAllGoods.setGoodsId(result.getGoodsId());
            mallRoomAllGoods.setAuditStatus(0);
            mallRoomAllGoods.setThirdPartyTag(1);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return this.save(mallRoomAllGoods);
    }

    @Override
    public boolean update(MallRoomAllGoodsEntity mallRoomAllGoods) {
        if (mallRoomAllGoods.getAuditStatus() == 1) {
            throw new BusinessException("审核中的商品不允许更新！请先同步商品信息");
        }
        try {
            WxMaLiveInfo.Goods goods = new WxMaLiveInfo.Goods();
            //审核通过的商品仅允许更新价格类型与价格
            if (mallRoomAllGoods.getAuditStatus() == 2) {
                goods.setGoodsId(mallRoomAllGoods.getGoodsId());
                goods.setPriceType(mallRoomAllGoods.getPriceType());
                goods.setPrice(mallRoomAllGoods.getPrice());
                goods.setPrice2(mallRoomAllGoods.getPrice2());
            }
            if (mallRoomAllGoods.getAuditStatus() == 0) {
                // 上传临时素材
                WxMediaUploadResult mediaUploadResult = maService.getMediaService().uploadMedia("image", "png",
                        HttpUtils.getNetResource(mallRoomAllGoods.getCoverImgUrl()));

                goods.setCoverImgUrl(mediaUploadResult.getMediaId());
                goods.setUrl(mallRoomAllGoods.getUrl());
                goods.setName(mallRoomAllGoods.getName());
            }
            boolean flag = maService.getLiveGoodsService().updateGoods(goods);
            if (flag) {
                return this.updateById(mallRoomAllGoods);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Integer goodsId) {
        try {
            boolean flag = maService.getLiveGoodsService().deleteGoods(goodsId);
            if (flag) {
                return this.removeById(goodsId);
            }
        } catch (WxErrorException e) {
            throw new BusinessException(e.getMessage());
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] goodsIds) {
        List<Integer> list = Arrays.asList(goodsIds);
        list.forEach(goodsId -> {
            try {
                maService.getLiveGoodsService().deleteGoods(goodsId);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        });
        return this.removeByIds(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean getapproved(Integer start, Integer limit) {
        try {
            List<WxMaLiveResult.Goods> goodsList = new ArrayList<>();

            WxMaLiveResult result = maService.getLiveGoodsService().getApprovedGoods(start, limit, 0);
            goodsList.addAll(result.getGoods());
            result = maService.getLiveGoodsService().getApprovedGoods(start, limit, 1);
            goodsList.addAll(result.getGoods());
            result = maService.getLiveGoodsService().getApprovedGoods(start, limit, 2);
            goodsList.addAll(result.getGoods());
            result = maService.getLiveGoodsService().getApprovedGoods(start, limit, 3);
            goodsList.addAll(result.getGoods());

            if (goodsList.size() > 0) {
                List<MallRoomAllGoodsEntity> mallRoomAllGoodsEntityList = new ArrayList<>();
                goodsList.forEach(goods -> {
                    MallRoomAllGoodsEntity goodsEntity = new MallRoomAllGoodsEntity();
                    goodsEntity.setGoodsId(goods.getGoodsId());
                    goodsEntity.setName(goods.getName());
                    goodsEntity.setPrice(goods.getPrice());
                    goodsEntity.setCoverImgUrl(goods.getCoverImgUrl());
                    goodsEntity.setPrice2(goods.getPrice2());
                    goodsEntity.setUrl(goods.getUrl());
                    goodsEntity.setPriceType(goods.getPriceType());
                    goodsEntity.setAuditStatus(goods.getAuditStatus());
                    goodsEntity.setThirdPartyTag(Integer.parseInt(goods.getThirdPartyTag()));
                    mallRoomAllGoodsEntityList.add(goodsEntity);
                });
                this.saveOrUpdateBatch(mallRoomAllGoodsEntityList);
            }
        } catch (WxErrorException e) {
            throw new BusinessException(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean resetaudit(Integer goodsId, Integer auditId) {
        try {
            boolean flag = maService.getLiveGoodsService().resetAudit(auditId, goodsId);
            if (flag) {
                MallRoomAllGoodsEntity entity = this.getById(goodsId);
                entity.setAuditStatus(0);
                baseMapper.updateById(entity);
            }
            return flag;
        } catch (WxErrorException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public boolean audit(int goodsId) {
        try {
            String auditId = maService.getLiveGoodsService().auditGoods(goodsId);
            MallRoomAllGoodsEntity entity = this.getById(goodsId);
            entity.setAuditStatus(1);
            entity.setAuditId(Integer.parseInt(auditId));
            return baseMapper.updateById(entity) > 0;
        } catch (WxErrorException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
