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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallCouponDao;
import com.platform.modules.mall.entity.MallCouponBrandEntity;
import com.platform.modules.mall.entity.MallCouponEntity;
import com.platform.modules.mall.entity.MallCouponGoodsEntity;
import com.platform.modules.mall.entity.MallUserCouponEntity;
import com.platform.modules.mall.service.MallCouponBrandService;
import com.platform.modules.mall.service.MallCouponGoodsService;
import com.platform.modules.mall.service.MallCouponService;
import com.platform.modules.mall.service.MallUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 优惠券列表Service实现类
 *
 * @author 李鹏军
 * @since 2020-04-10 13:52:21
 */
@Service("mallCouponService")
public class MallCouponServiceImpl extends ServiceImpl<MallCouponDao, MallCouponEntity> implements MallCouponService {
    @Autowired
    private MallCouponGoodsService couponGoodsService;
    @Autowired
    private MallCouponBrandService couponBrandService;
    @Autowired
    private MallUserCouponService userCouponService;

    @Override
    public List<MallCouponEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<MallCouponEntity> page = new Query<MallCouponEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallCouponPage(page, params));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(MallCouponEntity mallCoupon) {
        this.save(mallCoupon);

        List<MallCouponGoodsEntity> goodsList = new ArrayList<>();
        List<String> goodsIds = mallCoupon.getGoodsIds();
        MallCouponGoodsEntity couponGoodsEntity;
        if (mallCoupon.getLimitType() == 1 && null != goodsIds && goodsIds.size() > 0) {
            for (String goodsId : goodsIds) {
                couponGoodsEntity = new MallCouponGoodsEntity();
                couponGoodsEntity.setGoodsId(goodsId);
                couponGoodsEntity.setCouponId(mallCoupon.getId());
                goodsList.add(couponGoodsEntity);
            }
            couponGoodsService.saveBatch(goodsList);
        }
        List<MallCouponBrandEntity> brandList = new ArrayList<>();
        List<String> brandIds = mallCoupon.getBrandIds();
        MallCouponBrandEntity mallCouponBrandEntity;
        if (mallCoupon.getLimitType() == 2 && null != brandIds && brandIds.size() > 0) {
            for (String brandId : brandIds) {
                mallCouponBrandEntity = new MallCouponBrandEntity();
                mallCouponBrandEntity.setBrandId(brandId);
                mallCouponBrandEntity.setCouponId(mallCoupon.getId());
                brandList.add(mallCouponBrandEntity);
            }
            couponBrandService.saveBatch(brandList);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MallCouponEntity mallCoupon) {
        List<MallCouponGoodsEntity> goodsList = new ArrayList<>();
        List<MallCouponBrandEntity> brandList = new ArrayList<>();

        List<String> goodsIds = mallCoupon.getGoodsIds();
        List<String> brandIds = mallCoupon.getBrandIds();

        MallCouponGoodsEntity couponGoodsEntity;
        MallCouponBrandEntity couponBrandEntity;

        Map<String, Object> map = new HashMap<>(2);
        map.put("COUPON_ID", mallCoupon.getId());
        couponGoodsService.removeByMap(map);
        couponBrandService.removeByMap(map);
        if (mallCoupon.getLimitType() == 1 && null != goodsIds && goodsIds.size() > 0) {
            for (String goodsId : goodsIds) {
                couponGoodsEntity = new MallCouponGoodsEntity();
                couponGoodsEntity.setGoodsId(goodsId);
                couponGoodsEntity.setCouponId(mallCoupon.getId());
                goodsList.add(couponGoodsEntity);
            }
            couponGoodsService.saveBatch(goodsList);
        }
        if (mallCoupon.getLimitType() == 2 && null != brandIds && brandIds.size() > 0) {
            for (String brandId : brandIds) {
                couponBrandEntity = new MallCouponBrandEntity();
                couponBrandEntity.setBrandId(brandId);
                couponBrandEntity.setCouponId(mallCoupon.getId());
                brandList.add(couponBrandEntity);
            }
            couponBrandService.saveBatch(brandList);
        }
        return this.updateById(mallCoupon);
    }

    @Override
    public boolean delete(String id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("COUPON_ID", id);
        couponGoodsService.removeByMap(map);
        couponBrandService.removeByMap(map);
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        for (String id : ids) {
            this.delete(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public boolean sendUser(Map<String, Object> parmas) {
        String id = (String) parmas.get("id");
        MallCouponEntity couponEntity = this.getById(id);

        List<String> userIds = (List<String>) parmas.get("userIds");

        List<MallUserCouponEntity> userCouponEntityList = new ArrayList<>();
        MallUserCouponEntity userCouponEntity;
        for (int i = 0; i < userIds.size(); i++) {
            userCouponEntity = new MallUserCouponEntity();
            userCouponEntity.setUserId(userIds.get(i));
            userCouponEntity.setCouponId(id);
            userCouponEntity.setAddTime(new Date());
            userCouponEntity.setType(0);
            userCouponEntity.setStatus(0);
            userCouponEntityList.add(userCouponEntity);

            //检查会员拥有的优惠券是否超过限制数量
            if (userCouponService.checkUserCouponLimit(userIds.get(i), id, couponEntity.getLimitUser())) {
                throw new BusinessException("用户拥有的优惠券超出限制！");
            }
            //发放后+1
            couponEntity.setSendCount(couponEntity.getSendCount() + 1);
            if (couponEntity.getSendCount() > couponEntity.getTotalCount()) {
                throw new BusinessException("优惠券数量不足！");
            }
            update(couponEntity);
        }
        return userCouponService.saveBatch(userCouponEntityList);
    }

    /**
     * 优惠券过期
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void expireCoupon() {
        List<MallCouponEntity> couponEntities = baseMapper.selectList(new QueryWrapper<MallCouponEntity>().eq("status", 1));
        if (couponEntities != null && couponEntities.size() > 0) {
            List<MallUserCouponEntity> userCouponEntityList = new ArrayList<>();

            List<MallCouponEntity> list = new ArrayList<>();
            couponEntities.stream().filter(item -> new Date().after(item.getEndTime())).forEach(coupon -> {
                coupon.setStatus(2);
                list.add(coupon);

                // 会员已领用的优惠券
                userCouponEntityList.addAll(userCouponService.list(new QueryWrapper<MallUserCouponEntity>().eq("COUPON_ID", coupon.getId()).eq("STATUS", 0)));
            });
            if (list.size() > 0) {
                this.updateBatchById(list);
            }
            if (userCouponEntityList.size() > 0) {
                userCouponEntityList.forEach(item -> item.setStatus(2));
                userCouponService.updateBatchById(userCouponEntityList);
            }
        }
    }
}
