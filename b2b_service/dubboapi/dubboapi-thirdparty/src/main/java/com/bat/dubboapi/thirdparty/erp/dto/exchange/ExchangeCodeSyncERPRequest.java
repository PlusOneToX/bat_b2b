package com.bat.dubboapi.thirdparty.erp.dto.exchange;



import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExchangeCodeSyncERPRequest  implements Serializable {

    private static final long serialVersionUID = -3801628338873589549L;
    /**
     * 盒码数组
     */
    private List<BoxCodeCardErpRequest> HCodes;

    private String ACCESSTOKEN;


    public String getPath() {
        return "RockCardNew";
    }
}
