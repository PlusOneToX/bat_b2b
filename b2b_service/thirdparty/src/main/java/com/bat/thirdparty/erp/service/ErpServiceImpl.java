package com.bat.thirdparty.erp.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.thirdparty.common.base.BaseId;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.flexible.exchange.ErpGoodsCustomInfoErrorConstant;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.error.warehouse.ThirdWarehouseErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.erp.service.executor.DistributorExe;
import com.bat.thirdparty.erp.service.executor.OrderExe;
import com.bat.thirdparty.erp.validator.ErpOrderValidator;
import com.bat.thirdparty.erp.validator.ThirdFlexibleValidator;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.financial.basesetting.dto.data.AccountBalanceChangeReq;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.deposit.api.FinancialDepositServiceRpc;
import com.bat.dubboapi.financial.voucher.api.FinancialVoucherServiceRpc;
import com.bat.dubboapi.financial.voucher.dto.data.ErpVoucherDetailsDTO;
import com.bat.dubboapi.flexible.model.api.ModelMaterialRelevanceServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoListCmd;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverGoodsItemEntry;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderRequest;
import com.bat.dubboapi.order.delivery.dto.ErpDeliverOrderStatusRequest;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.ErpStockChangeCmd;
import com.bat.thirdparty.erp.api.ErpServiceI;
import com.bat.thirdparty.erp.api.request.AccountBalanceChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderChangeRequest;
import com.bat.thirdparty.erp.api.request.ErpOrderCheckRequest;
import com.bat.thirdparty.erp.api.request.ErpVoucherDetailsRequest;
import com.bat.thirdparty.erp.api.response.ErpDeliverGoodsItemResponse;

@Service
public class ErpServiceImpl implements ErpServiceI {

    private static final Logger logger = LoggerFactory.getLogger(ErpServiceImpl.class);

    @DubboReference(check = false, timeout = 20000, retries = 0)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private FinancialDepositServiceRpc financialDepositServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private FinancialVoucherServiceRpc financialVoucherServiceRpc;

    @DubboReference(check = false, retries = 0, timeout = 10000)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @Resource
    private DistributorExe distributorExe;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private ModelMaterialRelevanceServiceRpc modelMaterialRelevanceServiceRpc;

    @Resource
    private OrderExe orderExe;

    @Override
    public ResponseBaseBean warehouseStockChange(ErpStockChangeCmd erpStockChangeCmd) {
        try {
            com.bat.dubboapi.warehouse.common.Response response =
                warehouseStockServiceRpc.updateWarehouseStock(erpStockChangeCmd);
            if (!response.isSuccess()) {
                logger.error("erp------>库存变更接口失败、返回ERP错误信息{}", response.getErrMessage());
                return ResponseBaseBean.responseBean(ThirdWarehouseErrorCode.W_WAREHOUSE_STOCK_CHANGE_EXCEPTION_CODE,
                    response.getErrMessage());
            }
            return ResponseBaseBean.responseBean();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("erp------>库存变更接口处理系统异常{}", e.getMessage());
            return ResponseBaseBean.responseBean(ThirdWarehouseErrorCode.W_WAREHOUSE_STOCK_CHANGE_EXCEPTION_CODE,
                MessageUtils.get(ThirdWarehouseErrorCode.W_WAREHOUSE_STOCK_CHANGE_EXCEPTION_MSG));
        }
    }

    @Override
    public void orderChange(ErpOrderChangeRequest request) {
        logger.info("erp------>订单变更接口,请求参数：" + JSON.toJSONString(request));
        orderExe.orderChange(request);
    }

    @Override
    public void orderCheckByErp(ErpOrderCheckRequest request) {
        orderExe.orderCheckByErp(request);
    }

    @Override
    public ResponseBaseBean accountBalanceChange(AccountBalanceChangeRequest request) {
        AccountBalanceChangeReq req = new AccountBalanceChangeReq();
        BeanUtils.copyProperties(request, req);
        Response response = financialDepositServiceRpc.accountBalanceChange(req);
        if (response.isSuccess()) {
            return ResponseBaseBean.responseBean();
        } else {
            logger.error(response.getErrCode(), response.getErrMessage());
            return ResponseBaseBean.responseBean(500, response.getErrCode());
        }
    }

    @Override
    public ResponseBaseBean createVouchers(ErpVoucherDetailsRequest request) {
        logger.info("ERP->B2B 第三方生成收款单");
        List<ErpVoucherDetailsDTO> dtos = request.getVoucherDetails().stream().map(erpVoucherDetailsEntry -> {
            ErpVoucherDetailsDTO dto = new ErpVoucherDetailsDTO();
            BeanUtils.copyProperties(erpVoucherDetailsEntry, dto);
            return dto;
        }).collect(Collectors.toList());
        Response response = financialVoucherServiceRpc.createVouchers(dtos);
        if (response.isSuccess()) {
            return ResponseBaseBean.responseBean();
        } else {
            logger.error(response.getErrCode(), response.getErrMessage());
            return ResponseBaseBean.responseBean(500, response.getErrCode());
        }
    }

