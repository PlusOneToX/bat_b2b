package com.bat.thirdparty.wx.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/6/9 9:39
 */
@Data
@Component
@RefreshScope
public class WxProgramConfig {


    @Value("${wx.mini.program.userInfoUrl:null}")
    private String miniUserInfoUrl;
    @Value("${wx.official.program.userInfoUrl:null}")
    private String officialUserInfoUrl;

    //小程序令牌获取
    @Value("${wx.mini.program.accessTokenUrl:null}")
    private String miniAccessTokenUrl;

    //公众号令牌获取
    @Value("${wx.official.program.accessTokenUrl:null}")
    private String officialAccessTokenUrl;


    //小程序生成二维码
    @Value("${wx.mini.program.qrCodeUrl:null}")
    private String miniQrCodeUrl;

    //小程序scheme码请求地址
    @Value("${wx.mini.program.schemeLinkUrl:null}")
    private String miniSchemeLinkUrl;

    //小程序生成短链接
    @Value("${wx.mini.program.shortLinkUrl:null}")
    private String minishortLinkUrl;

    //JS接口临时票据
    @Value("${wx.official.program.jsapiTicket:null}")
    private String officialJsapiTicket;


    //微信服务商分账关系添加URL V3
    @Value("${wx.pay.subaccount.rela.createUrl:null}")
    private String wxPaySubAccountRelaCreateUrl;

    //微信服务商分账关系删除URL V3
    @Value("${wx.pay.subaccount.rela.deleteUrl:null}")
    private String wxPaySubAccountRelaDeleteUrl;

    //微信支付平台证书查询URL V3
    @Value("${wx.pay.platform.certificate.queryUrl:null}")
    private String wxPayPlatformCertificateQueryUrl;

}
