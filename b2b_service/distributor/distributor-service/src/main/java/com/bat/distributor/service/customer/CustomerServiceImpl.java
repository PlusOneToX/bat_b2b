package com.bat.distributor.service.customer;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.user.CustomerServiceI;
import com.bat.distributor.api.user.dto.customer.*;
import com.bat.distributor.api.user.dto.customer.data.CustomerAddressDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerDTO;
import com.bat.distributor.api.user.dto.customer.data.CustomerLoginDTO;
import com.bat.distributor.service.customer.executor.CustomerCmdExe;
import com.bat.distributor.service.customer.executor.CustomerQryExe;
import com.bat.distributor.api.user.dto.customer.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 14:38
 */
@Service
public class CustomerServiceImpl implements CustomerServiceI {

    @Resource
    private CustomerCmdExe customerCmdExe;

    @Resource
    private CustomerQryExe customerQryExe;

    @Override
    public void verify(CustomerVerify cmd, String distributorId) {
        customerCmdExe.verify(cmd, distributorId);
    }

    @Override
    public CustomerLoginDTO verifyLogin(CustomerVerifyCmd cmd, String distributorId, String platform) {
        return customerCmdExe.verifyLogin(cmd, distributorId, platform);
    }

    @Override
    public CustomerLoginDTO passwordLogin(CustomerLoginQry qry, String distributorId, String platform) {
        return customerQryExe.passwordLogin(qry, distributorId, platform);
    }

    @Override
    public CustomerLoginDTO thirdpartyLogin(CustomerThirdPartyLoginCmd cmd, String distributorId, String platform) {
        return customerCmdExe.thirdpartyLogin(cmd, distributorId, platform);
    }

    @Override
    public CustomerLoginDTO wxOfficialProgramLogin(CustomerWxOfficialProgramLoginCmd cmd, String distributorId,
                                                   String platform) {
        return customerCmdExe.wxOfficialProgramLogin(cmd, distributorId, platform);
    }

    @Override
    public CustomerLoginDTO wxMiniProgramLogin(CustomerWxMiniProgramLoginCmd cmd, String distributorId,
        String platform) {
        return customerCmdExe.wxMiniProgramLogin(cmd, distributorId, platform);
    }

    @Override
    public CustomerLoginDTO dyProgramMiniLogin(CustomerDyProgramMiniLoginCmd cmd, String distributorId, String platform) {
        return customerCmdExe.dyProgramMiniLogin(cmd, distributorId, platform);
    }

    @Override
    public CustomerLoginDTO alipayMiniProgramLogin(CustomerAlipayProgramMiniLoginCmd cmd, String distributorId, String platform) {
        return customerCmdExe.alipayMiniProgramLogin(cmd, distributorId, platform);
    }

    @Override
    public CustomerDTO getCustomer(BaseId qry) {
        return customerQryExe.getCustomer(qry);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerCmd cmd, String openId, String otherId, String platform) {
        return customerCmdExe.updateCustomer(cmd, openId, otherId, platform);
    }

    @Override
    public void agreementCustomer(CustomerAgreementCmd cmd) {
        customerCmdExe.agreementCustomer(cmd);
    }

    @Override
    public void passwordCustomer(CustomerPasswordCmd cmd) {
        customerCmdExe.passwordCustomer(cmd);
    }

    @Override
    public void verifyPasswordCustomer(CustomerVerifyPasswordCmd cmd) {
        customerCmdExe.verifyPasswordCustomer(cmd);
    }

    @Override
    public PageInfo<CustomerAddressDTO> listAddress(CustomerAddressListQry qry) {
        return customerQryExe.listAddress(qry);
    }

    @Override
    public Integer createAddress(CustomerAddressCmd cmd) {
        return customerCmdExe.createAddress(cmd);
    }

    @Override
    public void updateAddress(CustomerAddressCmd cmd) {
        customerCmdExe.updateAddress(cmd);
    }

    @Override
    public void defaultAddress(BaseId cmd) {
        customerCmdExe.defaultAddress(cmd);
    }

    @Override
    public void deleteAddress(BaseId cmd) {
        customerCmdExe.deleteAddress(cmd);
    }

    @Override
    public String getWxOfficialProgramOpenId(String appId,String code,Integer distributorId) {

        return customerCmdExe.getWxOfficialProgramOpenId(appId,code,distributorId);
    }
}
