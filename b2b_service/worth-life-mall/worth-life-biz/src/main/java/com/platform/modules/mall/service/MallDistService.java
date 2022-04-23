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
package com.platform.modules.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.bean.entpay.EntPayBankResult;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.platform.modules.mall.entity.MallDistAmountScheduledEntity;
import com.platform.modules.mall.entity.MallDistEntity;
import com.platform.modules.mall.entity.MallDistTeamEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 分销Service接口
 *
 * @author Cury
 * @since 2020-04-27 13:39:50
 */
public interface MallDistService extends IService<MallDistEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MallDistEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询分销
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增分销
     *
     * @param mallDist 分销
     * @return 新增结果
     */
    boolean add(MallDistEntity mallDist);

    /**
     * 根据主键更新分销
     *
     * @param mallDist 分销
     * @return 更新结果
     */
    boolean update(MallDistEntity mallDist);

    /**
     * 根据主键删除分销
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(Integer[] ids);

    MallDistEntity queryById(Integer id);

    void confirmAudit(Integer id);

    MallDistEntity queryByUserId(String userId);

    IPage<MallDistTeamEntity> teamPage(Page<MallDistTeamEntity> page, QueryWrapper<MallDistTeamEntity> queryWrapper);

    /**
     * 只显示昵称的第一个字和最后一个字，其余字符用"**"代替，如Geeny，显示为G**y。
     * 当昵称只有一个字或两个字时，昵称用**代替。
     *
     * @param nickname nickname
     * @return String
     */
    String dealNickname(String nickname);

    /**
     * 校验金额与用户额度是否合法
     * 检查内容：
     * 金额是否在单次可提现额度区间
     * 当日金额提取总数是否超过上限
     * 金额是否超过可提取金额
     * 零钱不收取手续费，银行卡按金额0.1%收取，最低1元，最高25元
     * 每个商户号可以出款10万元，单商户给同一银行卡每天限额2万元
     *
     * @param userId    用户id
     * @param typeValue 类型值
     * @param amount    提现金额
     * @return 返回实际扣除金额
     */
    Map<String, BigDecimal> checkAmountAvailable(String userId, Integer typeValue, Map<String, BigDecimal> amount);

    void updateAmount(String userId, BigDecimal deductionAmount, BigDecimal amount);

    EntPayResult entPay(String userId, BigDecimal amount, String orderSn,String ip) throws WxPayException;

    EntPayBankResult payBank(BigDecimal amount, String orderSn, String bankNo, String trueName, String bankCode) throws WxPayException;

    // 手续费：按金额0.1%收取，最低1元，最高25元
    BigDecimal calculateTheHandlingFee(BigDecimal amount);

    List<Integer> getFirstTeamIds(Integer id);

    Integer getSecondTeamCount(List<Integer> firstTeamIds);

    MallDistEntity getInvitationInfo(String userId);

    void addAmountAvailable(MallDistAmountScheduledEntity amount);

    MallDistEntity getByUserId(String userId);
}
