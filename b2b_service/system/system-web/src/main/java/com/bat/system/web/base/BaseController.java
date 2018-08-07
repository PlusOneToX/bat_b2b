package com.bat.system.web.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 13:51
 */
public class BaseController {

    @Autowired
    private HttpServletRequest request;

    protected String getPlatform() {
        return request.getHeader("platform");
    }

    protected void getCurrentAdmin() {
        request.getAttribute("admin");
    }

    protected void getCurrentUser() {
        request.getAttribute("user");
    }

    protected String getAppIdKey() {
        return request.getHeader("appId");
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    protected String getUserId() {
        return request.getHeader("userId");
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