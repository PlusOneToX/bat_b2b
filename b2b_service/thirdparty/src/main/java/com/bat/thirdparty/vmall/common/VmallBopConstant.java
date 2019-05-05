package com.bat.thirdparty.vmall.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VmallBopConstant {

    public static String deviceType;
    public static String deviceAccount;
    public static String dataPasswd;
    public static String priKey;
    public static String url;

    /**
     * 订单拉取地址
     */
    public static final String orderFulfillListUri="/bop/server/open/queryOrderFulfillList";

    /**
     * 订单状态更改地址
     */
    public static final String updateOrderStatusUri="/bop/server/open/updateOrderShipmentStatusForApp";

    /**
     * 发货单查询地址
     */
    public static final String queryShipmentListUri="/bop/server/open/queryShipmentListForApp";

    /**
     * 订单收货人信息解密地址
     */
    public static final String ocidDecryptUri="/open/ocidDecrypt";

    /**
     * 每次拉取条数
     */
    public static final Integer maxCount=10;

    @Value("${huawei.device.type}")
    public void setDeviceType(String deviceType) {
        VmallBopConstant.deviceType = deviceType;
    }

    @Value("${huawei.device.account}")
    public void setDeviceAccount(String deviceAccount) {
        VmallBopConstant.deviceAccount = deviceAccount;
    }

    @Value("${huawei.data.passwd}")
    public void setDataPasswd(String dataPasswd) {
        VmallBopConstant.dataPasswd = dataPasswd;
    }

    @Value("${huawei.pri.key}")
    public void setPriKey(String priKey) {
        VmallBopConstant.priKey = priKey;
    }

    @Value("${huawei.bop.url}")
    public void setUrl(String url) {
        VmallBopConstant.url = url;
    }
}
