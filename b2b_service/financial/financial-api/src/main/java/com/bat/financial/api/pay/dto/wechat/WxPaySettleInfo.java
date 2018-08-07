package com.bat.financial.api.pay.dto.wechat;

import lombok.Data;

@Data
public class WxPaySettleInfo {


    /**
     * 是否指定分账，枚举值
     * true：是
     * false：否
     */
    private Boolean profit_sharing=false;
}
