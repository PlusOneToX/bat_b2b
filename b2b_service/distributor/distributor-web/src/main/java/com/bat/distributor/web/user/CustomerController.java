package com.bat.distributor.web.user;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.user.CustomerServiceI;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.api.user.dto.customer.data.CustomerAddressDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerLoginDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/7 8:48
 */
@Api(tags = "C端客户前台接口", value = "CustomerController")
@RestController
@RequestMapping("/distributor/v1/web/user/customer")
public class CustomerController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Resource
    private CustomerServiceI service;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerVerify)
    @ApiOperation(value = "C端客户获取验证码")
    @PostMapping(value = "/verify")
    public Response verify(@RequestBody @Valid CustomerVerify cmd) {
        service.verify(cmd, getDistributorId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerVerifyLogin)
    @ApiOperation(value = "C端客户验证码登录")
    @PostMapping(value = "/verify/login")
    public Response<CustomerLoginDTO> verifyLogin(@RequestBody @Valid CustomerVerifyCmd cmd) {
        CustomerLoginDTO dto = service.verifyLogin(cmd, getDistributorId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerPasswordLogin)
    @ApiOperation(value = "C端客户密码登录")
    @PostMapping(value = "/password/login")
    public Response<CustomerLoginDTO> passwordLogin(@RequestBody @Valid CustomerLoginQry qry) {
        CustomerLoginDTO dto = service.passwordLogin(qry, getDistributorId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerThirdpartyLogin)
    @ApiOperation(value = "C端客户第三方系统登录")
    @PostMapping(value = "/thirdparty/login")
    public Response<CustomerLoginDTO> thirdpartyLogin(@RequestBody @Valid CustomerThirdPartyLoginCmd cmd) {
        CustomerLoginDTO dto = service.thirdpartyLogin(cmd, getDistributorId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerWxOfficialProgramLogin)
    @ApiOperation(value = "C端客户微信公众号授权登录")
    @PostMapping(value = "/wx/program/official/login")
    public Response<CustomerLoginDTO>
        wxOfficialProgramLogin(@RequestBody @Valid CustomerWxOfficialProgramLoginCmd cmd) {
        CustomerLoginDTO dto = service.wxOfficialProgramLogin(cmd, getDistributorId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerWxMiniProgramLogin)
    @ApiOperation(value = "C端客户微信小程序授权登录")
    @PostMapping(value = "/wx/program/mini/login")
    public Response<CustomerLoginDTO> wxMiniProgramLogin(@RequestBody @Valid CustomerWxMiniProgramLoginCmd cmd) {
        CustomerLoginDTO dto = service.wxMiniProgramLogin(cmd, getDistributorId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerDyProgramMiniLogin)
    @ApiOperation(value = "C端客户抖音小程序授权登录")
    @PostMapping(value = "/dy/program/mini/login")
    public Response<CustomerLoginDTO> dyProgramMiniLogin(@RequestBody @Valid CustomerDyProgramMiniLoginCmd cmd) {
        CustomerLoginDTO dto = service.dyProgramMiniLogin(cmd, getDistributorId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer,
            value = CommonLogTypeConstantDTO.CustomerAlipayProgramMiniLogin)
    @ApiOperation(value = "C端客户支付宝小程序授权登录")
    @PostMapping(value = "/alipay/program/mini/login")
    public Response<CustomerLoginDTO> alipayMiniProgramLogin(@RequestBody @Valid CustomerAlipayProgramMiniLoginCmd cmd) {
        CustomerLoginDTO dto = service.alipayMiniProgramLogin(cmd,getDistributorId(), getPlatform());
        return Response.of(dto);
    }

//    @ApiOperation(value = "柔性支付宝小程序授权openid")
//    @PostMapping(value = "/alipay/program/mini/openid")
//    public Response<String> alipayMiniProgramOpenId(@RequestBody @Valid UserAlipayMiniProgramOpenIdCmd cmd) {
//        String openId = service.alipayMiniProgramOpenId(cmd);
//        return Response.of(openId);
//    }

    @ApiOperation(value = "C端客户获取个人信息")
    @GetMapping()
    public Response<CustomerDTO> getCustomer(@Valid BaseId qry) {
        CustomerDTO dto = service.getCustomer(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerUpdate)
    @ApiOperation(value = "C端客户修改个人信息")
    @PutMapping()
    public Response<CustomerDTO> updateCustomer(@RequestBody @Valid CustomerCmd cmd) {
        CustomerDTO dto = service.updateCustomer(cmd, getOpenId(), getOtherId(), getPlatform());
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerAgreement)
    @ApiOperation(value = "C端客户协议确认")
    @PutMapping(value = "/agreement")
    public Response agreementCustomer(@RequestBody @Valid CustomerAgreementCmd cmd) {
        service.agreementCustomer(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerPassword)
    @ApiOperation(value = "C端客户通过旧密码修改新密码")
    @PutMapping(value = "/password")
    public Response passwordCustomer(@RequestBody @Valid CustomerPasswordCmd cmd) {
        service.passwordCustomer(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerVerifyPassword)
    @ApiOperation(value = "C端客户通过验证码修改密码")
    @PutMapping(value = "/verify/password")
    public Response verifyPasswordCustomer(@RequestBody @Valid CustomerVerifyPasswordCmd cmd) {
        service.verifyPasswordCustomer(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "查询C端客户收货地址列表(分页)")
    @GetMapping(value = "/address/list")
    public Response<PageInfo<CustomerAddressDTO>> listAddress(@Valid CustomerAddressListQry qry) {
        PageInfo<CustomerAddressDTO> pageInfo = service.listAddress(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerAddAddress)
    @ApiOperation(value = "新增C端客户收货地址")
    @PostMapping(value = "/address")
    public Response createAddress(@RequestBody @Valid CustomerAddressCmd cmd) {
        service.createAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerUpdateAddress)
    @ApiOperation(value = "修改C端客户收货地址")
    @PutMapping(value = "/address")
    public Response updateAddress(@RequestBody @Valid CustomerAddressCmd cmd) {
        service.updateAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerDefaultAddress)
    @ApiOperation(value = "设置C端客户收货地址为默认地址")
    @PutMapping(value = "/address/default")
    public Response defaultAddress(@RequestBody @Valid BaseId cmd) {
        service.defaultAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerDeleteAddress)
    @ApiOperation(value = "删除C端客户收货地址")
    @DeleteMapping(value = "/address")
    public Response deleteAddress(@RequestBody @Valid BaseId cmd) {
        service.deleteAddress(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Customer, value = CommonLogTypeConstantDTO.CustomerWxOfficialProgramOpenid)
    @ApiOperation(value = "C端客户微信公众号获取openId、在data里面")
    @PostMapping(value = "/wx/program/official/getOpenId")
    public Response<String>
    wxOfficialProgramOpenId(@RequestBody @Valid CustomerWxOfficialProgramOpenIdCmd cmd) {
        String openId = service.getWxOfficialProgramOpenId(cmd.getAppId(),cmd.getCode(),cmd.getDistributorId());
        return Response.of(openId);
    }
}
