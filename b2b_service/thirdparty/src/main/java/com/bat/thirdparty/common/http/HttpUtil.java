package com.bat.thirdparty.common.http;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.util.Sha1Handler;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/4/16 20:48
 */
@Component
@Slf4j
public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    @Resource
    private RestTemplate restTemplate;

    /**
     * 发送请求，不带参数，默认请求头
     *
     * @param url
     * @param method
     * @param response
     * @param <T>
     * @return
     */
    public <T> T requestFor(String url, HttpMethod method, Class<T> response) {
        return requestFor(url, method, null, null, response);
    }

    /**
     * 发送请求，不带参数，带请求头
     *
     * @param url
     * @param method
     * @param headers
     * @param response
     * @param <T>
     * @return
     */
    public <T> T requestFor(String url, HttpMethod method, HttpHeaders headers, Class<T> response) {
        System.out.println(url);
        return requestFor(url, method, headers, null, response);
    }

    /**
     * 发送请求，带参数，默认请求头
     *
     * @param url
     * @param method
     * @param request
     * @param response
     * @param <T>
     * @return
     */
    public <T> T requestFor(String url, HttpMethod method, Object request, Class<T> response) {
        return requestFor(url, method, null, request, response);
    }

    /**
     * 发送请求，带参数，带请求头
     *
     * @param url
     * @param method
     * @param headers
     * @param request
     * @param response
     * @param <T>
     * @return
     */
    public <T> T requestFor(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> response) {
        HttpEntity httpEntity = init(method, headers, request);
        ResponseEntity<T> exchange = restTemplate.exchange(url, method, httpEntity, response);
        return exchange.getBody();
    }

    public String requestForCookie(String url, HttpMethod method, HttpHeaders headers, Object request,
        Class<String> response) {
        HttpEntity httpEntity = init(method, headers, request);
        ResponseEntity<String> exchange = restTemplate.exchange(url, method, httpEntity, response);
        List<String> cookies = exchange.getHeaders().get("Set-Cookie");
        if (!CollectionUtils.isEmpty(cookies)) {
            return cookies.get(0);
        }
        return null;
    }

    /**
     * 初始化HttpEntity
     *
     * @param method
     * @param headers
     * @param request
     * @return
     */
    private HttpEntity init(HttpMethod method, HttpHeaders headers, Object request) {
        String jsonString = null;
        if (request != null) {
            jsonString = JSONObject.toJSONString(request);
        }
        if (headers == null) {
            headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = null;
        if (headers.getContentType() == null || headers.getContentType().equals(MediaType.APPLICATION_JSON)) {
            if (method == HttpMethod.GET) {
                requestEntity = new HttpEntity<>(headers);
            } else {
                requestEntity = new HttpEntity(jsonString, headers);
            }
        } else if (headers.getContentType().equals(MediaType.APPLICATION_FORM_URLENCODED)) {
            if (method == HttpMethod.GET) {
                requestEntity = new HttpEntity<>(headers);
            } else {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                Map map = JSON.parseObject(jsonString, Map.class);
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    params.add(key, String.valueOf(map.get(key)));
                }
                requestEntity = new HttpEntity(params, headers);
            }
        }
        return requestEntity;
    }

    /**
     * 对接请求头
     *
     * @param paramJson
     * @param appId
     * @param appKey
     * @return
     */
    public static HttpHeaders getHttpHeaders(String paramJson, String appId, String appKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Accept", "application/json");
        Long currentTimeMillis = System.currentTimeMillis();
        httpHeaders.add("timestamp", String.valueOf(currentTimeMillis));
        String signStr = appKey + paramJson + currentTimeMillis;
        httpHeaders.add("appId", appId);
        logger.info("签名字符串：" + signStr);
        String checkSignature = Sha1Handler.encryption(signStr);
        logger.info("得到签名：" + checkSignature);
        httpHeaders.add("sign", checkSignature);

        return httpHeaders;
    }

    /**
     * 重新设置请求头参数
     * 
     * @param requestHeaders
     * @param request
     */
    public static void setRequestParam(String requestHeaders, HttpServletRequest request) {
        if (StringUtils.isBlank(requestHeaders)) {
            return;
        }
        try {
            Class<? extends HttpServletRequest> requestClass = request.getClass();
            Field requestField = requestClass.getDeclaredField("request");
            requestField.setAccessible(true);
            Object requestObj = requestField.get(request);
            Field coyoteRequestField = requestObj.getClass().getDeclaredField("coyoteRequest");
            coyoteRequestField.setAccessible(true);
            Object coyoteRequestObj = coyoteRequestField.get(requestObj);
            Field headersField = coyoteRequestObj.getClass().getDeclaredField("headers");
            headersField.setAccessible(true);
            MimeHeaders headersObj = (MimeHeaders)headersField.get(coyoteRequestObj);
            JSONObject jsonObject = JSON.parseObject(requestHeaders);
            Iterator<Map.Entry<String, Object>> iterator = jsonObject.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                // 先移除
                headersObj.removeHeader(entry.getKey());
                headersObj.addValue(entry.getKey()).setString(String.valueOf(entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_SET_HEADER_AGAIN_FAIL);
        }
    }

}
