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
package com.platform.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.Query;
import com.platform.common.utils.SmsUtil;
import com.platform.common.utils.StringUtils;
import com.platform.modules.sys.dao.SysSmsLogDao;
import com.platform.modules.sys.entity.SmsConfig;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.service.SysConfigService;
import com.platform.modules.sys.service.SysSmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 短信发送日志Service实现类
 *
 * @author 李鹏军
 * @since 2019-02-12 09:51:15
 */
@Service("sysSmsLogService")
public class SysSmsLogServiceImpl extends ServiceImpl<SysSmsLogDao, SysSmsLogEntity> implements SysSmsLogService {

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public List<SysSmsLogEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.STIME");
        params.put("asc", false);
        Page<SysSmsLogEntity> page = new Query<SysSmsLogEntity>(params).getPage();
        return page.setRecords(baseMapper.selectSysSmsLogPage(page, params));
    }

    @Override
    public void add(SysSmsLogEntity sysSmsLog) {
        this.save(sysSmsLog);
    }

    @Override
    public void update(SysSmsLogEntity sysSmsLog) {
        this.update(sysSmsLog, new QueryWrapper<>());
    }

    @Override
    public void delete(String id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public SysSmsLogEntity sendSms(String userId, SysSmsLogEntity smsLog) {
        //获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            throw new BusinessException("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getAppid())) {
            throw new BusinessException("请先配置短信平台APPID");
        }
        if (StringUtils.isNullOrEmpty(config.getAppkey())) {
            throw new BusinessException("请先配置短信APP_KEY");
        }
        if (StringUtils.isNullOrEmpty(config.getSign())) {
            throw new BusinessException("请先配置短信平台签名");
        }
        /**
         * 腾讯
         */
        SmsMultiSenderResult result;
        if (config.getType() == 1) {
            try {
                result = SmsUtil.crSendSms(config.getAppid(), config.getAppkey(), smsLog.getMobile().split(","), smsLog.getContent());
                result.details.forEach((item) -> {
                    smsLog.setSendStatus(item.result);
                    //发送成功
                    if (item.result == 0) {
                        smsLog.setSendId(item.sid);
                        smsLog.setSuccessNum(1);
                        smsLog.setReturnMsg(item.errmsg);
                    } else {
                        //发送失败
                        smsLog.setReturnMsg(item.errmsg);
                    }
                    smsLog.setUserId(userId);
                    smsLog.setSign(config.getSign());
                    smsLog.setStime(new Date());
                    save(smsLog);
                });
            } catch (Exception e) {

            }
        }
        //保存发送记录
        return smsLog;
    }
}
