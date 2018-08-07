package com.bat.distributor.service.user.executor;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.user.dto.user.UserPhoneVerify;
import com.bat.distributor.api.user.dto.user.UserPhoneVerifyCodeCmd;
import com.bat.distributor.api.user.dto.user.UserQrCodeQry;
import com.bat.distributor.dao.distributor.DistributorContactsMapper;
import com.bat.distributor.dao.distributor.dataobject.DistributorContactsDO;
import com.bat.distributor.service.common.CommonRpcExe;
import com.bat.distributor.service.common.Constant;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.qrcode.api.ThirdPartyQrCodeServiceRpc;
import com.bat.dubboapi.thirdparty.qrcode.dto.UserQrCodeRpcCmd;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/25 14:17
 */
@Component
public class UserRpcCmdExe {

    @Resource
    private CommonRpcExe commonRpcExe;

    @Resource
    private DistributorContactsMapper contactsMapper;

    @DubboReference(check = false, timeout = 30000)
    private ThirdPartyQrCodeServiceRpc qrCodeServiceRpc;

    /**
     * 获取分销商手机号验证码
     *
     * @param cmd
     * @return
     */
    public Boolean getPhoneVerifyCode(UserPhoneVerify cmd) {
        if(!cmd.isSkipCheck()){
            DistributorContactsDO contactsDO = contactsMapper.selectByPhoneOrEmail(cmd.getPhone());

            if (ObjectUtils.isNotEmpty(contactsDO)){
                //根据分销商ID查询分销商是否被冻结
                Integer freezeStatus=contactsMapper.queryWhetherToFreeze(contactsDO.getDistributorId());
                if (freezeStatus==2){
                    throw DistributorException.buildException(ErrorCode.THE_ACCOUNT_HAS_BEEN_FROZEN);
                }
            }

            if (cmd.getCodeType().equals(Constant.CODE_TYPE_1) && contactsDO != null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
            }
            if (!cmd.getCodeType().equals(Constant.CODE_TYPE_1) && contactsDO == null) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_NULL);
            }
        }
        return commonRpcExe.getPhoneVerifyCode(cmd.getPhone(), cmd.getCodeType());
    }

    /**
     * 分销商前台手机号验证验证码
     * 
     * @param cmd
     * @return
     */
    public Boolean checkPhoneVerifyCode(UserPhoneVerifyCodeCmd cmd) {
        return commonRpcExe.checkPhoneVerifyCode(cmd.getPhone(), cmd.getCodeType(), cmd.getCode());
    }

    public String getDistributionQrCode(UserQrCodeQry qry) {
        UserQrCodeRpcCmd cmd = new UserQrCodeRpcCmd();
        BeanUtils.copyProperties(qry, cmd);
        Response<String> rpcResponse = qrCodeServiceRpc.distributionQrCode(cmd);
        if (rpcResponse.isSuccess()) {
            return rpcResponse.getData();
        } else {
            throw DistributorException.buildException(rpcResponse.getErrCode(), rpcResponse.getErrMessage());
        }
    }

    public boolean checkRegion(Integer countryId, Integer provinceId, Integer cityId, Integer districtId) {
        return commonRpcExe.checkRegion(countryId,provinceId,cityId,districtId);
    }
}
