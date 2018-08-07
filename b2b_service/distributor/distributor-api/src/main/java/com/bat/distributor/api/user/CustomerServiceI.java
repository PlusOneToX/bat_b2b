package com.bat.distributor.api.user;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.api.user.dto.customer.data.CustomerAddressDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerLoginDTO;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.user.dto.customer.*;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/7 8:49
 */
public interface CustomerServiceI {
    /**
     * C端客户获取验证码
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    void verify(CustomerVerify cmd, String distributorId);

    /**
     * C端客户验证码登录
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    CustomerLoginDTO verifyLogin(CustomerVerifyCmd cmd, String distributorId, String platform);

    /**
     * C端客户密码登录
     * 
     * @param qry
     * @param distributorId
     * @return
     */
    CustomerLoginDTO passwordLogin(CustomerLoginQry qry, String distributorId, String platform);

    /**
     * C端客户第三方系统登录
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    CustomerLoginDTO thirdpartyLogin(CustomerThirdPartyLoginCmd cmd, String distributorId, String platform);

    /**
     * C端客户微信公众号授权登录
     *
     * @param cmd
     * @param distributorId
     * @return
     */
    CustomerLoginDTO wxOfficialProgramLogin(CustomerWxOfficialProgramLoginCmd cmd, String distributorId,
                                            String platform);

    /**
     * C端客户微信小程序授权登录
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    CustomerLoginDTO wxMiniProgramLogin(CustomerWxMiniProgramLoginCmd cmd, String distributorId, String platform);

    /**
     * C端客户抖音授权登录
     * @param cmd
     * @param distributorId
     * @param platform
     * @return
     */
    CustomerLoginDTO dyProgramMiniLogin(CustomerDyProgramMiniLoginCmd cmd, String distributorId, String platform);

    /**
     * C端客户支付宝授权登录
     * @param cmd
     * @param distributorId
     * @param platform
     * @return
     */
    CustomerLoginDTO alipayMiniProgramLogin(CustomerAlipayProgramMiniLoginCmd cmd, String distributorId, String platform);

    /**
     * C端客户获取个人信息
     * 
     * @param qry
     * @return
     */
    CustomerDTO getCustomer(BaseId qry);

    /**
     * C端客户修改个人信息
     *
     * @param cmd
     * @return
     */
    CustomerDTO updateCustomer(CustomerCmd cmd, String openId, String otherId, String platform);

    /**
     * C端客户协议确认
     * 
     * @param cmd
     */
    void agreementCustomer(CustomerAgreementCmd cmd);

    /**
     * C端客户修改密码
     * 
     * @param cmd
     */
    void passwordCustomer(CustomerPasswordCmd cmd);

    /**
     * C端客户通过验证码修改密码
     * 
     * @param cmd
     */
    void verifyPasswordCustomer(CustomerVerifyPasswordCmd cmd);

    /**
     * 查询C端客户收货地址列表(分页)
     *
     * @param qry
     * @return
     */
    PageInfo<CustomerAddressDTO> listAddress(CustomerAddressListQry qry);

    /**
     * 新增C端客户收货地址
     *
     * @param cmd
     * @return
     */
    Integer createAddress(CustomerAddressCmd cmd);

    /**
     * 修改C端客户收货地址
     *
     * @param cmd
     * @return
     */
    void updateAddress(CustomerAddressCmd cmd);

    /**
     * 设置C端客户收货地址为默认地址
     *
     * @param cmd
     * @return
     */
    void defaultAddress(BaseId cmd);

    /**
     * 删除C端客户收货地址
     *
     * @param cmd
     * @return
     */
    void deleteAddress(BaseId cmd);

    /**
     * 获取微信公众号openId
     * @param appId
     * @return
     */
    String getWxOfficialProgramOpenId(String appId,String code,Integer distributorId);
}
