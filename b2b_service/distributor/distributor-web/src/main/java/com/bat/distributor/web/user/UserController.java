package com.bat.distributor.web.user;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.DistributorIdDTO;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.distributor.dto.DistributorId;
import com.bat.distributor.api.distributor.dto.data.DistributorBusinessDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorIdsDTO;
import com.bat.distributor.api.user.UserServiceI;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.dataobject.DistributorBusinessDO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "分销商前台接口", value = "UserController")
@RestController
@RequestMapping("/distributor/v1/web/user")
public class UserController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserServiceI service;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserOneLevelApply)
    @ApiOperation(value = "一级分销商前台注册申请")
    @PostMapping(value = "/one-level/apply")
    public Response<UserApplyDTO> oneLevelApply(@RequestBody @Valid UserOneLevelApplyCmd cmd) {
        UserApplyDTO dto = service.oneLevelApply(cmd);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserLogin)
    @ApiOperation(value = "分销商账号或分销商联系人登录")
    @PostMapping(value = "/login")
    public Response<UserLoginDTO> userLogin(@RequestBody @Valid UserLoginQry qry) {
        UserLoginDTO dto = service.userLogin(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserPhoneLogin)
    @ApiOperation(value = "第三方联系人手机号登录")
    @PostMapping(value = "/thirdparty/phone/login")
    public Response<UserLoginDTO> phoneLogin(@RequestBody @Valid UserPhoneQry qry) {
        UserLoginDTO dto = service.phoneLogin(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserWxMiniProgramLogin)
    @ApiOperation(value = "分销微信小程序授权登录")
    @PostMapping(value = "/wx/program/mini/login")
    public Response<UserLoginDTO> wxMiniProgramLogin(@RequestBody @Valid UserWxMiniProgramLoginCmd cmd) {
        UserLoginDTO dto = service.wxMiniProgramLogin(cmd);
        return Response.of(dto);
    }

    @ApiOperation(value = "分销微信小程序授权openid")
    @PostMapping(value = "/wx/program/mini/openid")
    public Response<String> wxMiniProgramOpenId(@RequestBody @Valid UserWxMiniProgramOpenIdCmd cmd) {
        String openId = service.wxMiniProgramOpenId(cmd);
        return Response.of(openId);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserUpdatePassword)
    @ApiOperation(value = "分销商账户修改密码")
    @PutMapping(value = "/password")
    public Response updatePassword(@RequestBody @Valid UserPasswordCmd cmd) {
        service.updatePassword(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserUpdateOpenId)
    @ApiOperation(value = "更新当前分销商openId")
    @PutMapping(value = "/update/openid")
    public Response updateOpenId(@RequestBody @Valid UserOpenIdCmd cmd) {
        service.updateOpenId(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserContactAdd)
    @ApiOperation(value = "新增分销商联系人")
    @PostMapping(value = "/contact")
    public Response createContact(@RequestBody @Valid UserContactCmd cmd) {
        service.createContact(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserContactUpdate)
    @ApiOperation(value = "修改分销商联系人")
    @PutMapping(value = "/contact")
    public Response updateContact(@RequestBody @Valid UserContactCmd cmd) {
        service.updateContact(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserDeleteContact)
    @ApiOperation(value = "删除分销商联系人")
    @DeleteMapping(value = "/contact")
    public Response deleteContact(@RequestBody @Valid BaseId cmd) {
        service.deleteContact(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "查询分销商联系人")
    @GetMapping(value = "/contact")
    public Response<ContactDTO> getContact(@Valid UserContactQry qry) {
        return Response.of(service.getContact(qry));
    }

    @ApiOperation(value = "查询分销商联系人列表(分页)")
    @GetMapping(value = "/contact/list")
    public Response<PageInfo<ContactDTO>> listContact(@Valid UserContactListQry qry) {
        PageInfo<ContactDTO> pageInfo = service.listContact(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "查询分销商联系人角色列表(分页)")
    @GetMapping(value = "/role/list")
    public Response<PageInfo<UserContactRoleDTO>> listContactRole(@Valid UserAddressListQry qry) {
        PageInfo<UserContactRoleDTO> pageInfo = service.listContactRole(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "查询分销商个人信息")
    @GetMapping(value = "/info")
    public Response<UserInfoDTO> getUserInfo(@Valid BaseId qry) {
        UserInfoDTO userInfo = service.getUserInfo(qry);
        return Response.of(userInfo);
    }

    @ApiOperation(value = "查询分销商收货地址列表(分页)")
    @GetMapping(value = "/address/list")
    public Response<PageInfo<UserAddressDTO>> listAddress(@Valid UserAddressListQry qry) {
        PageInfo<UserAddressDTO> pageInfo = service.listAddress(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserAddAddress)
    @ApiOperation(value = "新增分销商收货地址")
    @PostMapping(value = "/address")
    public Response createAddress(@RequestBody @Valid UserAddressCmd cmd) {
        service.createAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserUpdateAddress)
    @ApiOperation(value = "修改分销商收货地址")
    @PutMapping(value = "/address")
    public Response updateAddress(@RequestBody @Valid UserAddressCmd cmd) {
        service.updateAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserDefaultAddress)
    @ApiOperation(value = "设置分销商收货地址为默认地址")
    @PutMapping(value = "/address/default")
    public Response defaultAddress(@RequestBody @Valid BaseId cmd) {
        service.defaultAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserDeleteAddress)
    @ApiOperation(value = "删除分销商收货地址")
    @DeleteMapping(value = "/address")
    public Response deleteAddress(@RequestBody @Valid BaseId cmd) {
        service.deleteAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserUpdateLanguage)
    @ApiOperation(value = "更新平台显示语言和币种")
    @PutMapping(value = "/language")
    public Response updateLanguage(@RequestBody @Valid UserLanguageCmd cmd) {
        service.updateLanguage(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserGetPhoneVerifyCode)
    @ApiOperation(value = "分销商前台手机号获取验证码")
    @PostMapping(value = "/phone/verify")
    public Response getPhoneVerifyCode(@RequestBody @Valid UserPhoneVerify cmd) {
        boolean result = service.getPhoneVerifyCode(cmd);
        return Response.of(result);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserCheckPhoneVerifyCode)
    @ApiOperation(value = "分销商前台手机号验证验证码")
    @PutMapping(value = "/phone/verify")
    public Response checkPhoneVerifyCode(@RequestBody @Valid UserPhoneVerifyCodeCmd cmd) {
        service.checkPhoneVerifyCode(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserChangePhone)
    @ApiOperation(value = "分销商修改手机号")
    @PutMapping(value = "/change/phone")
    public Response changePhone(@RequestBody @Valid UserChangePhoneCmd cmd) {
        service.changePhone(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserNextLevelApply)
    @ApiOperation(value = "下级分销商前台注册申请")
    @PostMapping(value = "/next/apply")
    public Response<UserApplyDTO> nextLevelApply(@RequestBody @Valid UserNextLevelApplyCmd cmd) {
        UserApplyDTO dto = service.nextLevelApply(cmd);
        return Response.of(dto);
    }

    @ApiOperation(value = "分销商获取分销二维码")
    @GetMapping(value = "/distribution/qrcode")
    public Response<UserQrCodeDTO> distributionQrCode(@Valid UserQrCodeQry qry) {
        UserQrCodeDTO dto = service.distributionQrCode(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserAddNextScalePrice)
    @ApiOperation(value = "新增价格等级")
    @PostMapping(value = "/next/scaleprice")
    public Response createNextScalePrice(@RequestBody @Valid UserNextScalePriceCmd cmd) {
        service.createNextScalePrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserUpdateNextScalePrice)
    @ApiOperation(value = "修改价格等级")
    @PutMapping(value = "/next/scaleprice")
    public Response updateNextScalePrice(@RequestBody @Valid UserNextScalePriceCmd cmd) {
        service.updateNextScalePrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserDeleteNextScalePrice)
    @ApiOperation(value = "删除价格等级")
    @DeleteMapping(value = "/next/scaleprice")
    public Response deleteNextScalePrice(@RequestBody @Valid BaseId cmd) {
        service.deleteNextScalePrice(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserAddNextScalePriceSpecial)
    @ApiOperation(value = "新增价格等级特殊公式")
    @PostMapping(value = "/next/scaleprice/special")
    public Response createNextScalePriceSpecial(@RequestBody @Valid UserNextScalePriceSpecialCmd cmd) {
        service.createNextScalePriceSpecial(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserUpdateNextScalePriceSpecial)
    @ApiOperation(value = "修改价格等级特殊公式")
    @PutMapping(value = "/next/scaleprice/special")
    public Response updateNextScalePriceSpecial(@RequestBody @Valid UserNextScalePriceSpecialCmd cmd) {
        service.updateNextScalePriceSpecial(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserDeleteNextScalePriceSpecial)
    @ApiOperation(value = "删除价格等级特殊公式")
    @DeleteMapping(value = "/next/scaleprice/special")
    public Response deleteNextScalePriceSpecial(@RequestBody @Valid BaseId cmd) {
        service.deleteNextScalePriceSpecial(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "分销商获取价格等级列表(不分页)")
    @GetMapping(value = "/next/scaleprice/list")
    public Response<List<UserNextScalePriceDTO>> listUserNextScalePrice(@Valid UserNextScalePriceListQry qry) {
        List<UserNextScalePriceDTO> dtos = service.listUserNextScalePrice(qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "查看分销商价格等级详情")
    @GetMapping(value = "/next/scaleprice")
    public Response<UserNextScalePriceDTO> getUserNextScalePrice(@Valid BaseId qry) {
        UserNextScalePriceDTO dto = service.getUserNextScalePrice(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserCheckDistributor)
    @ApiOperation(value = "分销商审核下级分销商")
    @PutMapping(value = "/next/check")
    public Response checkDistributor(@RequestBody @Valid UserCheckCmd cmd) {
        service.checkDistributor(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User,
        value = CommonLogTypeConstantDTO.UserUpdateDistributorScalePrice)
    @ApiOperation(value = "调整下级分销商价格等级")
    @PutMapping(value = "/next/distributor/scaleprice")
    public Response updateDistributorScalePrice(@RequestBody @Valid UserDistributorScalePriceCmd cmd) {
        service.updateDistributorScalePrice(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "查询下级分销商列表")
    @GetMapping(value = "/next/list")
    public Response<PageInfo<UserNextListDTO>> nextDistributorList(@Valid UserNextListQry qry) {
        PageInfo<UserNextListDTO> dts = service.nextList(qry);
        return Response.of(dts);
    }

    @ApiOperation(value = "查看下级分销商详情")
    @GetMapping(value = "/next")
    public Response<UserNextDTO> getNextDistributor(@Valid BaseId qry) {
        UserNextDTO dto = service.getNextDistributor(qry);
        return Response.of(dto);
    }

    @ApiOperation(value = "根据分销商id查询品牌列表")
    @GetMapping(value = "/up/brand/list")
    public Response<List<UserBrandDTO>> listBrand(@Valid UserId qry) {
        List<UserBrandDTO> userBrandDTOS = service.listBrand(qry);
        return Response.of(userBrandDTOS);
    }

    @ApiOperation(value = "根据分销商id查询商品列表(分页)")
    @PostMapping(value = "/up/goods/list")
    public Response<PageInfo<UserGoodsDTO>> listGoods(@RequestBody @Valid UserGoodsListQry qry) {
        PageInfo<UserGoodsDTO> pageInfo = service.listGoods(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "获取下级分销商不可视品牌id列表(不分页)")
    @GetMapping(value = "/next/nobrand/list")
    public Response<List<Integer>> listNoBrand(@Valid UserId qry) {
        List<Integer> ids = service.listNoBrand(qry);
        return Response.of(ids);
    }

    @ApiOperation(value = "获取下级分销商不可视商品id列表(不分页)")
    @GetMapping(value = "/next/nogoods/list")
    public Response<List<Integer>> listNoGoods(@Valid UserId qry) {
        List<Integer> ids = service.listNoGoods(qry);
        return Response.of(ids);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserSetNoBrand)
    @ApiOperation(value = "设置下级分销商不可视品牌")
    @PostMapping(value = "/next/nobrand/list")
    public Response setNoBrand(@RequestBody @Valid UserNextNoBrandCmd cmd) {
        service.setNoBrand(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserSetNoGoods)
    @ApiOperation(value = "设置下级分销商不可视商品")
    @PostMapping(value = "/next/nogoods/list")
    public Response setNoGoods(@RequestBody @Valid UserNextNoGoodsCmd cmd) {
        service.setNoGoods(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "分销商获取支付方式")
    @GetMapping(value = "/payment/mode")
    public Response<UserPayMentModeDTO> paymentMode() {
        UserPayMentModeDTO dto = service.paymentMode(getUserId());
        return Response.of(dto);
    }

    @GetMapping(value = "/business/getBusiness")
    @ApiOperation(value = "根据分销商id获取业务数据")
    public Response<DistributorBusinessDTO> getBusiness(@Valid DistributorId distributorId) {
        DistributorBusinessDO distributorBusinessDO =
            service.getDistributorBusinessDOByDistributorId(distributorId.getId());
        DistributorBusinessDTO distributorBusinessDTO = new DistributorBusinessDTO();
        BeanUtils.copyProperties(distributorBusinessDO, distributorBusinessDTO);
        return Response.of(distributorBusinessDTO);
    }

    @ApiOperation(value = "查询微信平台列表")
    @GetMapping(value = "/wx/platform/list")
    public Response<List<UserWxPlatformListDTO>> listWxPlatform(@Valid UserWxPlatformListQry qry) {
        List<UserWxPlatformListDTO> dtos = service.listWxPlatform(getUserId(), qry);
        return Response.of(dtos);
    }

    @ApiOperation(value = "分销微信小程序根据openid登录")
    @PostMapping(value = "/wx/program/mini/loginByOpenId")
    public Response<UserLoginDTO> loginByOpenId(@RequestBody @Valid UserOpenIdQry userOpenIdQry) {
        UserLoginDTO userLoginDTO = service.wxMiniProgramLoginByOpenId(userOpenIdQry.getOpenId());
        return Response.of(userLoginDTO);
    }
    @ApiOperation(value = "分销微信小程序根据手机号码和验证码登录")
    @PostMapping(value = "/wx/program/mini/loginByPhoneAndSmsCode")
    public Response<UserLoginDTO> loginByPhoneAndSmsCode(@RequestBody @Valid UserMobileLoginQry userMobileLoginQry) {
        UserLoginDTO userLoginDTO = service.wxMiniProgramLoginByPhoneAndSmsCode(userMobileLoginQry);
        return Response.of(userLoginDTO);
    }

    @ApiOperation(value = "分销微信小程序退出登录、清除openId(传分销商联系人id、不是分销商id)")
    @PutMapping(value = "/wx/program/mini/logOut")
    public Response logOut(@RequestBody @Valid DistributorIdDTO distributorIdDTO) {
        service.logOut(distributorIdDTO.getId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据分销商id查找分销商基本数据")
    @GetMapping(value = "/getBaseMsgByDistributorId")
    public Response<DistributorIdsDTO> getBaseMsgByDistributorId(@Valid DistributorIdDTO distributorIdDTO) {
        DistributorIdsDTO distributorIdsDTO = service.getBaseMsgByDistributorId(distributorIdDTO.getId());
        return Response.of(distributorIdsDTO);
    }
}
