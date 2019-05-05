package com.bat.thirdparty.factory.feikuai.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:28
 */
@Data
public class FeiKuaiCancelOrderResp {

    /**
     * 接口执行状态：成功：非零，失败：0
     */
    private Integer code;
    /**
     * 错误异常
     */
    private String msg;
    /**
     * 执行失败的异常消息
     */
    private String errMessage;
    /**
     * 执行成功返回的数据
     */
    private Object responseData;

}