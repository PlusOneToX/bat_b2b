package com.bat.dubboapi.thirdparty.maike.api;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailFTPDTO;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailLocalUploadCmd;

import java.util.List;

public interface ThirdPartyMaikeServiceRpc {


    /**
     * B2B向工厂取消订单 返回true成功、false，失败
     *
     * @param factoryCode 工厂编码
     * @param orderId     订单id
     * @param remark      取消备注
     * @return
     */
    Response<Boolean> cancelOrderToFactory(String factoryCode, Integer orderId, String remark);

    /**
     * 批量上传到工厂本地存储
     *
     * @param uploadCmdList
     * @return
     */
    Response<List<OrderDetailFTPDTO>> updateToFTP(List<OrderDetailLocalUploadCmd> uploadCmdList);


}
