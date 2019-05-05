package com.bat.thirdparty.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.common.distributor.DistributorConstant;
import com.bat.thirdparty.common.error.wx.WechatErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.wx.service.executor.WxRpcExe;
import com.bat.dubboapi.financial.pay.WxPayServiceRpc;
import com.bat.dubboapi.financial.pay.dto.WxPayConfigQry;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantUrlRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.oss.ThirdPartySystemOssServiceRpc;
import com.bat.dubboapi.thirdparty.qrcode.api.ThirdPartyQrCodeServiceRpc;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.dubboapi.thirdparty.wx.dto.WxMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.WxOfficialProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxOfficialProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.subaccount.WxSubAccountReleCmd;
import com.bat.thirdparty.wx.config.WxProgramConfig;
import com.bat.thirdparty.wx.service.dto.SubAccountRespDTO;
import com.bat.thirdparty.wx.utils.WechatUtil;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 10:23
 */
@DubboService
public class WxServiceRpcImpl implements WxServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxServiceRpcImpl.class);

    @Resource
    private WxRpcExe wxRpcExe;

    @Autowired
    private ThirdPartySystemOssServiceRpc thirdPartySystemOssServiceRpc;

    @Autowired
    private WxProgramConfig wxProgramConfig;

    @Resource
    private HttpUtil httpUtil;

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private WxPayServiceRpc wxPayServiceRpc;

    @Resource
    private ConfigQry configQry;

    @Resource
    private ThirdPartyQrCodeServiceRpc thirdPartyQrCodeServiceRpc;

    @CreateCache(name = ThirdKeyConstant.WECHAT_ACCESS_TOKEN_PRE)
    private Cache<String, String> accessTokenCacheKey;

    @Override
    public Response<WxMiniProgramAuthorizeRpcDTO> wxMiniProgramAuthorize(WxMiniProgramAuthorizeRpcCmd cmd) {
        return wxRpcExe.wxMiniProgramAuthorize(cmd);
    }

    @Override
    public Response<WxOfficialProgramAuthorizeRpcDTO> wxOfficialProgramAuthorize(WxOfficialProgramAuthorizeRpcCmd cmd) {
        return wxRpcExe.wxOfficialProgramAuthorize(cmd);
    }

    /**
     * 生成小程序二维码、返回byte数组
     * 
     * @param appId
     *            小程序appId
     * @param scene
     *            exchangeId=97&orderType=1
     * @param pageUrl
     *            二维码跳转的url
     * @param fileName
     *            文件名
     * @param folder
     *            文件夹名
     */
    @Override
    public Response<String> createWechatProgramQrCode(String appId, String scene, String pageUrl, String fileName,
        String folder, String appSecret) {
        // 获取令牌
        String accessToken = wxRpcExe.getAccessToken(appId, appSecret, DistributorConstant.WX_PLATFORM_TYPE_PROGRAM);
        RestTemplate rest = new RestTemplate();
        try {
            String url = wxProgramConfig.getMiniQrCodeUrl() + accessToken;
            Map<String, Object> param = new HashMap<>();
            param.put("scene", scene);
            if (StringUtils.isNotBlank(pageUrl)) {
                // 设置页面跳转
                param.put("page", pageUrl);
            }
            param.put("width", 430);
            param.put("auto_color", false);
            LOGGER.info("生成小程序参数:{}", JSONObject.toJSONString(param));
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity =
                rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            String ss = JSONObject.toJSONString(entity.getBody());
            LOGGER.info("调用小程序生成微信永久小程序码URL接口返回结果:" + ss);
            byte[] result = entity.getBody();
            String qrUrl = thirdPartySystemOssServiceRpc.uploadExtendStream(fileName, folder, result);

            return Response.of(qrUrl);
        } catch (Exception e) {
            LOGGER.error(MessageUtils.get(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR) + "{}", e);
            return Response.buildFailure(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR,
                MessageUtils.get(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR));
        }

    }

    @Override
    public Response<String> createWechatProgramSchemeLink(String appId,  String pageUrl,  String appSecret) {
        // 获取令牌
        String accessToken = wxRpcExe.getAccessToken(appId, appSecret, DistributorConstant.WX_PLATFORM_TYPE_PROGRAM);
        try {
            String url = wxProgramConfig.getMiniSchemeLinkUrl() + accessToken;
            //String url = "https://api.weixin.qq.com/wxa/generatescheme?access_token=" + "434634f56sa4f36sa4f";
            Map<String, Object> param = new HashMap<>();
            param.put("is_expire", true);
            param.put("expire_time", (System.currentTimeMillis()/1000)+2592000);
            JSONObject json=new JSONObject();
            json.put("path",pageUrl);
            json.put("query","");
            param.put("jump_wxa", json);
            LOGGER.info("生成小程序参数:{}", JSONObject.toJSONString(param));

            String result = httpUtil.requestFor(url, HttpMethod.POST, param, String.class);
            //对结果进行判断
            if(result != null){
                JSONObject data = JSON.parseObject(result);
                if(data.get("errcode").equals(WechatErrorCode.WX_SECRET_ERR_CODE_40001)){
                    //删除redis缓存
                    LOGGER.info("删除redis缓存:{}", TenantContext.getTenantNo() + ":" + appId);
                    accessTokenCacheKey.remove(TenantContext.getTenantNo() + ":" + appId);
                    accessToken = wxRpcExe.getAccessToken(appId, appSecret, DistributorConstant.WX_PLATFORM_TYPE_PROGRAM);
                    url = wxProgramConfig.getMiniSchemeLinkUrl() + accessToken;
                    result = httpUtil.requestFor(url, HttpMethod.POST, param, String.class);
                }
            }
            return Response.of(result);
        } catch (Exception e) {
            LOGGER.error(MessageUtils.get(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR) + "{}", e);
            return Response.buildFailure(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR,
                    MessageUtils.get(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR));
        }

    }

    @Override
    public Response<String> createWechatH5QrCode(Short platformNo, Integer distributorId, String appId) {
        PlatformTenantUrlRpcDTO tenantUrlConfig = configQry.getTenantUrlConfig(ThirdCommonConstant.URL_TYPE_4);
        // https://diy.bat.com/index?platform={0}&distributorId={1}&appid={2}
        String url = MessageFormat.format(tenantUrlConfig.getUrl(), platformNo, distributorId + "", appId);
        url = url.replace("index", "bindSaleman");
        // https://diy.bat.com/bindSaleman?platform={0}&distributorId={1}&appid={2}
        String fileName = "qrcode_" + distributorId + ".jpg";
        return thirdPartyQrCodeServiceRpc.createQrCode(fileName, "subAccount/qr/", url);
    }

    @Override
    public Response<String> createWechatProgramShortLink(String appId, String pageUrl, String fileName, String folder) {
        // 获取令牌
        String accessToken = wxRpcExe.getAccessToken(appId, null, null);
        try {
            String url = wxProgramConfig.getMinishortLinkUrl() + accessToken;
            Map<String, Object> param = new HashMap<>();
            // 设置页面跳转
            param.put("page_url", pageUrl);
            param.put("page_title", "活动跳转");
            param.put("is_permanent", true);
            String result = httpUtil.requestFor(url, HttpMethod.POST, param, String.class);
            return Response.of(JSONObject.parseObject(result).getString("link"));
        } catch (Exception e) {
            LOGGER.error(MessageUtils.get(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR) + "{}", e);
            return Response.buildFailure(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR,
                MessageUtils.get(WechatErrorCode.WECHAT_PROGRAM_QR_CODE_CREATE_ERROR));
        }

    }

    @Override
    public Response<String> getJsapiTicket(String appId) {
        return Response.of(wxRpcExe.getJsapiTicket(appId));
    }

    /**
     * 微信服务商分账添加接收方V3
     * 
     * @param wxSubAccountReleCreateCmd
     * @param distributorId
     * @return
     */
    @Override
    public Response createSubAccountRela(WxSubAccountReleCmd wxSubAccountReleCreateCmd, Integer distributorId) {
        LOGGER.info("微信服务商分账添加接收方、请求参数{}，distributorId:{}", JSON.toJSONString(wxSubAccountReleCreateCmd),
            distributorId);
        com.bat.dubboapi.financial.common.Response<WxPayConfigQry> response =
            wxPayServiceRpc.getWxPayClientV3ByDistributorIdAndAppId(distributorId, null, null, (short)2);
        if (!response.isSuccess()) {
            return Response.buildFailure(response.getErrCode(), response.getErrMessage());
        }
        try {
            WxPayConfigQry wxPayConfigQry = response.getData();
            LOGGER.info("微信服务商分账添加接收方、wxPayConfigQry{}", JSON.toJSONString(wxPayConfigQry));

            HttpClient httpClient = WechatUtil.getWechatHttpClientV3(wxPayConfigQry);
            HttpPost httpPost = new HttpPost(wxProgramConfig.getWxPaySubAccountRelaCreateUrl());
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");

            httpPost.addHeader("Wechatpay-Serial",
                WechatUtil.getWechatPayV3SerialNo(httpClient, wxProgramConfig.getWxPayPlatformCertificateQueryUrl()));

            if (StringUtils.isNotBlank(wxSubAccountReleCreateCmd.getName())) {
                try {
                    String s = RsaCryptoUtil.encryptOAEP(wxSubAccountReleCreateCmd.getName(),
                        wxPayConfigQry.getX509Certificate());
                    wxSubAccountReleCreateCmd.setName(s);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("微信支付加密异常{}", e.getMessage());
                    return Response.buildFailure(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR,
                        MessageUtils.get(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR));
                }
            }
            LOGGER.info("微信服务商分账添加接收方、请求参数(加密后){}", JSON.toJSONString(wxSubAccountReleCreateCmd));
            httpPost.setEntity(new StringEntity(JSON.toJSONString(wxSubAccountReleCreateCmd), "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            LOGGER.info("微信服务商分账添加接收方、返回{}", result);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                LOGGER.error("微信服务商分账添加接收方失败{}", result);
                try {
                    SubAccountRespDTO respDTO = JSON.parseObject(result, SubAccountRespDTO.class);
                    return Response.buildFailure(respDTO.getCode(), respDTO.getMessage());
                } catch (Exception e) {
                    return Response.buildFailure(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR,
                        MessageUtils.get(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR));
                }
            }
            return Response.buildSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("微信服务商分账添加接收方失败、IO异常{}", e.getMessage());
            return Response.buildFailure(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR,
                MessageUtils.get(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_ADD_ERROR));
        }

    }

    /**
     * 微信服务商分账删除接收方V3
     * 
     * @param wxSubAccountReleCmd
     * @param distributorId
     * @return
     */
    @Override
    public Response deleteSubAccountRela(WxSubAccountReleCmd wxSubAccountReleCmd, Integer distributorId) {
        com.bat.dubboapi.financial.common.Response<WxPayConfigQry> response =
            wxPayServiceRpc.getWxPayClientV3ByDistributorIdAndAppId(distributorId, null, null, (short)2);
        if (!response.isSuccess()) {
            return Response.buildFailure(response.getErrCode(), response.getErrMessage());
        }
        LOGGER.info("微信服务商分账删除接收方、请求参数{}", JSON.toJSONString(wxSubAccountReleCmd));
        WxPayConfigQry wxPayConfigQry = response.getData();
        HttpClient httpClient = WechatUtil.getWechatHttpClientV3(wxPayConfigQry);
        HttpPost httpPost = new HttpPost(wxProgramConfig.getWxPaySubAccountRelaDeleteUrl());
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(wxSubAccountReleCmd), "UTF-8"));
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            LOGGER.info("微信服务商分账删除接收方、返回{}", result);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
                LOGGER.error("微信服务商分账删除接收方失败{}", result);
                return Response.buildFailure(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_DELETE_ERROR,
                    MessageUtils.get(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_DELETE_ERROR));
            }
            return Response.buildSuccess();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("微信服务商分账删除接收方失败、IO异常{}", e.getMessage());
            return Response.buildFailure(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_DELETE_ERROR,
                MessageUtils.get(WechatErrorCode.WECHAT_PARTNER_SUB_ACCOUNT_RECEIVER_DELETE_ERROR));
        }

    }

}
