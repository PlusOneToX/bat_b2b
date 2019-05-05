package com.bat.thirdparty.order.service.executor;

import java.util.List;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.ThirdNameErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.order.common.Response;
import com.bat.dubboapi.order.cost.api.OrderDistributorCostServiceRpc;
import com.bat.dubboapi.order.cost.dto.OrderDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.cost.dto.OrderGoodsDistributorCostRpcQryDTO;
import com.bat.dubboapi.order.exchange.api.OrderGoodsExchangeCodeServiceRpc;
import com.bat.dubboapi.order.exchange.dto.OrderGoodsExchangeCodeRpcQryDTO;
import com.bat.dubboapi.order.order.api.*;
import com.bat.dubboapi.order.order.dto.data.OrderCustomerDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderDistributorDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDiyRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsRpcDTO;
import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;
import com.bat.dubboapi.order.type.api.OrderTypeServiceRpc;
import com.bat.dubboapi.order.type.dto.OrderTypeRpcQryDTO;

@Component
public class OrderQryExe {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderInfoServiceRpc orderInfoServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderExtendDataServiceRpc orderExtendDataServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderTypeServiceRpc orderTypeServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsDubboServiceRpc orderGoodsDubboServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsDiyServiceRpc orderGoodsDiyServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDistributorDataServiceRpc orderDistributorDataServiceRpc;

    @DubboReference(check = false, timeout = 10000)
    private OrderDistributorCostServiceRpc orderDistributorCostServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderCustomerDataServiceRpc orderCustomerDataServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsExchangeCodeServiceRpc orderGoodsExchangeCodeServiceRpc;

