package com.bat.thirdparty.factory.feikuai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:28
 */
@Data
public class FeiKuaiAddOrderResp {

    /**
     * 接口执行状态：失败：非零，成功：0
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
    private ResponseData responseData;

    @Data
    public static class ResponseData{
        /**
         * 订单编号
         */
        private String tid;
        /**
         * 条形码列表
         */
        private List<String> barcode;
    }


}