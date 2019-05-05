package com.bat.thirdparty.common.base;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BaseController {

    @Resource
    private HttpServletRequest request;

    public String getLanguage() {
        return request.getHeader("language");
    }

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

    public String getContactId() {
        return request.getHeader("contactId");
    }

    public String getShopCode() {
        return request.getHeader("shopCode");
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
