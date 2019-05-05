package com.bat.thirdparty.alibaba.taobao.service;

import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.thirdparty.common.error.taobao.TaoBaoErrorCode;
import com.bat.thirdparty.common.taobao.TradeState;
import com.bat.thirdparty.alibaba.taobao.api.TaoBaoOrderServiceI;
import com.bat.thirdparty.alibaba.taobao.api.TaoBaoTradeServiceI;
import com.bat.thirdparty.alibaba.taobao.dao.dataobject.TaobaoTradeDO;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Shipping;
import com.taobao.api.request.LogisticsOnlineSendRequest;
import com.taobao.api.response.LogisticsOnlineSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaoBaoOrderServiceImpl implements TaoBaoOrderServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaoBaoOrderServiceImpl.class);

    @Autowired
    private TaoBaoTradeServiceI taoBaoTradeServiceI;


    /**
     * 工厂发完货之后的回调
     *
     * @param logistics
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseBaseBean sendGoodsCallBack(OrderLogistics logistics, DistributorElectricityRelationMappingRpcDTO mappingRpcDTO
            , String otherOrderNo) {
        LOGGER.info("天猫发货信息回调：{}", logistics);
        TaobaoClient client = new DefaultTaobaoClient(mappingRpcDTO.getUrl(), mappingRpcDTO.getAppKey(), mappingRpcDTO.getSecret());
        LogisticsOnlineSendRequest req = new LogisticsOnlineSendRequest();
        // 需要拆单发货的子订单集合，针对的是一笔交易下有多个子订单需要分开发货的场景；1次可传人多个子订单号，子订单间用逗号隔开；为空表示不做拆单发货。
        // req.setSubTid("1,2,3");
        // 淘宝交易ID 实际传参 logistics.getOtherOrderNo() 70128 不是天猫订单号
        Long tid = Long.valueOf(otherOrderNo);
        req.setTid(tid);
        // 表明是否是拆单，默认值0，1表示拆单
        // req.setIsSplit(0L);
        // 运单号.具体一个物流公司的真实运单号码。淘宝官方物流会校验，请谨慎传入；使用过的单号有可能不成功
        req.setOutSid(logistics.getExpressNo());
        // 物流公司代码.如"POST"就代表中国邮政,"ZJS"就代表宅急送.调用 taobao.logistics.companies.get 获取。
        req.setCompanyCode(logistics.getExpressCode());
        LogisticsOnlineSendResponse rsp;
        try {
            rsp = client.execute(req, mappingRpcDTO.getSessionKey());
            Shipping shipping = rsp.getShipping();
            if (shipping != null && shipping.getIsSuccess()) {
                // 发货成功修改数据库订单状态
                TaobaoTradeDO taobaoTradeDO = taoBaoTradeServiceI.getById(tid);
                taobaoTradeDO.setStatus(TradeState.TRADE_CLOSED.name());
                taoBaoTradeServiceI.update(taobaoTradeDO);
                return ResponseBaseBean.responseBean();
            } else {
                LOGGER.error(rsp.getSubMsg());
                //exceptionLogManager
                //       .save(new ExceptionLog(threadLocal.get().getDistributorId(), new Date(), rsp.getSubMsg()));
                return ResponseBaseBean.responseBean(TaoBaoErrorCode.TAOBAO_PUSH_LOGISTICS_FAIL_CODE, rsp.getSubMsg());
            }
        } catch (ApiException e) {
            LOGGER.error(e.getSubErrMsg());
            //exceptionLogManager
            //  .save(new ExceptionLog(threadLocal.get().getDistributorId(), new Date(), e.getSubErrMsg()));
            e.printStackTrace();
            return ResponseBaseBean.responseBean(TaoBaoErrorCode.TAOBAO_PUSH_LOGISTICS_FAIL_CODE, e.getSubErrMsg());
        }
    }
}
