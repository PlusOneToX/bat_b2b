package com.bat.distributor.service.customer.executor;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.user.dto.customer.CustomerAlipayProgramMiniLoginCmd;
import com.bat.distributor.api.user.dto.customer.CustomerDyProgramMiniLoginCmd;
import com.bat.distributor.api.user.dto.customer.CustomerWxMiniProgramLoginCmd;
import com.bat.distributor.api.user.dto.customer.CustomerWxOfficialProgramLoginCmd;
import com.bat.distributor.dao.platform.dataobject.AlipayPlatformDO;
import com.bat.distributor.dao.platform.dataobject.DyPlatformDO;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.service.customer.convertor.CustomerConvertor;
import com.bat.distributor.service.common.CommonRpcExe;
import com.bat.distributor.service.common.DistributorConfig;
import com.bat.dubboapi.thirdparty.alibaba.alipay.api.AlipayServiceRpc;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.AlipayProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data.AlipayProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.dy.api.DyServiceRpc;
import com.bat.dubboapi.thirdparty.dy.dto.DyMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.dy.dto.data.DyMiniProgramAuthorizeRpcDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.dubboapi.thirdparty.wx.dto.WxMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.WxOfficialProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxOfficialProgramAuthorizeRpcDTO;

import javax.annotation.Resource;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/8 14:39
 */
@Component
public class CustomerRpcCmdExe {
    @DubboReference(check = false, timeout = 5000)
    private WxServiceRpc wxServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private DyServiceRpc dyServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private AlipayServiceRpc alipayServiceRpc;

    @Resource
    private DistributorConfig config;

    @Resource
    private CommonRpcExe commonRpcExe;

    /**
     * 微信小程序授权
     * 
     * @param cmd
     * @param wxPlatformDO
     * @return
     */
    WxMiniProgramAuthorizeRpcDTO wxMiniProgramAuthorize(CustomerWxMiniProgramLoginCmd cmd, WxPlatformDO wxPlatformDO) {
        WxMiniProgramAuthorizeRpcCmd rpcCmd = CustomerConvertor.toWxMiniProgramAuthorizeRpcDTO(cmd, wxPlatformDO);
        Response<WxMiniProgramAuthorizeRpcDTO> rpcDTOResponse = wxServiceRpc.wxMiniProgramAuthorize(rpcCmd);
        if (!rpcDTOResponse.isSuccess()) {
            throw DistributorException.buildException(rpcDTOResponse.getErrCode(), rpcDTOResponse.getErrMessage());
        }
        return rpcDTOResponse.getData();
    }

    /**
     * 抖音小程序授权
     * @param cmd
     * @param dyPlatformDO
     * @return
     */
    public DyMiniProgramAuthorizeRpcDTO dyMiniProgramAuthorize(CustomerDyProgramMiniLoginCmd cmd, DyPlatformDO dyPlatformDO) {
        DyMiniProgramAuthorizeRpcCmd rpcCmd =
                CustomerConvertor.toDyMiniProgramAuthorizeRpcDTO(cmd, dyPlatformDO);
        Response<DyMiniProgramAuthorizeRpcDTO> rpcDTOResponse = dyServiceRpc.dyMiniProgramAuthorize(rpcCmd);
        if (!rpcDTOResponse.isSuccess()) {
            throw DistributorException.buildException(rpcDTOResponse.getErrCode(), rpcDTOResponse.getErrMessage());
        }
        return rpcDTOResponse.getData();
    }

    /**
     * 支付宝小程序授权
     * @param cmd
     * @param alipayPlatformDO
     */
    public AlipayProgramAuthorizeRpcDTO alipayMiniProgramAuthorize(CustomerAlipayProgramMiniLoginCmd cmd, AlipayPlatformDO alipayPlatformDO) {
        AlipayProgramAuthorizeRpcCmd rpcCmd =
                CustomerConvertor.toAlipayMiniProgramAuthorizeRpcDTO(cmd, alipayPlatformDO);
        Response<AlipayProgramAuthorizeRpcDTO> rpcDTOResponse = alipayServiceRpc.alipayMiniProgramAuthorize(rpcCmd);
        if (!rpcDTOResponse.isSuccess()) {
            throw DistributorException.buildException(rpcDTOResponse.getErrCode(), rpcDTOResponse.getErrMessage());
        }
        return rpcDTOResponse.getData();
    }

    /**
     * 微信公众号授权
     * 
     * @param cmd
     * @param wxPlatformDO
     * @return
     */
    WxOfficialProgramAuthorizeRpcDTO wxOfficialProgramAuthorize(CustomerWxOfficialProgramLoginCmd cmd,
                                                                WxPlatformDO wxPlatformDO) {
        WxOfficialProgramAuthorizeRpcCmd rpcCmd =
            CustomerConvertor.toWxOfficialProgramAuthorizeRpcDTO(cmd, wxPlatformDO);
        Response<WxOfficialProgramAuthorizeRpcDTO> rpcDTOResponse = wxServiceRpc.wxOfficialProgramAuthorize(rpcCmd);
        if (!rpcDTOResponse.isSuccess()) {
            throw DistributorException.buildException(rpcDTOResponse.getErrCode(), rpcDTOResponse.getErrMessage());
        }
        return rpcDTOResponse.getData();
    }

    public boolean checkRegion(Integer countryId, Integer provinceId, Integer cityId, Integer districtId) {
        if(countryId==null){
            countryId = config.getCountryChina();
        }
        // 移动端 去掉地址校验
        // return commonRpcExe.checkRegion(countryId,provinceId,cityId,districtId);
        return true;
    }
}