    /**
     * 根据订单id查询订单信息
     * 
     * @param orderId
     * @return
     */
    public OrderInfoRpcQryDTO getOrderInfoById(Integer orderId) {
        com.bat.dubboapi.order.common.Response<OrderInfoRpcQryDTO> orderInfoResp =
            orderInfoServiceRpc.getById(orderId);
        if (orderInfoResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderInfoResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderInfoResp.getErrCode(), orderInfoResp.getErrMessage());
        }
        OrderInfoRpcQryDTO orderInfoRpcQryDTO = orderInfoResp.getData();
        if (orderInfoRpcQryDTO == null) {
            throw ThirdPartyException.buildException(MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_ORDER)
                + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_ERROR));
        }

        return orderInfoRpcQryDTO;
    }

    /**
     * 根据订单id查询扩展数据
     * 
     * @param orderId
     * @return
     */
    public OrderExtendDataSimpleRpcDTO getExtendDataByOrderId(Integer orderId) {
        Response<OrderExtendDataSimpleRpcDTO> response =
            orderExtendDataServiceRpc.getExtendDataSimpleByOrderId(orderId);
        if (response == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    public OrderTypeRpcQryDTO getOrderTypeById(Integer orderTypeId) {
        com.bat.dubboapi.order.common.Response<OrderTypeRpcQryDTO> orderTypeResp =
            orderTypeServiceRpc.getById(orderTypeId);
        if (orderTypeResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderTypeResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderTypeResp.getErrCode(), orderTypeResp.getErrMessage());
        }
        OrderTypeRpcQryDTO orderTypeRpcQryDTO = orderTypeResp.getData();
        return orderTypeRpcQryDTO;
    }

    public OrderTypeRpcQryDTO getOrderTypeBySpecialFlag(Short specialFlag) {
        com.bat.dubboapi.order.common.Response<OrderTypeRpcQryDTO> orderTypeResp =
            orderTypeServiceRpc.getBySpecialFlag(specialFlag);
        if (orderTypeResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderTypeResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderTypeResp.getErrCode(), orderTypeResp.getErrMessage());
        }
        OrderTypeRpcQryDTO orderTypeRpcQryDTO = orderTypeResp.getData();
        return orderTypeRpcQryDTO;
    }

    public List<OrderGoodsRpcDTO> listByOrderId(Integer orderId) {

        com.bat.dubboapi.order.common.Response<List<OrderGoodsRpcDTO>> orderGoodsResp =
            orderGoodsDubboServiceRpc.listOrderGoodsByOrderId(orderId);
        if (orderGoodsResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderGoodsResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderGoodsResp.getErrCode(), orderGoodsResp.getErrMessage());
        }
        return orderGoodsResp.getData();
    }

    public List<OrderGoodsDiyRpcDTO> listOrderGoodsDiyByOrderId(Integer orderId) {
        // 定制订单
        com.bat.dubboapi.order.common.Response<List<OrderGoodsDiyRpcDTO>> diyResp =
            orderGoodsDiyServiceRpc.listByOrderId(orderId);
        if (diyResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!diyResp.isSuccess()) {
            throw ThirdPartyException.buildException(diyResp.getErrCode(), diyResp.getErrMessage());
        }
        return diyResp.getData();
    }

    /**
     * 根据订单id和ERP FLAG查询顶单归属分销商数据
     * 
     * @param orderId
     * @param erpFlag
     * @return
     */
    public List<OrderDistributorDataRpcDTO> getByOrderIdAndErpFlag(Integer orderId, Short erpFlag) {
        com.bat.dubboapi.order.common.Response<List<OrderDistributorDataRpcDTO>> distributorDataResp =
            orderDistributorDataServiceRpc.listByOrderIdAndErpFlag(orderId, (short)1);
        if (distributorDataResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!distributorDataResp.isSuccess()) {
            throw ThirdPartyException.buildException(distributorDataResp.getErrCode(),
                distributorDataResp.getErrMessage());
        }
        return distributorDataResp.getData();
    }

    /**
     * 根据订单id和分销商id查询订单费用归属分销商
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    public OrderDistributorCostRpcQryDTO getByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        com.bat.dubboapi.order.common.Response<OrderDistributorCostRpcQryDTO> costRpcQryDTOResponse =
            orderDistributorCostServiceRpc.getByOrderIdAndDistributorId(orderId, distributorId);
        if (costRpcQryDTOResponse == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!costRpcQryDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(costRpcQryDTOResponse.getErrCode(),
                costRpcQryDTOResponse.getErrMessage());
        }
        return costRpcQryDTOResponse.getData();
    }

    /**
     * 根据订单id和分销商id查询订单明细费用归属分销商
     * 
     * @param orderId
     * @param distributorId
     * @return
     */
    public List<OrderGoodsDistributorCostRpcQryDTO>
        listOrderGoodDistributorCostByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        com.bat.dubboapi.order.common.Response<List<OrderGoodsDistributorCostRpcQryDTO>> orderGoodsCostResp =
            orderDistributorCostServiceRpc.listGoodsCostByOrderIdAndDistributorId(orderId, distributorId);
        if (orderGoodsCostResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderGoodsCostResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderGoodsCostResp.getErrCode(),
                orderGoodsCostResp.getErrMessage());
        }
        return orderGoodsCostResp.getData();
    }

    /**
     * 根据工厂单号查询订单扩展数据（B2B的orderNo返回为空的）
     * 
     * @param orderFactoryNo
     * @return
     */
    public OrderExtendDataSimpleRpcDTO getOrderExtendDataRpcByOrderFactoryNo(String orderFactoryNo) {
        Response<OrderExtendDataSimpleRpcDTO> response = orderExtendDataServiceRpc.getByOrderFactoryNo(orderFactoryNo);
        if (response == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }

    /**
     * 根据订单id查询归属C端客户信息
     * 
     * @param orderId
     */
    public OrderCustomerDataRpcDTO getOrderCustomerDataByOrderId(Integer orderId) {
        try {
            Response<OrderCustomerDataRpcDTO> response = orderCustomerDataServiceRpc.getByOrderId(orderId);
            if (!response.isSuccess()) {
                throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
            }
            return response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }

    }

    /**
     *
     * @param orderId
     */
    public List<OrderGoodsExchangeCodeRpcQryDTO> getOrderGoodsExchangeCodeByUserOrderId(Integer orderId) {
        try {
            Response<List<OrderGoodsExchangeCodeRpcQryDTO>> response =
                orderGoodsExchangeCodeServiceRpc.listByOrderId(orderId);
            if (!response.isSuccess()) {
                throw ThirdPartyException.buildException(response.getErrCode(), response.getErrMessage());
            }
            return response.getData();
        } catch (Exception e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }

    }

    public OrderInfoRpcQryDTO getOrderInfoByOrderNo(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw ThirdPartyException.buildException(MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_ORDER)
                + MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL));
        }
        com.bat.dubboapi.order.common.Response<OrderInfoRpcQryDTO> orderInfoResp =
            orderInfoServiceRpc.getByOrderNo(orderNo);
        if (orderInfoResp == null) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
        }
        if (!orderInfoResp.isSuccess()) {
            throw ThirdPartyException.buildException(orderInfoResp.getErrCode(), orderInfoResp.getErrMessage());
        }
        OrderInfoRpcQryDTO orderInfoRpcQryDTO = orderInfoResp.getData();
        return orderInfoRpcQryDTO;
    }
}
