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

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.platform.annotation.IgnoreAuth;
import com.platform.common.utils.*;
import com.platform.common.validator.AbstractAssert;
import com.platform.config.AliMaProperties;
import com.platform.modules.app.entity.AppleUserInfo;
import com.platform.modules.app.entity.FullUserInfo;
import com.platform.modules.mall.dto.IntegralDto;
import com.platform.modules.mall.entity.*;
import com.platform.modules.mall.service.MallUserInviteRecordService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.mall.service.impl.MallAwardConfServiceImpl;
import com.platform.modules.mall.service.impl.MallDistServiceImpl;
import com.platform.modules.mall.service.impl.MallUserIntegralServiceImpl;
import com.platform.request.WxLoginRequest;
import com.platform.utils.JwtUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * APP登录授权
 *
 * @author 李鹏军
 */
@Slf4j
@RestController
@RequestMapping("/app/auth")
@Api(tags = "AppLoginController|APP登录接口")
@EnableConfigurationProperties(AliMaProperties.class)
public class AppLoginController extends AppBaseController {
    @Autowired
    private MallUserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private AliMaProperties aliMaProperties;

    @Value("${qq.miniapp.appid}")
    private String appid;

    @Value("${qq.miniapp.secret}")
    private String secret;

    @Autowired
    private MallUserInviteRecordService mallUserInviteRecordService;

    @Autowired
    private MallDistServiceImpl mallDistService;
    @Autowired
    private JedisUtil jedisUtil;


    //伪登录获取token
    @IgnoreAuth
    @PostMapping("logindesc")
    public RestResponse logindesc(String mobile) {
        if (mobile == null)
            AbstractAssert.isBlank(mobile, "手机号不能为空");
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("mobile", mobile);
        //用户登录
        Map<String, Object> loginMap = userService.loginDesc(reqMap);
        String token = null;
        if (loginMap != null) {
            String id = loginMap.get("ID").toString();
            token = jwtUtils.generateToken(id);
            System.out.println(token);
        }
        return RestResponse.success(token);
    }

