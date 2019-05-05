package com.bat.thirdparty.erp.api.request.dto.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExchangePlainCodeOrderErp implements Serializable {

    private static final long serialVersionUID = 7280594734497475495L;
    /**
     * 明码
     */
    private String FCardNo;

    /**
     * 备注
     */
    private String FCardMark;


}
