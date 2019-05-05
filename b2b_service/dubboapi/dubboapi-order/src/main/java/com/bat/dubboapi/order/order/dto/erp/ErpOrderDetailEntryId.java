package com.bat.dubboapi.order.order.dto.erp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ERP订单明细内码和B2B行序号关联
 */
@Data
public class ErpOrderDetailEntryId implements Serializable {

    private static final long serialVersionUID = 1935118570754203620L;
    /**
     * B2B行序号id
     */
    @JsonProperty(value = "FSerialNumber")
    private Integer fSerialNumber;

    /**
     * ERP明细内码
     */
    @JsonProperty(value = "ErpEntryId")
    private Integer erpEntryId;
}
