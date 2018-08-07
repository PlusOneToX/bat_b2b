package com.bat.order.service.third.factory.validator;

import static com.alibaba.nacos.client.utils.EnvUtil.LOGGER;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.order.service.common.constant.OrderInfoConstant;
import com.bat.order.service.common.error.OrderCommonErrorCode;
import com.bat.order.service.common.error.OrderInfoErrorCode;
import com.bat.order.service.common.error.OrderNameErrorCode;
import com.bat.order.service.third.factory.executor.FactoryQryExe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.flexible.label.dto.LabelDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.picture.dto.PictureDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.dao.cost.dataobject.OrderGoodsDistributorCostDO;
import com.bat.order.dao.data.dataobject.OrderDistributorDataDO;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.deliver.dataobject.OrderDeliveryDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: 验证器 伪装饰者 包装一层 校验数据
 * @date 2018/05/29 14:14
 */
@Component
@Slf4j
public class FactoryValidatorQryExe {
    @Resource
    private FactoryQryExe factoryQryExe;

    public OrderInfoDO getOrderInfo(Integer orderId) {
        OrderInfoDO orderInfoDO = factoryQryExe.getOrderInfo(orderId);
        if (orderInfoDO == null) {
            throw OrderException.buildException(MessageUtils.get(OrderNameErrorCode.ERROR_NAME_ORDER)
                + MessageUtils.get(OrderCommonErrorCode.COMMON_ID_ERROR));
        }
        return orderInfoDO;
    }

