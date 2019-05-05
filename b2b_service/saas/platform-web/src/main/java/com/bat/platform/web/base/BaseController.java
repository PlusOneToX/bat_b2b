package com.bat.platform.web.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.nacos.common.utils.StringUtils;

public class BaseController {

    @Autowired
    private HttpServletRequest request;

    protected String getUserId() {
        return request.getHeader("userId");
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    protected String getPlatform() {
        return request.getHeader("platform");
    }

    protected String getDistributorId() {
        return request.getHeader("distributorId");
    }

    public String getOpenId() {
        return request.getHeader("openId");
    }

    public String getOtherId() {
        return request.getHeader("otherId");
    }

    protected String getUserName() {
        String userName = request.getHeader("userName");
        if (StringUtils.isNotBlank(userName)) {
            try {
                return URLDecoder.decode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getContactName() {
        String contactName = request.getHeader("contactName");
        if (StringUtils.isNotBlank(contactName)) {
            try {
                return URLDecoder.decode(contactName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
