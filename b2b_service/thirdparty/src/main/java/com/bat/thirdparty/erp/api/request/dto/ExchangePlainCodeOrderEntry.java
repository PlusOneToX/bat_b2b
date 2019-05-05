package com.bat.thirdparty.erp.api.request.dto;

public class ExchangePlainCodeOrderEntry {

    /**
     * 明码
     */
    private String FCardNo;

    /**
     * 备注
     */
    private String FCardMark;

    public String getFCardNo() {
        return FCardNo;
    }

    public void setFCardNo(String FCardNo) {
        this.FCardNo = FCardNo;
    }

    public String getFCardMark() {
        return FCardMark;
    }

    public void setFCardMark(String FCardMark) {
        this.FCardMark = FCardMark;
    }
}
