package com.bat.warehouse.web.base;

import com.bat.warehouse.api.base.AdminResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class BaseController {



	protected String getPlatform(){
		return request.getHeader("platform");
	}
	
	protected AdminResponse getCurrentAdmin() {
		AdminResponse adminResponse =(AdminResponse) request.getAttribute("admin");
		if(adminResponse==null){
			adminResponse = new AdminResponse();
			adminResponse.setId(-1);
			adminResponse.setName("重构测试");
		}
		return adminResponse;
	}

	protected AdminResponse getCurrentUser() {
		AdminResponse adminResponse =(AdminResponse) request.getAttribute("user");
		if(adminResponse==null){
			adminResponse = new AdminResponse();
			adminResponse.setId(-1);
			adminResponse.setUserName("重构测试");
		}
		return adminResponse;
	}

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
