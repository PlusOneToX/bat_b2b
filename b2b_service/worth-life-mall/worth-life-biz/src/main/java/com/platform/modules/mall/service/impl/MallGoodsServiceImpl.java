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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallGoodsDao;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品表Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-03 17:58:29
 */
@Service("mallGoodsService")
public class MallGoodsServiceImpl extends ServiceImpl<MallGoodsDao, MallGoodsEntity> implements MallGoodsService {
    @Autowired
    private MallAttachmentService attachmentService;
    @Autowired
    private MallGoodsAttributeService goodsAttributeService;
    @Autowired
    private MallSpecificationService specificationService;
    @Autowired
    private MallGoodsSkuService goodsSkuService;
    @Autowired
    private MallOrderGoodsService orderGoodsService;
    @Autowired
    private MallShopsGoodsService shopsGoodsService;
    @Autowired
    private MallSeckillService seckillService;

    @Override
    public List<MallGoodsEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.SORT");
        params.put("asc", false);
        Page<MallGoodsEntity> page = new Query<MallGoodsEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallGoodsPage(page, params));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(MallGoodsEntity mallGoods) {
        this.save(mallGoods);

        //轮播图
        List<MallAttachmentEntity> attachmentEntities = mallGoods.getAttachmentEntityList();
        if (null != attachmentEntities && attachmentEntities.size() > 0) {
            int i = 0;
            for (MallAttachmentEntity attachmentEntity : attachmentEntities) {
                attachmentEntity.setOrderSort(i++);
                attachmentEntity.setBussinessId(mallGoods.getId());
            }
        }
        attachmentService.saveBatch(attachmentEntities);

        //商品详情页参数列表
        List<MallGoodsAttributeEntity> goodsAttributeEntityList = mallGoods.getGoodsAttributeEntityList();
        if (null != goodsAttributeEntityList && goodsAttributeEntityList.size() > 0) {
            for (MallGoodsAttributeEntity goodsAttributeEntity : goodsAttributeEntityList) {
                goodsAttributeEntity.setGoodsId(mallGoods.getId());
            }
        }
        goodsAttributeService.saveBatch(goodsAttributeEntityList);

        //商品SKU键表
        List<MallSpecificationEntity> specificationEntityList = mallGoods.getSpecificationEntityList();
        if (null != specificationEntityList && specificationEntityList.size() > 0) {
            for (MallSpecificationEntity specificationEntity : specificationEntityList) {
                specificationEntity.setGoodsId(mallGoods.getId());
            }
        }
        specificationService.saveBatch(specificationEntityList);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MallGoodsEntity mallGoods) {
        Map<String, Object> params = new HashMap<>(2);

        //轮播图
        params.put("BUSSINESS_ID", mallGoods.getId());
        attachmentService.removeByMap(params);
        List<MallAttachmentEntity> attachmentEntities = mallGoods.getAttachmentEntityList();
        if (null != attachmentEntities && attachmentEntities.size() > 0) {
            int i = 0;
            for (MallAttachmentEntity attachmentEntity : attachmentEntities) {
                attachmentEntity.setOrderSort(i++);
                attachmentEntity.setBussinessId(mallGoods.getId());
            }
        }
        attachmentService.saveBatch(attachmentEntities);

        //商品详情页参数列表
        params = new HashMap<>(2);
        params.put("GOODS_ID", mallGoods.getId());
        goodsAttributeService.removeByMap(params);
        List<MallGoodsAttributeEntity> goodsAttributeEntityList = mallGoods.getGoodsAttributeEntityList();
        if (null != goodsAttributeEntityList && goodsAttributeEntityList.size() > 0) {
            for (MallGoodsAttributeEntity goodsAttributeEntity : goodsAttributeEntityList) {
                goodsAttributeEntity.setGoodsId(mallGoods.getId());
            }
        }
        goodsAttributeService.saveBatch(goodsAttributeEntityList);

        //商品SKU键表
        specificationService.removeByMap(params);
        List<MallSpecificationEntity> specificationEntityList = mallGoods.getSpecificationEntityList();
        if (null != specificationEntityList && specificationEntityList.size() > 0) {
            for (MallSpecificationEntity specificationEntity : specificationEntityList) {
                specificationEntity.setGoodsId(mallGoods.getId());
            }
        }
        specificationService.saveBatch(specificationEntityList);
        return this.updateById(mallGoods);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public MallGoodsEntity queryById(String id) {
        MallGoodsEntity goodsEntity = this.getById(id);

        Map<String, Object> params = new HashMap<>(4);

        //轮播图
        params.put("bussinessId", goodsEntity.getId());
        List<MallAttachmentEntity> attachmentEntities = attachmentService.queryAll(params);
        goodsEntity.setAttachmentEntityList(attachmentEntities);

        //商品详情页参数列表
        params = new HashMap<>(4);
        params.put("goodsId", goodsEntity.getId());
        List<MallGoodsAttributeEntity> goodsAttributeEntityList = goodsAttributeService.queryAll(params);
        goodsEntity.setGoodsAttributeEntityList(goodsAttributeEntityList);

        //商品SKU键表
        List<MallSpecificationEntity> specificationEntityList = specificationService.queryAll(params);
        goodsEntity.setSpecificationEntityList(specificationEntityList);

        //商品SKU值表
        List<MallGoodsSkuEntity> goodsSkuEntityList = goodsSkuService.queryAll(params);
        goodsEntity.setGoodsSkuEntityList(goodsSkuEntityList);
        return goodsEntity;
    }

    @Override
    public int changeSale(String[] ids) {
        MallGoodsEntity commentEntity = this.getById(ids[0]);
        if (commentEntity.getIsOnSale() == 1) {
            commentEntity.setIsOnSale(0);
        } else {
            commentEntity.setIsOnSale(1);
        }
        return baseMapper.updateById(commentEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean backGoodsNumber(MallOrderEntity order) {
        String orderId = order.getId();
        String shopsId = order.getShopsId();
        Integer orderType = order.getOrderType();
        //订单下的商品列表
        List<MallOrderGoodsEntity> orderGoodsEntities = orderGoodsService.lambdaQuery().eq(MallOrderGoodsEntity::getOrderId, orderId).list();
        if (null != orderGoodsEntities && orderGoodsEntities.size() > 0) {
            orderGoodsEntities.forEach(orderGoods -> {
                //秒杀订单
                if (orderType == 3) {
                    //取消的订单释放商品库存
                    MallSeckillEntity seckillEntity = seckillService.getOne(new QueryWrapper<MallSeckillEntity>().eq("GOODS_ID", order.getGoodsId()), false);
                    if (null != seckillEntity) {
                        seckillEntity.setStock(seckillEntity.getStock() + orderGoods.getNumber());
                        seckillService.update(seckillEntity);
                    }
                } else {
                    //加库存
                    MallGoodsEntity goodsVo = getById(orderGoods.getGoodsId());
                    goodsVo.setGoodsNumber(goodsVo.getGoodsNumber() + orderGoods.getNumber());
                    updateById(goodsVo);
                    //商城订单
                    if (orderType == 1) {
                        if (StringUtils.isNotBlank(orderGoods.getSkuId())) {
                            MallGoodsSkuEntity goodsSkuEntity = goodsSkuService.getById(orderGoods.getSkuId());
                            goodsSkuEntity.setGoodsNumber(goodsSkuEntity.getGoodsNumber() + orderGoods.getNumber());
                            goodsSkuService.update(goodsSkuEntity);
                        }
                    } else if (orderType == 2) {
                        //店铺自提订单
                        MallShopsGoodsEntity shopsGoodsEntity = shopsGoodsService.getOne(new LambdaQueryWrapper<MallShopsGoodsEntity>()
                                        .eq(MallShopsGoodsEntity::getGoodsId, goodsVo.getId())
                                        .eq(MallShopsGoodsEntity::getShopsId, shopsId)
                                , false);
                        shopsGoodsEntity.setGoodsNumber(shopsGoodsEntity.getGoodsNumber() + orderGoods.getNumber());
                        shopsGoodsService.update(shopsGoodsEntity);
                    }
                }
            });
        }
        return true;
    }
}
