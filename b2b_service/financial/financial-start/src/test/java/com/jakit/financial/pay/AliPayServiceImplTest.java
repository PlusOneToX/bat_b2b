package com.bat.financial.pay;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayMarketingCashlessvoucherTemplateCreateRequest;
import com.alipay.api.request.AlipayMarketingVoucherSendRequest;
import com.alipay.api.request.AlipayMarketingVoucherTemplatedetailQueryRequest;
import com.alipay.api.response.AlipayMarketingCashlessvoucherTemplateCreateResponse;
import com.alipay.api.response.AlipayMarketingVoucherSendResponse;
import com.alipay.api.response.AlipayMarketingVoucherTemplatedetailQueryResponse;
import com.alipay.easysdk.kernel.Config;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.financial.pay.data.AliConfig;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/4/6 19:22
 */
public class AliPayServiceImplTest {

    private static Config getOptions(AliConfig alipayConfig) {
        Config config = new Config();
        String url = "https://openapi.alipay.com/gateway.do";
        // 公司测试环境沙箱账号
        if ("2016102100730692".equals(alipayConfig.getAppId())) {
            url = "https://openapi.alipaydev.com/gateway.do";
        }
        config.protocol = url.substring(0, url.indexOf("://"));
        config.gatewayHost = url.substring(url.indexOf("://") + 3, url.indexOf("/gateway.do"));
        config.signType = "RSA2";
        config.appId = alipayConfig.getAppId();
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayConfig.getAppPrivateSecret();
        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2018051064521003.crt -->";
        // config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        // config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";
        // 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = alipayConfig.getAppPublicSecret();
        // 可设置异步通知接收服务地址（可选）
        // config.notifyUrl = "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        // 可设置AES密钥，调用AES加解密相关接口时需要（可选）
        // config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";
        return config;
    }

    private static AlipayClient getAlipayClient() {
        AliConfig config = new AliConfig();
        config.setAppId("2018022162645163");
        config.setBackType((short)1);
        config.setAppPrivateSecret(
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLvHFmgV5xoOK3m0sK0DxnDlF4OwOEwJa9/5v8MRha8b9eHylqAJea2NXGeyodHITMc8jOLmIFyomfdg1vwxLhqF2YUc+IsJj4WsyZ69LdjkUbqYQNi7/IQK5kpSC/hJernPfbdQdXETEGXtRh3cskDGlzC0iiN4FsfD6NX51hDemFgMx+fG4Wsv8/xdle1dCjbfNwXGZDsa78TVZ2SG3lDecTMBoZHOxFpi34f45816GYk/t2UgyFs/euuagUxsaS9YLsoLsQM+DVxOBqSaorbpfO/PlEB4BIFKcfmltyx9wcH2kyN7J1WGprCE8utmXFDlqKD6B3RGLk3LuU3JqzAgMBAAECggEAWFMt5n6XbzOMloTgl6OHTDZM447SC69iqqQx/7dZE7RSfn+L+MSg9SSkKRQkQdKnT+uyVpCNNm2EFHmdgaJUgTf4u5xcGKpW5vSWVvSBykNvenqXIThAwJQqBr//3oehKWk3mxC1Se3ODzD1aig5dTtcS9/McWcW2O1UQP3+wJ/8wv+hZX6W92V5IsysWeB9gmrMdEq+FIC3Q4R2dp47DecZr/6+EROMMdRVuAg1pNuJp/1YfN52gzNH8wqST6gRDXLe/hlAxONIA5+BXtO+qW9t+mowGRzSMrJHcRUCcKeNJybpv7HmJ+vxO5w/0O+2ud3cqAFFGpEjcS4y6j3JQQKBgQD3WZ8YY9ck2QQMGGFsTAeiGCEn/cQySJrCzbVyc8xyW/tUF+w43ibhij30VK1u8rZyF+SAOaKW2z8E2mrXP1x43vyDnNnRWpLsJHUQ32Wiblr/T8qYLdugpKwl8o3zFFy1RGpz3EBNdhc6SOCPPLzfMnS/+oaTgxGa2HSPKdJsCwKBgQDS3F8WRRopy5LCyMSgoBQ3GIGa6JB4CIH3fy8He4EMniMe5rfE+cjDSUKaMNUQP588FdUj2nuaH9zTogH//cvAlqLIcsVNIKudqciTVTcp5Rlwi/QviVB/YHm/v8SLyEDb9fsWEtxrSbJYwXrLLepRwhjOXvh6zwUQLo8iEgMM+QKBgB8vro59sBeWQRthS49C+sdmMXAAX+dTHignlZQLo6BblpGgvojhAZf4PR18O628bmJuPsIoAibxF7395/ChrYAT5VSWxyPNPq8FXbk14XOcZF7CabHMPl+/w3C5Z0pZq+Ky8JURFQ5vMDiKDb4hk1K04uF9rdAVe3fBiNiwFjbHAoGBAMqBUeC1ZLcXB0+COTR6CHARXQdEqtpccVUcFDJ/biSMCva2ZW8K5MCILyqeE0GTmH3ACf5w6ZvPVej6AimzuGaxRIx1jP7RjX/RewvkHyJhH9lyz4SHv4NVSBfbOAt2urOEvSsp2f4/vMR4rFFNO2hhnCk2FDT9dMNhs5zo530hAoGBAPHzr/RcTTCjH8kGz1xd6ETGE1sF+VXfk87WP1y0hRRf6p1wVT1VFqWtXSa0zq9Ua+zrbeSpgAG0TnSIC0O73h26+JcSSQJpKc5YiRLtkkEr7wmiLiyXgAuPtkLlMPnS89AmB5kmUKmgLY9YGAkYblPSgpXxWLD+Vv+PzSmnpOhf");
        config.setAppPublicSecret(
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArUl6zkBFBJ2o78ZmOdimORJV2uvtCYB2/xDIr1DXCxalsOCGzBQQM7stIv8JTHWHJNvlBl1PZ7khASS20CUIGLZVa0JVHTGWbV7AKCe1WAFUIJnpzN5ZSx+G1nYt/yCsOBqpGbdQkCzsOvWlJcbNIu1uneybY2wxcqHgUQSoG2Du1UBelABSG/ESvpEDA6co9Y80FvApGB0fHXUire6SY1Wq5jE3z0+nlw+Zpsv9Ivr3bOplHu6eLJFYw29Oxc6k0+yfcVK9xJ9krXjTE6V7Qi41zOCWTwapXDVGSNLFsQruf76H3GW4PoI2SBf4pRMJ47oSSqOrVRxz5kbzoGH6pwIDAQAB");
        // Config options = getOptions(config);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", config.getAppId(),
            config.getAppPrivateSecret(), "json", "UTF-8", config.getAppPublicSecret(), "RSA2");
        return alipayClient;
    }

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        AlipayClient alipayClient = getAlipayClient();
        // String templateId = marketingCashlessvoucherTemplateCreate(mapper, alipayClient);
        String templateId = "20180323000730011163006YVUPU";
        System.out.println(templateId);
        // marketingVoucherSend(alipayClient,mapper,templateId);
        marketingVoucherTemplatedetailQuery(alipayClient,templateId);
    }

