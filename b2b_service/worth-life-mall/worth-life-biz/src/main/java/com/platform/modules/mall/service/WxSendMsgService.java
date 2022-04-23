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

import com.platform.modules.mall.entity.MallOrderEntity;

/**
 * 订阅消息Service接口
 *
 * @author 李鹏军
 * @since 2019-07-04 18:08:15
 */
public interface WxSendMsgService {

    /**
     * 微信通知
     *
     * @param orderInfo    订单
     * @param templateType 0:新订单提醒 1:下单成功通知 2:订单评价提醒 3:退款 4:秒杀成功通知 5:订单配送通知
     * @return boolean
     */
    boolean notifyPaySuccess(MallOrderEntity orderInfo, int templateType);
}
