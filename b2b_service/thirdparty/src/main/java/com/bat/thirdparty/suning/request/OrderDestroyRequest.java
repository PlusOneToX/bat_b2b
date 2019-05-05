package com.bat.thirdparty.suning.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderDestroyRequest {

    /**
     * 来源系统
     */
   private String fromSys;

    /**
     * 服务订单号
     */
    private String objectId;

    /**
     * 作业人员BP
     */
    private String partnerNo;

    /**
     * 销单状态
     */
    private String userStatus;

    /**
     * 销单时间  8位日期(纯数字)+1位空格+6位时间(纯数字)
     */
    private String xdsj;

    /**
     * 销单地址
     */
    private String xdAddress;

    /**
     * 服务备注
     */
    private String srvMemo;

    /**
     * 维修措施代码
     */
    private String spotWxcs;

    /**
     * 维修措施描述
     */
    private String spotWxcsDec;

    /**
     * 签名
     */
    private String sign;

    /**
     * 收旧list,列表字段若为空，就不要塞值，不能塞空字符串
     */
    private List sjwtList;

}
