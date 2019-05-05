package com.bat.thirdparty.order.response;

public class JiuJiLogisticData {

    /**
     * --是否已经同步
     */
    private Boolean HasSync;


    /**
     * --是否还能同步
     */
    private Boolean CanSync;


    public Boolean getHasSync() {
        return HasSync;
    }

    public void setHasSync(Boolean hasSync) {
        HasSync = hasSync;
    }

    public Boolean getCanSync() {
        return CanSync;
    }

    public void setCanSync(Boolean canSync) {
        CanSync = canSync;
    }
}
