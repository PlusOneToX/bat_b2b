package com.bat.thirdparty.order.response;

public class JiujiuLogisticResponse {

    private Integer code;


    private String msg;

    private String userMsg;


    private JiuJiLogisticData data;


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

    public String getUserMsg() {
        return userMsg;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public JiuJiLogisticData getData() {
        return data;
    }

    public void setData(JiuJiLogisticData data) {
        this.data = data;
    }
}
