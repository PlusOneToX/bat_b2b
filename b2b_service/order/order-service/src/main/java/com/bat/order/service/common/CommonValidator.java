package com.bat.order.service.common;

import static com.bat.order.service.common.Constant.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.data.dao.OrderCustomerDO;
import com.bat.order.service.common.data.dao.OrderDistributorDO;
import com.bat.order.service.common.data.dto.OrderDeliveryDTO;
import com.bat.order.service.common.data.dto.OrderInfoDTO;
import com.bat.order.service.common.data.dto.OrderInvoiceDTO;
import com.bat.order.service.common.error.ImportOrderErrorCode;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.customer.api.CustomerServiceRpc;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.cost.dataobject.OrderCustomerCostDO;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.dao.order.dataobject.OrderTypeDO;
import com.bat.order.service.order.executor.OrderTypeQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/6/16 13:44
 */

@Component
public class CommonValidator {

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private CustomerServiceRpc customerServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;
    @Resource
    private OrderConfig orderConfig;

    private static final Logger log = LoggerFactory.getLogger(CommonValidator.class);
    @Resource
    private OrderTypeQryExe orderTypeQryExe;

    @DubboReference(check = false, timeout = 5000)
    private SystemRegionServiceRpc systemRegionServiceRpc;

    /**
     * 检查用户id不能为空
     * 
     * @param userId
     */
    public void checkUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw OrderException.buildException(CommonErrorCode.B_USER_LOGIN_ERROR);
        }
    }

    /**
     * 检查用户id不能为空且归属分销商不能为空
     * 
     * @param userId
     * @param distributorId
     */
    public void checkUserId(String userId, String distributorId) {
        if (StringUtils.isBlank(userId)) {
            throw OrderException.buildException(CommonErrorCode.B_USER_LOGIN_ERROR);
        }
        if (StringUtils.isBlank(distributorId)) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_DISTRIBUTOR_ID_ERROR);
        }
    }

    /**
     * 检查分销商id不能为空，且分销商平台不能为空
     * 
     * @param distributorId
     * @param platform
     */
    public void checkDistributorAndPlatformValidity(String distributorId, String platform) {
        if (StringUtils.isBlank(distributorId)) {
            throw OrderException.buildException(CommonErrorCode.B_USER_LOGIN_ERROR);
        }
        if (StringUtils.isBlank(platform)) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_PLATFORM_NULL);
        }
    }

    /**
     * 检查发票参数
     * 
     * @param orderDTO
     */
    public void checkInvoice(OrderInfoDTO orderDTO) {
        // 开发票
        if (orderDTO.getInvoiceFlag().equals(INVOICE_FLAG_1)) {
            OrderInvoiceDTO invoice = orderDTO.getInvoice();
            if (invoice == null) {
                throw OrderException.buildException("P_ORDER_INVOICE_NULL");
            }
            if (invoice.getInvoiceType() == null) {
                // 发票类型不能为空
                throw OrderException.buildException("P_ORDER_INVOICE_TYPE_NULL");
            }
            if (invoice.getInvoiceTitleType() == null) {
                // 发票抬头类型不能为空
                throw OrderException.buildException("P_ORDER_INVOICE_TITLE_TYPE_NULL");
            }
            if (invoice.getInvoiceTitleType() == (short)2 && StringUtils.isBlank(invoice.getTaxpayerNumber())) {
                // 纳税人识别号(发票抬头为2时必填)
                throw OrderException.buildException("P_ORDER_INVOICE_NAME_NULL");
            }
        } else {
            // 不开发票 就把发票对象置空 避免以后的存储
            orderDTO.setInvoice(null);
        }
    }

    /**
     * 检查分销商可用性
     * 
     * @param distributorId
     * @return
     */
    public DistributorRpcDTO checkDistributorValidity(Integer distributorId) {
        Response<DistributorRpcDTO> response = distributorServiceRpc.distributorById(Integer.valueOf(distributorId));
        log.info("得到返回的分销商信息:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            DistributorRpcDTO distributorRpcDTO = response.getData();
            if (distributorRpcDTO.getFreezeStatus().equals(FREEZE_STATUS_2)) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_DISTRIBUTOR_FREEZE_ERROR);
            }
            if ((distributorRpcDTO.getErpFlag().equals(ERP_FLAG_0)
                && !distributorRpcDTO.getApplyStatus().equals(APPLY_STATUS_2))
                || ((distributorRpcDTO.getErpFlag().equals(ERP_FLAG_1) || distributorRpcDTO.getTreeNode() == 1)
                    && !distributorRpcDTO.getProfileStatus().equals(PROFILE_STATUS_2))) {
                throw OrderException.buildException(CommonErrorCode.B_ORDER_DISTRIBUTOR_STATUS_ERROR);
            }
            return distributorRpcDTO;
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 检查分销商可用性 多个
     *
     * @param distributorIds
     * @return
     */
    public List<DistributorRpcDTO> checkDistributorValidity(List<Integer> distributorIds) {
        Response<List<DistributorRpcDTO>> responses = distributorServiceRpc.distributorByIds(distributorIds);
        log.info("得到返回的分销商信息:{}", JSONObject.toJSONString(responses));
        if (responses.isSuccess()) {
            List<DistributorRpcDTO> distributorRpcDTOS = responses.getData();
            if (CollectionUtils.isEmpty(distributorRpcDTOS)) {
                throw OrderException.buildException(ImportOrderErrorCode.B_DISTRIBUTOR_ID_NOT_EXIST_OR_UNAVAILABLE,
                    "ids:" + distributorIds.toString() + MessageUtils.get(ImportOrderErrorCode.B_DISTRIBUTOR_ID_NOT_EXIST_OR_UNAVAILABLE));
            } else {
                for (Integer distributorId : distributorIds) {
                    DistributorRpcDTO rpcDTO = distributorRpcDTOS.stream()
                        .filter(distributorRpcDTO -> distributorRpcDTO.getId().equals(distributorId)).findFirst()
                        .orElse(null);
                    if (rpcDTO == null) {
                        throw OrderException.buildException(ImportOrderErrorCode.B_DISTRIBUTOR_ID_NOT_EXIST_OR_UNAVAILABLE,
                            "id:" + distributorId + MessageUtils.get(ImportOrderErrorCode.B_DISTRIBUTOR_ID_NOT_EXIST_OR_UNAVAILABLE));
                    } else {
                        if (rpcDTO.getFreezeStatus().equals(FREEZE_STATUS_2)) {
                            throw OrderException.buildException(
                                "id:" + rpcDTO.getId() + MessageUtils.get(CommonErrorCode.B_ORDER_DISTRIBUTOR_FREEZE_ERROR));
                        }
                        if ((rpcDTO.getErpFlag().equals(ERP_FLAG_0) && !rpcDTO.getApplyStatus().equals(APPLY_STATUS_2))
                            || ((rpcDTO.getErpFlag().equals(ERP_FLAG_1) || rpcDTO.getTreeNode() == 1)
                                && !rpcDTO.getProfileStatus().equals(PROFILE_STATUS_2))) {
                            throw OrderException.buildException(
                                "id:" + rpcDTO.getId() + MessageUtils.get(CommonErrorCode.B_ORDER_DISTRIBUTOR_STATUS_ERROR));
                        }
                    }
                }
            }
            return distributorRpcDTOS;
        } else {
            throw OrderException.buildException(responses.getErrCode(), responses.getErrMessage());
        }
    }

    /**
     * 检查商品的可用性
     * 
     * @param itemCodes
     * @return
     */
    public List<GoodsItemRpcDTO> checkGoodsItems(List<String> itemCodes) {
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> listResponse =
            goodsServiceRpc.listGoodsItemByCodes(itemCodes);
        if (listResponse.isSuccess()) {
            List<GoodsItemRpcDTO> goodsItemRpcDTOS = listResponse.getData();
            goodsItemRpcDTOS.forEach(goodsItemRpcDTO -> {
                if (!goodsItemRpcDTO.getSaleStatus().equals(SALE_STATUS_3)) {
                    throw OrderException.buildException(CommonErrorCode.B_ORDER_GOODS_STATUS_ERROR,
                        goodsItemRpcDTO.getItemCode() + MessageUtils.get(CommonErrorCode.B_ORDER_GOODS_STATUS_ERROR));
                }
            });
            return goodsItemRpcDTOS;
        } else {
            throw OrderException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
        }
    }

    /**
     * 检查前端传的金额和后端计算金额是否一致（只有分销商前台下单情况需进行对比）
     * 
     * @param orderDTO
     *            下单时DTO数据
     * @param orderDistributorDOList
     *            计算后订单数据
     */
    public void checkDistributorOrderAmount(OrderInfoDTO orderDTO, List<OrderDistributorDO> orderDistributorDOList) {
        AtomicReference<BigDecimal> orderAmount = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> distributionTotalAmount = new AtomicReference<>(new BigDecimal(0));
        orderDistributorDOList.forEach(orderDistributorDO -> {
            OrderDistributorCostDO orderCost = orderDistributorDO.getOrderCost();
            OrderDistributorDataDO orderDistributorData = orderDistributorDO.getOrderDistributorData();
            // orderAmount.set(orderAmount.get().add(orderCost.getPayAmount())
            // .divide(orderDistributorData.getCurrentRates(), 2, BigDecimal.ROUND_HALF_UP));
            // distributionTotalAmount.set(distributionTotalAmount.get().add(orderCost.getDistributionAmount()
            // .divide(orderDistributorData.getCurrentRates(), 2, BigDecimal.ROUND_HALF_UP)));
            orderAmount.set(orderAmount.get().add(orderCost.getPayAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
            distributionTotalAmount.set(distributionTotalAmount.get().add(orderCost.getDistributionAmount()).setScale(2,
                BigDecimal.ROUND_HALF_UP));
        });
        if (orderDTO.getOrderAmount().subtract(orderAmount.get()).doubleValue() != 0) {
            log.error("orderDTO.getOrderAmount():{}", orderDTO.getOrderAmount());
            log.error("orderAmount.get():{}", orderAmount.get());
            log.error("orderDTO:{}", JSON.toJSONString(orderDTO));
            log.error("orderDistributorDOList:{}", JSON.toJSONString(orderDistributorDOList));
            throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_ORDER_AMOUNT_ERROR,
                MessageUtils.get(OrderInfoErrorCode.B_ORDER_ORDER_AMOUNT_ERROR) + MessageUtils.get("B_ORDER_ORDER_AMOUNT")
                    + orderAmount.get().toString() + "," + MessageUtils.get("B_ORDER_DISTRIBUTION_AMOUNT")
                    + distributionTotalAmount.get().toString());
        }
    }

    /**
     * 检查前端传的金额和后端计算金额是否一致（C端客户）
     *
     * @param orderDTO
     *            下单时DTO数据
     * @param orderCustomerDOList
     *            计算后订单数据
     */
    public void checkCustomerOrderAmount(OrderInfoDTO orderDTO, List<OrderCustomerDO> orderCustomerDOList) {
        AtomicReference<BigDecimal> orderAmount = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> distributionTotalAmount = new AtomicReference<>(new BigDecimal(0));
        orderCustomerDOList.forEach(orderCustomerDO -> {
            OrderCustomerCostDO orderCost = orderCustomerDO.getOrderCost();
            orderAmount.set(orderAmount.get().add(orderCost.getPayAmount().subtract(orderCost.getDistributionAmount()))
                .setScale(2, BigDecimal.ROUND_HALF_UP));
            distributionTotalAmount.set(distributionTotalAmount.get().add(orderCost.getDistributionAmount()).setScale(2,
                BigDecimal.ROUND_HALF_UP));
        });
        if (orderDTO.getOrderAmount().subtract(orderAmount.get()).doubleValue() != 0) {
            throw OrderException.buildException(OrderInfoErrorCode.B_ORDER_ORDER_AMOUNT_ERROR,
                MessageUtils.get(OrderInfoErrorCode.B_ORDER_ORDER_AMOUNT_ERROR) + MessageUtils.get("B_ORDER_ORDER_AMOUNT")
                    + orderAmount.get().toString() + "," + MessageUtils.get("B_ORDER_DISTRIBUTION_AMOUNT")
                    + distributionTotalAmount.get().toString());
        }
    }

    /**
     * 检查C端客户下单时有效性
     *
     * @param customerId
     * @param distributorId
     * @param platform
     */
    public void checkCustomerValidity(String customerId, String distributorId, String platform) {
        if (StringUtils.isBlank(customerId)) {
            throw OrderException.buildException(CommonErrorCode.B_USER_LOGIN_ERROR);
        }
        if (StringUtils.isBlank(distributorId)) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_DISTRIBUTOR_ID_ERROR);
        }
        if (StringUtils.isBlank(platform)) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_PLATFORM_NULL);
        }
    }

    /**
     * 检查兑换卡来源
     * 
     * @param platform
     */
    public void checkExchangerPlatform(String platform) {
        List<String> platformExchanges = orderConfig.getPlatformCards();
        if (CollectionUtils.isEmpty(platformExchanges) && StringUtils.isBlank(orderConfig.getRongyaoexchange())) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_PLATFORM_EXCHANGER_NULL);
        }
        if ((!CollectionUtils.isEmpty(platformExchanges) && !platformExchanges.contains(platform))
            && (StringUtils.isNotBlank(orderConfig.getRongyaoexchange())
                && !orderConfig.getRongyaoexchange().equals(platform))) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_PLATFORM_EXCHANGER_ERROR);
        }
    }

    /**
     * 检查C端客户下单时有效性
     * 
     * @param customerId
     * @param distributorId
     * @param platform
     */
    public CustomerRpcDTO checkCustomerValidity(Integer customerId, Integer distributorId, String platform) {
        Response<CustomerRpcDTO> response = customerServiceRpc.getCustomerByCustomerIdAndDistributorIdAndPlatform(
            Integer.valueOf(customerId), Integer.valueOf(distributorId), platform);
        if (response.isSuccess()) {
            CustomerRpcDTO data = response.getData();
            if (data == null) {
                throw OrderException.buildException(CommonErrorCode.B_USER_NULL);
            }
            if ((data.getCustomerStatus() != null && data.getCustomerStatus().equals(FREEZE_STATUS_2))
                || (data.getPlatformStatus() != null && data.getPlatformStatus().equals(FREEZE_STATUS_2))) {
                throw OrderException.buildException(CommonErrorCode.B_USER_STATUS_ERROR);
            }
            if (StringUtils.isBlank(data.getPlatform()) || !data.getPlatformOpenFlag().equals(OPEN_YES)) {
                throw OrderException.buildException(CommonErrorCode.B_USER_PLATFORM_ERROR);
            }
            return data;
        } else {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 检查订单erp变更
     * 
     * @param order
     * @param orderExtendData
     */
    public void checkOrderChangeErp(OrderInfoDO order, OrderExtendDataDO orderExtendData) {
        List<OrderTypeDO> orderTypeDOS = orderTypeQryExe.listBySpecialFlag(SPECIAL_FLAG_4);
        if (!CollectionUtils.isEmpty(orderTypeDOS) && (order.getOrderTypeId().equals(orderTypeDOS.get(0).getId())
            || StringUtils.isNotBlank(orderExtendData.getOrderThirdpartyNo()))) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_CHANGE_ERROR);
        }
    }

    /**
     * 检查省市区 是否完整
     * 
     * @param orderDeliveryDO
     * @return
     */
    public boolean checkRegion(OrderDeliveryDTO orderDeliveryDO) {
        if (orderDeliveryDO == null) {
            return false;
        }
        Integer countryId = orderDeliveryDO.getCountryId();
        Integer provinceId = orderDeliveryDO.getProvinceId();
        Integer cityId = orderDeliveryDO.getCityId();
        Integer districtId = orderDeliveryDO.getDistrictId();
        if (countryId == null) {
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_COUNTRY_CHECK_ERROR);
        }
        try {
            com.bat.dubboapi.system.common.Response<RegionRpcDTO> countryResp =
                systemRegionServiceRpc.getRegionById(countryId);
            if (!countryResp.isSuccess()) {
                throw OrderException.buildException(countryResp.getErrCode(), countryResp.getErrMessage());
            }
            if (countryResp.getData().getHaveNext().equals((short)0)) {
                return true;
            }
        } catch (OrderException e) {
            e.printStackTrace();
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_COUNTRY_CHECK_ERROR);
        }
        if (provinceId == null) {
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_PROVINCE_CHECK_ERROR);
        }
        try {
            com.bat.dubboapi.system.common.Response<RegionRpcDTO> provinceResp =
                systemRegionServiceRpc.getRegionById(provinceId);
            if (!provinceResp.isSuccess()) {
                throw OrderException.buildException(provinceResp.getErrCode(), provinceResp.getErrMessage());
            }
            if (provinceResp.getData().getHaveNext().equals((short)0)) {
                return true;
            }
        } catch (OrderException e) {
            e.printStackTrace();
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_PROVINCE_CHECK_ERROR);
        }
        if (cityId == null) {
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_CITY_CHECK_ERROR);
        }
        try {
            com.bat.dubboapi.system.common.Response<RegionRpcDTO> cityResp =
                systemRegionServiceRpc.getRegionById(cityId);
            if (!cityResp.isSuccess()) {
                throw OrderException.buildException(cityResp.getErrCode(), cityResp.getErrMessage());
            }
            if (cityResp.getData().getHaveNext().equals((short)0)) {
                return true;
            }
        } catch (OrderException e) {
            e.printStackTrace();
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_CITY_CHECK_ERROR);
        }
        try {
            if (districtId == null || districtId == 0) {
                throw OrderException.buildException(CommonErrorCode.B_ADDRESS_DISTRICT_CHECK_ERROR);
            }
            com.bat.dubboapi.system.common.Response<RegionRpcDTO> districtResp =
                systemRegionServiceRpc.getRegionById(districtId);
            if (!districtResp.isSuccess()) {
                throw OrderException.buildException(districtResp.getErrCode(), districtResp.getErrMessage());
            }
            return true;
        } catch (OrderException e) {
            e.printStackTrace();
            throw OrderException.buildException(CommonErrorCode.B_ADDRESS_DISTRICT_CHECK_ERROR);
        }
    }
}
