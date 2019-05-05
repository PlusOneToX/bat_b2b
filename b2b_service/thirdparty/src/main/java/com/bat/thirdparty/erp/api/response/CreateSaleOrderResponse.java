package com.bat.thirdparty.erp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.bat.dubboapi.order.order.dto.erp.ErpOrderDetailEntryId;
import lombok.Data;

import java.util.List;

/**
 * Created by apple on 2019/7/13.
 */
@Data
public class CreateSaleOrderResponse extends BaseResponse {

    @JsonProperty("Data")
    private String Data;


    private String deliverNo;

    public String getData() {
        return Data;
    }

    public Object object;

    //行序号和明细内码之间关联关系
    @JsonProperty("EntryIds")
    private List<ErpOrderDetailEntryId> entryIds;


}
