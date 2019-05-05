package com.bat.dubboapi.thirdparty.wx.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.wx.dto.WxMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.WxOfficialProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxOfficialProgramAuthorizeRpcDTO;
import com.bat.dubboapi.thirdparty.wx.dto.subaccount.WxSubAccountReleCmd;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/6/9 9:48
 */
public interface WxServiceRpc {
    /**
     * 微信小程序授权
     *
     * @param cmd
     * @return
     */
    Response<WxMiniProgramAuthorizeRpcDTO> wxMiniProgramAuthorize(WxMiniProgramAuthorizeRpcCmd cmd);

    /**
     * 微信公众号授权
     *
     * @param cmd
     * @return
     */
    Response<WxOfficialProgramAuthorizeRpcDTO> wxOfficialProgramAuthorize(WxOfficialProgramAuthorizeRpcCmd cmd);


    /**
     * 生成小程序二维码
     * @param appId 小程序appId
     * @param scene   exchangeId=97&orderType=1
     * @param pageUrl 二维码跳转的url
     * @param fileName 文件名
     * @param folder 文件夹名
     * @param appSecret 密码、不传就去分销商服务取
     */
    Response<String> createWechatProgramQrCode(String appId,String scene,String pageUrl,String fileName,String folder,String appSecret);


    /**
     * 获取小程序scheme码
     * @param appId
     * @param pageUrl
     * @param appSecret
     * @return
     */
    Response<String> createWechatProgramSchemeLink(String appId,String pageUrl,String appSecret);

    /**
     * 生成公众号网页二维码
     * @param platformNo
     * @param distributorId
     * @param appId
     * @return
     */
    Response<String> createWechatH5QrCode(Short platformNo,Integer distributorId, String appId);

    /**
     * 生成短链接
     * @param appId 小程序appId
     * @param pageUrl 短链接跳转的url
     * @param fileName 文件名
     * @param folder 文件夹名
     */
    Response<String> createWechatProgramShortLink(String appId,String pageUrl,String fileName,String folder);

    /**
     * 获取JS接口临时票据
     */
    Response<String> getJsapiTicket(String appId);

    /**
     * 微信服务商分账关系添加(V3版本)
     * @param wxSubAccountReleCmd
     * @return
     */
    Response createSubAccountRela(WxSubAccountReleCmd wxSubAccountReleCmd, Integer distributorId);

    /**
     * 微信服务商分账关系删除(V3版本)
     * @param wxSubAccountReleCmd
     * @return
     */
    Response deleteSubAccountRela(WxSubAccountReleCmd wxSubAccountReleCmd, Integer distributorId);
}
