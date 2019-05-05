package com.bat.thirdparty.factory.api;

import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/26 9:24
 */
public interface FactoryService {

    /**
     * 同步工厂、返回工厂单号
     * @param data
     */
    String syncOrder(FactoryOrderAddCmd data);

    /**
     * 物流回调
     * @param resp
     * @return
     */
    void deliverOrder(FactoryDeliverOrderReq resp);

    /**
     * 定时器触发同步工厂
     *
     * @param startTime
     *            工厂编码
     */
    void diyOrderSyncToFactory(String startTime);
}
