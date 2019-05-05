package com.bat.thirdparty.vmall.request;

import lombok.Data;

@Data
public class BopOcidDecryptRequest {

    /**
     * 收货人id
     */
    private String ocid;

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 解密场景编号：
     * 1001(客户售后服务)、1002(客户关怀)、
     * 2001(顺丰电子面单发货)、2002(4通一达电子面单发货)、2003(EMS电子面单发货)、2004(其他电子面单发货)
     */
    private String scense;

}
