package com.bat.order.service.importOrder.executor;

import static com.bat.order.service.common.Constant.*;
import static com.bat.order.service.common.error.ImportOrderErrorCode.*;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorERPRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorPromitonGroupSeckillRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.distributor.platform.api.PlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;
import com.bat.dubboapi.financial.basesetting.api.FinancialCurrencyServiceRpc;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRateRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.goods.goods.dto.UserGoodsItemListRpcQry;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsRpcDTO;
import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.promotion.dto.GoodsItemPromotionPriceRpcQry;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.dubboapi.system.storesetting.api.SystemGlobeShopSettingServiceRpc;
import com.bat.dubboapi.system.storesetting.dto.ShopSettingRpcDTO;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemInventorySummaryRpcDTO;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.order.dto.common.OrderTypeListQry;
import com.bat.order.api.order.dto.distributor.OrderGoodsCmd;
import com.bat.order.api.order.dto.distributor.OrderInfoCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.dao.importOrder.dataobject.ImportOrderDO;
import com.bat.order.dao.importOrder.dataobject.ImportOrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.CommonValidator;
import com.bat.order.service.common.data.dto.OrderGoodsDTO;
import com.bat.order.service.order.convertor.OrderDistributorConvertor;
import com.bat.order.service.order.executor.OrderRpcExe;
import com.bat.order.service.order.executor.OrderTypeQryExe;
import com.bat.order.service.order.executor.distributor.UserDistributorOrderCmdExe;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: ImportOrderCmd 的零散方法 包含RPC调用
 * @date 2018/05/12 16:38
 */
@Service
@Slf4j
public class ImportOrderRpcCmdExe {

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private FinancialCurrencyServiceRpc financialCurrencyServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private SystemRegionServiceRpc systemRegionServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private SystemGlobeShopSettingServiceRpc systemGlobeShopSettingServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private PromotionServiceDistributorRpc promotionServiceDistributorRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @DubboReference(check = false, timeout = 30000, retries = 0)
    private PlatformServiceRpc platformServiceRpc;

    @Resource
    private CommonValidator commonValidator;

    @Resource
    private OrderRpcExe orderRpcExe;

    @Resource
    private OrderTypeQryExe orderTypeQryExe;

    @Resource
    private UserDistributorOrderCmdExe cmdExe;

    public List<GoodsItemPromotionPriceRpcDTO>
        goodsItemPromotionPriceDistributor(GoodsItemPromotionPriceRpcQry toGoodsItemPromotionPriceRpcQry) {
        List<GoodsItemPromotionPriceRpcDTO> priceRpcDTOS =
            orderRpcExe.goodsItemPromotionPriceDistributor(toGoodsItemPromotionPriceRpcQry);
        return priceRpcDTOS;
    }

