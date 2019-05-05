package com.bat.thirdparty.erp.api.request;


import com.bat.thirdparty.common.base.RequestPageBean;

public class ItemsGetRequest extends RequestPageBean {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
