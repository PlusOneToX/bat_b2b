package com.bat.dubboapi.flexible.exchange.dto;



import java.io.Serializable;
import java.util.List;

public class ExchangeCodeRefundDTO implements Serializable {

    private static final long serialVersionUID = 7469364250637265613L;
    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款时间
     */
    private String refundTime;


    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 盒码明码列表
     */
    private List<ExchangeBoxPlainCodeDTO> codeDTOList;

    /**
     * 操作人
     */
    private String operatorName;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public List<ExchangeBoxPlainCodeDTO> getCodeDTOList() {
        return codeDTOList;
    }

    public void setCodeDTOList(List<ExchangeBoxPlainCodeDTO> codeDTOList) {
        this.codeDTOList = codeDTOList;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