    public OrderDistributorDataDO getOrderDistributorData(Integer orderId) {
        OrderDistributorDataDO orderDistributorDataDO = factoryQryExe.getOrderDistributorData(orderId);
        if (OrderInfoConstant.ORDER_STATUS_CANCEL.equals(orderDistributorDataDO.getOrderStatus())) {
            // 订单已取消
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_STATUS_HAS_BEEN_CANCEL);
        }
        if (OrderInfoConstant.ORDER_STATUS_COMPLETED.equals(orderDistributorDataDO.getOrderStatus())) {
            // 订单已完成
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_STATUS_HAS_BEEN_COMPLETED);
        }
        if (OrderInfoConstant.ORDER_STATUS_UN_CONFIRM.equals(orderDistributorDataDO.getOrderStatus())) {
            // 订单待确认
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_STATUS_HAS_NO_CONFIRM);
        }
        return orderDistributorDataDO;
    }

    public Map<Integer, OrderGoodsDO> getOrderGoodsMap(Integer id) {
        List<OrderGoodsDO> orderGoodsDOS = factoryQryExe.listOrderGoods(id);
        return orderGoodsDOS.stream().collect(Collectors.toMap(OrderGoodsDO::getId, orderGoodsDO -> orderGoodsDO));
    }

    /**
     * 获取定制信息 并校验 如果没有标签 重新生成标签
     * 
     * @param id
     * @param factoryNo
     * @return
     */
    public List<OrderGoodsDiyDO> listOrderGoodsDiy(Integer id, String factoryNo) {
        List<OrderGoodsDiyDO> orderGoodsDiyDOS = factoryQryExe.listOrderGoodsDiy(id);
        if (CollectionUtils.isEmpty(orderGoodsDiyDOS)) {
            throw OrderException.buildException("此订单商品信息异常");
        }
        for (OrderGoodsDiyDO orderGoodsDiyDO : orderGoodsDiyDOS) {
            Integer orderId = orderGoodsDiyDO.getOrderId();
            if (StringUtils.isBlank(orderGoodsDiyDO.getPreviewImage())) {
                String errorMsg = "订单号【" + orderId + "】预览图url不能为空";
                log.error(errorMsg);
                throw OrderException.buildException(errorMsg);
            }
            if (StringUtils.isBlank(orderGoodsDiyDO.getGenerateImage())) {
                String errorMsg = "订单号【" + orderId + "】打印图url不能为空";
                log.error(errorMsg);
                throw OrderException.buildException(errorMsg);
            }
            if (FactoryEnum.MK.name().toUpperCase().equals(factoryNo)) {
                if (StringUtils.isBlank(orderGoodsDiyDO.getLabelUrl())) {
                    boolean result = factoryQryExe.retryCreateLabel(FactoryEnum.MK.name(), orderGoodsDiyDOS);
                    if (!result) {
                        String errorMsg = "麦克订单号【" + orderId + "】标签url不能为空";
                        log.error(errorMsg + ",进过重试任然失败");
                        throw OrderException.buildException(errorMsg);
                    }
                }
                if (!orderGoodsDiyDO.getGenerateImage().endsWith(".pdf")) {
                    String errorMsg = "麦克订单号【" + orderId + "】打印图url格式非pdf";
                    log.error(errorMsg);
                    throw OrderException.buildException(errorMsg);
                }
            } else if (FactoryEnum.DH.name().toUpperCase().equals(factoryNo)) {
                if (StringUtils.isBlank(orderGoodsDiyDO.getLabelUrl())) {
                    boolean result = factoryQryExe.retryCreateLabel(FactoryEnum.MK.name(), orderGoodsDiyDOS);
                    if (!result) {
                        String errorMsg = "多鸿订单号【" + orderId + "】标签url为空";
                        log.error(errorMsg + ",进过重试任然失败");
                    }
                }
                if (!orderGoodsDiyDO.getGenerateImage().endsWith(".png")) {
                    String errorMsg = "多鸿订单号【" + orderId + "】打印图url格式非png";
                    log.error(errorMsg);
                    throw OrderException.buildException(errorMsg);
                }
            } else if (FactoryEnum.LHW.name().toUpperCase().equals(factoryNo)) {
                if (StringUtils.isBlank(orderGoodsDiyDO.getLabelUrl())) {
                    boolean result = factoryQryExe.retryCreateLabel(FactoryEnum.LHW.name(), orderGoodsDiyDOS);
                    if (!result) {
                        String errorMsg = "海星订单号【" + orderId + "】标签url不能为空";
                        log.error(errorMsg + ",进过重试任然失败");
                        throw OrderException.buildException(errorMsg);
                    }
                }
                if (!orderGoodsDiyDO.getGenerateImage().endsWith(".png")) {
                    String errorMsg = "海星订单号【" + orderId + "】打印图url格式非png";
                    log.error(errorMsg);
                    throw OrderException.buildException(errorMsg);
                }
            }
        }
        return orderGoodsDiyDOS;
    }

    public ModelMaterialRelevanceDTORpcQry getModelMaterialRelevanceByModelIdAndMaterialId(
        Map<String, ModelMaterialRelevanceDTORpcQry> relevanceDTORpcQryMap, Integer modelId, Integer materialId) {
        return factoryQryExe.getModelMaterialRelevanceByModelIdAndMaterialId(relevanceDTORpcQryMap, modelId,
            materialId);
    }

    public LabelDTORpcQry getLabelByModelIdAndMaterialId(OrderDistributorDataDO aDo, OrderGoodsDiyDO orderGoodsDiyDO,
        String factoryNo) {
        List<LabelDTORpcQry> labels = factoryQryExe.listLabelByModelIdAndMaterialId(aDo.getDistributorId(),
            orderGoodsDiyDO.getCategoryId(), orderGoodsDiyDO.getPictureId());
        Integer orderId = aDo.getOrderId();
        if (factoryNo.startsWith(FactoryEnum.DH.name())) {
            LabelDTORpcQry rpcQry =
                labels.stream().filter(label -> StringUtils.isNotBlank(label.getThirdSkuNo())).findFirst().orElse(null);
            if (rpcQry == null) {
                String labelNames = labels.stream().map(LabelDTORpcQry::getName).collect(Collectors.joining(","));
                List<Integer> labelIds = labels.stream().map(LabelDTORpcQry::getId).collect(Collectors.toList());
                log.error("标签id集合：{}", labelIds);
                String errorMsg = "订单号【" + orderId + "】标签【" + labelNames + "】均不存在编码，同步多鸿系统必传！";
                log.error(errorMsg + "，现在改成不必传");
                // throw OrderException.buildException(errorMsg);
                return null;
            }
            return rpcQry;
        }
        return labels.get(0);
    }

    public OrderDeliveryDO getOrderDelivery(Integer id, String factoryCode) {
        OrderDeliveryDO orderDeliveryDO = factoryQryExe.getOrderDelivery(id);
        // 检查省市区
        if (orderDeliveryDO.getCountryName().contains("中国")) {
            // 检查国内订单省市区名称
            Boolean changeName = false;
            if (StringUtils.isBlank(orderDeliveryDO.getProvinceName()) && orderDeliveryDO.getProvinceId() != null) {
                RegionRpcDTO region = factoryQryExe.getRegionById(orderDeliveryDO.getProvinceId());
                if (region != null) {
                    if (!region.getRegionName().equals(orderDeliveryDO.getProvinceName())) {
                        orderDeliveryDO.setProvinceName(region.getRegionName());
                        changeName = true;
                    }
                }
            }
            if (StringUtils.isBlank(orderDeliveryDO.getProvinceName())) {
                LOGGER.error("订单{}缺少省信息", orderDeliveryDO.getOrderId());
                throw OrderException.buildException("订单缺少省信息");
            }
            if (StringUtils.isBlank(orderDeliveryDO.getCityName()) && orderDeliveryDO.getCityId() != null) {
                RegionRpcDTO region = factoryQryExe.getRegionById(orderDeliveryDO.getCityId());
                if (region != null) {
                    if (!region.getRegionName().equals(orderDeliveryDO.getCityName())) {
                        orderDeliveryDO.setCityName(region.getRegionName());
                        changeName = true;
                    }
                }
            }
            if (StringUtils.isBlank(orderDeliveryDO.getCityName())) {
                LOGGER.error("订单{}缺少市信息", orderDeliveryDO.getOrderId());
                throw OrderException.buildException("订单缺少市信息");
            }
            // 可能会存在没有区的情况
            try {
                if (StringUtils.isBlank(orderDeliveryDO.getDistrictName()) && orderDeliveryDO.getDistrictId() != null) {
                    RegionRpcDTO region = factoryQryExe.getRegionById(orderDeliveryDO.getDistrictId());
                    if (region != null) {
                        if (!region.getRegionName().equals(orderDeliveryDO.getDistrictName())) {
                            orderDeliveryDO.setDistrictName(region.getRegionName());
                            changeName = true;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            if (FactoryEnum.MK.name().toUpperCase().equals(factoryCode)) {
                if (StringUtils.isBlank(orderDeliveryDO.getDistrictName())) {
                    LOGGER.error("订单{}缺少区信息 使用市信息填充", orderDeliveryDO.getOrderId());
                    // 麦克工厂 确实区信息 用市信息填充
                    orderDeliveryDO.setDistrictName(orderDeliveryDO.getCityName());
                    // throw OrderException.buildException("订单缺少区信息");
                }
            }
            if (changeName) {
                factoryQryExe.updateOrderDelivery(orderDeliveryDO);
            }
        }
        return orderDeliveryDO;
    }

    public OrderExtendDataDO getOrderExtendData(Integer id) {
        OrderExtendDataDO orderExtendDataDO = factoryQryExe.getOrderExtendData(id);
        if (StringUtils.isNotBlank(orderExtendDataDO.getOrderFactoryNo())) {
            // 已同步工厂
            throw OrderException.buildException(OrderInfoErrorCode.ORDER_OTHER_FACTORYNO_EXIST);
        }
        return orderExtendDataDO;
    }

    public List<OrderGoodsDO> listOrderGoods(Integer id) {
        List<OrderGoodsDO> orderGoodsDOS = factoryQryExe.listOrderGoods(id);
        if (CollectionUtils.isEmpty(orderGoodsDOS)) {
            throw OrderException.buildException("此订单商品信息异常");
        }
        return orderGoodsDOS;
    }

    public OrderDistributorDataDO getFirstTreeNode(Integer orderId) {
        return factoryQryExe.getFirstTreeNode(orderId);
    }

    public List<OrderGoodsDistributorCostDO> listByOrderIdAndDistributorId(Integer orderId, Integer distributorId) {
        return factoryQryExe.listByOrderIdAndDistributorId(orderId, distributorId);
    }

    public PictureDTORpcQry getPictureById(Integer pictureId) {
        PictureDTORpcQry rpcQry = new PictureDTORpcQry();
        rpcQry.setName("");
        rpcQry.setEnglishName("");
        rpcQry.setCode("");
        try {
            PictureDTORpcQry picture = factoryQryExe.getPictureById(pictureId);
            if (picture != null) {
                if (picture.getName() != null) {
                    rpcQry.setName(picture.getName());
                }
                if (picture.getEnglishName() != null) {
                    rpcQry.setEnglishName(picture.getEnglishName());
                }
                if (picture.getCode() != null) {
                    rpcQry.setCode(picture.getCode());
                }
            }
        } catch (Exception e) {
            log.info("获取图片信息出现异常:{}", e.getMessage());
        }
        return rpcQry;
    }

    public GoodsItemRpcDTO goodsItemByCode(String itemCode) {
        try {
            if (StringUtils.isNotBlank(itemCode)) {
                GoodsItemRpcDTO goodsItemRpcDTO = factoryQryExe.goodsItemByCode(itemCode);
                if (goodsItemRpcDTO != null) {
                    return goodsItemRpcDTO;
                }
            }
        } catch (Exception e) {
            log.info("获取货品信息出现异常:{}", e.getMessage());
        }
        GoodsItemRpcDTO goodsItemRpcDTO = new GoodsItemRpcDTO();
        goodsItemRpcDTO.setItemNameEn("");
        return goodsItemRpcDTO;
    }

    public LogisticsRpcDTO getLogisticsByDistributionId(Integer distributionId){
        return factoryQryExe.getLogisticsByDistributionId(distributionId);
    }
}
