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
import com.github.binarywang.wxpay.bean.entpay.EntPayBankRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayBankResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallDistDao;
import com.platform.modules.mall.entity.MallDistAmountScheduledEntity;
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.entity.MallDistTeamEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.service.MallDistOrderService;
import com.platform.modules.mall.service.MallDistService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 分销表Service实现类
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
@Slf4j
@Service("mallDistService")
public class MallDistServiceImpl extends ServiceImpl<MallDistDao, MallDistEntity> implements MallDistService {

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private MallDistOrderService mallDistOrderService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MallUserService mallUserService;

    @Override
    public List<MallDistEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<MallDistEntity> page = new Query<MallDistEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallDistPage(page, params));
    }

    @Override
    public boolean add(MallDistEntity mallDist) {
        return this.save(mallDist);
    }

    @Override
    public boolean update(MallDistEntity mallDist) {
        return this.updateById(mallDist);
    }

    @Override
    public boolean delete(Integer id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public MallDistEntity queryById(Integer id) {
        return baseMapper.queryById(id);
    }

    @Override
    public void confirmAudit(Integer id) {
        final MallDistEntity distEntity = baseMapper.selectById(id);
        // 审核状态
        if (distEntity.getIsAudit()) {
            throw new BusinessException("此订单已审核，不能重复审核！");
        }
        // 更新审核状态
        distEntity.setIsAudit(true);
        update(distEntity);
    }

    @Override
    public MallDistEntity queryByUserId(String userId) {
        return baseMapper.queryByUserId(userId);
    }

    @Override
    public IPage<MallDistTeamEntity> teamPage(Page<MallDistTeamEntity> page, QueryWrapper<MallDistTeamEntity> queryWrapper) {
        return baseMapper.queryTeam(page, queryWrapper);
    }

    @Override
    public String dealNickname(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            return null;
        }

        String centerString = "**";
        if (nickname.length() <= 2) {
            return centerString;
        }

        nickname = nickname.substring(0, 1) + centerString + nickname.substring(nickname.length() - 1);
        return nickname;
    }

    @Override
    public Map<String, BigDecimal> checkAmountAvailable(String userId, Integer typeValue, Map<String, BigDecimal> amountMap) {
        BigDecimal amount = amountMap.get("amount");

        if (getNumberOfDecimalPlaces(amount) > 2) {
            throw new BusinessException("金额小数位数超过2位！");
        }

        // 当次提现金额区间
        String singleLowest = sysConfigService.getValue(Constant.WITHDRAW_SINGLE_LOWEST, "1");
        String singleHighest = sysConfigService.getValue(Constant.WITHDRAW_SINGLE_HIGHEST, "5000");
        log.info(singleLowest + "~" + singleHighest);
        if (amount.compareTo(new BigDecimal(singleLowest)) < 0 || amount.compareTo(new BigDecimal(singleHighest)) > 0) {
            throw new BusinessException("不符合提现额度范围：" + singleLowest + "~" + singleHighest);
        }

        // 当日提现金额上限
        BigDecimal dayHighest = new BigDecimal(sysConfigService.getValue(Constant.WITHDRAW_DAY_HIGHEST, "5000"));
        Map<String, Long> time = mallDistOrderService.getTime();
        BigDecimal todayWithdraw = mallDistOrderService.getTotalWithdraw(
                userId,
                new Timestamp(time.get("CUR_DAY_START")),
                new Timestamp(time.get("CUR_DAY_END"))
        );// 当日已提现金额
        todayWithdraw = todayWithdraw == null ? new BigDecimal("0.00") : todayWithdraw.abs();
        log.info("用户" + userId + "当日已提现金额：" + todayWithdraw);
        // 当日剩余可提取额度
        BigDecimal withdrawQuota = dayHighest.subtract(todayWithdraw);
        if (withdrawQuota.subtract(amount).compareTo(new BigDecimal("0")) < 0) {
            throw new BusinessException("金额超过可提取额度，今日还可提取额度：" + withdrawQuota);
        }

        //用户钱包额度
        BigDecimal amountAvailable = mallUserService.getById(userId).getBalance();

        if(amountAvailable.compareTo(amount)<1){
            throw new BusinessException("不可大于可提现金额，请重新输入！");
        }

/*        MallDistEntity mallDistEntity = this.queryByUserId(userId);
        // 可提取金额
        BigDecimal amountAvailable = mallDistEntity.getAmountAvailable();
        if (amountAvailable.compareTo(amount) < 0) {
            throw new BusinessException("不可大于可提现金额，请重新输入！");
        }*/

        // 提现类型为余额
        if (typeValue == 1) {
            return amountMap;
        }

        // 提现类型为银行卡
        BigDecimal handlingFee = calculateTheHandlingFee(amount);// 手续费
        BigDecimal deductionAmount = amount.add(handlingFee);// 实际扣除金额
        // 余额不足时使用“全部提现”功能，可提取扣除服务费后的最大可提现金额
        // “全部提现”逻辑：提现金额=最大可提现额度-对应的手续费，再计算提现金额的手续费
        if (amountAvailable.compareTo(deductionAmount) < 0) {
            amount = amountAvailable.subtract(calculateTheHandlingFee(amountAvailable));
            // 扣取手续费后≤0，不可提取，这种情况只有当提取金额小于1时出现
            if (amount.compareTo(new BigDecimal("0")) <= 0) {
                throw new BusinessException("可提现金额不足");
            }
            deductionAmount = amount.add(calculateTheHandlingFee(amount));
        }
        amountMap.put("amount", amount);
        amountMap.put("deductionAmount", deductionAmount);
        return amountMap;
    }

    @Override
    public void updateAmount(String userId, BigDecimal deductionAmount, BigDecimal amount) {
        this.baseMapper.updateAmount(userId, deductionAmount, amount);
    }

    @Override
    public EntPayResult entPay(String userId, BigDecimal amount, String orderSn,String ip) throws WxPayException {
        MallUserEntity userEntity = mallUserService.getById(userId);

        EntPayRequest request = new EntPayRequest();
        request.setPartnerTradeNo(orderSn);
        request.setOpenid(userEntity.getOpenId());
        // 不校验真实姓名
        request.setCheckName("NO_CHECK");
        request.setReUserName(userEntity.getNickname());
        request.setDescription("佣金提现");
        request.setSpbillCreateIp(ip);
        // amount是以分为单位
        request.setAmount(amount.multiply(new BigDecimal(100)).intValue());

        return wxPayService.getEntPayService().entPay(request);
    }

    @Override
    public EntPayBankResult payBank(BigDecimal amount, String orderSn, String bankNo, String trueName, String bankCode) throws WxPayException {
        EntPayBankRequest request = new EntPayBankRequest();

        request.setPartnerTradeNo(orderSn);
        request.setEncBankNo(bankNo);
        request.setEncTrueName(trueName);
        request.setBankCode(bankCode);
        request.setAmount(amount.multiply(new BigDecimal(100)).intValue());
        request.setDescription("佣金提现");

        return wxPayService.getEntPayService().payBank(request);
    }

    /**
     * 手续费：按金额0.1%收取，最低1元，最高25元
     *
     * @param amount amount
     * @return BigDecimal
     */
    @Override
    public BigDecimal calculateTheHandlingFee(BigDecimal amount) {
        BigDecimal handlingFee = new BigDecimal("1");
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            handlingFee = amount.divide(new BigDecimal("1000"));
        }
        if (amount.compareTo(new BigDecimal("25000")) > 0) {
            handlingFee = new BigDecimal("25");
        }
        // 保留小数点后两位并四舍五入
        DecimalFormat df = new DecimalFormat("0.00");
        return new BigDecimal(df.format(handlingFee));
    }

    @Override
    public List<Integer> getFirstTeamIds(Integer id) {
        return this.baseMapper.getFirstTeamIds(id);
    }

    @Override
    public Integer getSecondTeamCount(List<Integer> firstTeamIds) {
        if (firstTeamIds.size() == 0) {
            return 0;
        }

        return this.baseMapper.selectCount(new QueryWrapper<MallDistEntity>().in("SUPERIOR_ID", firstTeamIds));
    }

    @Override
    public MallDistEntity getInvitationInfo(String userId) {
        return this.baseMapper.getInvitationInfo(userId);
    }

    @Override
    public void addAmountAvailable(MallDistAmountScheduledEntity amount) {
        this.baseMapper.addAmountAvailable(amount.getIncome(), amount.getUserId());
    }

    @Override
    public MallDistEntity getByUserId(String userId) {
        return baseMapper.queryByUserId(userId);
    }


    private Integer getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        return index < 0 ? 0 : string.length() - index - 1;
    }
}
