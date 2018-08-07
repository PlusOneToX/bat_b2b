package com.bat.order.web.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class BaseController {

    @Autowired
    private HttpServletRequest request;

    public String getLanguage() {
        return (String)request.getAttribute("language");
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
    public static void exportExcel(String fileName, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response)throws IOException {
        final String userAgent = request.getHeader("USER-AGENT");
        if (org.apache.commons.lang.StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else if (org.apache.commons.lang.StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");//其他浏览器
        }
        response.setContentType("application/octet-stream;charset=utf-8");
//        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8")));
        //输出Excel文件
        OutputStream output = response.getOutputStream();
        wb.write(output);
        output.close();
    }
}