    /**
     * ERP同步出库单到B2B
     *
     * @param erpDeliverOrderRequest
     * @return
     */
    @Override
    public com.bat.dubboapi.order.common.ResponseBaseBean
        deliverOrder(ErpDeliverOrderRequest erpDeliverOrderRequest) {
        ErpDeliverGoodsItemResponse response = new ErpDeliverGoodsItemResponse();

        logger.info("erp------>b2b的erp同步发货单,请求参数：{}", JSON.toJSONString(erpDeliverOrderRequest));
        // 参数校验
        com.bat.dubboapi.order.common.ResponseBaseBean validResponse =
            ErpOrderValidator.validateErpSyncOutBoundParam(erpDeliverOrderRequest);
        if (validResponse.getCode() != 0) {
            logger.error("erp------>b2b的erp同步发货单、参数校验失败、返回{}", JSON.toJSONString(validResponse));
            return validResponse;
        }
        try {
            com.bat.dubboapi.order.common.ResponseBaseBean responseBaseBean =
                orderDeliveryServiceRpc.syncOutboundOrderFromERP(erpDeliverOrderRequest);
            if (responseBaseBean == null) {
                throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
            if (responseBaseBean.getCode() != 0) {
                logger.info("erp------>b2b的erp同步发货单失败、返回ERP,{}", JSON.toJSONString(responseBaseBean));
                return responseBaseBean;
            }
            List<ErpDeliverGoodsItemEntry> itemEntries = (List<ErpDeliverGoodsItemEntry>)responseBaseBean.getData();
            response.setDeliverDetails(itemEntries);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("erp------>b2b的erp同步发货单异常、返回{}", e.getMessage());
            response.setCode(ThirdOrderErrorCode.T_ERP_SYNC_OUTBOUND_B2B_ERROR_CODE);
            response.setMsg(StringUtils.isBlank(e.getMessage()) ? ThirdOrderErrorCode.T_ERP_SYNC_OUTBOUND_B2B_ERROR_MSG
                : e.getMessage());
        }
        logger.info("erp------>b2b的erp同步发货单、返回ERP,{}", JSON.toJSONString(response));
        return response;
    }

    @Override
    public ResponseBaseBean changeDeliverOrderStatus(ErpDeliverOrderStatusRequest request) {
        logger.info("erp------>b2b的erp同步销售单发货单状态变更,请求参数：{}", JSON.toJSONString(request));
        com.bat.dubboapi.order.common.ResponseBaseBean baseBean =
            ErpOrderValidator.validateErpDeliverOrderStatusRequest(request);
        if (baseBean.getCode() != 0) {
            ResponseBaseBean bean = com.bat.thirdparty.common.util.BeanUtils.copy(baseBean, ResponseBaseBean.class);
            logger.error("erp------>b2b的erp同步销售单发货单状态变更、参数校验失败、返回{}", JSON.toJSONString(bean));
            return bean;
        }
        try {
            com.bat.dubboapi.order.common.ResponseBaseBean responseBaseBean =
                orderDeliveryServiceRpc.changeDeliverOrderStatus(request);
            logger.info("erp------>b2b的erp同步销售单发货单状态变更、返回ERP{}", JSON.toJSONString(responseBaseBean));
            return ResponseBaseBean.responseBean(responseBaseBean.getCode(), responseBaseBean.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("erp------>b2b的erp同步销售单发货单状态变更异常、返回{}", e.getMessage());
            return ResponseBaseBean.responseBean(ThirdOrderErrorCode.T_ERP_SYNC_OUTBOUND_STATUS_B2B_ERROR_CODE,
                ThirdOrderErrorCode.T_ERP_SYNC_OUTBOUND_STATUS_B2B_ERROR_MSG);
        }
    }

    @Override
    public ResponseBaseBean
        syncMaterialAndModelRelaStockOutStatus(ErpGoodsCustomInfoListCmd erpGoodsCustomInfoListCmd) {
        logger.info("ERP操作B2B型号和材质的关联关系是否缺货：" + JSON.toJSONString(erpGoodsCustomInfoListCmd));
        logger.info("缺货调用-当前租户编号:{}", TenantContext.getTenantNo());
        try {
            ThirdFlexibleValidator.validateErpGoodsCustomInfoListCmd(erpGoodsCustomInfoListCmd);
            // 调用柔性服务
            com.bat.dubboapi.flexible.common.Response response =
                modelMaterialRelevanceServiceRpc.syncMaterialAndModelRelaStockOutStatus(erpGoodsCustomInfoListCmd);
            if (response == null) {
                throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
            }
            if (!response.isSuccess()) {
                return ResponseBaseBean.responseBean(
                    ErpGoodsCustomInfoErrorConstant.ErpSyncStockOutStatusError.getCode(), response.getErrMessage());
            }
            return ResponseBaseBean.responseBean();
        } catch (ThirdPartyOpenApiException e) {
            e.printStackTrace();
            logger.error("ERP操作B2B型号和材质的关联关系是否缺货出现api异常{}", e.getMessage());
            return ResponseBaseBean.responseBean(ErpGoodsCustomInfoErrorConstant.ErpSyncStockOutStatusError.getCode(),
                e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ERP操作B2B型号和材质的关联关系是否缺货出现系统异常{}", e.getMessage());
            return ResponseBaseBean.responseBean(ErpGoodsCustomInfoErrorConstant.ErpSyncStockOutStatusError.getCode(),
                ErpGoodsCustomInfoErrorConstant.ErpSyncStockOutStatusError.getMsg());
        }

    }

    @Override
    public void distributorSync(BaseId cmd) throws Exception {
        distributorExe.syncDistributorToERP(cmd.getId());
    }

}
