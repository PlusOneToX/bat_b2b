package com.bat.order.service.deliver.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.VoucherDistributorRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.response.ResponseBaseBean;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.constant.OrderDeliverConstant;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.error.DubboServiceErrorCode;
import com.bat.order.service.common.error.OrderCommonErrorCode;
import com.bat.order.service.common.error.OrderDeliverErrorCode;

@Component
public class OrderDeliverValidator {

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private FinancialVoucherServiceRpc financialVoucherServiceRpc;

    public static ResponseBaseBean validOrderDeliverBill(Short orderDeliveryType, OrderInfoDO orderInfoDO) {
        if (orderDeliveryType != OrderDeliverConstant.ORDER_DELIVER_TYPE_SALE
            && orderDeliveryType != OrderDeliverConstant.ORDER_DELIVER_TYPE_CHANGE) {
            return ResponseBaseBean.responseBean(OrderDeliverErrorCode.ORDER_DELIVER_TYPE_ERROR_CODE,
                MessageUtils.get(OrderDeliverErrorCode.ORDER_DELIVER_TYPE_ERROR_MSG));
        }
        if (orderInfoDO == null) {
            return ResponseBaseBean.responseBean(OrderCommonErrorCode.ORDER_NULL_CODE,
                MessageUtils.get(OrderCommonErrorCode.ORDER_NULL));
        }
        return new ResponseBaseBean();
    }

    /**
     * 同步柔性出库单采购单到ERP需要先判断收款单
     * 
     * @param payStatus
     * @param orderId
     */
    public void validOrderStatusBySyncRxToErp(Short payStatus, Integer orderId) {
        if (!OrderInfoConstant.ORDER_INVOICE_FLAG_YES.equals(payStatus)) {
            return;
        }
        com.bat.dubboapi.financial.common.Response<VoucherDistributorRpcDTO> voucherDistributorRpcDTOResponse =
            financialVoucherServiceRpc.listVouchersByBusinessId(orderId);
        if (voucherDistributorRpcDTOResponse == null) {
            throw OrderException.buildException(DubboServiceErrorCode.DUBBO_FINANCIAL_SERVICE_EXCEPTION,
                MessageUtils.get(DubboServiceErrorCode.DUBBO_FINANCIAL_SERVICE_EXCEPTION));
        }
        /*要忽略这个判断、因为可能没有收款单
        if (!voucherDistributorRpcDTOResponse.isSuccess()) {
             return com.bat.dubboapi.order.common.Response.buildFailure(
                 voucherDistributorRpcDTOResponse.getErrCode(), voucherDistributorRpcDTOResponse.getErrMessage());
         }*/
        if (voucherDistributorRpcDTOResponse.isSuccess()) {
            // 成功才返回
            VoucherDistributorRpcDTO voucherDistributorRpcDTOResponseData = voucherDistributorRpcDTOResponse.getData();
            if (StringUtils.isBlank(voucherDistributorRpcDTOResponseData.getVoucherErpNo())) {
                // 需要先同步收款单、再同步出库单和采购单
                throw OrderException.buildException(
                    OrderDeliverErrorCode.ORDER_DELIVER_SYNC_ERP_MUST_AFTER_SYNC_VOUCHER,
                    MessageUtils.get(OrderDeliverErrorCode.ORDER_DELIVER_SYNC_ERP_MUST_AFTER_SYNC_VOUCHER));
            }
        }
    }
}