    private static void marketingVoucherTemplatedetailQuery(AlipayClient alipayClient, String templateId) throws AlipayApiException {
        AlipayMarketingVoucherTemplatedetailQueryRequest request =
            new AlipayMarketingVoucherTemplatedetailQueryRequest();
        request.setBizContent("{" + "\"template_id\":\""+templateId+"\"" + "  }");
        AlipayMarketingVoucherTemplatedetailQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println(JSON.toJSONString(response));
            System.out.println("调用成功");
        } else {
            String code = response.getCode();
            String msg = response.getMsg();
            String subCode = response.getSubCode();
            String subMsg = response.getSubMsg();
            System.out.println("调用失败");
            System.out.println("code:" + code);
            System.out.println("msg:" + msg);
            System.out.println("subCode:" + subCode);
            System.out.println("subMsg:" + subMsg);
        }
    }

    private static void marketingVoucherSend(AlipayClient alipayClient, ObjectMapper mapper, String templateId)
        throws JsonProcessingException, AlipayApiException {
        AlipayMarketingVoucherSendRequest request = new AlipayMarketingVoucherSendRequest();
        VoucherReq req = new VoucherReq();
        req.setTemplateId(templateId);
        req.setLoginId("18995912083");
        req.setTaobaoNick("bat2018少2012");
        req.setOutBizNo("2016122814164328aadaee_1aee_4");
        String s = mapper.writeValueAsString(req);
        System.out.println(s);
        request.setBizContent(s);
        AlipayMarketingVoucherSendResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            String code = response.getCode();
            String msg = response.getMsg();
            String subCode = response.getSubCode();
            String subMsg = response.getSubMsg();
            System.out.println("调用失败");
            System.out.println("code:" + code);
            System.out.println("msg:" + msg);
            System.out.println("subCode:" + subCode);
            System.out.println("subMsg:" + subMsg);
        }
    }

    private static String marketingCashlessvoucherTemplateCreate(ObjectMapper mapper, AlipayClient alipayClient)
        throws JsonProcessingException, AlipayApiException {
        AlipayMarketingCashlessvoucherTemplateCreateRequest request =
            new AlipayMarketingCashlessvoucherTemplateCreateRequest();
        TemplateReq req = new TemplateReq();
        req.setVoucherType("CASHLESS_FIX_VOUCHER");
        req.setBrandName("");
        // req.setBrandName("全场订单满19元享5折优惠");
        req.setPublishStartTime("2018-03-21 00:00:00");
        req.setPublishEndTime("2018-04-30 23:59:59");
        TemplateReq.ValidPeriod period = new TemplateReq.ValidPeriod();
        period.setType("RELATIVE");
        period.setDuration("60");
        period.setUnit("DAY");
        req.setVoucherValidPeriod(mapper.writeValueAsString(period));
        List<TemplateReq.AvailableTime> list = new ArrayList<>();
        req.setVoucherAvailableTime(mapper.writeValueAsString(list));
        req.setOutBizNo("20170101000001654bb46ba");
        req.setVoucherDescription("[\"1、本券不可兑换现金，不可找零。\",\"2、每个用户最多可以领取1张。\",\"3、如果订单发生退款，优惠券无法退还。\"]");
        req.setFloorAmount(19.00);
        String s = mapper.writeValueAsString(req);
        System.out.println(s);
        request.setBizContent(s);
        AlipayMarketingCashlessvoucherTemplateCreateResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
            return response.getTemplateId();
        } else {
            String code = response.getCode();
            String msg = response.getMsg();
            String subCode = response.getSubCode();
            String subMsg = response.getSubMsg();
            System.out.println("调用失败");
            System.out.println("code:" + code);
            System.out.println("msg:" + msg);
            System.out.println("subCode:" + subCode);
            System.out.println("subMsg:" + subMsg);
        }
        return null;
    }

}
