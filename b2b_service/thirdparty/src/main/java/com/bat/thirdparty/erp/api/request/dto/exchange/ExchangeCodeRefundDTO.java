package com.bat.thirdparty.erp.api.request.dto.exchange;


import com.bat.dubboapi.flexible.exchange.dto.ExchangeBoxPlainCodeDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ExchangeCodeRefundDTO {

    /**
     * 退款单号
     */
    @NotBlank(message = "退货单号不能为空")
    private String refundNo;

    /**
     * 退款时间
     */
    @NotBlank(message = "退货时间不能为空")
    private String refundTime;


    /**
     * 退款原因
     */
    @NotBlank(message = "退货原因不能为空")
    private String refundReason;

    /**
     * 盒码明码列表
     */
    @NotEmpty(message = "盒码明码对应列表不能为空")
    private List<ExchangeBoxPlainCodeDTO> codeDTOList;

    /**
     * 操作人
     */
    @NotBlank(message = "操作人不能为空")
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
