package com.bat.dubboapi.flexible.common;


public class BaseOpenApiException extends RuntimeException{

	/**
	 *
	 */
	private static final long serialVersionUID = 4827627082698137220L;

	private Integer code;

	private String msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BaseOpenApiException(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
}
