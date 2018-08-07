package com.bat.distributor.service.common;

import com.bat.distributor.api.base.DistributorException;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.system.user.api.SystemUserServiceRpc;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.sms.api.ThirdPartySmsServiceRpc;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyCodeRpcCmd;
import com.bat.dubboapi.thirdparty.sms.dto.user.UserPhoneVerifyRpc;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 15:39
 */
@Component
public class CommonRpcExe {

    @DubboReference(check = false, timeout = 5000)
    private ThirdPartySmsServiceRpc smsServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private SystemUserServiceRpc systemUserServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private SystemRegionServiceRpc systemRegionServiceRpc;

    @Resource
    private DistributorConfig config;

    /**
     * 获取手机号验证码
     *
     * @param phone
     * @param codeType
     * @return
     */
    public Boolean getPhoneVerifyCode(String phone, Short codeType) {
        UserPhoneVerifyRpc rpcCmd = new UserPhoneVerifyRpc();
        rpcCmd.setPhone(phone);
        rpcCmd.setCodeType(codeType);
        Response<Boolean> rpcResponse = smsServiceRpc.getPhoneVerifyCode(rpcCmd);
        if (rpcResponse.isSuccess()) {
            return true;
        } else {
            throw DistributorException.buildException(rpcResponse.getErrCode(), rpcResponse.getErrMessage());
        }
    }

    /**
     * 手机号验证验证码
     *
     * @param phone
     * @param codeType
     * @param code
     * @return
     */
    public Boolean checkPhoneVerifyCode(String phone, Short codeType, String code) {
        UserPhoneVerifyCodeRpcCmd rpcCmd = new UserPhoneVerifyCodeRpcCmd();
        rpcCmd.setPhone(phone);
        rpcCmd.setCodeType(codeType);
        rpcCmd.setCode(code);
        Response<Boolean> rpcResponse = smsServiceRpc.checkPhoneVerifyCode(rpcCmd);
        if (rpcResponse.isSuccess()) {
            return true;
        } else {
            throw DistributorException.buildException(rpcResponse.getErrCode(), rpcResponse.getErrMessage());
        }
    }

    /**
     * 根据用户id获取用户信息
     * 
     * @param salesId
     * @return
     */
    public UserRpcDTO getUserById(Integer salesId) {
        com.bat.dubboapi.system.common.Response<UserRpcDTO> response = systemUserServiceRpc.getUserById(salesId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    public boolean checkRegion(Integer countryId, Integer provinceId, Integer cityId, Integer districtId) {
        if (countryId == null) {
            throw DistributorException.buildException("P_DISTRIBUTOR_COUNTRY_ID_NULL");
        }
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> countryResp =
            systemRegionServiceRpc.getRegionById(countryId);
        if (!countryResp.isSuccess()) {
            throw DistributorException.buildException(countryResp.getErrCode(), countryResp.getErrMessage());
        }
        if (countryResp.getData().getHaveNext().equals((short)0)) {
            return true;
        }
        if (provinceId == null) {
            throw DistributorException.buildException("P_DISTRIBUTOR_PROVINCE_ID_NULL");
        }
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> provinceResp =
            systemRegionServiceRpc.getRegionById(provinceId);
        if (!provinceResp.isSuccess()) {
            throw DistributorException.buildException(countryResp.getErrCode(), countryResp.getErrMessage());
        }
        if (provinceResp.getData().getHaveNext().equals((short)0)) {
            return true;
        }
        if (cityId == null) {
            throw DistributorException.buildException("P_DISTRIBUTOR_CITY_ID_NULL");
        }
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> cityResp =
            systemRegionServiceRpc.getRegionById(cityId);
        if (!cityResp.isSuccess()) {
            throw DistributorException.buildException(countryResp.getErrCode(), countryResp.getErrMessage());
        }
        if (cityResp.getData().getHaveNext().equals((short)0)) {
            return true;
        }
        if (districtId == null || districtId == 0) {
            throw DistributorException.buildException("P_DISTRIBUTOR_DISTRICT_ID_NULL");
        }
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> districtResp =
            systemRegionServiceRpc.getRegionById(districtId);
        if (!districtResp.isSuccess()) {
            throw DistributorException.buildException(countryResp.getErrCode(), countryResp.getErrMessage());
        }
        return true;
    }
}
