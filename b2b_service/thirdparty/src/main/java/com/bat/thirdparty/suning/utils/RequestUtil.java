package com.bat.thirdparty.suning.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.suning.request.CommonUrlRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 沙漠
 */
public class RequestUtil {

    private static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 获取请求中参数
     *
     * @param request
     * @return
     */
    public static JSONObject getParameters(HttpServletRequest request) {
        BufferedReader bReader = null;
        InputStreamReader isr = null;
        try {
            InputStream iis = request.getInputStream();
            isr = new InputStreamReader(iis, "utf-8");
            bReader = new BufferedReader(isr);
            String str = null;
            StringBuffer buffer = new StringBuffer();

            while ((str = bReader.readLine()) != null) {
                buffer.append(str).append("\n");
            }
            String jsonStr = buffer.toString();
            //logger.info("获取到苏宁传参的json字符串:{}", jsonStr);
            LinkedHashMap<String, Object> json = JSONObject.parseObject(jsonStr, LinkedHashMap.class, Feature.OrderedField);
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.putAll(json);
            return jsonObject;
        } catch (IOException e) {
            logger.error("获取苏宁请求参数出现异常:{}",e);
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        } finally {
            try {
                if (bReader != null) {
                    bReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("获取苏宁请求参数流关闭异常异常:{}",e);
            }
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("获取苏宁请求参数流关闭异常异常:{}",e);
            }
        }
    }

    /**
     * 对苏宁发起请求
     * @param commonUrlRequest
     * @param jsonStr
     * @param baseUrl
     * @return
     */
    public static String sendRequest(CommonUrlRequest commonUrlRequest, String jsonStr, String baseUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            FormHttpMessageConverter fc = new FormHttpMessageConverter();
            StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
            List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();
            partConverters.add(s);
            partConverters.add(new ResourceHttpMessageConverter());
            fc.setPartConverters(partConverters);
            restTemplate.getMessageConverters().addAll(Arrays.asList(fc, new MappingJackson2HttpMessageConverter()));
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("sign", URLEncoder.encode(commonUrlRequest.getSign(), "UTF-8"))
                    .queryParam("method", commonUrlRequest.getMethod()).queryParam("app_key", commonUrlRequest.getApp_key());
            HttpEntity<String> entity = new HttpEntity<>(jsonStr, headers);
            URI uri = builder.build(true).toUri();
            ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            return exchange.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
    }
}

