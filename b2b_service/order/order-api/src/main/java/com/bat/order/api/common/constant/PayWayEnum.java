package com.bat.order.api.common.constant;

/**
 * 付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付
 */
public enum PayWayEnum {

    Ali((short) 1,"支付宝","支付宝支付"),
    Wechat((short) 2,"微信","微信支付"),
    Interval((short) 3,"区间结算","区间支付"),
    OfflineTransfer((short) 4,"线下转账","线下转账"),
    Balance((short) 5,"余额支付","余额支付"),
    Quick((short) 6,"快钱支付","网银支付"),
    BalanceAli((short) 7,"余额+支付宝"),
    BalanceWechat((short) 8,"余额+微信"),
    BalanceQuick((short) 9,"余额+快钱支付"),
    ;

    private Short payStatus;

    private String payStatusStr;

    private String excelImportStr;

    PayWayEnum(Short payStatus, String payStatusStr) {
        this.payStatus = payStatus;
        this.payStatusStr = payStatusStr;
    }

    PayWayEnum(Short payStatus, String payStatusStr, String excelImportStr) {
        this.payStatus = payStatus;
        this.payStatusStr = payStatusStr;
        this.excelImportStr = excelImportStr;
    }

    public static String statusStr(Short status) {
        if(status==null){
            return "";
        }
        for (PayWayEnum statusEnum : values()) {
            if (statusEnum.getPayStatus() == status.shortValue()) {
                return statusEnum.getPayStatusStr();
            }
        }
        return "";
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusStr() {
        return payStatusStr;
    }

    public void setPayStatusStr(String payStatusStr) {
        this.payStatusStr = payStatusStr;
    }

    public String getExcelImportStr() {
        return excelImportStr;
    }

    public void setExcelImportStr(String excelImportStr) {
        this.excelImportStr = excelImportStr;
    }

    public static PayWayEnum getByExcelImportStr(String excelImportStr ){
        for (PayWayEnum e : PayWayEnum.values()) {
            if(excelImportStr.equals(e.getExcelImportStr())){
                return e;
            }
        }
        return null;
    }
}