    /**
     * 计算促销价
     * 
     * @param promotionRpcDTO
     * @param goodsItemPriceRpcDTO
     * @param goodsItemRpcDTO
     * @param distributorId
     * @param importOrderGoods
     * @return
     */
    public GoodsItemPromotionPriceRpcDTO calcGoodsItemPromotionPrice(GoodsItemPromotionRpcDTO promotionRpcDTO,
        GoodsItemPriceRpcDTO goodsItemPriceRpcDTO, GoodsItemRpcDTO goodsItemRpcDTO, Integer distributorId,
        ImportOrderGoodsDO importOrderGoods) {
        BigDecimal salePrice = goodsItemPriceRpcDTO.getSalePrice();
        if (promotionRpcDTO != null) {
            List<PromotionRpcDTO> promotions = promotionRpcDTO.getPromotions();
            if (!CollectionUtils.isEmpty(promotions)) {
                for (PromotionRpcDTO promotion : promotions) {
                    if (promotion == null) {
                        continue;
                    }
                    for (PromotionRuleRpcDTO rule : promotion.getRules()) {
                        if (rule == null) {
                            continue;
                        }
                        // 按照金额 或 数量 倒序 for循环时处理
                        Short moneyOrCount = rule.getMoneyOrCount();
                        if (moneyOrCount.equals(MONEY)) {
                            rule.getConditions()
                                .sort(Comparator.comparing(PromotionRuleConditionRpcDTO::getOneBuyMoney).reversed());
                        } else if (moneyOrCount.equals(COUNT)) {
                            rule.getConditions()
                                .sort(Comparator.comparing(PromotionRuleConditionRpcDTO::getOneBuyCount).reversed());
                        }
                        for (PromotionRuleConditionRpcDTO condition : rule.getConditions()) {
                            if (condition == null) {
                                continue;
                            }
                            // 满赠的不考虑
                            if (condition.getReduceOrPresent().equals(CONDITION_PRESENT)) {
                                continue;
                            }
                            if (moneyOrCount.equals(MONEY)) {
                                BigDecimal money =
                                    salePrice.multiply(BigDecimal.valueOf(importOrderGoods.getActualOrderCount()));
                                if (money.compareTo(condition.getOneBuyMoney()) < 1) {
                                    continue;
                                }
                            } else if (moneyOrCount.equals(COUNT)) {
                                if (importOrderGoods.getActualOrderCount() < condition.getOneBuyCount()) {
                                    continue;
                                }
                            }
                            // 计算商品活动价格(最终价格)
                            Map<String, GoodsItemRpcDTO> goodsItemRpcDTOMap = new HashMap<>();
                            goodsItemRpcDTOMap.put(goodsItemRpcDTO.getItemCode(), goodsItemRpcDTO);
                            OrderGoodsDTO dto = new OrderGoodsDTO();
                            dto.setGoodsPromotionId(condition.getId());
                            dto.setItemCode(importOrderGoods.getItemCode());
                            dto.setItemCount(importOrderGoods.getActualOrderCount());
                            dto.setItemInCount(importOrderGoods.getItemCount());
                            dto.setItemType(ITEM_TYPE_1);
                            List<OrderGoodsDTO> goodss = Collections.singletonList(dto);
                            List<GoodsItemPromotionPriceRpcDTO> goodsItemPrices;
                            try {
                                goodsItemPrices = goodsItemPromotionPriceDistributor(
                                    OrderDistributorConvertor.toGoodsItemPromotionPriceRpcQry(distributorId, new Date(),
                                        goodss, Collections.singletonList(goodsItemPriceRpcDTO), goodsItemRpcDTOMap));
                            } catch (Exception e) {
                                // 哎呀！活动已结束或未开始，商品将按原价计算，是否继续下单购买呢！
                                continue;
                            }
                            if (goodsItemPrices != null && goodsItemPrices.size() == 1) {
                                return goodsItemPrices.get(0);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 查找该分销商 货品所对应的 促销价格
     *
     * @param itemRpcs
     * @param distributorId
     * @return
     */
    public Map<Integer, GoodsItemPromotionRpcDTO> getPromotionPriceMap(List<GoodsItemRpcQry> itemRpcs,
        Integer distributorId) {
        com.bat.dubboapi.promotion.common.Response<List<GoodsItemPromotionRpcDTO>> response =
            promotionServiceDistributorRpc.goodsItemPromotion(itemRpcs, distributorId, PRESENT_FLAG_0);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            List<GoodsItemPromotionRpcDTO> data = response.getData();
            if (CollectionUtils.isEmpty(data)) {
                return new HashMap<>();
            }
            // 去除促销中的满赠（暂时）
            // 去除因为上述变动导致的空集合
            log.info("getPromotionPriceMap json:{}", JSON.toJSONString(data));
            return data.stream().collect(Collectors.toMap(GoodsItemPromotionRpcDTO::getId,
                goodsItemPromotionRpcDTO -> goodsItemPromotionRpcDTO));
        }

    }

    /**
     * 获取单个分销商信息 并校验
     * 
     * @param distributorIds
     * @return
     */
    public DistributorRpcDTO getDistributorInfo(Integer distributorIds) {
        return commonValidator.checkDistributorValidity(distributorIds);
    }

    /**
     * 获取分销商信息 并校验
     *
     * @param distributorIds
     * @return
     */
    public List<DistributorRpcDTO> getDistributorInfos(List<Integer> distributorIds) {
        return commonValidator.checkDistributorValidity(distributorIds);
    }

    /**
     * 获取分销商信息 并校验
     *
     * @param distributorIds
     * @return
     */
    public Map<Integer, DistributorRpcDTO> getDistributorInfosMap(List<Integer> distributorIds) {
        log.info("getDistributorInfosMap:{}", distributorIds);
        return getDistributorInfos(distributorIds).stream().collect(Collectors.toMap(DistributorRpcDTO::getId, k -> k));
    }

    /**
     * 查询同一序号下 同一分销商 的所有货品的分销商价格(一次获取)
     *
     * @param orderGoods
     * @param distributorId
     * @return
     */
    public Map<Integer, GoodsItemPriceRpcDTO> getDistributorPriceMap(List<ImportOrderGoodsDO> orderGoods,
        Integer distributorId) {
        log.info("getDistributorPriceMap=====================");
        log.info("distributorId:{}", distributorId);
        log.info("orderGoods:{}", JSON.toJSONString(orderGoods));
        List<GoodsItemRpc> itemRpcs = orderGoods.stream().map(importOrderGoodsDO -> {
            GoodsItemRpc rpc = new GoodsItemRpc();
            rpc.setItemId(importOrderGoodsDO.getItemId());
            rpc.setGoodsId(importOrderGoodsDO.getGoodsId());
            return rpc;
        }).collect(Collectors.toList());
        GoodsItemPriceRpcQry qry = new GoodsItemPriceRpcQry();
        qry.setDistributorId(distributorId);
        qry.setGoodsItems(itemRpcs);
        qry.setRetailPriceFlag((short)0);
        List<GoodsItemPriceRpcDTO> data = orderRpcExe.listDistributorGoodsItemPrice(qry);
        Map<Integer, GoodsItemPriceRpcDTO> collect = data.stream()
            .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, goodsItemPriceRpcDTO -> goodsItemPriceRpcDTO));
        log.info("collect:{}", JSON.toJSONString(collect));
        return collect;
    }

    /**
     * 获取 物流 不做省市区校验
     * 
     * @return
     */
    public Map<String, LogisticsRpcDTO> initLogisticsMap() {
        LogisticsRpcQry qry = new LogisticsRpcQry();
        List<LogisticsRpcDTO> data = getLogisticsByParams(qry);
        return data.stream().collect(Collectors.toMap(LogisticsRpcDTO::getName, logisticsRpcDTO -> logisticsRpcDTO));
    }

    public List<LogisticsRpcDTO> getLogisticsByParams(LogisticsRpcQry qry) {
        qry.setEnable((short)1);
        com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> response =
            systemLogisticsServiceRpc.listLogisticsFromRpcByParams(qry);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    /**
     * 获取币别信息
     * 
     * @param currencyTypes
     * @return
     */
    public Map<String, CurrencyRpcDO> getCurrencyInfoMap(List<String> currencyTypes) {
        Map<String, CurrencyRpcDO> map = new HashMap<>();
        for (String currencyType : currencyTypes) {
            Response<CurrencyRpcDO> response = financialCurrencyServiceRpc.getCurrencyByCurrencyCode(currencyType);
            if (!response.isSuccess()) {
                throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
            } else {
                CurrencyRpcDO data = response.getData();
                map.put(data.getCurrencyCode(), data);
            }
        }
        return map;
    }

    /**
     * 获取汇率
     * 
     * @param currencyTypes
     * @return
     */
    public Map<String, BigDecimal> getRateMap(List<String> currencyTypes) {
        Map<String, BigDecimal> map = new HashMap<>();
        for (String currencyType : currencyTypes) {
            if ("CNY".equals(currencyType)) {
                map.put("CNY-CNY", BigDecimal.ONE);
            } else {
                Response<CurrencyRateRpcDTO> response =
                    financialCurrencyServiceRpc.getCurrencyRate(currencyType, "CNY");
                if (!response.isSuccess()) {
                    throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
                } else {
                    CurrencyRateRpcDTO data = response.getData();
                    map.put(currencyType + "-CNY", data.getExchangeRate());
                }
            }
        }
        return map;
    }

    /**
     * 获取省市区 校验
     *
     * @param regionMap
     * @param regionName
     * @param parentId
     * @param line
     * @return
     */
    public RegionRpcDTO getRegionRpcDTO(Map<String, RegionRpcDTO> regionMap, String regionName, Integer parentId,
        int line) {
        RegionRpcDTO regionRpcDTO = regionMap.get(regionName);
        if (regionRpcDTO == null) {
            com.bat.dubboapi.system.common.Response<List<RegionRpcDTO>> listResponse =
                systemRegionServiceRpc.listRegionByParentIdAndRegionName(parentId, regionName);
            if (listResponse.isSuccess() && listResponse.getData().size() == 1) {
                regionRpcDTO = listResponse.getData().get(0);
                regionMap.put(regionName, regionRpcDTO);
            } else {
                log.error("parentId:{},regionName:{}", parentId, regionName);
                if (listResponse.getErrCode() != null) {
                    throw OrderException.buildLineException(listResponse.getErrCode(), listResponse.getErrMessage(),
                        line);
                } else {
                    throw OrderException.buildLineException(B_REGION_NOT_EXITS,
                        regionName + ":" + MessageUtils.get(B_REGION_NOT_EXITS), line);
                }
            }
        }
        return regionRpcDTO;
    }

    public List<OrderTypeDTO> getOrderTypeDTOList() {
        OrderTypeListQry qry = new OrderTypeListQry();
        qry.setPage(1);
        qry.setSize(10000);
        return orderTypeQryExe.listOrderType(qry).getList();
    }

    public Map<Short, OrderTypeDTO> getOrderTypeDTOMap() {
        Map<Short, OrderTypeDTO> orderTypeMap;
        List<OrderTypeDTO> dtos = getOrderTypeDTOList();
        orderTypeMap = dtos.stream()
            .collect(Collectors.toMap(OrderTypeDTO::getSpecialFlag, orderTypeDTO -> orderTypeDTO, (t, t2) -> t));
        return orderTypeMap;
    }

    /**
     * 检查分销商可视
     * 
     * @param line
     * @param importOrderDO
     * @param goodsItemRpcDTO
     */
    public void validDistributor(int line, ImportOrderDO importOrderDO, GoodsItemRpcDTO goodsItemRpcDTO) {
        UserGoodsItemListRpcQry qry = new UserGoodsItemListRpcQry();
        qry.setDistributorId(importOrderDO.getDistributorId());
        qry.setItemIds(Collections.singletonList(goodsItemRpcDTO.getId()));
        qry.setPage(1);
        qry.setSize(10000);
        log.info("validDistributor qry json:{}", JSON.toJSONString(qry));
        com.bat.dubboapi.goods.common.Response<PageInfo<GoodsItemRpcDTO>> pageInfoResponse =
            goodsServiceRpc.listDistributorGoodsItem(qry);
        if (!pageInfoResponse.isSuccess()) {
            throw OrderException.buildLineException(pageInfoResponse.getErrCode(), pageInfoResponse.getErrMessage(),
                line);
        } else {
            if (CollectionUtils.isEmpty(pageInfoResponse.getData().getList())) {
                throw OrderException.buildLineException(B_GOODS_ITEM_DISTRIBUTOR_SCOPE_NULL, line);
            }
        }
    }

    /**
     * 货品的状态
     * 
     * @param line
     * @param orderTypeName
     * @param goodsItemRpcDTO
     * @param countSum
     */
    public void validGoodsItem(Integer line, String orderTypeName, GoodsItemRpcDTO goodsItemRpcDTO, Integer countSum,
        Map<Short, OrderTypeDTO> orderTypeMap) {
        if (goodsItemRpcDTO.getMoq() == null) {
            goodsItemRpcDTO.setMoq("0");
        }
        String diyOrderTypeName = orderTypeMap.get(SPECIAL_FLAG_4).getName();
        String autoDeliveryOrderTypeName = orderTypeMap.get(SPECIAL_FLAG_5).getName();
        String mtoOrderTypeName = orderTypeMap.get(SPECIAL_FLAG_2).getName();
        if (mtoOrderTypeName.equals(orderTypeName) || autoDeliveryOrderTypeName.equals(orderTypeName)) {
            // 检查 预售订单的购买数量是否达到预售最低数量
            if (mtoOrderTypeName.equals(orderTypeName)) {
                if (goodsItemRpcDTO.getAdvanceSaleFlag().equals(ADVANCE_SALE_DISABLE)) {
                    throw OrderException.buildLineException(B_PRE_SALE_GOODS_ITEM_NOT_SUPPORT_PRE_SALE_MODULE, line);
                }
                if (StringUtils.isNotBlank(goodsItemRpcDTO.getMoq()) && "停产".equals(goodsItemRpcDTO.getMoq())) {
                    throw OrderException.buildLineException(B_PRE_SALE_GOODS_ITEM_HAS_BEEN_DISCONTINUED, line);
                }
            }
            if (StringUtils.isBlank(goodsItemRpcDTO.getMoq()) || !StringUtils.isNumeric(goodsItemRpcDTO.getMoq())) {
                throw OrderException.buildLineException(B_PRE_SALE_GOODS_ITEM_MOQ_ILLEGAL, line);
            }
            // 校验拼团 TODO
            if (countSum < Integer.parseInt(goodsItemRpcDTO.getMoq())) {
                throw OrderException.buildLineException(B_ITEM_COUNT_LESS_THAN_MOQ, line);
            }
            if (countSum > 100000000L) {
                throw OrderException.buildLineException(B_ITEM_COUNT_MORE_THAN_100_MILLION, line);
            }
        }
        // 校验订单类型
        if (goodsItemRpcDTO.getGoodsType() == 1) {
            if (diyOrderTypeName.equals(orderTypeName)) {
                throw OrderException.buildLineException(B_ORDER_TYPE_IS_INCONSISTENT_WITH_THE_GOODS_ITEM_TYPE, line);
            }
        } else if (goodsItemRpcDTO.getGoodsType() == 2) {
            if (!diyOrderTypeName.equals(orderTypeName)) {
                throw OrderException.buildLineException(B_ORDER_TYPE_IS_INCONSISTENT_WITH_THE_GOODS_ITEM_TYPE, line);
            }
        }
    }

    /**
     * 校验货品有效性(存在 并且是已上架状态) 并且返回 货品编码与对应的实体，一次查完避免频繁查询
     *
     * @param itemCodes
     * @return
     */
    public List<GoodsItemRpcDTO> validGoodsItemAndGetList(List<String> itemCodes) {
        return validGoodsItemAndGetList(itemCodes, false);
    }

    public List<GoodsItemRpcDTO> validGoodsItemAndGetList(List<String> itemCodes, boolean throwException) {
        // 没有使用已有的接口 主要是为了加上行号 以及有可能有漏的 打印提示
        List<GoodsItemRpcDTO> data = listGoodsItemByCodes(itemCodes);
        for (int i = 0; i < itemCodes.size(); i++) {
            String itemCode = itemCodes.get(i);
            GoodsItemRpcDTO goodsItemRpcDTO =
                data.stream().filter(rpcDTO -> rpcDTO.getItemCode().equals(itemCode)).findFirst().orElse(null);
            if (goodsItemRpcDTO == null) {
                String msg = "Row:{0},Code:{1}," + MessageUtils.get(B_GOODS_ITEM_IS_NOT_EXITS);
                throw OrderException.buildException(B_GOODS_ITEM_IS_NOT_EXITS,
                    MessageFormat.format(msg, i + 2, itemCode));
            } else {
                // 之前代码没有
                if (!goodsItemRpcDTO.getSaleStatus().equals(SALE_STATUS_3)) {
                    log.error("货品非在售状态 itemCode:{},goodsItemRpcDTO:{}", itemCode, JSON.toJSONString(goodsItemRpcDTO));
                    if (throwException) {
                        throw OrderException.buildLineException(B_GOODS_ITEM_STATUS_NOT_ON_SALE,
                            itemCode + ":" + MessageUtils.get(B_GOODS_ITEM_STATUS_NOT_ON_SALE), (i + 2));
                    }
                }
            }
        }
        return data;
    }

    public List<GoodsItemRpcDTO> listGoodsItemByCodes(List<String> itemCodes) {
        // 没有使用已有的接口 主要是为了加上行号 以及有可能有漏的 打印提示
        com.bat.dubboapi.goods.common.Response<List<GoodsItemRpcDTO>> response =
            goodsServiceRpc.listGoodsItemByCodes(itemCodes);
        // log.info("listGoodsItemByCodes json:{}", JSON.toJSONString(response));
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public List<GoodsRpcDTO> findGoodsByIds(List<Integer> goodsIds) {
        com.bat.dubboapi.goods.common.Response<List<GoodsRpcDTO>> response = goodsServiceRpc.listGoods(goodsIds);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    /**
     * 在库存服务总 会去查销售区域 会去查直发用户 以及 货品是否支持直发在途
     * 
     * @param itemIds
     * @param distributorId
     * @return
     */
    public List<GoodsItemInventorySummaryRpcDTO> findItemStocks(List<Integer> itemIds, Integer distributorId) {
        com.bat.dubboapi.warehouse.common.Response<List<GoodsItemInventorySummaryRpcDTO>> response =
            warehouseStockServiceRpc.summaryByItemIdListAndAreaIdListAndDistributorId(itemIds, null, distributorId,
                null);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    /**
     * 获取全局店铺配置
     * 
     * @return
     */
    public ShopSettingRpcDTO getSystemConfig() {
        com.bat.dubboapi.system.common.Response<ShopSettingRpcDTO> response =
            systemGlobeShopSettingServiceRpc.getSystemConfig();
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    /**
     * 获取平台编码
     * 
     * @return
     */
    public List<PlatformRpcDTO> getPlatform() {
        com.bat.dubboapi.distributor.common.Response<List<PlatformRpcDTO>> response =
            platformServiceRpc.listByOpenFlag((short)1);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public DistributorPromitonGroupSeckillRpcDTO getDistributorPromotionGroupSeckill(Integer id) {
        return distributorServiceRpc.getDistributorPromotionGroupSeckill(id);
    }

    public LogisticsCalculationRpcDTO getLogisticsCalculation(LogisticsCalculationRpcQry rpcQry) {
        return orderRpcExe.getLogisticsCalculation(rpcQry);
    }

    public DistributorERPRpcDTO getDistributorERPData(Integer distributorId) {
        com.bat.dubboapi.distributor.common.Response<DistributorERPRpcDTO> response =
            distributorServiceRpc.getDistributorERPData(distributorId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public RegionRpcDTO getRegionById(Integer regionId) {
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> response =
            systemRegionServiceRpc.getRegionById(regionId);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        } else {
            return response.getData();
        }
    }

    public List<OrderInfoDO> createOrder(OrderInfoCmd cmd, String userId, String userName, String contactId,
        String contactName, String platformNo, String language) {
        try {
            return cmdExe.createOrder(cmd, userId, userName, contactId, contactName, platformNo, language);
        } catch (OrderException e) {
            String matchStr = "库存不足、在库可下单量只有:";
            // 代码意义 是吧库存返回的错误信息中的中文名，转换为itemCode
            if (e.getMsg().contains(matchStr)) {
                // 在库库存不足、在库可下单量只有:
                // 在途库存不足、在途可下单量只有:
                List<String> itemCode =
                    cmd.getGoodss().stream().map(OrderGoodsCmd::getItemCode).collect(Collectors.toList());
                List<GoodsItemRpcDTO> itemRpcDTOS = listGoodsItemByCodes(itemCode);
                String itemName = e.getMsg().substring(0, e.getMsg().indexOf(matchStr) - 2);
                String errMsg = e.getMsg().substring(e.getMsg().indexOf(matchStr) - 2);
                GoodsItemRpcDTO goodsItemRpcDTO1 = itemRpcDTOS.stream()
                    .filter(goodsItemRpcDTO -> goodsItemRpcDTO.getItemName().equals(itemName)).findFirst().orElse(null);
                if (goodsItemRpcDTO1 != null) {
                    throw OrderException.buildException(goodsItemRpcDTO1.getItemCode() + errMsg);
                }
            }
            throw e;
        }
    }
}
