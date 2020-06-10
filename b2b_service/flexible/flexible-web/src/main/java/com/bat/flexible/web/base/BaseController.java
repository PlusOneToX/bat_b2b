package com.bat.flexible.web.base;


import com.bat.flexible.api.base.common.response.AdminResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class BaseController {
	

	@Autowired
    private HttpServletRequest request;

	protected String getLanguage(){
		return request.getHeader("language");
	}


	protected String getPlatform(){
		return request.getHeader("platform");
	}


	protected Integer getUserId(){
		if(request.getHeader("userId")==null){
			return -1;
		}
		return Integer.parseInt(request.getHeader("userId"));
	}


	protected String getUserName(){
		String userName = request.getHeader("userName");
		if (org.apache.commons.lang3.StringUtils.isNotBlank(userName)) {
			try {
				return URLDecoder.decode(userName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return userName;
	}


	
	protected AdminResponse getCurrentAdmin() {
		AdminResponse adminResponse = new AdminResponse();

		adminResponse.setId(getUserId());
		adminResponse.setUserName(getUserName());
		return adminResponse;
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


	public static void exportExcel(String fileName, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response)throws IOException {
		final String userAgent = request.getHeader("USER-AGENT");
		if (StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
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

	public static void exportExcelByInputStream(HttpServletRequest request, HttpServletResponse response, InputStream inputStream,String fileName){
		//缓冲文件输出流
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			final String userAgent = request.getHeader("USER-AGENT");
			if (StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				fileName = URLEncoder.encode(fileName, "UTF-8");//其他浏览器
			}

			response.setContentType("application/octet-stream;charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
			//response.setHeader("Content-Length", inputStream.available()+""); // 内容长度
			byte[] car = new byte[1024];
			int L;

			while ((L = inputStream.read(car)) != -1) {
				if (car.length != 0) {
					outputStream.write(car, 0, L);
				}
			}

			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
