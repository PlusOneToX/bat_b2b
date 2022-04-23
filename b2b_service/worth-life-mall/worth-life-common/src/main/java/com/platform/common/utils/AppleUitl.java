/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwk.Jwk;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lipengjun
 */
@Slf4j
public class AppleUitl {
    private static JSONObject toJsonObj(Map<String, Object> map) {
        Iterator it = map.keySet().iterator();
        JSONObject resultJson = new JSONObject();
        while (it.hasNext()) {
            String key = (String) it.next();
            resultJson.put(key, map.get(key));
        }
        return resultJson;
    }

    /**
     * 获取苹果的公钥
     *
     * @return
     * @throws Exception
     */
    private static List<Map<String, Object>> getAuthKeys() throws Exception {
        String url = "https://appleid.apple.com/auth/keys";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url, JSONObject.class);
        List<Map<String, Object>> arr = json.getObject("keys", List.class);
        return arr;
    }

    public static Boolean verify(String jwt) throws Exception {
        List<Map<String, Object>> arr = getAuthKeys();
        if (arr == null) {
            return false;
        }
        JSONObject authKey = null;

        //先取苹果第一个key进行校验
        authKey = toJsonObj(arr.get(0));
        if (verifyExc(jwt, authKey)) {
            return true;
        } else {
            //再取第二个key校验
            authKey = toJsonObj(arr.get(1));
            return verifyExc(jwt, authKey);
        }

    }

    /**
     * 对前端传来的identityToken进行验证
     *
     * @param jwt     对应前端传来的 identityToken
     * @param authKey 苹果的公钥 authKey
     * @return Boolean
     * @throws Exception Exception
     */
    public static Boolean verifyExc(String jwt, JSONObject authKey) throws Exception {

        Jwk jwa = Jwk.fromValues(authKey);
        PublicKey publicKey = jwa.getPublicKey();

        String aud = "";
        String sub = "";
        if (jwt.split("\\.").length > 1) {
            String claim = new String(Base64.decodeBase64(jwt.split("\\.")[1]));
            aud = JSONObject.parseObject(claim).get("aud").toString();
            sub = JSONObject.parseObject(claim).get("sub").toString();
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
        jwtParser.requireIssuer("https://appleid.apple.com");
        jwtParser.requireAudience(aud);
        jwtParser.requireSubject(sub);

        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(jwt);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                System.out.println(claim);
                return true;
            }
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("apple identityToken expired" + e);
            return false;
        } catch (Exception e) {
            log.error("apple identityToken illegal" + e);
            return false;
        }
    }

    /**
     * 对前端传来的JWT字符串identityToken的第二部分进行解码
     * 主要获取其中的aud和sub，aud大概对应ios前端的包名，sub大概对应当前用户的授权的openID
     *
     * @param identityToken identityToken
     * @return {"aud":"com.xkj.****","sub":"000***.8da764d3f9e34d2183e8da08a1057***.0***","c_hash":"UsKAuEoI-****","email_verified":"true","auth_time":1574673481,"iss":"https://appleid.apple.com","exp":1574674081,"iat":1574673481,"email":"****@qq.com"}
     */
    public static JSONObject parserIdentityToken(String identityToken) {
        String[] arr = identityToken.split("\\.");
        String decode = new String(Base64.decodeBase64(arr[1]));
        String substring = decode.substring(0, decode.indexOf("}") + 1);
        JSONObject jsonObject = JSON.parseObject(substring);
        return jsonObject;
    }

    public static void main(String[] args) throws Exception {
        String identityToken = "eyJraWQiOiJlWGF1bm1MIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLnBsYXRmb3JtLmZseTJ5b3UiLCJleHAiOjE1OTcyOTUxMTQsImlhdCI6MTU5NzI5NDUxNCwic3ViIjoiMDAwNzYyLjQyNGY1N2I3NTQ1YzQ4NWQ4NDYxNGJiMzRjMTE2ODBmLjE4MzgiLCJjX2hhc2giOiJLbDRra3E1WFVXMy14Z1gxdC1aNDlRIiwiZW1haWwiOiI5Mzk5NjEyNDFAcXEuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiYXV0aF90aW1lIjoxNTk3Mjk0NTE0LCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.sFOABMjMxuRBVaTv5YZjJeI378Wl_tOl_ao5v-BW7J6r4EsOvslm0xvPxIYw05GJ0LJVeEBZzfQP9gtRv7IOhlGOt4vrV6-OrwHc3_a6v9B3ltORtvAe8FGj-gIkFS6Jx0m96h9c16a-ee97RkyIXtuSlj3fOoAIud7zjPoNa5sHchC51zqh_4puopNx4T5tat9aophUtJK-7ulCl_U9aM_flelPvldu0dHUJl1XaCPp5DmtXKXQgY_kjWsfftKzSLMVncUZA0c2g1MwFz__PD3Irx3mSRK8-_-KmoyOM1LXEtvV_0Qo3qCE9cV819NBSi0RdzaeLqJEK-xpS4TFRg";
        System.out.println(verify(identityToken));
        System.out.println(parserIdentityToken(identityToken));
    }
}
