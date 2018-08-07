package com.bat.distributor.service.platform.wxplatform;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.service.distributor.executor.DistributorConfigExe;
import com.bat.distributor.service.platform.wxplatform.executor.WxPlatformQryExe;
import com.bat.distributor.service.common.DistributorConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.distributor.service.common.MessageUtils;
import com.bat.distributor.service.common.platform.PlatformErrorCode;
import com.bat.distributor.service.platform.convertor.PlatformConvertor;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;

@DubboService
public class WxPlatformServiceRpcImpl implements WxPlatformServiceRpc {


    @Resource
    private WxPlatformQryExe wxPlatformQryExe;

    @Resource
    private DistributorConfigExe distributorConfigExe;

    /**
     * 根据AppId获取微信平台配置
     * @param appId
     * @return
     */
    @Override
    public Response<WxPlatformRpcDTO> getWxPlatformRpcDTOByAppId(String appId) {
        if(StringUtils.isBlank(appId)){
            return Response.buildFailure(PlatformErrorCode.P_APPID_NULL, MessageUtils.get(PlatformErrorCode.P_APPID_NULL));
        }
        WxPlatformDO wxPlatformDO = wxPlatformQryExe.getByAppId(appId);
        if(wxPlatformDO ==null){
            return Response.buildFailure(PlatformErrorCode.P_APPID_ERROR, MessageUtils.get(PlatformErrorCode.P_APPID_ERROR));
        }
        return Response.of(PlatformConvertor.toWxPlatformRpcDTO(wxPlatformDO));
    }

    /**
     * 其他服务
     * 
     * 获取 配置中心授权小程序的appId appSecret
     * 
     * @return
     */
    @Override
    public Response<WxPlatformRpcDTO> getMiniProgramAuthorize() {
        DistributorConfig distributorConfig= distributorConfigExe.getConfig();
        WxPlatformRpcDTO dto = new WxPlatformRpcDTO();
        dto.setAppId(distributorConfig.getWxMiniProgramAppId());
        dto.setAppSecret(distributorConfig.getWxMiniProgramAppSecret());
        return Response.of(dto);
    }

    @Override
    public Response<List<WxPlatformRpcDTO>> listByDistributorIdAndType(Integer distributorId, Short type) {
        List<WxPlatformDO> list = wxPlatformQryExe.listByDistributorIdAndType(distributorId,type);
        if(list ==null || list.size()==0){
            return Response.of(null);
        }
        return Response.of(PlatformConvertor.toWxPlatformRpcDTOList(list));
    }
}
