package com.platform.modules.app.controller;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.annotation.LoginUser;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.ShareCodeUtil;
import com.platform.common.utils.StringUtils;
import com.platform.model.RewardAmountVo;
import com.platform.modules.mall.entity.MallRewardRecordEntity;
import com.platform.modules.mall.entity.MallUserEntity;
import com.platform.modules.mall.entity.MallUserInviteRecordEntity;
import com.platform.modules.mall.service.MallUserInviteRecordService;
import com.platform.modules.mall.service.MallUserService;
import com.platform.modules.mall.service.impl.MallRewardRecordServiceImpl;
import com.platform.modules.oss.cloud.UploadFactory;
import com.platform.request.PageRequest;
import io.swagger.annotations.*;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/reward")
@Api(tags = "AppRewardController|APP合伙人接口")
public class AppRewardController {
    @Autowired
    private MallUserService userService;
    @Autowired
    private MallUserInviteRecordService mallUserInviteRecordService;
    @Autowired
    private MallRewardRecordServiceImpl mallRewardRecordService;
    @Autowired
    private WxMaQrcodeService wxMaQrcodeService;


    @GetMapping("getInviteQrCode")
    @ApiOperation("获取邀请二维码,路径为：pages/auth/btnAuth/btnAuth")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string")
    })
    public RestResponse getInviteQrCode(@LoginUser MallUserEntity loginUser) {
        Map<String, String> data = new HashMap<>();
        String page = "pages/auth/btnAuth/btnAuth";
        MallUserEntity mallUserEntity = userService.lambdaQuery().eq(MallUserEntity::getId, loginUser.getId()).one();
        if (StringUtils.isEmpty(mallUserEntity.getInviteCode())) {
            String serialCode = ShareCodeUtil.toSerialCode(Long.valueOf(mallUserEntity.getId()));
            mallUserEntity.setInviteCode(serialCode);
            userService.saveOrUpdate(mallUserEntity);
        }
        String invitationCode = loginUser.getInviteCode();
        // 获取小程序码
        try {
            byte[] maQrcodeBytes = wxMaQrcodeService.createWxaCodeUnlimitBytes(invitationCode, page, 430, false, (WxMaCodeLineColor) null, false);

            String maQrcodeUrl = UploadFactory.build().uploadSuffix(maQrcodeBytes, ".png");
            System.out.println(maQrcodeUrl);
            data.put("qrCodeUrl", maQrcodeUrl);
            data.put("inviteCode", invitationCode);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取小程序码失败！");
        }
        return RestResponse.success().put("data", data);
    }
    /*public RestResponse getInviteQrCode(@LoginUser MallUserEntity loginUser) {
        Map<String, String> data = new HashMap<>();
        String page = "pages/auth/btnAuth/btnAuth";

        MallUserEntity mallUserEntity = userService.lambdaQuery().eq(MallUserEntity::getId, loginUser.getId()).one();
        if (StringUtils.isEmpty(mallUserEntity.getInviteCode())) {
            String serialCode = ShareCodeUtil.toSerialCode(Long.valueOf(mallUserEntity.getId()));
            mallUserEntity.setInviteCode(serialCode);
            userService.saveOrUpdate(mallUserEntity);
        }
        String invitationCode = mallUserEntity.getInviteCode();
        // 获取小程序码
        try {
            byte[] maQrcodeBytes = wxMaQrcodeService.createWxaCodeUnlimitBytes(invitationCode, page, 430, false, (WxMaCodeLineColor) null, false);

            String maQrcodeUrl = UploadFactory.build().uploadSuffix(maQrcodeBytes, ".png");
            System.out.println(maQrcodeUrl);
            data.put("qrCodeUrl", maQrcodeUrl);
            data.put("inviteCode", invitationCode);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BusinessException("获取小程序码失败！");
        }
        return RestResponse.success().put("data", data);
    }
*/
    @PostMapping("getInviteRecord")
    @ApiOperation("分页获取邀请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string"),
    })
    public RestResponse getInviteRecord(@LoginUser MallUserEntity loginUser, @RequestBody PageRequest request) {
        LambdaQueryWrapper<MallUserInviteRecordEntity> wrapper = Wrappers.lambdaQuery(MallUserInviteRecordEntity.class);
        wrapper.eq(MallUserInviteRecordEntity::getInviteUserId, loginUser.getId()).orderByDesc(MallUserInviteRecordEntity::getCreateTime);
        Page<MallUserInviteRecordEntity> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getLimit());
        page = mallUserInviteRecordService.getBaseMapper().selectPage(page, wrapper);
        //取出名称
        for (MallUserInviteRecordEntity record : page.getRecords()) {
            MallUserEntity one = userService.lambdaQuery().select(MallUserEntity::getUserName).eq(MallUserEntity::getId, record.getUserId()).one();
            record.setUserName(one.getUserName());
        }
        return RestResponse.success().put("data", page);
    }


    @PostMapping("getRewardRecord")
    @ApiOperation("分页获取邀请奖励记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string"),
    })
    public RestResponse getRewardRecord(@LoginUser MallUserEntity loginUser, @RequestBody PageRequest request) {
        LambdaQueryWrapper<MallRewardRecordEntity> wrapper = Wrappers.lambdaQuery(MallRewardRecordEntity.class);
        wrapper.eq(MallRewardRecordEntity::getUserId, loginUser.getId()).orderByDesc(MallRewardRecordEntity::getCreateTime);
        Page<MallRewardRecordEntity> page = new Page<>();
        page.setSize(request.getLimit());
        page.setCurrent(request.getCurrent());
        page = mallRewardRecordService.getBaseMapper().selectPage(page, wrapper);
        for (MallRewardRecordEntity record : page.getRecords()) {
            MallUserEntity one = userService.lambdaQuery().select(MallUserEntity::getUserName).eq(MallUserEntity::getId, record.getOrderUserId()).one();
            record.setOrderUserNickName(one.getUserName());
        }
        return RestResponse.success().put("data", page);
    }

    @PostMapping("getRewardInfo")
    @ApiOperation("获取合伙人奖励详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "用户token", dataType = "string"),
    })
    public RestResponse getRewardInfo(@LoginUser MallUserEntity loginUser) {
        Integer count = mallUserInviteRecordService.lambdaQuery().eq(MallUserInviteRecordEntity::getInviteUserId, loginUser.getId()).count();
        BigDecimal rewardAmount = mallRewardRecordService.getBaseMapper().sumRewardAmount(loginUser.getId());
        RewardAmountVo vo = new RewardAmountVo();
        vo.setInviteCount(count);
        vo.setRewardAmount(rewardAmount);
        return RestResponse.success().put("data", vo);
    }


}