    /**
     * 用户名密码登录
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @PostMapping("loginByMobile")
    @ApiOperation(value = "手机号密码登录", notes = "根据手机号密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "mobile", value = "15209831990"),
                    @ExampleProperty(mediaType = "password", value = "admin")
            }), required = true, dataType = "string")
    })
    public RestResponse loginByMobile(@RequestBody JSONObject jsonObject) {
        String mobile = jsonObject.getString("mobile");
        AbstractAssert.isBlank(mobile, "手机号不能为空");

        String password = jsonObject.getString("password");
        AbstractAssert.isBlank(password, "密码不能为空");
        //用户登录
        MallUserEntity user = userService.loginByMobile(mobile, password);
        //生成token
        String token = jwtUtils.generateToken(user.getId());

        Map<String, Object> map = new HashMap<>(4);
        map.put("token", token);
        map.put("openId", user.getOpenId());
        map.put("unionId", user.getUnionId());
        map.put("userInfo", user);
        map.put("expire", jwtUtils.getExpire());

        return RestResponse.success(map);
    }

    /**
     * apple登录
     */
    @IgnoreAuth
    @PostMapping("AppleLogin")
    @ApiOperation(value = "apple登录", notes = "apple登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userInfo", value = "uni.login返回的authResult信息，JSON格式参数")
            }), required = true, dataType = "string")
    })
    public RestResponse AppleLogin(@RequestBody JSONObject jsonObject) {
        AppleUserInfo appleUserInfo = null;

        if (null != jsonObject.get("userInfo")) {
            appleUserInfo = jsonObject.getObject("userInfo", AppleUserInfo.class);
        }
        AbstractAssert.isNull(appleUserInfo, "登录失败：userInfo为空");

        try {
//            //验证identityToken
//            if (!AppleUitl.verify(appleUserInfo.getIdentityToken())) {
//                return RestResponse.error("授权验证失败");
//            }
//            //对identityToken解码
//            JSONObject json = AppleUitl.parserIdentityToken(appleUserInfo.getIdentityToken());
//            if (json == null) {
//                return RestResponse.error("授权解码失败");
//            }
//            log.error(json.toJSONString());
            MallUserEntity user = userService.selectByOpenId(appleUserInfo.getOpenId());
            if (user == null) {
                user = new MallUserEntity();
                user.setHeadImgUrl("/static/images/mall/apple.png");
                user.setRegisterTime(new Date());
                user.setRegisterIp(this.getClientIp());
                user.setUserLevelId("1");
                user.setPassword(DigestUtils.sha256Hex("123456"));
                user.setMobile(appleUserInfo.getEmail());
                String name;
                if (StringUtils.isNotBlank(appleUserInfo.getFullName().getFamilyName())) {
                    name = appleUserInfo.getFullName().getFamilyName() + appleUserInfo.getFullName().getGivenName();
                } else {
                    name = "Apple用户" + CharUtil.getRandomString(8);
                }
                user.setUserName(name);
                user.setNickname(name);
                user.setOpenId(appleUserInfo.getOpenId());
                //性别 0：未知、1：男、2：女
                user.setGender(0);
            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(new Date());

            userService.saveOrUpdate(user);

            String token = jwtUtils.generateToken(user.getId());

            if (StringUtils.isNullOrEmpty(token)) {
                log.error("登录失败：token生成异常");
                return RestResponse.error("登录失败");
            }
            return RestResponse.success().put("token", token).put("userInfo", user).put("userId", user.getId());
        } catch (Exception e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }
    }

    /**
     * 微信小程序登录
     */
    @IgnoreAuth
    @PostMapping("LoginByMa")
    @ApiOperation(value = "微信小程序登录", notes = "wx.login()每次返回的code只能使用一次")
    public RestResponse LoginByMa(@RequestBody WxLoginRequest request) {
        FullUserInfo fullUserInfo = request.getUserInfo();
        String code = request.getCode();
        AbstractAssert.isBlank(code, "登录失败：code为空");

        AbstractAssert.isNull(fullUserInfo, "登录失败：userInfo为空");

        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            // 用户信息校验
            log.info("》》》微信返回sessionData：" + session.toString());

            if (!wxMaService.getUserService().checkUserInfo(session.getSessionKey(), fullUserInfo.getRawData(), fullUserInfo.getSignature())) {
                log.error("登录失败：数据签名验证失败");
                return RestResponse.error("登录失败");
            }

            // 解密用户信息
            WxMaUserInfo wxMpUser = wxMaService.getUserService().getUserInfo(session.getSessionKey(), fullUserInfo.getEncryptedData(), fullUserInfo.getIv());
            if (ObjectUtil.isNotEmpty(session.getOpenid())){
                wxMpUser.setOpenId(session.getOpenid());
            }
//            MallUserEntity user = userService.selectByOpenUnionId(wxMpUser.getUnionId());
            // 如果没有开通关联到同一个开放平台使用openId
//            MallUserEntity user = userService.selectByOpenId(wxMpUser.getOpenId());
            MallUserEntity user = userService.lambdaQuery().eq(MallUserEntity::getOpenId, wxMpUser.getOpenId()).one();
            boolean isInvite = false;
            boolean register = false;
            MallUserEntity inviteUser = null;
            if (user == null) {
                register = true;
                user = new MallUserEntity();
                user.setId(Math.abs(new Random().nextInt())+"");
                user.setRegisterTime(new Date());
                user.setRegisterIp(this.getClientIp());
                user.setUserLevelId("1");
                user.setPassword(DigestUtils.sha256Hex("123456"));
                user.setUnionId(wxMpUser.getUnionId());
                user.setOpenId(wxMpUser.getOpenId());
                //性别 0：未知、1：男、2：女
                user.setGender(Integer.parseInt(wxMpUser.getGender()));
                user.setUserName(wxMpUser.getNickName());
                user.setNickname(wxMpUser.getNickName());
                user.setHeadImgUrl(wxMpUser.getAvatarUrl());
                /*MallUserEntity codeUser = null;
                String inviteCode;*/
                //保证邀请码唯一
                /*do {
                    inviteCode = GenerateUtil.generateStrRecaptcha(6);
                    codeUser = userService.lambdaQuery().eq(MallUserEntity::getInviteCode, inviteCode).one();
                } while (codeUser != null);
                user.setInviteCode(inviteCode);*/
                //查询邀请人
                if (StringUtils.isNotBlank(request.getInviteCode())) {
                    inviteUser = userService.lambdaQuery().eq(MallUserEntity::getInviteCode, request.getInviteCode()).one();
                    if (ObjectUtils.isEmpty(inviteUser)) {
                        return RestResponse.error("邀请码错误");
                    }

                    user.setInviteUserId(inviteUser.getId());
                    ArrayList<MallUserEntity> mallUserEntities = new ArrayList<>();
                    Integer relation = 1;
                    recursion(relation, mallUserEntities, inviteUser);
                    Integer shopRelation = 0;
                    int count = 0;
                    Integer shopCount = 0;
                    for (MallUserEntity mallUserEntity : mallUserEntities) {
                        if (2 == mallUserEntity.getRoleType()) {
                            shopRelation = mallUserEntity.getRelation();
                            shopCount++;
                            continue;
                        }
                        if (shopCount >= 2 || (shopCount == 0 && mallUserEntity.getRelation() == 3)) {
                            break;
                        }

                        if (0 == shopRelation || (shopRelation == 1 && shopCount == 1)) {
                            Integer type = 1;
                            String label = "直推奖励";
                            if (2 == mallUserEntity.getRelation()) {
                                label = "间推奖励";
                                type = 2;
                            }
                            MallAwardConfEntity oneConf = mallAwardConfService.lambdaQuery().eq(MallAwardConfEntity::getType, type).one();
                            IntegralDto oneIntegralDto = new IntegralDto().setUserId(mallUserEntity.getId()).setUpdateTime(new Date()).setAmount(oneConf.getAmount()).setLabel(label).setRemark(JSONObject.toJSONString(oneConf)).setType(Integer.valueOf(type));
                            System.out.println("***********"+oneConf+"**********");
                            mallUserIntegralService.exchange(oneIntegralDto);
                        } else {
                            Integer type = 9;
                            String label = "店铺直推奖励";
                            if (3 == mallUserEntity.getRelation()) {
                                label = "店铺间推奖励";
                                type = 10;
                            }
                            MallAwardConfEntity oneConf = mallAwardConfService.lambdaQuery().eq(MallAwardConfEntity::getType, type).one();
                            IntegralDto oneIntegralDto = new IntegralDto().setUserId(mallUserEntity.getId()).setUpdateTime(new Date()).setAmount(oneConf.getAmount()).setLabel(label).setRemark(JSONObject.toJSONString(oneConf)).setType(Integer.valueOf(type));
                            mallUserIntegralService.exchange(oneIntegralDto);
                        }
                        count++;
                    }


                    isInvite = true;
                }

            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(new Date());
            userService.saveOrUpdate(user);
            if (register) {
                //  String serialCode = ShareCodeUtil.toSerialCode(Long.valueOf(user.getId()));
                // MallDistEntity mallDistEntity = new MallDistEntity().setUserId(user.getId()).setJoinTime(new Date()).setInvitationCode(serialCode);
                MallUserIntegralEntity mallUserIntegralEntity = new MallUserIntegralEntity().setUserId(user.getId()).setAmount(BigDecimal.ZERO).setFreeze(BigDecimal.ZERO).setUpdateTime(new Date());
                mallUserIntegralService.save(mallUserIntegralEntity);
                if (isInvite) {
                    //记录邀请记录
                    MallUserInviteRecordEntity inviteRecord = new MallUserInviteRecordEntity();
                    inviteRecord.setCreateTime(new Date());
                    inviteRecord.setInviteUserId(user.getInviteUserId());
                    inviteRecord.setUserId(user.getId());
                    inviteRecord.setUserName(user.getNickname());
                    inviteRecord.setInviteUserName(inviteUser.getNickname());
                    mallUserInviteRecordService.save(inviteRecord);
                }
            }


            String token = jwtUtils.generateToken(user.getId());
            jedisUtil.set("TOKEN:" + user.getId(), token, 1800);
            if (StringUtils.isNullOrEmpty(token)) {
                log.error("登录失败：token生成异常");
                return RestResponse.error("登录失败");
            }
            return RestResponse.success().put("token", token).put("userInfo", wxMpUser).put("userId", user.getId());
        } catch (WxErrorException e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }
    }


    @IgnoreAuth
    @PostMapping("testRegister")
    @ApiOperation(value = "测试注册", notes = "测试注册")
    public RestResponse testRegister(@RequestBody WxLoginRequest request) {

        try {

            // 解密用户信息

//            MallUserEntity user = userService.selectByOpenUnionId(wxMpUser.getUnionId());
            // 如果没有开通关联到同一个开放平台使用openId
//            MallUserEntity user = userService.selectByOpenId(wxMpUser.getOpenId());
            MallUserEntity source = userService.lambdaQuery().last("limit 1").one();
            MallUserEntity user = new MallUserEntity();
            boolean isInvite = false;
            boolean register = false;
            MallUserEntity inviteUser = null;
            if (!register) {
                register = true;
                user = new MallUserEntity();
                user.setRegisterTime(new Date());
                user.setRegisterIp(this.getClientIp());
                user.setUserLevelId("1");
                user.setPassword(DigestUtils.sha256Hex("123456"));
                user.setUnionId(source.getUnionId());
                String generateStrRecaptcha = GenerateUtil.generateStrRecaptcha(6);
                user.setOpenId(source.getOpenId() + generateStrRecaptcha);
                //性别 0：未知、1：男、2：女
                user.setGender(source.getGender());
                user.setUserName(source.getUserName() + generateStrRecaptcha);
                user.setNickname(source.getNickname() + generateStrRecaptcha);
                user.setHeadImgUrl(source.getHeadImgUrl());
                /*MallUserEntity codeUser = null;
                String inviteCode;*/
                //保证邀请码唯一
                /*do {
                    inviteCode = GenerateUtil.generateStrRecaptcha(6);
                    codeUser = userService.lambdaQuery().eq(MallUserEntity::getInviteCode, inviteCode).one();
                } while (codeUser != null);
                user.setInviteCode(inviteCode);*/
                //查询邀请人
                if (StringUtils.isNotBlank(request.getInviteCode())) {
                    inviteUser = userService.lambdaQuery().eq(MallUserEntity::getInviteCode, request.getInviteCode()).one();
                    if (ObjectUtils.isEmpty(inviteUser)) {
                        return RestResponse.error("邀请码错误");
                    }

                    user.setInviteUserId(inviteUser.getId());
                    ArrayList<MallUserEntity> mallUserEntities = new ArrayList<>();
                    Integer relation = 1;
                    recursion(relation, mallUserEntities, inviteUser);
                    Integer shopRelation = 0;
                    int count = 0;
                    Integer shopCount = 0;
                    for (MallUserEntity mallUserEntity : mallUserEntities) {
                        if (2 == mallUserEntity.getRoleType()) {
                            shopRelation = mallUserEntity.getRelation();
                            shopCount++;
                            continue;
                        }
                        if (shopCount >= 2 || (shopCount == 0 && mallUserEntity.getRelation() == 3)) {
                            break;
                        }

                        if (0 == shopRelation || (shopRelation == 1 && shopCount == 1)) {
                            Integer type = 1;
                            String label = "直推奖励";
                            if (2 == mallUserEntity.getRelation()) {
                                label = "间推奖励";
                                type = 2;
                            }
                            MallAwardConfEntity oneConf = mallAwardConfService.lambdaQuery().eq(MallAwardConfEntity::getType, type).one();
                            IntegralDto oneIntegralDto = new IntegralDto().setUserId(mallUserEntity.getId()).setUpdateTime(new Date()).setAmount(oneConf.getAmount()).setLabel(label).setRemark(JSONObject.toJSONString(oneConf)).setType(Integer.valueOf(type));
                            mallUserIntegralService.exchange(oneIntegralDto);
                        } else {
                            Integer type = 9;
                            String label = "店铺直推奖励";
                            if (3 == mallUserEntity.getRelation()) {
                                label = "店铺间推奖励";
                                type = 10;
                            }
                            MallAwardConfEntity oneConf = mallAwardConfService.lambdaQuery().eq(MallAwardConfEntity::getType, type).one();
                            IntegralDto oneIntegralDto = new IntegralDto().setUserId(mallUserEntity.getId()).setUpdateTime(new Date()).setAmount(oneConf.getAmount()).setLabel(label).setRemark(JSONObject.toJSONString(oneConf)).setType(Integer.valueOf(type));
                            mallUserIntegralService.exchange(oneIntegralDto);
                        }
                        count++;
                    }


                    isInvite = true;
                }

            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(new Date());
            userService.saveOrUpdate(user);
            if (register) {
                //  String serialCode = ShareCodeUtil.toSerialCode(Long.valueOf(user.getId()));
                // MallDistEntity mallDistEntity = new MallDistEntity().setUserId(user.getId()).setJoinTime(new Date()).setInvitationCode(serialCode);
                MallUserIntegralEntity mallUserIntegralEntity = new MallUserIntegralEntity().setUserId(user.getId()).setAmount(BigDecimal.ZERO).setFreeze(BigDecimal.ZERO).setUpdateTime(new Date());
                mallUserIntegralService.save(mallUserIntegralEntity);
                if (isInvite) {
                    //记录邀请记录
                    MallUserInviteRecordEntity inviteRecord = new MallUserInviteRecordEntity();
                    inviteRecord.setCreateTime(new Date());
                    inviteRecord.setInviteUserId(user.getInviteUserId());
                    inviteRecord.setUserId(user.getId());
                    inviteRecord.setUserName(user.getNickname());
                    inviteRecord.setInviteUserName(inviteUser.getNickname());
                    mallUserInviteRecordService.save(inviteRecord);
                }
            }


            String token = jwtUtils.generateToken(user.getId());

            if (StringUtils.isNullOrEmpty(token)) {
                log.error("登录失败：token生成异常");
                return RestResponse.error("登录失败");
            }
            return RestResponse.success().put("token", token).put("userInfo", user).put("userId", user.getId());
        } catch (Exception e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }
    }


    public void recursion(Integer relation, ArrayList<MallUserEntity> mallUserEntities, MallUserEntity mallUserEntity) {
        if (ObjectUtil.isNotEmpty(mallUserEntity)) {
            mallUserEntities.add(mallUserEntity.setRelation(relation));
            MallUserEntity inviteUser = userService.lambdaQuery().eq(MallUserEntity::getId, mallUserEntity.getInviteUserId()).one();
            relation = relation + 1;
            recursion(relation, mallUserEntities, inviteUser);
        }


    }


    @Autowired
    private MallUserIntegralServiceImpl mallUserIntegralService;
    @Autowired
    private MallAwardConfServiceImpl mallAwardConfService;

    /**
     * 微信公众号登录
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @PostMapping("loginByMp")
    @ApiOperation(value = "微信公众号登录", notes = "根据微信code登录，每一个code只能使用一次")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "code", value = "oxaA11ulr9134oBL9Xscon5at_Gc")
            }), required = true, dataType = "string")
    })
    public RestResponse loginByMp(@RequestBody JSONObject jsonObject) {
        String code = jsonObject.getString("code");

        AbstractAssert.isBlank(code, "登录失败：code为空");

        Map<String, Object> map = new HashMap<>(8);
        try {
            WxMpOAuth2AccessToken auth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);

            String openId = auth2AccessToken.getOpenId();

            //获取微信用户信息
            WxMpUser wxMpUser = wxMpService.getOAuth2Service().getUserInfo(auth2AccessToken, null);

            //保存或者更新
            MallUserEntity user = userService.selectByOpenUnionId(wxMpUser.getUnionId());
            // 如果没有开通关联到同一个开放平台使用openId
//            MallUserEntity user = userService.selectByOpenId(wxMpUser.getOpenId());
            if (user == null) {
                user = new MallUserEntity();
                user.setRegisterTime(new Date());
                user.setRegisterIp(this.getClientIp());
                user.setUserLevelId("1");
                user.setPassword(DigestUtils.sha256Hex("123456"));
                user.setUserName(wxMpUser.getNickname());
                user.setUnionId(wxMpUser.getUnionId());
                user.setOpenId(wxMpUser.getOpenId());
                user.setNickname(wxMpUser.getNickname());
                user.setHeadImgUrl(wxMpUser.getHeadImgUrl());
                user.setGender(wxMpUser.getSex());
            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(new Date());
            user.setSubscribe(wxMpUser.getSubscribe() ? 1 : 0);
            userService.saveOrUpdate(user);

            //生成token
            String token = jwtUtils.generateToken(user.getId());

            map.put("token", token);
            map.put("expire", jwtUtils.getExpire());
            map.put("openId", openId);
            map.put("unionId", wxMpUser.getUnionId());
            map.put("userInfo", user);
        } catch (WxErrorException e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }

        return RestResponse.success(map);
    }

    /**
     * APP端微信登录
     */
    @IgnoreAuth
    @PostMapping("AppLoginByWx")
    @ApiOperation(value = "APP端微信登录", notes = "APP端微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "userInfo", value = "uni.login返回的authResult信息，JSON格式参数")
            }), required = true, dataType = "string")
    })
    public RestResponse AppLoginByWx(@RequestBody JSONObject jsonObject) {
        WxMpOAuth2AccessToken auth2AccessToken = null;

        if (null != jsonObject.get("userInfo")) {
            auth2AccessToken = jsonObject.getObject("userInfo", WxMpOAuth2AccessToken.class);
        }
        AbstractAssert.isNull(auth2AccessToken, "登录失败：userInfo为空");

        try {
            //获取微信用户信息
            WxMpUser wxMpUser = wxMpService.getOAuth2Service().getUserInfo(auth2AccessToken, null);


            MallUserEntity user = userService.selectByOpenUnionId(wxMpUser.getUnionId());
            // 如果没有开通关联到同一个开放平台使用openId
//            MallUserEntity user = userService.selectByOpenId(wxMpUser.getOpenId());
            if (user == null) {
                user = new MallUserEntity();
                user.setRegisterTime(new Date());
                user.setRegisterIp(this.getClientIp());
                user.setUserLevelId("1");
                user.setPassword(DigestUtils.sha256Hex("123456"));
                user.setUserName(wxMpUser.getNickname());
                user.setUnionId(wxMpUser.getUnionId());
                user.setOpenId(wxMpUser.getOpenId());
                user.setNickname(wxMpUser.getNickname());
                user.setHeadImgUrl(wxMpUser.getHeadImgUrl());
                user.setGender(wxMpUser.getSex());
            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(new Date());
            userService.saveOrUpdate(user);

            String token = jwtUtils.generateToken(user.getId());

            if (StringUtils.isNullOrEmpty(token)) {
                log.error("登录失败：token生成异常");
                return RestResponse.error("登录失败");
            }
            return RestResponse.success().put("token", token).put("userInfo", wxMpUser).put("userId", user.getId());
        } catch (WxErrorException e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }
    }

    /**
     * 支付宝登录
     */
    @IgnoreAuth
    @ApiOperation(value = "支付宝登录")
    @PostMapping("LoginByAli")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "code", value = "oxaA11ulr9134oBL9Xscon5at_Gc")
            }), required = true, dataType = "string")
    })
    public RestResponse LoginByAli(@RequestBody JSONObject jsonObject) {
        String code = jsonObject.getString("code");

        AbstractAssert.isBlank(code, "登录失败：code为空");

        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliMaProperties.getAppId(),
                aliMaProperties.getPrivateKey(), "json", "UTF-8", aliMaProperties.getPubKey(), "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(code);
        request.setGrantType("authorization_code");
        try {
            //code 换取token
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            String accessToken = oauthTokenResponse.getAccessToken();

            //根据token获取用户头像、昵称等信息
            AlipayUserInfoShareRequest userInfoShareRequest = new AlipayUserInfoShareRequest();
            AlipayUserInfoShareResponse userInfoResponse = alipayClient.execute(userInfoShareRequest, accessToken);

            Date nowTime = new Date();
            MallUserEntity user = userService.getOne(new QueryWrapper<MallUserEntity>().eq("ALI_USER_ID", userInfoResponse.getUserId()));
            if (null == user) {
                user = new MallUserEntity();
                String realName = userInfoResponse.getUserName();
                if (realName == null) {
                    realName = CharUtil.getRandomString(12);
                }
                user.setUserName("支付宝用户：" + realName);
                user.setRegisterTime(nowTime);
                user.setRegisterIp(this.getClientIp());
                user.setAliUserId(userInfoResponse.getUserId());
                user.setHeadImgUrl(userInfoResponse.getAvatar());
                user.setPassword(DigestUtils.sha256Hex("123456"));
                //性别 0：未知、1：男、2：女
                //F：女性；M：男性
                user.setGender("m".equalsIgnoreCase(userInfoResponse.getGender()) ? 1 : 0);
                user.setNickname(userInfoResponse.getNickName());
            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(nowTime);
            userService.saveOrUpdate(user);

            String token = jwtUtils.generateToken(user.getId());

            AbstractAssert.isBlank(token, "登录失败：token生成异常");

            Map<String, Object> resultObj = new HashMap<>();
            resultObj.put("token", token);
            resultObj.put("userInfo", userInfoResponse);
            resultObj.put("userId", user.getId());
            return RestResponse.success(resultObj);
        } catch (AlipayApiException e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }
    }

    /**
     * QQ小程序登录
     */
    @IgnoreAuth
    @PostMapping("LoginByQQ")
    @ApiOperation(value = "QQ小程序登录", notes = "qq.login()每次返回的code只能使用一次")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "code", value = "oxaA11ulr9134oBL9Xscon5at_Gc"),
                    @ExampleProperty(mediaType = "userInfo", value = "qq.login()返回的userInfo信息，JSON格式参数")
            }), required = true, dataType = "string")
    })
    public RestResponse LoginByQQ(@RequestBody JSONObject jsonObject) {
        FullUserInfo fullUserInfo = null;
        String code = jsonObject.getString("code");
        AbstractAssert.isBlank(code, "登录失败：code为空");

        if (null != jsonObject.get("userInfo")) {
            fullUserInfo = jsonObject.getObject("userInfo", FullUserInfo.class);
        }
        AbstractAssert.isNull(fullUserInfo, "登录失败：userInfo为空");

        try {
            Map<String, String> params = new HashMap<>(8);
            params.put("appid", appid);
            params.put("secret", secret);
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");

            String result = wxMaService.get("https://api.q.qq.com/sns/jscode2session", Joiner.on("&").withKeyValueSeparator("=").join(params));
            WxMaJscode2SessionResult session = WxMaJscode2SessionResult.fromJson(result);
            // 用户信息校验
            log.info("》》》QQ返回sessionData：" + session.toString());

            if (!wxMaService.getUserService().checkUserInfo(session.getSessionKey(), fullUserInfo.getData(), fullUserInfo.getSignature())) {
                log.error("登录失败：数据签名验证失败");
                return RestResponse.error("登录失败");
            }

            // 解密用户信息
            WxMaUserInfo wxMaUserInfo = wxMaService.getUserService().getUserInfo(session.getSessionKey(), fullUserInfo.getEncryptedData(), fullUserInfo.getIv());

            Date nowTime = new Date();
            MallUserEntity user = userService.selectByOpenId(wxMaUserInfo.getOpenId());
            if (null == user) {
                user = new MallUserEntity();
                user.setUserName(wxMaUserInfo.getNickName());
                user.setPassword(wxMaUserInfo.getOpenId());
                user.setRegisterTime(nowTime);
                user.setRegisterIp(this.getClientIp());
                user.setQqOpenId(wxMaUserInfo.getOpenId());
                user.setHeadImgUrl(wxMaUserInfo.getAvatarUrl());
                //性别 0：未知、1：男、2：女
                user.setGender(Integer.parseInt(wxMaUserInfo.getGender()));
                user.setNickname(wxMaUserInfo.getNickName());
                user.setUserLevelId("1");
            }
            user.setLastLoginIp(this.getClientIp());
            user.setLastLoginTime(nowTime);
            userService.saveOrUpdate(user);

            String token = jwtUtils.generateToken(user.getId());

            if (null == wxMaUserInfo || StringUtils.isNullOrEmpty(token)) {
                log.error("登录失败：token生成异常");
                return RestResponse.error("登录失败");
            }
            return RestResponse.success().put("token", token).put("userInfo", wxMaUserInfo).put("userId", user.getId());
        } catch (WxErrorException e) {
            log.error("登录失败：" + e.getMessage());
            return RestResponse.error("登录失败");
        }
    }

    /**
     * 根据openId换取登录token，方便本地开发调试
     *
     * @return RestResponse
     */
    @IgnoreAuth
    @PostMapping("loginByOpenId")
    @ApiOperation(value = "openId换取登录token", notes = "根据openId换取登录token，方便本地开发调试")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", name = "jsonObject", value = "JSON格式参数", examples = @Example({
                    @ExampleProperty(mediaType = "openId", value = "ok8KW5GEIwAYTa-Z92JfbzxkVNpA")
            }), required = true, dataType = "string")
    })
    public RestResponse loginByOpenId(@RequestBody JSONObject jsonObject) {
        String openId = jsonObject.getString("openId");
        MallUserEntity user = userService.selectByOpenId(openId);
        AbstractAssert.isNull(user, "登录失败：用户为空");

        //生成token
        String token = jwtUtils.generateToken(user.getId());
        Map<String, Object> map = new HashMap<>(8);
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("openId", openId);
        map.put("user", user);
//sda,nlsdabk见客户
        return RestResponse.success(map);
    }
}
