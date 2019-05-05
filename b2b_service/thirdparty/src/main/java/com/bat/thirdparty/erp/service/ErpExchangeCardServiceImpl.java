package com.bat.thirdparty.erp.service;

import com.bat.thirdparty.common.error.flexible.exchange.ThirdFlexibleErrorCode;
import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.validator.ThirdExchangeCardValidator;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.flexible.common.BaseOpenApiException;
import com.bat.dubboapi.flexible.exchange.ExchangeCardServiceRpc;
import com.bat.dubboapi.flexible.exchange.dto.ExchangeCodeRefundDTO;
import com.bat.dubboapi.flexible.exchange.dto.OrderExchangeCardBindErpRequest;
import com.bat.dubboapi.order.order.api.OrderGoodsDubboServiceRpc;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.erp.api.ErpExchangeCardServiceI;

@Service
public class ErpExchangeCardServiceImpl implements ErpExchangeCardServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErpExchangeCardServiceImpl.class);

    @DubboReference(check = false, timeout = 800000, retries = 0)
    private ExchangeCardServiceRpc exchangeCardServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsDubboServiceRpc orderGoodsDubboServiceRpc;

    @Override
    public ResponseBaseBean bindOrderAndBoxCode(OrderExchangeCardBindErpRequest orderExchangeCardBindErpRequest) {
        ThirdExchangeCardValidator.validErpBindingBoxCode(orderExchangeCardBindErpRequest.getItemCodeList());
        try {
            com.bat.dubboapi.flexible.common.ResponseBaseBean responseBaseBean =
                exchangeCardServiceRpc.bindOrderAndBoxCode(orderExchangeCardBindErpRequest);
            ResponseBaseBean baseBean = BeanUtils.copy(responseBaseBean, ResponseBaseBean.class);
            LOGGER.info("erp出库、绑定分销商订单和盒码关系返回{}", JSON.toJSONString(baseBean));
            return baseBean;
        } catch (BaseOpenApiException e) {
            e.printStackTrace();
            LOGGER.error("erp出库、绑定分销商订单和盒码关系异常,{}", e.getMsg());
            return ResponseBaseBean.responseBean(ThirdFlexibleErrorCode.ERP_OUTBOUND_BIND_BOX_CODE_AND_ORDER_ERROR_CODE,
                e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("erp出库、绑定分销商订单和盒码关系异常,{}", e.getMessage());
            return ResponseBaseBean.responseBean(ThirdFlexibleErrorCode.ERP_OUTBOUND_BIND_BOX_CODE_AND_ORDER_ERROR_CODE,
                MessageUtils.get(ThirdFlexibleErrorCode.ERP_OUTBOUND_BIND_BOX_CODE_AND_ORDER_ERROR_MSG));
        }
    }

    @Override
    public ResponseBaseBean refund(ExchangeCodeRefundDTO exchangeCodeRefundDTO) {
        LOGGER.info("erp兑换卡退货，参数为：" + JSON.toJSONString(exchangeCodeRefundDTO));
        ThirdExchangeCardValidator.validErpRefund(exchangeCodeRefundDTO.getCodeDTOList());
        try {
            com.bat.dubboapi.flexible.common.ResponseBaseBean refundResponse =
                exchangeCardServiceRpc.refund(exchangeCodeRefundDTO);
            LOGGER.info("erp兑换卡退货，返回为：" + JSON.toJSONString(refundResponse));
            return BeanUtils.copy(refundResponse, ResponseBaseBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("erp兑换码退款、同步B2B异常,{}", e.getMessage());
            return ResponseBaseBean.responseBean(ThirdFlexibleErrorCode.ERP_EXCHANGE_CODE_REFUND_SYNC_ERROR_CODE,
                MessageUtils.get(ThirdFlexibleErrorCode.ERP_EXCHANGE_CODE_REFUND_SYNC_ERROR_MSG));
        }

    }

    public static void main(String[] args) {
        ResponseBaseBean test = test();
        System.out.println(JSON.toJSONString(test));
    }

    public static ResponseBaseBean test() {
        try {
            System.out.println(1);
            throw new BaseOpenApiException(22, "222222222222");
        } catch (BaseOpenApiException e) {
            e.printStackTrace();
            System.out.println(22);
            return ResponseBaseBean.responseBean(22, "22");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(333);
            return ResponseBaseBean.responseBean(333, "333");
        }
    }
}
