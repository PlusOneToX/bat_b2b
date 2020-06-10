package com.bat.flexible.manager.wechat.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.flexible.api.wechat.dto.GzConfigDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.flexible.manager.common.ConfigQry;
import com.bat.flexible.manager.common.config.FlexibleConfig;
import com.bat.flexible.manager.common.constant.dubbo.DistributorConstant;
import com.bat.flexible.manager.common.utils.Sha1Handler;
import com.bat.flexible.manager.common.utils.wechat.WechatUtil;
import com.bat.flexible.manager.error.common.FlexibleDubboServiceErrorCode;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

@Component
public class WechatExe {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WxServiceRpc wxServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @Resource
    private ConfigQry configQry;

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatExe.class);

    public GzConfigDTO getGzConfig(String url, String appId) {
        if(StringUtils.isBlank(appId)) {
            // 平台类型 1、公众号、2小程序
            FlexibleConfig flexibleConfig = configQry.getTenantExchangeDistributorId();
            com.bat.dubboapi.distributor.common.Response<List<WxPlatformRpcDTO>> distributorResponse =
                    wxPlatformServiceRpc.listByDistributorIdAndType(flexibleConfig.getExchangeDistributorId(),
                            DistributorConstant.DISTRIBUTOR_WECHAT_TYPE_MINI_PROGRAM);
            if (distributorResponse == null || !distributorResponse.isSuccess()) {
                throw new FlexibleCustomException(FlexibleDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
            }
            List<WxPlatformRpcDTO> platformRpcDTOList = distributorResponse.getData();
            if (platformRpcDTOList == null || platformRpcDTOList.size() == 0) {
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_QR_CODE_FAIL_DISTRIBUTOR_RELA_WECHAT_NULL);
            }
        }
        String ticket = wxServiceRpc.getJsapiTicket(appId).getData();// 公众号网页授权ticket
        String noncestr = WechatUtil.createNonceStr();// 随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
        // 将参数排序并拼接字符串
        String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        LOGGER.info("微信授权拼接后的字符串{}", str);
        // 将字符串进行sha1加密
        String signature = Sha1Handler.encryption(str);
        GzConfigDTO gzConfigDTO = new GzConfigDTO();
        gzConfigDTO.setAppId(appId);
        gzConfigDTO.setNonceStr(noncestr);
        gzConfigDTO.setTimestamp(timestamp);
        gzConfigDTO.setSignature(signature.toLowerCase());
        LOGGER.info("微信授权信息{}", JSON.toJSONString(gzConfigDTO));
        return gzConfigDTO;
    }
}
