package com.bat.thirdparty.alibaba.alipay.executor;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.AlipayProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.alibaba.alipay.dto.data.AlipayProgramAuthorizeRpcDTO;
import com.bat.thirdparty.common.base.ThirdPartyException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/12/3 20:25
 */
@Component
@Slf4j
public class AlipayRpcExe {
    @SneakyThrows
    public AlipayProgramAuthorizeRpcDTO alipayMiniProgramAuthorize(AlipayProgramAuthorizeRpcCmd rpcCmd) {
        String code = rpcCmd.getCode();
        if (code.contains("&")) {
            code = code.substring(0, code.indexOf("&"));
        }
        String appId = rpcCmd.getAppId();
        String privateKey = rpcCmd.getAppPrivateKey();
        String publicKey = rpcCmd.getAppPublicKey();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey,
            "json", "GBK", publicKey, "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            String accessToken = response.getAccessToken();
            AlipayUserInfoShareResponse userInfo = getUserInfo(alipayClient, accessToken);
            AlipayProgramAuthorizeRpcDTO rpcDTO = new AlipayProgramAuthorizeRpcDTO();
            rpcDTO.setUserId(userInfo.getUserId());
            rpcDTO.setAvatar(userInfo.getAvatar());
            rpcDTO.setCity(userInfo.getCity());
            rpcDTO.setProvince(userInfo.getProvince());
            rpcDTO.setNickName(userInfo.getNickName());
            rpcDTO.setMobile(userInfo.getMobile());
            return rpcDTO;
        } else {
            String errorMsg = "调用失败，原因：" + response.getMsg() + "，" + response.getSubMsg();
            log.error(errorMsg);
            throw ThirdPartyException.buildException(response.getCode() + "-" + response.getSubCode(), errorMsg);
        }
    }

    private static AlipayUserInfoShareResponse getUserInfo(AlipayClient alipayClient, String accessToken)
        throws AlipayApiException {
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = alipayClient.execute(request, accessToken);
        if (response.isSuccess()) {
            log.info("获取用户信息成功 json:{}", JSON.toJSONString(response));
            return response;
        } else {
            String errorMsg = "调用失败，原因：" + response.getMsg() + "，" + response.getSubMsg();
            log.error(errorMsg);
            throw ThirdPartyException.buildException(response.getCode() + "-" + response.getSubCode(), errorMsg);
        }
    }

    public static void main(String[] args) throws AlipayApiException {
        String code = "358328a032844a1bbddcb380a4b7UX28&app_id=2019002198612750&source=alipay_wallet&scope=auth_user";
        if (code.contains("&")) {
            code = code.substring(0, code.indexOf("&"));
        }
        String appId = "2019002198612750";
        String privateKey =
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWxcGf+lwcNYncsdhvhPioMPNr0OqFHDqdrJ71OlXmSPq7H4eHodq+3wTXLzpBOJumJE5PXHiv7iQcsMRcFrOQ7wibeLchxWVeZ7V9U6FxTbfQ9YxI6uZ/SyhvtP8ziYGKkclnxcx9GbKL+zWyWV9ctcbhxisAH+s/WpsZxyDElnpc75Q/z8jo3BwW+zOXkEgul7/0OB747KhgICDdx06Gn71HIGqOAsOH60ruD9wJ9HBx1Q4JSiTjYzbU/7eWSjP9d+oL5aWleFFE5groqRahs5mmCMKUKy97D8GbBLDqD/SbST3xQUZ/b5vQHkLSvkv7O0IjLkgFdg5zZtlz4B4fAgMBAAECggEATDx+ZVgAjzr7FJ7Qo8MGwHTQAOpISzo04VlRnwn6OVEZhtiwfQJ4Sx8VoYD94dCv01h0SGinlb3CgMdY4n+QGG7lWUe2wdrAIPYrrM1GyrXThlNCtfQgzKjCL4nDqNmUqO6Cenhnp7IAuAsz4ZSTRZucjrjXMV7yYrx7xJMNnJj67yESOnX38KlPGNM6Xfq162F3zHSOLczWOUXsB/9oey63S7YlLwSzc6r/rMfc4oz0eOpBI19lVqOGp9EUvubqumhAGgBxTk8FPP5Q6789PpPgwlJc+8y7RQdBher/dQYL/NLfJprG23P6dKgsJDaYlK0SoofzXrH2gqPETYFm8QKBgQDSO7ps6iwuNiL7A1SerqjDhfSDtqaJO9ejp+Y7qYPwejgxsq993xkiFYvUSApIFD6/hwtcfvt3AgpUKTV545DSQre0E3HERk+Ya7K+3xzugxhY+UjTWOyirJMX8mLdIjtNuRNryGDahPNNb1H1CM/y2RuyNyxjnepaUKY6/3U/WwKBgQC3mEjvcCaTTtNloTi0JDXuM19t4jjswNgcupq6r1S+22LYNhLS5xtsknnD7w9gTjI64EKru9ZwCA9A7KdFp8HCQRXxIuwHc5UIqnNTIOxEUXYBqnw8UBrR8xnGZzHwdOVgg+DIBzXiBNDGZJwHu6ocygLz/4RTkO1rRC9sRur7jQKBgQCEPMujzJxjqOvR5iNo+MXwzqah3tbcBYc7StYX+yWhss77+8BFNXqypMjiiznkYs+hW4l68eisz9y9WXpopAqpJww0OfLambthTYUOaHrjUBSViqATSKD4JgMUEgO7wtd4qQydRFofuXbs4zOzJIrg3017iRbjxiXuD0KwY5VVdwKBgCMSFQSgEM5c5WbpiDJpJGAvMhCEgUlAgUfjoYD90xtuPxB8K9ZbzVJNHLN/7BkCdvfmNPYnOIxIDdniGwXkSM/5rN5WRG4b9yEjpkn/5QIb6YXh8j93MdhfLtJGjjSXr+Vi68JqFjHpp5PuThvwDkvXjv7bFYT9D01JRiHR3jtVAoGBAKP2312FxxOhlQUZ2SJQ4+pC35wEFcwVe/tTXfGEZOkb92N9evolqnSfqfMGZ6/T4Sn/IUVE9YXUC3ykgwwVnRSBzs+hWwa4dirpe52moNDnqHomqKXdP3x4qF7Ab9qznlStVmDgSllttw/ZNAOLZS6VuL6i1AF3THg12VLO9jAm";
        String publicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiJWz5tKpUsYO0aUT5FYrPpZ20tjvvoXyhX6w9ZWdeBtphKzGIul2eE4nNjqXCUASMcmAJaluQ6yBusrVt0VKDOhZzVKuwo0KTu6JhVFG/u9lKxCPgptaQ6cJwodztbobxLS4pAg/377Z1nVcyMYJCDDeo3XlTbl8SJ81CZIARilq4we96FwsjoOofloevH/c2TmD4VMs3teQ850HI8QEAI6yr4hEW8DFBZC9EqMPeBZd0vXct7OSly46fyKnBGVi3HcPyg1PZ6TToc11zOJIPX/2nLtFMt43S0Jma06psE5RsaGZ27rXpnl3Kw8NSwgXq3z094sRjoPsGTTcvDHfLwIDAQAB";
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, privateKey,
                "json", "GBK", publicKey, "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            String accessToken = response.getAccessToken();
            AlipayUserInfoShareResponse userInfo = getUserInfo(alipayClient, accessToken);
            log.info(JSON.toJSONString(userInfo));
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
