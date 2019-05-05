package com.bat.dubboapi.thirdparty.erp.dto.exchange;



import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * B2B作废明码、同步在B2B
 */
@Data
public class ExchangeCodeInvalidCodeDTO implements Serializable {

    private static final long serialVersionUID = 8540688668733135345L;
    //盒码、明码不存在时、必填、盒码明码二选一
    private String F_HCode;

    //明码 盒码不存在时、必填
    private List<String> F_MCode;

    //备注
    private String F_PAEZ_REMARK;

    //作废时间 必填
    private Date CancelTime;

    //作废人
    private String F_CancelUser;

    private String ACCESSTOKEN;




    public String getPath() {
        //兑换卡失效
        return "RockCardCancel";
    }
}
