package com.bat.thirdparty.wx.service.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.wx.service.convertor.WxConvertor;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.wx.dto.WxMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.WxOfficialProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxOfficialProgramAuthorizeRpcDTO;
import com.bat.thirdparty.wx.api.response.WxProgramAuthorizeResponse;
import com.bat.thirdparty.wx.api.response.dto.WxUserInfoDTO;
import com.bat.thirdparty.wx.config.WxProgramConfig;
import com.bat.thirdparty.wx.utils.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 10:26
 */
@Component
public class WxRpcExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxRpcExe.class);

    @Resource
    private HttpUtil httpUtil;
    @Resource
    private WxProgramConfig programConfig;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @CreateCache(name = ThirdKeyConstant.WECHAT_ACCESS_TOKEN_PRE)
    private Cache<String, String> accessTokenCacheKey;

    @CreateCache(name = ThirdKeyConstant.WECHAT_JSAPI_TICKET_PRE)
    private Cache<String, String> jsapiTicketCacheKey;

    /**
     * 微信小程序授权(openid)
     *
     * @param cmd
     * @return
     */
    public Response<WxMiniProgramAuthorizeRpcDTO> wxMiniProgramAuthorize(WxMiniProgramAuthorizeRpcCmd cmd) {
        String url = programConfig.getMiniUserInfoUrl().replace("APPID", cmd.getAppid())
            .replace("SECRET", cmd.getSecret()).replace("JSCODE", cmd.getCode());
        WxProgramAuthorizeResponse response = httpUtil.requestFor(url, HttpMethod.GET,
            WechatUtil.getWxProgramAuthorizeHeaders(), WxProgramAuthorizeResponse.class);
        if (response.getErrcode() != null) {
            return Response.buildFailure(ErrorCode.B_WX_CODE_USED_ERROR, response.getErrmsg());
        }
        WxUserInfoDTO wxUserInfoDTO = null;
        if (StringUtils.isNotBlank(cmd.getEncryptedData())) {
            wxUserInfoDTO = WechatUtil.getUserInfo(cmd.getEncryptedData(), response.getSession_key(), cmd.getIv());
            if (wxUserInfoDTO == null) {
                LOGGER.info("微信小程序授权无法解析用户数据、返回{}", JSON.toJSONString(response));
                return Response.buildFailure(ErrorCode.B_WX_CODE_USED_ERROR, MessageUtils.get("B_WX_CODE_USED_ERROR"));
            }
        }
        return Response.of(WxConvertor.toWxProgramAuthorizeRpcDTO(response, wxUserInfoDTO));
    }

    /**
     * 微信公众号授权
     *
     * @param cmd
     * @return
     */
    public Response<WxOfficialProgramAuthorizeRpcDTO> wxOfficialProgramAuthorize(WxOfficialProgramAuthorizeRpcCmd cmd) {
        String url = programConfig.getOfficialUserInfoUrl().replace("APPID", cmd.getAppid())
            .replace("SECRET", cmd.getSecret()).replace("CODE", cmd.getCode());
        WxProgramAuthorizeResponse response = httpUtil.requestFor(url, HttpMethod.GET,
            WechatUtil.getWxProgramAuthorizeHeaders(), WxProgramAuthorizeResponse.class);
        if (response.getErrcode() != null) {
            return Response.buildFailure(ErrorCode.B_WX_CODE_USED_ERROR, response.getErrmsg());
        }
        WxOfficialProgramAuthorizeRpcDTO authorizeRpcDTO = new WxOfficialProgramAuthorizeRpcDTO();
        authorizeRpcDTO.setOpenid(response.getOpenid());
        return Response.of(authorizeRpcDTO);
    }

    /**
     * 根据APPID查询微信平台配置
     * 
     * @param appId
     * @return
     */
    public Response<WxPlatformRpcDTO> getWxPlatformByAppId(String appId) {
        com.bat.dubboapi.distributor.common.Response<WxPlatformRpcDTO> response =
            wxPlatformServiceRpc.getWxPlatformRpcDTOByAppId(appId);
        if (response == null) {
            return Response.buildFailure(ThirdDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION,
                MessageUtils.get(ThirdDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION));
        }
        return BeanUtils.copy(response, Response.class);
    }

    /**
     * 根据AppId获取令牌
     * 
     * @param appId
     * @param appSecret
     *            app密钥
     * @param type
     *            1、公众号 2、小程序
     * @return
     */
    public String getAccessToken(String appId, String appSecret, Short type) {
        String token = accessTokenCacheKey.get(TenantContext.getTenantNo() + ":" + appId);
        if (StringUtils.isBlank(token)) {
            if (StringUtils.isBlank(appSecret)) {
                Response<WxPlatformRpcDTO> response = getWxPlatformByAppId(appId);
                if (!response.isSuccess()) {
                    throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
                }
                WxPlatformRpcDTO wxPlatformRpcDTO = response.getData();
                // 获取token
                token = queryAccessToken(wxPlatformRpcDTO);
            } else {

                WxPlatformRpcDTO wxPlatformRpcDTO = new WxPlatformRpcDTO();
                wxPlatformRpcDTO.setAppId(appId);
                wxPlatformRpcDTO.setAppSecret(appSecret);
                wxPlatformRpcDTO.setType(type);
                // 获取token
                token = queryAccessToken(wxPlatformRpcDTO);
            }

        }
        return token;
    }

    public String getJsapiTicket(String appId) {
        String ticket = jsapiTicketCacheKey.get(TenantContext.getTenantNo() + ":" + appId);
        if (StringUtils.isNotBlank(ticket)) {
            return ticket;
        }
        // 获取令牌
        String accessToken = getAccessToken(appId, null, null);
        String url = programConfig.getOfficialJsapiTicket().replace("ACCESS_TOKEN",accessToken);
        LOGGER.info("获取ticket请求地址:{}",url);
        String result = httpUtil.requestFor(url, HttpMethod.GET, String.class);
        LOGGER.info("微信返回（结果）:" + result);
        ticket = JSONObject.parseObject(result).getString("ticket");
        if (StringUtils.isNotBlank(ticket)) {
            // 将ticket存入缓存中
            jsapiTicketCacheKey.put(TenantContext.getTenantNo() + ":" + appId, ticket, 3600, TimeUnit.SECONDS);
        }
        return ticket;
    }

    /**
     * 获取调用凭据并存入redis
     */
    public String queryAccessToken(WxPlatformRpcDTO wxPlatformRpcDTO) {

        String url = programConfig.getMiniAccessTokenUrl().replace("APPID", wxPlatformRpcDTO.getAppId())
            .replace("APPSECRET", wxPlatformRpcDTO.getAppSecret());
        if (wxPlatformRpcDTO.getType() - 1 == 0) {
            // 公众号
            url = programConfig.getOfficialAccessTokenUrl().replace("APPID", wxPlatformRpcDTO.getAppId())
                .replace("APPSECRET", wxPlatformRpcDTO.getAppSecret());
        }
        LOGGER.info("全局唯一调用凭据url:" + url);
        // 进行接口调用
        String result = httpUtil.requestFor(url, HttpMethod.GET, String.class);
        LOGGER.info("微信返回（结果）:" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String token = jsonObject.getString("access_token");
        if (StringUtils.isBlank(token)) {
            LOGGER.info("获取不到微信令牌数据");
            throw ThirdPartyException.buildException("获取微信小程序数据失败");
        }
        LOGGER.info("微信（令牌）返回的token", token);

        // 将token存入缓存中
        accessTokenCacheKey.put(TenantContext.getTenantNo() + ":" + wxPlatformRpcDTO.getAppId(), token, 7100,
            TimeUnit.SECONDS);
        return token;
    }
}
