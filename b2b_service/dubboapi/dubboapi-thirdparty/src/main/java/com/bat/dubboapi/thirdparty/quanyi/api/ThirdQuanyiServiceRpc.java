package com.bat.dubboapi.thirdparty.quanyi.api;

import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcCmd;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;

import java.util.List;

/**
 * 权益
 */
public interface ThirdQuanyiServiceRpc {

    /**
     * 个人那句分销商id以及第三方兑换码找到材质信息
     * @return
     */
    Response<ThirdQuanyiRpcDTO> findByDistributorIdAndThirdCode(Integer distributorId, String thirdCode);

    /**
     * 对权益进行签到
     * @return
     */
    Response signIn(ThirdQuanyiRpcCmd cmd);

    /**
     * 对权益进行核销
     * @return
     */
    Response destroy(Integer exchangeCodeId,Integer orderId);

    /**
     * 根据兑换码ids获取权益列表
     * @return
     */
    Response<List<ThirdQuanyiRpcDTO>> findByExchangeCodeIds(List<Integer> exchangeCodeIds);

}
