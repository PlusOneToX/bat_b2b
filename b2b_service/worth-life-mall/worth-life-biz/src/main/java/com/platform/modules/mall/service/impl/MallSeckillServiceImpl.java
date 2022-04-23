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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.annotation.ServiceLimit;
import com.platform.common.utils.*;
import com.platform.modules.mall.dao.MallOrderGoodsDao;
import com.platform.modules.mall.dao.MallSeckillDao;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallOrderGoodsEntity;
import com.platform.modules.mall.entity.MallSeckillEntity;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.MallSeckillService;
import com.platform.modules.mall.service.WxSendMsgService;
import com.platform.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 秒杀库存表Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-07 12:05:21
 */
@Service("mallSeckillService")
public class MallSeckillServiceImpl extends ServiceImpl<MallSeckillDao, MallSeckillEntity> implements MallSeckillService {
    /**
     * 超时时间 10s
     */
    private static final int TIMEOUT = 10 * 1000;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private MallOrderGoodsDao orderGoodsDao;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private WxSendMsgService sendMsgService;

    @Override
    public List<MallSeckillEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<MallSeckillEntity> page = new Query<MallSeckillEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallSeckillPage(page, params));
    }

    @Override
    public boolean add(MallSeckillEntity mallSeckill) {
        return this.save(mallSeckill);
    }

    @Override
    public boolean update(MallSeckillEntity mallSeckill) {
        return this.updateById(mallSeckill);
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

    @ServiceLimit
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse startSeckill(String seckillId, String userId, Integer fromType) {
        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if (!redisLock.lock(seckillId, String.valueOf(time))) {
            return RestResponse.error(SeckillStatEnum.MUCH.getInfo());
        }

        MallSeckillEntity skill = baseMapper.selectById(seckillId);

        if (new Date().before(skill.getStartTime())) {
            return RestResponse.error(SeckillStatEnum.NOT_TIME.getInfo());
        }
        if (new Date().after(skill.getEndTime())) {
            return RestResponse.error(SeckillStatEnum.END.getInfo());
        }
        if (skill.getStock() <= 0) {
            return RestResponse.error(SeckillStatEnum.GOODS_END.getInfo());
        }

        //秒杀成功，库存-1
        skill.setStock(skill.getStock() - 1);
        //使用updateById，数据库乐观锁
        this.updateById(skill);

        //秒杀到的商品
        String goodsId = skill.getGoodsId();
        MallOrderEntity orderEntity = new MallOrderEntity();
        orderEntity.setGoodsId(goodsId);
        orderEntity.setFromType(fromType);
        orderEntity.setGoodsCount(1);
        orderEntity.setOrderPrice(skill.getPrice());

        BigDecimal shippingFee = BigDecimal.valueOf(0);
        //免邮门槛
        String shippingFeeFre = sysConfigService.getValue(Constant.SHIPPING_FEE_FREE, "80");
        if (skill.getPrice().compareTo(new BigDecimal(shippingFeeFre)) <= 0) {
            shippingFee = new BigDecimal(sysConfigService.getValue(Constant.SHIPPING_FEE, "12"));
        }

        orderEntity.setShippingFee(shippingFee);
        orderEntity.setActualPrice(skill.getPrice().add(shippingFee));
        orderEntity.setOrderType(Constant.SECKILL_ORDER);
        orderEntity.setOrderSn(StringUtils.generateOrderNumber());
        orderEntity.setUserId(userId);
        orderEntity.setOrderStatus(Constant.OrderStatus.DXD.getValue());
        orderEntity.setShippingStatus(Constant.ShippingStatus.WFH.getValue());
        orderEntity.setPayStatus(Constant.PayStatus.WFK.getValue());
        orderEntity.setAddTime(new Date());
        orderEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), Integer.parseInt(sysConfigService.getValue(Constant.ORDER_EXPIRE, "30"))));
        orderService.save(orderEntity);

        MallOrderGoodsEntity orderGoodsEntity = new MallOrderGoodsEntity();
        orderGoodsEntity.setOrderId(orderEntity.getId());
        orderGoodsEntity.setGoodsId(orderEntity.getGoodsId());
        orderGoodsEntity.setGoodsName(skill.getName());
        orderGoodsEntity.setListPicUrl(skill.getListPicUrl());
        orderGoodsEntity.setMarketPrice(skill.getMarketPrice());
        orderGoodsEntity.setRetailPrice(skill.getPrice());
        orderGoodsEntity.setNumber(1);
        orderGoodsDao.insert(orderGoodsEntity);

        sendMsgService.notifyPaySuccess(orderEntity, 4);

        //解锁
        redisLock.unlock(seckillId, String.valueOf(time));

        return RestResponse.success(SeckillStatEnum.SUCCESS.getInfo());
    }
}
