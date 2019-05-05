package com.bat.dubboapi.flexible.exchange.dto;



import java.io.Serializable;
import java.util.List;

public class OrderExchangeCardBindErpRequest implements Serializable {

    private static final long serialVersionUID = -9061168965469902125L;
    /**
     * order订单号
     */

    private String outboundNo;

    /**
     * 出库日
     */
    private String outboundDate;

    /**
     * 是否修改 同步订单的盒码错误、需要重新传时、
     */
    private Boolean isUpdate;


    private List<ItemBoxCodeRequest> itemCodeList;

    public String getOutboundNo() {
        return outboundNo;
    }

    public void setOutboundNo(String outboundNo) {
        this.outboundNo = outboundNo;
    }

    public String getOutboundDate() {
        return outboundDate;
    }

    public void setOutboundDate(String outboundDate) {
        this.outboundDate = outboundDate;
    }

    public List<ItemBoxCodeRequest> getItemCodeList() {
        return itemCodeList;
    }

    public void setItemCodeList(List<ItemBoxCodeRequest> itemCodeList) {
        this.itemCodeList = itemCodeList;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean update) {
        isUpdate = update;
    }
}
