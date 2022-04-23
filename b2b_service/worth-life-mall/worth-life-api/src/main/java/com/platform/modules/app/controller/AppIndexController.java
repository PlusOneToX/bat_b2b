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
package com.platform.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.common.utils.*;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.*;
import com.platform.modules.sys.entity.SmsConfig;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.service.SysConfigService;
import com.platform.modules.sys.service.SysSmsLogService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/index")
@Api(tags = "AppIndexController|APP首页接口")
public class AppIndexController extends AppBaseController {
    @Autowired
    private MallBannerService bannerService;
    @Autowired
    private MallChannelService channelService;
    @Autowired
    private MallBulletinService bulletinService;
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysSmsLogService smsLogService;
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallTemplateConfService templateConfService;
    @Autowired
    private JedisUtil jedisUtil;
    @Value("${tx.templateId}")
    private Integer templateId;

    /**
     * 获取协议
     */
    @IgnoreAuth
    @GetMapping("/getTreaty")
    @ApiOperation(value = "获取协议", notes = "获取协议")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "轮播ID", example = "1", required = true, dataType = "string")
    })
    public RestResponse getTreaty(String type) {
        String key = Constant.USER_TREATY;
        if ("2".equals(type)) {
            key = Constant.PRIVACY_TREATY;
        }
        String treaty = sysConfigService.getValue(key);
        return RestResponse.success().put("data", treaty);
    }

    /**
     * 获取订阅消息id
     */
    @IgnoreAuth
    @GetMapping("/getTemplateId")
    @ApiOperation(value = "获取订阅消息id", notes = "获取订阅消息id")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateTypes", value = "模板类型", example = "'1,2'", required = true, dataType = "string")
    })
    public RestResponse getTemplateId(String templateTypes) {
        Object[] templates = templateTypes.split(",");
        List<MallTemplateConfEntity> list = templateConfService.list(
                new QueryWrapper<MallTemplateConfEntity>().in("TEMPLATE_TYPE", templates));

        List<String> templateIds = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (MallTemplateConfEntity item : list) {
                templateIds.add(item.getTemplateId());
            }
        }
        return RestResponse.success().put("data", templateIds);
    }

    /**
     * 首页轮播图列表
     */
    @IgnoreAuth
    @GetMapping("/bannerList")
    @ApiOperation(value = "首页轮播图列表", notes = "首页轮播图列表")
    public RestResponse bannerList() {
        List<MallBannerEntity> bannerEntityList = bannerService.list(
                new QueryWrapper<MallBannerEntity>().eq("ENABLED", 1).ge("END_TIME", new Date()));
        return RestResponse.success().put("data", bannerEntityList);
    }

    /**
     * 首页轮播图详情
     */
    @IgnoreAuth
    @GetMapping("/bannerDetail")
    @ApiOperation(value = "首页轮播详情", notes = "首页轮播为文本（mediaType=2）时，根据轮播ID获取详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bannerId", value = "轮播ID", example = "1", required = true, dataType = "string")
    })
    public RestResponse bannerDetail(String bannerId) {
        MallBannerEntity bannerEntity = bannerService.getById(bannerId);
        return RestResponse.success().put("data", bannerEntity);
    }

    /**
     * 首页频道列表
     */
    @IgnoreAuth
    @GetMapping("/channelList")
    @ApiOperation(value = "首页频道列表", notes = "首页频道列表")
    public RestResponse channelList() {
        List<MallChannelEntity> bannerEntityList = channelService.list(
                new QueryWrapper<MallChannelEntity>().eq("STATUS", 1).orderByDesc("SORT"));
        return RestResponse.success().put("data", bannerEntityList);
    }

    /**
     * 商城公告列表
     */
    @IgnoreAuth
    @GetMapping("/bulletinList")
    @ApiOperation(value = "商城公告列表", notes = "商城公告列表")
    public RestResponse bulletinList() {
        List<MallBulletinEntity> bulletinEntityList = bulletinService.list(
                new QueryWrapper<MallBulletinEntity>().eq("ENABLED", 1).orderByDesc("SORT"));
        return RestResponse.success().put("data", bulletinEntityList);
    }

    /**
     * 个人中心统计
     */
    @GetMapping("userCount")
    @ApiOperation(value = "个人中心统计", notes = "个人中心统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string")
    })
    public RestResponse userCount(@LoginUser MallUserEntity user) {
        // 统计个人中心订单数据
        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", user.getId());
        return RestResponse.success().put("countMap", orderService.queryUserCountMap(params));
    }

    /**
     * 获取是否开启余额支付功能
     * 1: 开启（默认）
     * 2：禁用
     */
    @IgnoreAuth
    @GetMapping("getRechargeStatus")
    @ApiOperation(value = "获取是否开启余额支付功能", notes = "获取是否开启余额支付功能")
    public RestResponse getRechargeStatus() {
        return RestResponse.success().put("rechargeStatus", sysConfigService.getValue(Constant.RECHARGE_STATUS, "1"));
    }

    /**
     * 获取是否开启分销功能
     * 1: 开启（默认）
     * 2：禁用
     */
    @IgnoreAuth
    @GetMapping("getDistributionStatus")
    @ApiOperation(value = "获取是否开启分销功能", notes = "获取是否开启分销功能")
    public RestResponse getDistributionStatus() {
        return RestResponse.success().put("distributionStatus", sysConfigService.getValue(Constant.DISTRIBUTION_STATUS, "2"));
    }

    /**
     * 发送短信
     */
    @IgnoreAuth
    @PostMapping("smsCode")
    @ApiOperation(value = "发送短信", notes = "发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "phone", value = "15209831990")
            }), required = true, dataType = "string")
    })
    public RestResponse smsCode() {
        JSONObject jsonParams = getJsonRequest();
        String phone = jsonParams.getString("phone");
        if (!isMobile(phone)) {
            return RestResponse.error("请输入正确的手机号");
        }
        // 五分钟之内不能重复发送短信
        String code = jedisUtil.get(Constant.PRE_SMS + phone);
        if (StringUtils.isNotBlank(code)) {
            return RestResponse.success("短信已发送");
        }

        //生成验证码
        String smsCode = CharUtil.getRandomNum(6);
        //获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            return RestResponse.error("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getAppid())) {
            return RestResponse.error("请先配置短信平台APPID");
        }
        if (StringUtils.isNullOrEmpty(config.getAppkey())) {
            return RestResponse.error("请先配置短信APP_KEY");
        }
        if (StringUtils.isNullOrEmpty(config.getSign())) {
            return RestResponse.error("请先配置短信平台签名");
        }
        // 发送短信
        SmsSingleSenderResult result;

        try {
            /**
             * 您的验证码是{1}，请于{2}分钟内填写。如非本人操作，请忽略本短信。
             */
            result = SmsUtil.crSendSms(config.getAppid(), config.getAppkey(), phone, templateId, new String[]{smsCode, "15"}, config.getSign());
            //发送成功之后将验证码存入redis，有效期为15分钟
            jedisUtil.set(Constant.PRE_SMS + phone, smsCode, 900);

            SysSmsLogEntity smsLogVo = new SysSmsLogEntity();
            smsLogVo.setUserId("0");
            smsLogVo.setTemplateId(templateId);
            smsLogVo.setCode(smsCode);
            smsLogVo.setMobile(phone);
            smsLogVo.setStime(new Date());
            smsLogVo.setSign(config.getSign());
            smsLogVo.setReturnMsg(result.errMsg);

            if (result.result == 0) {
                smsLogVo.setSendStatus(result.result);
                smsLogVo.setSendId(result.sid);
                smsLogVo.setSuccessNum(1);
                smsLogService.save(smsLogVo);
                return RestResponse.success("短信发送成功");
            } else {
                smsLogVo.setSuccessNum(0);
                smsLogVo.setSendStatus(1);
                smsLogService.save(smsLogVo);
                return RestResponse.error("短信发送失败");
            }
        } catch (Exception e) {
            return RestResponse.error("短信发送失败：" + e);
        }
    }

    /**
     * 绑定手机
     */
    @PostMapping("bindMobile")
    @ApiOperation(value = "绑定手机", notes = "校验验证码绑定手机")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "mobileCode", value = "1234"),
                    @ExampleProperty(mediaType = "password", value = "123456")
            }), required = true, dataType = "string")
    })
    public RestResponse bindMobile(@LoginUser MallUserEntity loginUser) {
        JSONObject jsonParams = getJsonRequest();

        String mobile = jsonParams.getString("mobile");
        String mobileCode = jsonParams.getString("mobileCode");
        String password = jsonParams.getString("password");

        if (!isMobile(mobile)) {
            return RestResponse.error("请输入正确的手机号");
        }
        String smsCode = jedisUtil.get(Constant.PRE_SMS + mobile);
        if (StringUtils.isNullOrEmpty(smsCode)) {
            return RestResponse.error("验证码已失效，请重新获取");
        }
        if (!mobileCode.equals(smsCode)) {
            return RestResponse.error("验证码错误");
        }
        MallUserEntity userVo = userService.getById(loginUser.getId());
        userVo.setMobile(mobile);
        userVo.setPassword(DigestUtils.sha256Hex(password));
        userService.update(userVo);

        //验证通过后删除redis中的验证码
        jedisUtil.del(Constant.PRE_SMS + mobile);
        return RestResponse.success("手机绑定成功");
    }

    /**
     * 根据手机号修改密码
     */
    @IgnoreAuth
    @PostMapping("modifyPw")
    @ApiOperation(value = "修改密码", notes = "校验验证码修改密码，无需用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonParam", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "mobileCode", value = "1234"),
                    @ExampleProperty(mediaType = "password", value = "123456")
            }), required = true, dataType = "string")
    })
    public RestResponse modifyPw() {
        JSONObject jsonParams = getJsonRequest();

        String mobile = jsonParams.getString("mobile");
        String mobileCode = jsonParams.getString("mobileCode");
        String password = jsonParams.getString("password");

        if (!isMobile(mobile)) {
            return RestResponse.error("请输入正确的手机号");
        }
        String smsCode = jedisUtil.get(Constant.PRE_SMS + mobile);
        if (StringUtils.isNullOrEmpty(smsCode)) {
            return RestResponse.error("验证码已失效，请重新获取");
        }
        if (!mobileCode.equals(smsCode)) {
            return RestResponse.error("验证码错误");
        }
        MallUserEntity userVo = userService.queryByMobile(mobile);
        userVo.setMobile(mobile);
        userVo.setPassword(DigestUtils.sha256Hex(password));
        userService.update(userVo);

        //验证通过后删除redis中的验证码
        jedisUtil.del(Constant.PRE_SMS + mobile);
        return RestResponse.success("手机绑定成功");
    }

    /**
     * 验证手机号
     *
     * @param mobile
     * @return
     */
    private boolean isMobile(String mobile) {
        if (StringUtils.isNotBlank(mobile)) {
            String s2 = "^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";
            Pattern p = Pattern.compile(s2);
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
        return false;
    }
}
