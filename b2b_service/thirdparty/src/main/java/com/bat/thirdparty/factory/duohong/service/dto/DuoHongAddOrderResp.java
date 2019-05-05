package com.bat.thirdparty.factory.duohong.service.dto;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:28
 */
@Data
public class DuoHongAddOrderResp {

    /**
     * // {"rspType":"msg", "status":"success", "msg":"推送成功"}
     *
     * // {"rspType":"msg", "status":"fail", "msg":"推送失败，以下订单号已存在不能重复推送(如确认没有重复推送，请修改订单编号长度)：O1637823142473"}
     */
    private String rspType;
    private String status;
    private String msg;
}