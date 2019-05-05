package com.bat.thirdparty.factory.maike.response;

public class MaikeResponse {
    
    /**
     * 1正常、其他异常
     */
    private Integer status;


    private String msg;


    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



}