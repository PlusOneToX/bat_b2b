package com.bat.order.service.third.flexible;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.shop.ShopServiceRpc;
import com.bat.dubboapi.flexible.shop.dto.ShopDTORpcQry;
import com.bat.order.api.common.exception.OrderException;

@Component
public class FlexibleQryExeRpc {

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private ShopServiceRpc shopServiceRpc;

    /**
     * 根据分销商id和门店编码查询门店
     * 
     * @param distributorId
     * @param shopCode
     * @return
     */
    public ShopDTORpcQry getByDistributorIdAndShopCode(Integer distributorId, String shopCode) {
        Response<ShopDTORpcQry> qryResponse = shopServiceRpc.getByDistributorIdAndShopCode(distributorId, shopCode);
        if (!qryResponse.isSuccess()) {
            throw OrderException.buildException(qryResponse.getErrCode(), qryResponse.getErrMessage());
        }

        return qryResponse.getData();
    }
}
