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
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallOrderDao;
import com.platform.modules.mall.dao.MallShippingDao;
import com.platform.modules.mall.entity.MallOrderEntity;
import com.platform.modules.mall.entity.MallRewardPreEntity;
import com.platform.modules.mall.entity.MallShippingEntity;
import com.platform.modules.mall.service.MallDistAmountScheduledService;
import com.platform.modules.mall.service.MallOrderService;
import com.platform.modules.mall.service.MallRewardPreService;
import com.platform.modules.mall.service.WxSendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表Service实现类
 *
 * @author 李鹏军
 * @since 2019-07-05 19:29:18
 */
@Service("mallOrderService")
public class MallOrderServiceImpl extends ServiceImpl<MallOrderDao, MallOrderEntity> implements MallOrderService {
    @Autowired
    private MallShippingDao shippingDao;
    @Autowired
    private WxSendMsgService sendMsgService;
    @Autowired
    private MallDistAmountScheduledService mallDistAmountScheduledService;
    @Autowired
    private MallRewardPreService mallRewardPreService;

    @Override
    public List<MallOrderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page<MallOrderEntity> queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ADD_TIME");
        params.put("asc", false);
        Page<MallOrderEntity> page = new Query<MallOrderEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallOrderPage(page, params));
    }

    @Override
    public boolean add(MallOrderEntity mallOrder) {
        return this.save(mallOrder);
    }

    @Override
    public boolean update(MallOrderEntity mallOrder) {
        return this.updateById(mallOrder);
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
    public boolean sendGoods(MallOrderEntity orderEntity) {
        MallOrderEntity order = baseMapper.selectById(orderEntity.getId());
        if (Constant.SHOPS_ORDER.equals(order.getOrderType())) {
            throw new BusinessException("自取订单无需发货！");
        }

        //付款状态
        Integer payStatus = order.getPayStatus();
        if (!Constant.PayStatus.YFK.getValue().equals(payStatus)) {
            throw new BusinessException("此订单未付款！");
        }

        MallShippingEntity shippingEntity = shippingDao.selectById(orderEntity.getShippingId());
        if (null != shippingEntity) {
            order.setShippingName(shippingEntity.getName());
            order.setShippingCode(shippingEntity.getCode());
        }
        order.setShippingId(orderEntity.getShippingId());
        order.setShippingNo(orderEntity.getShippingNo());
        order.setPostalCode(orderEntity.getPostalCode());
        //订单已发货
        order.setConfirmTime(new Date());
        order.setOrderStatus(Constant.OrderStatus.YFH.getValue());
        //已发货
        order.setShippingStatus(Constant.ShippingStatus.YFH.getValue());
        update(order);

        //发货通知
        return sendMsgService.notifyPaySuccess(order, 5);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceive(String id) {
        MallOrderEntity orderEntity = baseMapper.selectById(id);
        //发货状态
        Integer shippingStatus = orderEntity.getShippingStatus();
        //付款状态
        Integer payStatus = orderEntity.getPayStatus();
        if (!Constant.PayStatus.YFK.getValue().equals(payStatus)) {
            throw new BusinessException("此订单未付款，不能确认收货！");
        }
        if (!Constant.ShippingStatus.YFH.getValue().equals(shippingStatus)) {
            throw new BusinessException("此订单未发货或正在退货，不能确认收货！");
        }
        orderEntity.setShippingStatus(Constant.ShippingStatus.YSH.getValue());
        orderEntity.setOrderStatus(Constant.OrderStatus.QRSH.getValue());
        update(orderEntity);

        // 添加分销定时到账记录
        mallDistAmountScheduledService.addAmountScheduled(orderEntity.getId());
        //合伙人分销

//        MallRewardRecordEntity reward = new MallRewardRecordEntity();
        MallRewardPreEntity rewardPreEntity = new MallRewardPreEntity();
        rewardPreEntity.setOrderId(orderEntity.getId());
        rewardPreEntity.setOrderSn(orderEntity.getOrderSn());
        rewardPreEntity.setStatus(0);
        rewardPreEntity.setCreateTime(new Date());
        mallRewardPreService.add(rewardPreEntity);

    }

    @Override
    public Map<String, Object> queryUserCountMap(Map<String, Object> params) {
        return baseMapper.queryUserCountMap(params);
    }

    @Override
    public MallOrderEntity queryById(String orderId) {

        return baseMapper.queryById(orderId);
    }

    @Override
    public List<Map<String, Object>> shopsGoodsSalesCount(Map<String, Object> params) {
        return baseMapper.shopsGoodsSalesCount(params);
    }

    @Override
    public List<Map<String, Object>> allShopsGoodsSalesCount(Map<String, Object> params) {
        return baseMapper.allShopsGoodsSalesCount(params);
    }
}
