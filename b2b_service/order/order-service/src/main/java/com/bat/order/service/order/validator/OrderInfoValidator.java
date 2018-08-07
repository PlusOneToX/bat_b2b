package com.bat.order.service.order.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bat.order.dao.cost.dataobject.*;
import com.bat.order.service.common.CommonErrorCode;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.constant.FlexibleRpcConstant;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.enumtype.DeliverStatus;
import com.bat.order.service.common.error.FlexibleRpcErrorCode;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.error.OrderNameErrorCode;
import com.bat.order.service.flexible.validator.MaterialValidator;
import com.bat.order.service.flexible.validator.ModelValidator;
import com.bat.order.service.flexible.validator.PictureValidator;
import com.bat.order.service.utils.distributor.FlexiblePriceUtils;
import com.bat.order.service.utils.goods.GoodsServiceRpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.ModelDTORpcQry;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.order.dto.common.OrderInfoParamDTO;
import com.bat.order.api.order.dto.common.OrderInvoiceCmd;
import com.bat.order.api.order.dto.diy.OrderDetailDiyDTO;
import com.bat.order.api.order.dto.diy.OrderDiyCmd;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

@Component
public class OrderInfoValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoValidator.class);

    /**
     * 参数校验、返回order_goods和order_goods_diy
     * 
     * @param orderDiyCmd
     * @return
     */
    public static final OrderInfoParamDTO validDiyOrder(OrderDiyCmd orderDiyCmd) {
        List<OrderDetailDiyDTO> diyDetailList = orderDiyCmd.getDiyDetailList();
        // DIY定制的列表
        List<OrderGoodsDiyDO> diyDOList = new ArrayList<>();

        List<OrderGoodsDO> orderGoodsDOList = new ArrayList<>();

        // 货品的价格
        Map<Integer, BigDecimal> itemPriceMap = new HashMap<>();
        // 订单费用明细归属客户列表
        List<OrderGoodsCustomerCostDO> orderGoodsCustomerCostDOList = new ArrayList<>();
        // 订单费用归属分销商列表
        List<OrderGoodsDistributorCostDO> orderGoodsDistributorCostDOList = new ArrayList<>();

        // 货品总体积
        BigDecimal volumeSum = new BigDecimal("0.00");

        // 货品总重量
        BigDecimal weightSum = new BigDecimal("0.00");
        diyDetailList.stream().forEach(orderDetailDiyDTO -> {

        });

        for (int x = 0; x < diyDetailList.size(); x++) {
            OrderDetailDiyDTO orderDetailDiyDTO = diyDetailList.get(x);
            // 组装定制参数
            OrderGoodsDiyDO orderGoodsDiyDO = new OrderGoodsDiyDO();
            MaterialDTORpcQry materialDTORpcQry =
                MaterialValidator.getUsableByMaterialId(orderDetailDiyDTO.getMaterialId());
            if (orderDetailDiyDTO.getPictureId() > 0
                && FlexibleRpcConstant.MATERIAL_ALL_UPLOAD_FLAG_NO.equals(materialDTORpcQry.getAllowUploadFlag())) {
                // 不允许上传图片
                throw OrderException.buildException(FlexibleRpcErrorCode.FLEXIBLE_MATERIAL_NOT_ALLOW_UPLOAD_PICTURE);
            }
            orderGoodsDiyDO.setMaterialId(orderDetailDiyDTO.getMaterialId());
            orderGoodsDiyDO.setMaterialName(materialDTORpcQry.getName());
            orderGoodsDiyDO.setCategoryId(materialDTORpcQry.getCategoryId());
            orderGoodsDiyDO.setCategoryName(materialDTORpcQry.getCategoryName());
            // 设置型号信息
            ModelDTORpcQry modelDTORpcQry = ModelValidator.getUsableByModelId(orderDetailDiyDTO.getModelId());
            orderGoodsDiyDO.setModelId(modelDTORpcQry.getId());
            orderGoodsDiyDO.setModelName(modelDTORpcQry.getName());
            orderGoodsDiyDO.setBrandId(modelDTORpcQry.getParentId());
            orderGoodsDiyDO.setBrandName(modelDTORpcQry.getParentName());
            // 计算价格
            BigDecimal itemPrice = itemPriceMap.get(materialDTORpcQry.getItemId());
            if (itemPrice == null) {
                itemPrice = FlexiblePriceUtils.getPriceByCondition(orderDiyCmd.getDistributorId(),
                    materialDTORpcQry.getItemId(), materialDTORpcQry.getItemCode(), orderDiyCmd.getCustomerId());
                itemPriceMap.put(materialDTORpcQry.getItemId(), itemPrice);
            }
            orderGoodsDiyDO.setPictureId(orderDetailDiyDTO.getPictureId());
            if (orderGoodsDiyDO.getPictureId() > 0) {
                PictureDTORpcQry pictureDTORpcQry =
                    PictureValidator.getUsableByPictureId(orderDetailDiyDTO.getPictureId());
                // 计算版本费
                itemPrice = itemPrice.add(pictureDTORpcQry.getCopyrightCost());
            }
            // 校验实际价格
            if (itemPrice.compareTo(orderDetailDiyDTO.getActualPrice()) != 0) {
                String msg = MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                    + MessageUtils.get(OrderInfoErrorCode.ORDER_ITEM_ACTUAL_PRICE_ERROR);
                throw OrderException.buildException(msg);
            }
            orderGoodsDiyDO.setGenerateImage(orderDetailDiyDTO.getGenerateImage());
            orderGoodsDiyDO.setPreviewImage(orderDetailDiyDTO.getPreviewImage());

            // 组装Goods参数
            OrderGoodsDO orderGoodsDO = new OrderGoodsDO();
            GoodsItemRpcDTO goodsItemRpcDTO =
                GoodsServiceRpcUtils.getGoodsItemRpcByItemIdOrItemCode(materialDTORpcQry.getItemId(), null);
            orderGoodsDO.setGoodsId(goodsItemRpcDTO.getGoodsId());
            orderGoodsDO.setGoodsName(goodsItemRpcDTO.getGoodsName());
            orderGoodsDO.setGoodsNo(goodsItemRpcDTO.getGoodsNo());
            orderGoodsDO.setBarCode(goodsItemRpcDTO.getBarCode());
            // 货品信息
            orderGoodsDO.setItemId(materialDTORpcQry.getItemId());
            orderGoodsDO.setItemCode(goodsItemRpcDTO.getItemCode());
            orderGoodsDO.setItemName(goodsItemRpcDTO.getItemName());
            orderGoodsDO.setGoodsType(Constant.GOODS_TYPE_2);
            orderGoodsDO.setDiyType(orderDetailDiyDTO.getDiyType());
            orderGoodsDO.setSpecsName(goodsItemRpcDTO.getSpecsName());
            orderGoodsDO.setColorName(goodsItemRpcDTO.getColorName());

            orderGoodsDO.setItemCount(orderDetailDiyDTO.getItemCount());
            // 设置长宽高(先写死)
            orderGoodsDO.setWeight(BigDecimal.ONE);
            orderGoodsDO.setHeight(BigDecimal.ONE);
            orderGoodsDO.setLength(BigDecimal.ONE);
            orderGoodsDO.setWidth(BigDecimal.ONE);
            // 体积 = 长*宽*高
            BigDecimal volume =
                orderGoodsDO.getLength().multiply(orderGoodsDO.getWidth()).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .multiply(orderGoodsDO.getHeight()).setScale(2, BigDecimal.ROUND_HALF_UP);
            volumeSum = volumeSum.add(volume);
            weightSum = weightSum.add(orderGoodsDO.getWeight());
            orderGoodsDOList.add(orderGoodsDO);
            diyDOList.add(orderGoodsDiyDO);
            if (orderDiyCmd.getCustomerId() != null) {
                // 计算客户
                OrderGoodsCustomerCostDO orderGoodsCustomerCostDO = new OrderGoodsCustomerCostDO();
                orderGoodsCustomerCostDO.setCustomerId(orderDiyCmd.getCustomerId());
                orderGoodsCustomerCostDO.setItemType(orderDetailDiyDTO.getItemType());
                orderGoodsCustomerCostDO.setSalePrice(orderDetailDiyDTO.getSalePrice());
                orderGoodsCustomerCostDO.setActualPrice(orderDetailDiyDTO.getActualPrice());
                orderGoodsCustomerCostDOList.add(orderGoodsCustomerCostDO);
            } else {
                // 计算分销商
                OrderGoodsDistributorCostDO orderGoodsDistributorCostDO = new OrderGoodsDistributorCostDO();
                orderGoodsDistributorCostDO.setDistributorId(orderDiyCmd.getDistributorId());
                orderGoodsDistributorCostDO.setItemType(orderDetailDiyDTO.getItemType());
                orderGoodsDistributorCostDO.setActualPrice(orderDetailDiyDTO.getActualPrice());
                orderGoodsDistributorCostDO.setSalePrice(orderDetailDiyDTO.getSalePrice());
                orderGoodsDistributorCostDOList.add(orderGoodsDistributorCostDO);
            }
        }

        // 得到订单的总金额
        OrderCustomerCostDO orderCustomerCostDO = validCustomerOrderAmount(orderDiyCmd);
        // 计算分销商的订单金额
        OrderDistributorCostDO orderDistributorCostDO = validDistributorOrderAmount(orderDiyCmd);
        LOGGER.info("继续执行");
        OrderInfoParamDTO orderInfoParamDTO = new OrderInfoParamDTO();
        orderInfoParamDTO.setDiyDOList(diyDOList);
        orderInfoParamDTO.setOrderGoodsDOList(orderGoodsDOList);
        orderInfoParamDTO.setOrderCustomerCostDO(orderCustomerCostDO);
        orderInfoParamDTO.setOrderDistributorCostDO(orderDistributorCostDO);
        orderInfoParamDTO.setOrderGoodsDistributorCostDOList(orderGoodsDistributorCostDOList);
        orderInfoParamDTO.setOrderGoodsCustomerCostDOList(orderGoodsCustomerCostDOList);

        // 判断发票
        OrderInvoiceDO orderInvoiceDO = validInvoice(orderDiyCmd.getInvoiceFlag(), orderDiyCmd.getOrderInvoiceCmd());
        orderInfoParamDTO.setOrderInvoiceDO(orderInvoiceDO);

        // 判断运费
        // OrderLogisticValidator.validOrderDistributionAmount(orderDiyCmd.getOrderAddress(),
        // orderDiyCmd.getPayAmount(),
        // orderDiyCmd.getDistributionAmount(), volumeSum, weightSum, 1);

        return orderInfoParamDTO;
    }

    private static OrderInvoiceDO validInvoice(Short invoiceFlag, OrderInvoiceCmd orderInvoiceCmd) {
        if (OrderInfoConstant.ORDER_INVOICE_FLAG_NO.equals(invoiceFlag)) {
            return null;
        }
        if (orderInvoiceCmd == null) {
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_INVOICE_NULL_CHOOSE_OPEN_INVOICE);
        }
        if (orderInvoiceCmd.getInvoiceTitleType() == null) {
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_INVOICE_TITLE_TYPE_NULL);
        }
        if (orderInvoiceCmd.getInvoiceType() == null) {
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_INVOICE_TYPE_NULL);
        }
        // 需要处理分销商的信息、对接了分销商的接口再处理
        return null;
    }

    private static OrderCustomerCostDO validCustomerOrderAmount(OrderDiyCmd orderDiyCmd) {
        List<OrderDetailDiyDTO> diyDetailList = orderDiyCmd.getDiyDetailList();
        if (orderDiyCmd.getCustomerId() == null) {
            return null;
        }
        OrderCustomerCostDO orderCustomerCostDO = new OrderCustomerCostDO();
        // 订单实际支付总金额
        BigDecimal actualPrice = BigDecimal.ZERO;

        for (int x = 0; x < diyDetailList.size(); x++) {
            BigDecimal itemAmount = diyDetailList.get(x).getActualPrice()
                .multiply(new BigDecimal(String.valueOf(diyDetailList.get(x).getItemCount())));
            actualPrice = actualPrice.add(itemAmount);

        }
        if (actualPrice.add(orderDiyCmd.getDistributionAmount()).compareTo(orderDiyCmd.getPayAmount()) != 0) {
            // 订单金额计算错误
            String msg = MessageUtils.get(OrderInfoErrorCode.ORDER_PAY_AMOUNT_ERROR)
                + MessageUtils.get(OrderNameErrorCode.ERROR_PROMPT_ACTUAL_SHOULD_BE) + actualPrice;
            throw OrderException.buildException(msg);
        }
        orderCustomerCostDO.setOrderPromotionAmount(orderDiyCmd.getOrderPromotionAmount());
        orderCustomerCostDO.setGoodsPromotionAmount(orderDiyCmd.getGoodsPromotionAmount());
        orderCustomerCostDO.setDistributionAmount(orderDiyCmd.getDistributionAmount());
        // orderAmount和payAmount一样的
        orderCustomerCostDO.setPayAmount(orderDiyCmd.getPayAmount());
        orderCustomerCostDO.setCustomerId(orderDiyCmd.getCustomerId());
        orderCustomerCostDO.setGoodsAmount(orderDiyCmd.getGoodsAmount());
        orderCustomerCostDO.setPaidAmount(BigDecimal.ZERO);
        return orderCustomerCostDO;
    }

    private static OrderDistributorCostDO validDistributorOrderAmount(OrderDiyCmd orderDiyCmd) {
        List<OrderDetailDiyDTO> diyDetailList = orderDiyCmd.getDiyDetailList();
        if (orderDiyCmd.getCustomerId() != null) {
            return null;
        }
        OrderDistributorCostDO orderDistributorCostDO = new OrderDistributorCostDO();
        // 订单实际支付总金额
        BigDecimal actualPrice = BigDecimal.ZERO;

        for (int x = 0; x < diyDetailList.size(); x++) {
            BigDecimal itemAmount = diyDetailList.get(x).getActualPrice()
                .multiply(new BigDecimal(String.valueOf(diyDetailList.get(x).getItemCount())));
            actualPrice = actualPrice.add(itemAmount);

        }
        if (actualPrice.add(orderDiyCmd.getDistributionAmount()).compareTo(orderDiyCmd.getPayAmount()) != 0) {
            // 订单金额计算错误
            String msg = MessageUtils.get(OrderInfoErrorCode.ORDER_PAY_AMOUNT_ERROR)
                + MessageUtils.get(OrderNameErrorCode.ERROR_PROMPT_ACTUAL_SHOULD_BE) + actualPrice;
            throw OrderException.buildException(msg);
        }
        orderDistributorCostDO.setOrderPromotionAmount(orderDiyCmd.getOrderPromotionAmount());
        orderDistributorCostDO.setGoodsPromotionAmount(orderDiyCmd.getGoodsPromotionAmount());
        orderDistributorCostDO.setDistributionAmount(orderDiyCmd.getDistributionAmount());
        // orderAmount和payAmount一样的
        orderDistributorCostDO.setPayAmount(orderDiyCmd.getPayAmount());
        orderDistributorCostDO.setGoodsAmount(orderDiyCmd.getGoodsAmount());
        orderDistributorCostDO.setPaidAmount(BigDecimal.ZERO);
        return orderDistributorCostDO;
    }

    /**
     * 检查订单扩展数据
     *
     * @param orderExtendDataDO
     */
    public static void checkOrderExtendDataDO(OrderExtendDataDO orderExtendDataDO) {
        if (orderExtendDataDO == null) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_NULL, MessageUtils.get(CommonErrorCode.B_ORDER_NULL));
        }
    }

    /**
     * 检查是否第三方订单
     *
     * @param orderExtendDataDO
     */
    public static void checkThirdpartyOrderCancelOrder(OrderExtendDataDO orderExtendDataDO) {
        if (orderExtendDataDO == null) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_NULL, MessageUtils.get(CommonErrorCode.B_ORDER_NULL));
        }
        // 第三方暂时不控制
        // if (StringUtils.isNotBlank(orderExtendDataDO.getOrderThirdpartyNo())) {
        // throw OrderException.buildException(B_ORDER_CANCEL_THIRDPARTY, MessageUtils.get(B_ORDER_CANCEL_THIRDPARTY));
        // }
    }

    /**
     * 检查取消订单
     * 
     * @param order
     */
    public static void checkCancelOrder(OrderInfoDO order) {
        if (order == null) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_NULL, MessageUtils.get(CommonErrorCode.B_ORDER_NULL));
        }
        // if (order.getDeliverStatus().equals(DeliverStatus.Delivering.getValue())) {
        // throw OrderException.buildException(B_ORDER_CANCEL_DELIVERING, MessageUtils.get(B_ORDER_CANCEL_DELIVERING));
        // }
        if (order.getDeliverStatus().equals(DeliverStatus.Delivered.getValue())) {
            throw OrderException.buildException(CommonErrorCode.B_ORDER_CANCEL_DELIVERED, MessageUtils.get(CommonErrorCode.B_ORDER_CANCEL_DELIVERED));
        }
    }
}
