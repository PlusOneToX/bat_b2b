package com.bat.financial.refund;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.pay.dto.data.RefundTradeRespDTO;
import com.bat.financial.api.refund.dto.data.OrderRefundDTO;
import com.bat.financial.pay.data.AliConfig;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.utils.TradeNoUtils;

import lombok.extern.slf4j.Slf4j;

// @RunWith(SpringRunner.class)
// @SpringBootTest
@Slf4j
public class RefundServiceImplTest {

    @Resource
    private RefundServiceImpl refundService;

    @Test
    public void onlineRefund() {
        String json =
            "{\"businessTypes\":1,\"cashAmount\":8.00,\"customerId\":365803,\"depositAmount\":0,\"distributorRefundId\":7152,\"operatorId\":150512,\"operatorName\":\"\",\"orderId\":280572,\"outTradeNo\":\"alc1ce1475749903445569536\",\"receiverType\":2,\"refundMode\":2,\"refundType\":1,\"remark\":\"测试\"}";
        OrderRefundDTO orderRefundDTO = JSON.parseObject(json, OrderRefundDTO.class);
        refundService.onlineRefund(orderRefundDTO);
    }

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

    private static AliConfig getAliConfig() {
        AliConfig config = new AliConfig();
        config.setAppId("2018022162645163");
        config.setBackType((short)1);
        config.setAppPrivateSecret(
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLvHFmgV5xoOK3m0sK0DxnDlF4OwOEwJa9/5v8MRha8b9eHylqAJea2NXGeyodHITMc8jOLmIFyomfdg1vwxLhqF2YUc+IsJj4WsyZ69LdjkUbqYQNi7/IQK5kpSC/hJernPfbdQdXETEGXtRh3cskDGlzC0iiN4FsfD6NX51hDemFgMx+fG4Wsv8/xdle1dCjbfNwXGZDsa78TVZ2SG3lDecTMBoZHOxFpi34f45816GYk/t2UgyFs/euuagUxsaS9YLsoLsQM+DVxOBqSaorbpfO/PlEB4BIFKcfmltyx9wcH2kyN7J1WGprCE8utmXFDlqKD6B3RGLk3LuU3JqzAgMBAAECggEAWFMt5n6XbzOMloTgl6OHTDZM447SC69iqqQx/7dZE7RSfn+L+MSg9SSkKRQkQdKnT+uyVpCNNm2EFHmdgaJUgTf4u5xcGKpW5vSWVvSBykNvenqXIThAwJQqBr//3oehKWk3mxC1Se3ODzD1aig5dTtcS9/McWcW2O1UQP3+wJ/8wv+hZX6W92V5IsysWeB9gmrMdEq+FIC3Q4R2dp47DecZr/6+EROMMdRVuAg1pNuJp/1YfN52gzNH8wqST6gRDXLe/hlAxONIA5+BXtO+qW9t+mowGRzSMrJHcRUCcKeNJybpv7HmJ+vxO5w/0O+2ud3cqAFFGpEjcS4y6j3JQQKBgQD3WZ8YY9ck2QQMGGFsTAeiGCEn/cQySJrCzbVyc8xyW/tUF+w43ibhij30VK1u8rZyF+SAOaKW2z8E2mrXP1x43vyDnNnRWpLsJHUQ32Wiblr/T8qYLdugpKwl8o3zFFy1RGpz3EBNdhc6SOCPPLzfMnS/+oaTgxGa2HSPKdJsCwKBgQDS3F8WRRopy5LCyMSgoBQ3GIGa6JB4CIH3fy8He4EMniMe5rfE+cjDSUKaMNUQP588FdUj2nuaH9zTogH//cvAlqLIcsVNIKudqciTVTcp5Rlwi/QviVB/YHm/v8SLyEDb9fsWEtxrSbJYwXrLLepRwhjOXvh6zwUQLo8iEgMM+QKBgB8vro59sBeWQRthS49C+sdmMXAAX+dTHignlZQLo6BblpGgvojhAZf4PR18O628bmJuPsIoAibxF7395/ChrYAT5VSWxyPNPq8FXbk14XOcZF7CabHMPl+/w3C5Z0pZq+Ky8JURFQ5vMDiKDb4hk1K04uF9rdAVe3fBiNiwFjbHAoGBAMqBUeC1ZLcXB0+COTR6CHARXQdEqtpccVUcFDJ/biSMCva2ZW8K5MCILyqeE0GTmH3ACf5w6ZvPVej6AimzuGaxRIx1jP7RjX/RewvkHyJhH9lyz4SHv4NVSBfbOAt2urOEvSsp2f4/vMR4rFFNO2hhnCk2FDT9dMNhs5zo530hAoGBAPHzr/RcTTCjH8kGz1xd6ETGE1sF+VXfk87WP1y0hRRf6p1wVT1VFqWtXSa0zq9Ua+zrbeSpgAG0TnSIC0O73h26+JcSSQJpKc5YiRLtkkEr7wmiLiyXgAuPtkLlMPnS89AmB5kmUKmgLY9YGAkYblPSgpXxWLD+Vv+PzSmnpOhf");
        config.setAppPublicSecret(
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArUl6zkBFBJ2o78ZmOdimORJV2uvtCYB2/xDIr1DXCxalsOCGzBQQM7stIv8JTHWHJNvlBl1PZ7khASS20CUIGLZVa0JVHTGWbV7AKCe1WAFUIJnpzN5ZSx+G1nYt/yCsOBqpGbdQkCzsOvWlJcbNIu1uneybY2wxcqHgUQSoG2Du1UBelABSG/ESvpEDA6co9Y80FvApGB0fHXUire6SY1Wq5jE3z0+nlw+Zpsv9Ivr3bOplHu6eLJFYw29Oxc6k0+yfcVK9xJ9krXjTE6V7Qi41zOCWTwapXDVGSNLFsQruf76H3GW4PoI2SBf4pRMJ47oSSqOrVRxz5kbzoGH6pwIDAQAB");
        return config;
    }

    private static AliConfig getAliConfig1() {
        AliConfig config = new AliConfig();
        config.setAppId("2018002198612750");
        config.setBackType((short)1);
        config.setAppPrivateSecret(
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWxcGf+lwcNYncsdhvhPioMPNr0OqFHDqdrJ71OlXmSPq7H4eHodq+3wTXLzpBOJumJE5PXHiv7iQcsMRcFrOQ7wibeLchxWVeZ7V9U6FxTbfQ9YxI6uZ/SyhvtP8ziYGKkclnxcx9GbKL+zWyWV9ctcbhxisAH+s/WpsZxyDElnpc75Q/z8jo3BwW+zOXkEgul7/0OB747KhgICDdx06Gn71HIGqOAsOH60ruD9wJ9HBx1Q4JSiTjYzbU/7eWSjP9d+oL5aWleFFE5groqRahs5mmCMKUKy97D8GbBLDqD/SbST3xQUZ/b5vQHkLSvkv7O0IjLkgFdg5zZtlz4B4fAgMBAAECggEATDx+ZVgAjzr7FJ7Qo8MGwHTQAOpISzo04VlRnwn6OVEZhtiwfQJ4Sx8VoYD94dCv01h0SGinlb3CgMdY4n+QGG7lWUe2wdrAIPYrrM1GyrXThlNCtfQgzKjCL4nDqNmUqO6Cenhnp7IAuAsz4ZSTRZucjrjXMV7yYrx7xJMNnJj67yESOnX38KlPGNM6Xfq162F3zHSOLczWOUXsB/9oey63S7YlLwSzc6r/rMfc4oz0eOpBI19lVqOGp9EUvubqumhAGgBxTk8FPP5Q6789PpPgwlJc+8y7RQdBher/dQYL/NLfJprG23P6dKgsJDaYlK0SoofzXrH2gqPETYFm8QKBgQDSO7ps6iwuNiL7A1SerqjDhfSDtqaJO9ejp+Y7qYPwejgxsq993xkiFYvUSApIFD6/hwtcfvt3AgpUKTV545DSQre0E3HERk+Ya7K+3xzugxhY+UjTWOyirJMX8mLdIjtNuRNryGDahPNNb1H1CM/y2RuyNyxjnepaUKY6/3U/WwKBgQC3mEjvcCaTTtNloTi0JDXuM19t4jjswNgcupq6r1S+22LYNhLS5xtsknnD7w9gTjI64EKru9ZwCA9A7KdFp8HCQRXxIuwHc5UIqnNTIOxEUXYBqnw8UBrR8xnGZzHwdOVgg+DIBzXiBNDGZJwHu6ocygLz/4RTkO1rRC9sRur7jQKBgQCEPMujzJxjqOvR5iNo+MXwzqah3tbcBYc7StYX+yWhss77+8BFNXqypMjiiznkYs+hW4l68eisz9y9WXpopAqpJww0OfLambthTYUOaHrjUBSViqATSKD4JgMUEgO7wtd4qQydRFofuXbs4zOzJIrg3017iRbjxiXuD0KwY5VVdwKBgCMSFQSgEM5c5WbpiDJpJGAvMhCEgUlAgUfjoYD90xtuPxB8K9ZbzVJNHLN/7BkCdvfmNPYnOIxIDdniGwXkSM/5rN5WRG4b9yEjpkn/5QIb6YXh8j93MdhfLtJGjjSXr+Vi68JqFjHpp5PuThvwDkvXjv7bFYT9D01JRiHR3jtVAoGBAKP2312FxxOhlQUZ2SJQ4+pC35wEFcwVe/tTXfGEZOkb92N9evolqnSfqfMGZ6/T4Sn/IUVE9YXUC3ykgwwVnRSBzs+hWwa4dirpe52moNDnqHomqKXdP3x4qF7Ab9qznlStVmDgSllttw/ZNAOLZS6VuL6i1AF3THg12VLO9jAm");
        config.setAppPublicSecret(
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiJWz5tKpUsYO0aUT5FYrPpZ20tjvvoXyhX6w9ZWdeBtphKzGIul2eE4nNjqXCUASMcmAJaluQ6yBusrVt0VKDOhZzVKuwo0KTu6JhVFG/u9lKxCPgptaQ6cJwodztbobxLS4pAg/377Z1nVcyMYJCDDeo3XlTbl8SJ81CZIARilq4we96FwsjoOofloevH/c2TmD4VMs3teQ850HI8QEAI6yr4hEW8DFBZC9EqMPeBZd0vXct7OSly46fyKnBGVi3HcPyg1PZ6TToc11zOJIPX/2nLtFMt43S0Jma06psE5RsaGZ27rXpnl3Kw8NSwgXq3z094sRjoPsGTTcvDHfLwIDAQAB");
        return config;
    }

    public static void main(String[] args) throws Exception {

        String outTradeNo = "alc1ce1512059513651355648";
        String refundAmount = "9.5";

        AliConfig config = getAliConfig1();
        Config options = getOptions(config);
        Factory.setOptions(options);
        String al = TradeNoUtils.getRefundTradeNo("al", (short)0);
        log.info("退款凭证号：{}", al);
        AlipayTradeRefundResponse response = Factory.Payment.Common().optional("refund_reason", "订单关闭退款")
            .optional("refund_currency", "CNY").optional("out_request_no", al).refund(outTradeNo, refundAmount);
        log.info("退款返回信息：" + response.getHttpBody());
        if (ResponseChecker.success(response)) {
            log.info("调用成功");
            ObjectMapper objectMapper = new ObjectMapper();
            RefundTradeRespDTO.AlipayRefundTradeRespDTO respDTO =
                objectMapper.readValue(response.getHttpBody(), RefundTradeRespDTO.AlipayRefundTradeRespDTO.class);
            respDTO.setOutRequestNo(al);
            if ("Y".equals(respDTO.getAlipayTradeRefundResponse().getFundChange())) {
                log.info("退款成功");
            }
        } else {
            // 有一种情况 交易已关闭是 已经退款的意思 40004，ACQ.TRADE_HAS_CLOSE Business Failed，交易已经关闭
            log.info("调用失败");
            log.error(response.code + "，" + response.subCode);
            log.error(response.msg + "，" + response.subMsg);
            if ("40004".equals(response.code) && "ACQ.TRADE_HAS_CLOSE".equals(response.subCode)) {
            }
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR,
                response.msg + "，" + response.subMsg);
        }
    }
}