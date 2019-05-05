package com.bat.dubboapi.thirdparty.feikuai.api;

import com.bat.dubboapi.thirdparty.common.Response;

public interface FeiKuaiServiceRpc {

    /**
     * B2B向工厂取消订单 返回true成功、false，失败
     *
     * @param factoryCode 工厂编码
     * @param orderNo     订单编码
     * @return
     */
    Response<Boolean> cancelOrderToFactory(String factoryCode, String orderNo);

}
