package com.bat.dubboapi.flexible.exchange;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.exchange.dto.*;

import java.util.List;

public interface ExchangeCodeServiceRpc {


    /**
     * 根据暗码列表（未加密）查询B兑换码2B订单参数（返回暗码已解密）
     */
    Response<List<ExchangeCodeB2BOrderDTORpcQry>> listB2BOrderMsgBySecretCodeList(List<String> secretCodeList);

    /**
     * 暗码解密
     */
    Response<List<ExchangeCodeDecryptRpcDTO>> decryptSecretCodes(List<String> encryptSecretCodes);


    /**
     * 设置核销C端用户信息
     * @param list 核销兑换码列表
     */
    Response setUseOrderMsg(List<ExchangeCodeUseCmd> list);


    /**
     * 根据核销订单查询兑换码列表
     * @param orderId 核销订单id
     * @param secretCodeFlag 是否将暗码解密 true解密 false 不
     */
    Response<List<ExchangeCodeDTORpcQry>> listByUserOrderId(Integer orderId, Boolean secretCodeFlag);

    /**
     * 根据暗码列表（未加密）查询B兑换码分页参数（返回暗码已解密）
     */
    Response<List<ExchangeCodePageMsgDTO>> listExchangePageMsgBySecretCodeList(List<String> secretCodeList);

    /**
     * 对兑换卡进行作废
     * @param cmd
     * @return
     */
    Response invalid(ExchangeCodeStatusCmd cmd);

    /**
     * 撤销对兑换卡进行作废
     * @param cmd
     * @return
     */
    Response unInvalid(ExchangeCodeStatusCmd cmd);
}
