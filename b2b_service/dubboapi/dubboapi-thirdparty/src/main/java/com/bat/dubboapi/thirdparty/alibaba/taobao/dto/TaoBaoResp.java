package com.bat.dubboapi.thirdparty.alibaba.taobao.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TaoBaoResp implements Serializable {


    private static final long serialVersionUID = 1519970827572701253L;

    private String code;


    private String message;


    private String errorCode;


    private String msg;


    private String subCode;


    private String subMsg;


    private String subMessage;


    private String flag;


    private String requestId;


    private String qimenType;

    private String body; // API响应JSON或XML串

    private Map<String, String> headerContent; // 响应头

    /**
     * API请求URL(不包含body)
     */
    private String requestUrl;

    private Map<String, String> params; // API请求参数列表
}
