package com.bat.dubboapi.order.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseBaseBean implements Serializable {

	private static final long serialVersionUID = -1612221601752148396L;
	private Integer code = 0; //响应码
	private String msg = ""; //对应的响应描述

	private Object data;

	public ResponseBaseBean(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ResponseBaseBean responseBean(Integer code, String msg) {
		return new ResponseBaseBean(code, msg);
	}

	public static ResponseBaseBean responseBean() {
		return new ResponseBaseBean();
	}
	public ResponseBaseBean() {

	}
}
