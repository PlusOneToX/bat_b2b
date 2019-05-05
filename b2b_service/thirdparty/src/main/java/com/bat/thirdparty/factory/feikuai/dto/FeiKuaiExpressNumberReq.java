package com.bat.thirdparty.factory.feikuai.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:28
 */
@Data
public class FeiKuaiExpressNumberReq {

    private List<OrderList> orderList = new ArrayList<>();

    @Data
    public static class OrderList{
        /**
         * 主订单号:自己方
         */
        private String tid;
        /**
         * 用户ID
         */
        private Integer user_id;
        /**
         * 平台标识
         */
        private Short platform;
    }
}