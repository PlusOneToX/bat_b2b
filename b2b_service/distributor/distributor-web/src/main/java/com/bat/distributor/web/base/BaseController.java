package com.bat.distributor.web.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BaseController {

    @Autowired
    private HttpServletRequest request;

    protected String getUserId() {
        return request.getHeader("userId");
    }

    protected String getPlatform() {
        return request.getHeader("platform");
    }

    protected String getDistributorId() {
        return request.getHeader("distributorId");
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
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
