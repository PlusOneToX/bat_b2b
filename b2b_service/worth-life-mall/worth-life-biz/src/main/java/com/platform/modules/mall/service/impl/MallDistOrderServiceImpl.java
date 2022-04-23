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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.modules.mall.dao.MallDistOrderDao;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 分销订单Service实现类
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Slf4j
@Service("mallDistOrderService")
public class MallDistOrderServiceImpl extends ServiceImpl<MallDistOrderDao, MallDistOrderEntity> implements MallDistOrderService {

    @Autowired
    private MallDistService mallDistService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallDistPromoService mallDistPromoService;
    @Autowired
    private MallOrderGoodsService mallOrderGoodsService;
    @Autowired
    private MallOrderService mallOrderService;
    @Autowired
    private MallUserService mallUserService;

    @Override
    public List<MallDistOrderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallDistOrderEntity> page = new Query<MallDistOrderEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistOrderPage(page, params));
    }

    @Override
    public boolean add(MallDistOrderEntity mallDistOrder) {
        return this.save(mallDistOrder);
    }

    @Override
    public boolean update(MallDistOrderEntity mallDistOrder) {
        return this.updateById(mallDistOrder);
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
    @Transactional(rollbackFor = Exception.class)
    public void confirmAudit(String[] ids, String auditStatus, String auditDesc,String ip) throws WxPayException {
        for (String id : ids) {
            final MallDistOrderEntity orderEntity = baseMapper.selectById(id);
            log.info(String.valueOf(orderEntity));
            // 订单类型
            Integer orderType = orderEntity.getType();
            if (!Constant.DistOrderType.WITHDRAW.getValue().equals(orderType)) {
                throw new BusinessException("此订单非佣金提现，不能审核！");
            }
            // 审核状态
            if (!orderEntity.getAuditStatus().equals(Constant.AuditStatus.AUDITING.getValue())) {
                throw new BusinessException("此订单已审核，不能重复审核！");
            }
            // 当前时间
            Date now = new Date();
            // 更新审核状态
            orderEntity.setAuditStatus(Constant.AuditStatus.valueOf(auditStatus).getValue());
            orderEntity.setAuditDesc(auditDesc);
            orderEntity.setIncomeTime(now);
            update(orderEntity);

            String userId = orderEntity.getUserId();
            String orderSn = orderEntity.getOrderId();
            BigDecimal income = orderEntity.getIncome().abs();
            Integer withdrawType = orderEntity.getWithdrawType();
            boolean isPayBank = withdrawType.equals(Constant.WithdrawType.PAY_BANK.getValue());
            //审核通过
            if (Constant.AuditStatus.valueOf(auditStatus).equals(Constant.AuditStatus.AUDIT_PASSED)) {
                // 企业付款
                log.info("企业付款到余额/银行卡");
                String encBankNo = orderEntity.getEncBankNo();
                String encTrueName = orderEntity.getEncTrueName();
                String bankCode = orderEntity.getBankCode();
                if (isPayBank) {
                    mallDistService.payBank(income, orderSn, encBankNo, encTrueName, bankCode);
                } else {
                    mallDistService.entPay(userId, income, orderSn,ip);
                }
            } else {
                // 审核不通过，返回用户佣金
                log.info("审核不通过");
                BigDecimal handlingFee = new BigDecimal("0.00");
                if (isPayBank) {
                    // 计算利息
                    handlingFee = mallDistService.calculateTheHandlingFee(income);
                }
                // 可提现佣金-=(本金+手续费)，已提取的佣金-=本金
                mallDistService.updateAmount(userId, income.add(handlingFee).negate(), income.negate());

                //提现审核失败则加钱
                BigDecimal amountAvailable = mallUserService.getById(userId).getBalance();
                mallUserService.lambdaUpdate().set(MallUserEntity::getBalance,amountAvailable.add(income)).eq(MallUserEntity::getId,userId).update();

            }
        }
    }

    @Override
    public MallDistOrderEntity queryById(String id) {
        return baseMapper.queryById(id);
    }

    @Override
    public Map<String, Long> getTime() {
        Map<String, Long> time = new HashMap<>();
        //当前开始
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        time.put("CUR_DAY_START", calendar.getTime().getTime());
        //当天结束
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        time.put("CUR_DAY_END", calendar.getTime().getTime());
        //当月开始
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        time.put("CUR_MONTH_START", calendar.getTime().getTime());
        //本月结束
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        time.put("CUR_MONTH_END", calendar.getTime().getTime());
        return time;
    }

    @Override
    public BigDecimal getTotalWithdraw(String userId, Timestamp startTime, Timestamp endTime) {
        return baseMapper.getTotalWithdraw(userId, startTime, endTime, Constant.DistOrderType.WITHDRAW.getValue());
    }

    @Override
    public IPage<MallDistOrderDetailEntity> distOrderPage(Page<MallDistOrderDetailEntity> page, QueryWrapper<MallDistOrderDetailEntity> queryWrapper) {
        return baseMapper.distOrderPage(page, queryWrapper);
    }

    @Override
    public BigDecimal getIncomeDetails(String userId, Timestamp startTime, Timestamp endTime) {
        List<Integer> orderType = new ArrayList<>();
        orderType.add(Constant.DistOrderType.PROXY.getValue());
        orderType.add(Constant.DistOrderType.SALE.getValue());
        List<Integer> orderStatus = new ArrayList<>();
        orderStatus.add(Constant.OrderStatus.QRSH.getValue());
        return baseMapper.getIncomeDetails(userId, startTime, endTime, orderType, orderStatus);
    }

    @Override
    public IPage<IncomeDetailsEntity> getIncomeDetailsPage(Page<IncomeDetailsEntity> page, QueryWrapper<IncomeDetailsEntity> queryWrapper) {
        return baseMapper.getIncomeDetailsPage(page, queryWrapper);
    }

    @Override
    public Integer getOrderCount(String userId) {
        return baseMapper.selectCount(new QueryWrapper<MallDistOrderEntity>().eq("USER_ID", userId).isNotNull("COMMISSION_TYPE"));
    }

    /**
     * 支付时调用创建分销/推广提成订单
     */
    @Override
    public void addDistOrder(String userId, String orderId) {
        if (userId == null || orderId == null) {
            return;
        }

        // 根据订单号获取订单实付价、总价
        MallOrderEntity orderEntity = mallOrderService.getById(orderId);
        BigDecimal actualPrice = orderEntity.getActualPrice();
        BigDecimal orderPrice = orderEntity.getOrderPrice();

        // 根据订单号查找【推广追踪表】区分分销订单与推广订单
        List<MallDistPromoEntity> promoList = mallDistPromoService.getPromoListByOrderId(orderId);
        // 根据订单号查找包含的商品
        List<MallOrderGoodsEntity> goodsList = mallOrderGoodsService.getGoodsList(orderId);
        if (promoList.size() != 0) {
            if (goodsList.size() == promoList.size()) {
                // 该订单的商品均为推广订单
                addDistPromoOrder(userId, promoList, goodsList, actualPrice, orderPrice);
                return;
            } else {
                // 部分为推广订单，通过addDistPromoOrder方法生成推广订单并剔除推广商品
                goodsList = addDistPromoOrder(userId, promoList, goodsList, actualPrice, orderPrice);
            }
        }
        // 处理分销订单
        for (MallOrderGoodsEntity goods : goodsList) {
            BigDecimal goodsActualPrice = calcActualPrice(goods.getRetailPrice(), goods.getNumber(), actualPrice, orderPrice);
            addDistProxyOrder(userId, orderId, goods.getGoodsId(), goods.getSkuId(), goodsActualPrice);
        }
    }

    @Override
    public List<MallDistOrderEntity> getDistOrderList(QueryWrapper<MallDistOrderEntity> qw) {
        return baseMapper.selectList(qw);
    }

    private List<MallOrderGoodsEntity> addDistPromoOrder(String userId, List<MallDistPromoEntity> promoList, List<MallOrderGoodsEntity> goodsList, BigDecimal actualPrice, BigDecimal orderPrice) {
        promoList.forEach(promo -> {
            for (MallOrderGoodsEntity goods : goodsList) {
                String goodsId = goods.getGoodsId();
                String skuId = goods.getSkuId();
                String orderId = goods.getOrderId();
                String promoGoodsId = promo.getGoodsId();
                String promoSkuId = promo.getSkuId();
                if (goodsId.equals(promoGoodsId)
                        && ((skuId == null && promo == null ) || (skuId != null && promo != null && skuId.equals(promoSkuId)))) {
                    // 一级推广提成
                    MallDistEntity fMallDist = mallDistService.getByUserId(promo.getUserId());
                    if (fMallDist == null) {
                        return; // 本次循环无分销商记录，开启下一次循环
                    }
                    BigDecimal goodsActualPrice = calcActualPrice(goods.getRetailPrice(), goods.getNumber(), actualPrice, orderPrice);
                    addDistOrder(fMallDist.getId(), userId, orderId, goodsId, skuId, goodsActualPrice, Constant.CommissionType.PROMO_1);
                    Integer sDistId =fMallDist.getSuperiorId();
                    if (sDistId != null) {
                        // 二级推广提成
                        addDistOrder(sDistId, userId, orderId, goodsId, skuId, goodsActualPrice, Constant.CommissionType.PROMO_2);
                    }
                    goodsList.remove(goods);//剔除推广订单，剩余均可通过分销订单处理
                    return;
                }
            }
        });
        return goodsList;
    }

    /**
     * 商品实付价格 = (商品零售价 * 商品个数 / 订单总价) * 订单实付价
     * @param retailPrice   商品零售价
     * @param number        商品个数
     * @param actualPrice   订单实付价
     * @param orderPrice    订单总价
     * @return
     */
    private BigDecimal calcActualPrice(BigDecimal retailPrice, Integer number, BigDecimal actualPrice, BigDecimal orderPrice) {

        BigDecimal d = retailPrice.multiply(BigDecimal.valueOf(number)).multiply(actualPrice).divide(orderPrice);
        // 保留小数点后两位并四舍五入
        DecimalFormat df = new DecimalFormat("0.00");
        return new BigDecimal(df.format(d));
    }

    // 创建代理提成订单
    private void addDistProxyOrder(String userId, String orderId, String goodsId, String skuId, BigDecimal actualPrice) {

        MallDistEntity mallDist = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("USER_ID", userId).isNotNull("SUPERIOR_ID"));
        if (mallDist == null) {
            // 不是分销商或者为一级分销商
            return;
        }
        //用户的一级分销商
        Integer fDistId = mallDist.getSuperiorId();
        addDistOrder(fDistId, userId, orderId, goodsId, skuId, actualPrice, Constant.CommissionType.DIST_1);
        //用户的二级分销商
        MallDistEntity fMallDist = mallDistService.getOne(new QueryWrapper<MallDistEntity>().eq("ID", fDistId));
        Integer sDistId =fMallDist.getSuperiorId();
        if (sDistId != null) {
            // 用户拥有二级分销商
            addDistOrder(sDistId, userId, orderId, goodsId, skuId, actualPrice, Constant.CommissionType.DIST_2);
        }
    }

    /**
     * @param fDistId        提成者分销id（确定为分销商）
     * @param buyerId        购买者id
     * @param orderId        订单id
     * @param goodsId        商品id
     * @param skuId          商品sku
     * @param actualPrice    实际付款金额
     * @param commissionType 提成类型
     */
    private void addDistOrder(Integer fDistId, String buyerId, String orderId, String goodsId, String skuId, BigDecimal actualPrice, Constant.CommissionType commissionType) {
        String userId = mallDistService.getById(fDistId).getUserId();
        // 分销订单类型
        Constant.DistOrderType type;
        // 提成类型
        String sCommission;
        switch (commissionType) {
            case DIST_1:
                type = Constant.DistOrderType.PROXY;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_DIST_1, "0.00");
                break;
            case DIST_2:
                type = Constant.DistOrderType.PROXY;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_DIST_2, "0.00");
                break;
            case PROMO_1:
                type = Constant.DistOrderType.SALE;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_PROMO_1, "0.00");
                break;
            case PROMO_2:
                type = Constant.DistOrderType.SALE;
                sCommission = sysConfigService.getValue(Constant.COMMISSION_TYPE_PROMO_2, "0.00");
                break;
            default:
                throw new BusinessException("找不到提成类型");
        }
        BigDecimal commission = new BigDecimal(sCommission);
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal income = new BigDecimal(df.format(actualPrice.multiply(commission)));

        MallDistOrderEntity mallDistOrderEntity = new MallDistOrderEntity();
        mallDistOrderEntity.setUserId(userId);
        mallDistOrderEntity.setBuyerId(buyerId);
        mallDistOrderEntity.setOrderId(orderId);
        mallDistOrderEntity.setType(type.getValue());
        mallDistOrderEntity.setIncome(income);
        mallDistOrderEntity.setIncomeTime(new Date());
        mallDistOrderEntity.setCommissionType(commissionType.getValue());
        mallDistOrderEntity.setCommission(commission);
        mallDistOrderEntity.setAuditStatus(1); // 提成订单不用审核
        mallDistOrderEntity.setGoodsId(goodsId);
        mallDistOrderEntity.setSkuId(skuId);
        // 保存分销订单
        this.save(mallDistOrderEntity);
    }
}
