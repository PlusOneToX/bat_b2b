package com.bat.thirdparty.erp.api.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.bat.thirdparty.erp.api.request.dto.ErpOrderChangeDetailEntry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ERP订单变更信息")
public class ErpOrderChangeRequest {
    @ApiModelProperty(value = "erp订单编号", required = true, example = "SO80462336544")
    @NotBlank(message = "P_THIRDPARTY_ORDER_NO_NULL")
    private String orderNo;
    @ApiModelProperty(value = "订单交期时间", required = false, example = "2019-07-11 10:52:00")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date deliverTime;
    @ApiModelProperty(value = "订单备注", required = false, example = "订单备注")
    private String remark;
    @ApiModelProperty(value = "erp订单变更单编号", required = true, example = "XSO80462336544")
    @NotBlank(message = "P_THIRDPARTY_XORDER_NO_NULL")
    private String xOrderNo;
    @ApiModelProperty(value = "erp变更单币种", required = false, example = "CNY")
    @JsonProperty("FSettleCurrId")
    private String FSettleCurrId;
    @ApiModelProperty(value = "erp订单变更明细", required = true)
    @NotNull(message = "P_THIRDPARTY_ORDER_DETAIL_NULL")
    private List<ErpOrderChangeDetailEntry> orderDetails;
}
