package com.bat.thirdparty.distributor.api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SamsungVo<T> {
    /**
     * 系统信息
     */
    private String message;
    /**
     * 前端用户可见的
     */
    private String error;
    /**
     * 编码
     */
    private Integer status;
    /**
     * 数据
     */
    private T data;
    /**
     * 时间戳
     */
    private Date timestamp;
}
