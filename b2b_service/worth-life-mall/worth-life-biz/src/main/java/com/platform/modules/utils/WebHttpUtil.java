package com.platform.modules.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WebHttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T post(String url, Object request, Class<T> responseType, Object... urlVariables) {
        return restTemplate.postForObject(url, request, responseType, urlVariables);
    }

    public <T> T get(String url, Class<T> responseType, Object... urlVariables) {
        return restTemplate.getForObject(url, responseType, urlVariables);
    }

}
