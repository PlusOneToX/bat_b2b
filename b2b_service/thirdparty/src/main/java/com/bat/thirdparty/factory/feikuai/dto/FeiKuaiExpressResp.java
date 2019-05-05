package com.bat.thirdparty.factory.feikuai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:28
 */
@Data
public class FeiKuaiExpressResp {

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
    private List<ResponseData> responseData;


    @Data
    public static class ResponseData{
        /**
         * 订单编号（我方）
         */
        private String tid;
        /**
         * 物流代码（查询物流使用）
         */
        private String cpCode;
        /**
         *  物流查询状态
         *  0=> '未处理'
         *  1=> '已传图未提交'
         *  2=> '已提交代发'
         *  3=> '已生产'
         *  4=> '已发货'
         *  5=> '发货失败'
         *  6=> '已反审'
         */
        @JsonProperty("DFStatus")
        private Integer DFStatus;
        /**
         * 物流编号
         */
        private String cpNum;
    }

}