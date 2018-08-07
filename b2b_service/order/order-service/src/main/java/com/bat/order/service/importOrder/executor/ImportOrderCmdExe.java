package com.bat.order.service.importOrder.executor;

import static com.bat.order.service.common.Constant.*;
import static com.bat.order.service.common.error.ImportOrderErrorCode.*;
import static com.bat.order.service.common.error.OrderCommonErrorCode.ORDER_NULL;
import static com.bat.order.service.common.error.OrderInfoErrorCode.B_ORDER_NOT_EXIST;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.order.service.importOrder.dto.ImportOrderExcelDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorERPRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorFinancialRpcDO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorPromitonGroupSeckillRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionRpcDTO;
import com.bat.dubboapi.system.logistics.dto.LogisticsCalculationRpcQry;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsCalculationRpcDTO;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.dubboapi.system.storesetting.dto.ShopSettingRpcDTO;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemInventorySummaryRpcDTO;
import com.bat.order.api.basic.ErrorCode;
import com.bat.order.api.common.constant.PayWayEnum;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.api.importOrder.dto.*;
import com.bat.order.api.order.dto.common.OrderInvoiceCmd;
import com.bat.order.api.order.dto.distributor.OrderDeliveryCmd;
import com.bat.order.api.order.dto.distributor.OrderGoodsCmd;
import com.bat.order.api.order.dto.distributor.OrderInfoCmd;
import com.bat.order.api.order.dto.distributor.OrderLogisticsCmd;
import com.bat.order.api.order.dto.orderquery.common.OrderTypeDTO;
import com.bat.order.dao.cost.OrderDistributorCostMapper;
import com.bat.order.dao.cost.dataobject.OrderDistributorCostDO;
import com.bat.order.dao.data.OrderExtendDataMapper;
import com.bat.order.dao.data.dataobject.OrderExtendDataDO;
import com.bat.order.dao.importOrder.ImportOrderGoodsMapper;
import com.bat.order.dao.importOrder.ImportOrderMapper;
import com.bat.order.dao.importOrder.dataobject.ImportOrderDO;
import com.bat.order.dao.importOrder.dataobject.ImportOrderGoodsDO;
import com.bat.order.dao.order.dataobject.OrderInfoDO;
import com.bat.order.service.common.config.OrderConfig;
import com.bat.order.service.common.utils.ValidateUtils;
import com.bat.order.tenant.TenantContext;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/2 21:21
 */
@Component
@Slf4j
public class ImportOrderCmdExe {

    @Resource
    private ImportOrderMapper importOrderMapper;

    @Resource
    private ImportOrderGoodsMapper importOrderGoodsMapper;

    @Resource
    private ImportOrderRpcCmdExe importOrderRpcCmdExe;

    @Resource
    private OrderExtendDataMapper orderExtendDataMapper;

    @Resource
    private OrderDistributorCostMapper orderDistributorCostMapper;

    @Resource
    private OrderConfig orderConfig;

    @Transactional(rollbackFor = Exception.class)
    public void save(List<ImportOrderExcelDTO> list) {
        boolean diyFlag = StringUtils.isNotBlank(list.get(0).getDiyMaterialName());
        // 缓存 必要数据 map,避免频繁调用RPC 虽然dubbo有缓存功能，但是不要功能蔓延
        // 货币 key 货币类型
        List<String> currencyTypes =
            list.stream().map(ImportOrderExcelDTO::getCurrencyType).distinct().collect(toList());
        Map<String, CurrencyRpcDO> currencyMap = importOrderRpcCmdExe.getCurrencyInfoMap(currencyTypes);
        // 区域 key 区域名称
        Map<String, RegionRpcDTO> regionMap = new HashMap<>();
        // 分销商 key 分销商id
        List<Integer> distributorIds =
            list.stream().map(ImportOrderExcelDTO::getDistributorId).distinct().collect(toList());
        Map<Integer, DistributorRpcDTO> distributorMap = importOrderRpcCmdExe.getDistributorInfosMap(distributorIds);
        // 订单类型 key 订单类型名称
        Map<Short, OrderTypeDTO> orderTypeMap = importOrderRpcCmdExe.getOrderTypeDTOMap();;
        // 物流 key 物流名称
        Map<String, LogisticsRpcDTO> logisticsMap = importOrderRpcCmdExe.initLogisticsMap();
        // 汇率 key 原币-目标币
        Map<String, BigDecimal> currencyRateMap = importOrderRpcCmdExe.getRateMap(currencyTypes);
        // 货品 key 货品Id
        List<String> itemCodes = list.stream().map(ImportOrderExcelDTO::getItemCode).collect(toList());
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = importOrderRpcCmdExe.validGoodsItemAndGetList(itemCodes);
        Map<String, GoodsItemRpcDTO> itemMap = goodsItemRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getItemCode, goodsItemRpcDTO -> goodsItemRpcDTO));
        // 校验非空
        validExcelParams(diyFlag, list);
        // 最后保存的 importOrder 集合 importOrderGoods 集合
        List<ImportOrderDO> importOrders = new ArrayList<>();
        List<ImportOrderGoodsDO> importOrderGoodss = new ArrayList<>();
        // key 序号 value order 行项 value orderGoods 行项
        Map<Integer, ImportOrderDO> importOrderMap = new HashMap<>();
        Map<Integer, List<ImportOrderGoodsDO>> importOrderGoodsMap = new HashMap<>();
        // key 序号 Map key 货品id value orderGoods 行项(用来合并 同一序号下 同一货品)
        Map<Integer, Map<Integer, ImportOrderGoodsDO>> orderGoodsMapMap = new HashMap<>();
        // key 序号 value 货品总数
        Map<Integer, Integer> countAllMap = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            // 行号 用于日志报错
            int rowNum = i + 2;
            ImportOrderExcelDTO excelItemData = list.get(i);
            Integer orderNo = excelItemData.getSerialNumber();
            // 同序号订单
            ImportOrderDO importOrderDO = importOrderMap.get(orderNo);
            Integer countAll = countAllMap.get(orderNo);
            if (importOrderDO == null) {
                countAll = 0;
                importOrderDO = new ImportOrderDO();
                countAllMap.put(orderNo, countAll);
                DistributorRpcDTO distributorInfo = distributorMap.get(excelItemData.getDistributorId());
                importOrderDO.setErpDistributorNo(distributorInfo.getErpNo() + "");
                importOrderDO.setDistributorId(distributorInfo.getId());
                importOrderDO.setDistributorName(distributorInfo.getName());;
                importOrderDO.setCurrencyType(currencyMap.get(excelItemData.getCurrencyType()).getCurrencyCode());
                importOrderDO.setCurrencyRates(currencyRateMap.get(excelItemData.getCurrencyType()));
                extractedAddress(rowNum, regionMap, excelItemData, importOrderDO);
                extractedOrderType(orderTypeMap, diyFlag, excelItemData, importOrderDO);
                importOrderDO.setPayWay(getPayWayEnum(excelItemData, distributorInfo, rowNum).getPayStatus());
                LogisticsRpcDTO logisticsRpcDTO = logisticsMap.get(excelItemData.getLogisticsName());
                if (logisticsRpcDTO == null) {
                    throw OrderException.buildLineException(B_ORDER_LOGISTICS_ERROR, rowNum);
                }
                importOrderDO.setDistributionId(logisticsRpcDTO.getId());
                importOrderDO.setDistributionName(logisticsRpcDTO.getName());
                importOrderDO.setRemark(excelItemData.getRemark());
                importOrderDO.setHandleFlag(HANDLE_FLAG_2);
                Date date = new Date();
                importOrderDO.setCreateTime(date);
                importOrderDO.setUpdateTime(date);
                importOrderDO.setInvoiceType(INVOICE_TYPE_1);
                importOrderDO.setIsInvoice(INVOICE_FLAG_0);
                importOrderDO.setCountSum(0);
                importOrderDO.setSubmitStatus(SUBMIT_STATUS_1);
                if (excelItemData.getOnWaySplitFlag().equals(ON_WAY_SPLIT_FLAG_0)
                    || excelItemData.getOnWaySplitFlag().equals(ON_WAY_SPLIT_FLAG_1)) {
                    importOrderDO.setOrderSplitFlag(String.valueOf(excelItemData.getOnWaySplitFlag()));
                } else {
                    throw OrderException.buildLineException(B_ORDER_ON_WAY_SPLIT_FLAG_ERROR, rowNum);
                }
                importOrders.add(importOrderDO);
                importOrderMap.put(orderNo, importOrderDO);
            }
            GoodsItemRpcDTO goodsItem = itemMap.get(excelItemData.getItemCode());
            // 校验分销商是否可视 等等条件
            Integer countSum = excelItemData.getItemCount();
            // 检查分销商可视
            importOrderRpcCmdExe.validDistributor(rowNum, importOrderDO, goodsItem);
            importOrderRpcCmdExe.validGoodsItem(rowNum, importOrderDO.getOrderTypeName(), goodsItem, countSum,
                orderTypeMap);
            countAll += countSum;
            countAllMap.put(orderNo, countAll);
            // key 序号 value orderGoods 集合
            List<ImportOrderGoodsDO> orderGoods = importOrderGoodsMap.get(orderNo);
            // key 序号 value orderGoods 行项
            Map<Integer, ImportOrderGoodsDO> orderGoodsMap = orderGoodsMapMap.get(orderNo);
            if (orderGoods == null) {
                orderGoods = new ArrayList<>();
                orderGoodsMap = new HashMap<>();
                importOrderGoodsMap.put(orderNo, orderGoods);
                orderGoodsMapMap.put(orderNo, orderGoodsMap);
            }
            // 序号一样 货品编号一样时，合并
            ImportOrderGoodsDO importOrderGoods = orderGoodsMap.get(goodsItem.getId());
            if (importOrderGoods != null) {
                importOrderGoods.setItemCount(importOrderGoods.getItemCount() + countSum);
                importOrderGoods.setActualOrderCount(importOrderGoods.getActualOrderCount() + countSum);
            } else {
                importOrderGoods = new ImportOrderGoodsDO();
                importOrderGoods.setGoodsId(goodsItem.getGoodsId());
                importOrderGoods.setItemCode(goodsItem.getItemCode());
                importOrderGoods.setItemCount(countSum);
                importOrderGoods.setActualOrderCount(countSum);
                importOrderGoods.setInWarehouseCount(countSum);
                importOrderGoods.setOnWayCount(0);
                importOrderGoods.setItemLoseCount(0);
                importOrderGoods.setItemType(Integer.valueOf(ITEM_TYPE_1));
                importOrderGoods.setItemId(goodsItem.getId());
                importOrderGoods.setItemName(goodsItem.getItemName());
                importOrderGoods.setGoodsName(goodsItem.getGoodsName());
                importOrderGoods.setGoodsNo(goodsItem.getGoodsNo());
                importOrderGoods.setGoodsType(goodsItem.getGoodsType());
                importOrderGoods.setSpecificationValueId(goodsItem.getSpecsId());
                importOrderGoods.setColorValueId(goodsItem.getColorId());
                orderGoodsMap.put(importOrderGoods.getItemId(), importOrderGoods);
                if (orderGoods.size() > 0) {
                    ImportOrderGoodsDO importDetail = orderGoods.get(0);
                    if (importDetail.getGoodsType().equals(importOrderGoods.getGoodsType())) {
                        orderGoods.add(importOrderGoods);
                    } else {
                        throw OrderException.buildLineException(
                            B_STANDARD_GOODS_DIY_GOODS_PRE_SALE_GOODS_YOU_CANNOT_PLACE_ORDERS_AT_THE_SAME_TIME, rowNum);
                    }
                } else {
                    orderGoods.add(importOrderGoods);
                }
                importOrderGoodss.add(importOrderGoods);
            }
        }
        for (Integer orderNo : importOrderMap.keySet()) {
            List<ImportOrderGoodsDO> orderGoods = importOrderGoodsMap.get(orderNo);
            ImportOrderDO importOrder = importOrderMap.get(orderNo);
            Integer countAll = countAllMap.get(orderNo);
            if (CollectionUtils.isEmpty(orderGoods)) {
                String msg =
                    MessageUtils.get(B_ORDER_SERIAL_NUMBER) + ":{0}," + MessageUtils.get(B_ORDER_GOODS_ITEM_DATA_EMPTY);
                throw OrderException.buildException(B_ORDER_GOODS_ITEM_DATA_EMPTY, MessageFormat.format(msg, orderNo));
            }
            // 查询同一序号下 同一分销商 的所有货品的分销商价格(一次获取)
            Integer distributorId = importOrder.getDistributorId();
            Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap =
                importOrderRpcCmdExe.getDistributorPriceMap(orderGoods, distributorId);
            // 海外版美元结算，进行汇率转换
            // B2bRate rate = rateMap.get(orderNo);
            // if (currentUser.getCurrencyType().equals("USD")) {
            // Double r = rate.getReverseExRate();
            // priceBean.transformRateOrder(rateService, r);
            // }
            // 查找该分销商 货品所对应的 促销价格 TODO
            List<GoodsItemRpcQry> itemRpcs = orderGoods.stream().map(importOrderGoodsDO -> {
                GoodsItemRpcQry rpc = new GoodsItemRpcQry();
                rpc.setItemId(importOrderGoodsDO.getItemId());
                rpc.setGoodsId(importOrderGoodsDO.getGoodsId());
                return rpc;
            }).collect(toList());
            Map<Integer, GoodsItemPromotionRpcDTO> promotionRpcDTOMap =
                importOrderRpcCmdExe.getPromotionPriceMap(itemRpcs, distributorId);
            log.info("导入订单活动数据:{}", JSON.toJSONString(promotionRpcDTOMap));
            BigDecimal amountSum = BigDecimal.ZERO;
            for (ImportOrderGoodsDO importOrderGoods : orderGoods) {
                // 根据分销商获取货品价格列表
                GoodsItemPriceRpcDTO goodsItemPriceRpcDTO = priceRpcDTOMap.get(importOrderGoods.getItemId());
                // 分销商 等级价
                BigDecimal salePrice = goodsItemPriceRpcDTO.getSalePrice();
                GoodsItemRpcDTO goodsItemRpcDTO = itemMap.get(importOrderGoods.getItemCode());
                GoodsItemPromotionRpcDTO promotionRpcDTO = promotionRpcDTOMap.get(importOrderGoods.getItemId());
                GoodsItemPromotionPriceRpcDTO rpcDTO = importOrderRpcCmdExe.calcGoodsItemPromotionPrice(promotionRpcDTO,
                    goodsItemPriceRpcDTO, goodsItemRpcDTO, importOrder.getDistributorId(), importOrderGoods);
                BigDecimal actualPrice = salePrice;
                if (rpcDTO != null) {
                    actualPrice = rpcDTO.getActualPrice();
                    importOrderGoods.setRuleId(rpcDTO.getGoodsPromotionId());
                }
                amountSum =
                    amountSum.add(actualPrice.multiply(BigDecimal.valueOf(importOrderGoods.getActualOrderCount())));
                importOrderGoods.setDistributorPrice(salePrice);
                importOrderGoods.setPromotionAmount(salePrice.subtract(actualPrice));
                importOrderGoods.setActualPrice(actualPrice);
            }
            importOrder.setAmountSum(amountSum);
            importOrder.setCountSum(countAll);
            // 获取列表
            // List<ItemCountRequest> requestList = getItemCountList(orderGoods);
            //
            // Admin admin = adminDBManager.findOne(currentUser.getSalesMan());
            // List<GroupGoods> groupGoodsList =
            // iGroupGoods.listStarting(requestList,currentUser.getId(),currentUser.getSalesMan(),currentUser.getGradeId(),admin.getDepartmentId(),currentUser);
            // if(groupGoodsList !=null && groupGoodsList.size()>0){
            // GoodsItem goodsItem = goodsItemDBManager.findById(groupGoodsList.get(0).getItemId().longValue());
            // throw new BaseException(ItemBelongToGroupCode, sheetName + "表订单号：" + (orderNo ) +
            // "购买货品"+goodsItem.getItemName()+"属于拼团货品、后台无法导入");
            // }
        }
        importOrderMapper.insertBatch(importOrders);
        for (Integer orderNo : importOrderMap.keySet()) {
            List<ImportOrderGoodsDO> orderGoods = importOrderGoodsMap.get(orderNo);
            ImportOrderDO importOrder = importOrderMap.get(orderNo);
            for (ImportOrderGoodsDO importOrderGoods : orderGoods) {
                importOrderGoods.setImportOrderId(importOrder.getId());
            }
        }
        importOrderGoodsMapper.insertBatch(importOrderGoodss);
    }

    /**
     * 获取支付方式
     *
     * @param importOrderExcelDTO
     * @param distributorInfo
     * @param line
     * @return
     */
    private PayWayEnum getPayWayEnum(ImportOrderExcelDTO importOrderExcelDTO, DistributorRpcDTO distributorInfo,
        int line) {
        PayWayEnum wayEnum = PayWayEnum.getByExcelImportStr(importOrderExcelDTO.getPayWayName());
        if (wayEnum == null) {
            throw OrderException.buildLineException(B_ORDER_PAY_WAY_ERROR, line);
        }
        // 区间结算客户不支持余额支付
        if (distributorInfo.getPayWay().equals(CASH_LINE_2) && wayEnum == PayWayEnum.Balance) {
            throw OrderException.buildLineException(B_BALANCE_NOT_SUPPORT, line);
        }
        // 在线支付客户不支持区间支付
        if (distributorInfo.getPayWay().equals(CASH_LINE_1) && wayEnum == PayWayEnum.Interval) {
            throw OrderException.buildLineException(B_DISTRIBUTOR_NOT_SUPPORT_INTERVAL_SETTLEMENT, line);
        }
        return wayEnum;
    }

    /**
     * 预先填充订单类型 后面根据库存 有可能变化
     *
     * @param orderTypeMap
     * @param diyFlag
     * @param importOrderExcelDTO
     * @param importOrderDO
     */
    private void extractedOrderType(Map<Short, OrderTypeDTO> orderTypeMap, boolean diyFlag,
        ImportOrderExcelDTO importOrderExcelDTO, ImportOrderDO importOrderDO) {
        OrderTypeDTO diyOrderType = orderTypeMap.get(SPECIAL_FLAG_4);
        OrderTypeDTO standardOrderType = orderTypeMap.get(SPECIAL_FLAG_1);
        OrderTypeDTO autoDeliveryOrderType = orderTypeMap.get(SPECIAL_FLAG_5);
        OrderTypeDTO mtoOrderType = orderTypeMap.get(SPECIAL_FLAG_2);
        if (diyFlag) {
            importOrderDO.setOrderTypeName(diyOrderType.getName());
            importOrderDO.setOrderTypeValue(diyOrderType.getErpType());
        } else {
            if (importOrderExcelDTO.getSpecialOrderFlag() == null) {
                importOrderDO.setOrderTypeName(standardOrderType.getName());
                importOrderDO.setOrderTypeValue(standardOrderType.getErpType());
            } else {
                if (importOrderExcelDTO.getSpecialOrderFlag() == 1) {
                    importOrderDO.setOrderTypeName(autoDeliveryOrderType.getName());
                    importOrderDO.setOrderTypeValue(autoDeliveryOrderType.getErpType());
                } else if (importOrderExcelDTO.getSpecialOrderFlag() == 2) {
                    importOrderDO.setOrderTypeName(mtoOrderType.getName());
                    importOrderDO.setOrderTypeValue(mtoOrderType.getErpType());
                }
                // 3 是定制订单 不支持
            }
        }
    }

    /**
     * 地址校验填充
     *
     * @param regionMap
     * @param importOrderExcelDTO
     * @param importOrderDO
     * @param line
     */
    private void extractedAddress(int line, Map<String, RegionRpcDTO> regionMap,
        ImportOrderExcelDTO importOrderExcelDTO, ImportOrderDO importOrderDO) {
        importOrderDO.setUserName(importOrderExcelDTO.getUserName());
        RegionRpcDTO countryRegion =
            importOrderRpcCmdExe.getRegionRpcDTO(regionMap, importOrderExcelDTO.getCountryName(), 0, line);
        importOrderDO.setCountryId(countryRegion.getId());
        importOrderDO.setCountryName(countryRegion.getRegionName());
        if (StringUtils.isNotBlank(importOrderExcelDTO.getMobile())) {
            boolean chinaPhoneLegal = ValidateUtils.isChinaPhoneLegal(importOrderExcelDTO.getMobile());
            if (!chinaPhoneLegal) {
                throw OrderException.buildLineException(ADDRESS_MOBILE_NULL, line);
            }
            importOrderDO.setMobile(importOrderExcelDTO.getMobile());
        }
        // 详细地址
        importOrderDO.setAddress(importOrderExcelDTO.getAddress());
        if (importOrderDO.getCountryId() != null && StringUtils.isNotBlank(importOrderExcelDTO.getProvinceName())) {
            RegionRpcDTO provinceRegion = importOrderRpcCmdExe.getRegionRpcDTO(regionMap,
                importOrderExcelDTO.getProvinceName(), importOrderDO.getCountryId(), line);
            importOrderDO.setProvinceId(provinceRegion.getId());
            importOrderDO.setProvinceName(provinceRegion.getRegionName());
            if (importOrderDO.getProvinceId() != null && StringUtils.isNotBlank(importOrderExcelDTO.getCityName())) {
                RegionRpcDTO cityRegion = importOrderRpcCmdExe.getRegionRpcDTO(regionMap,
                    importOrderExcelDTO.getCityName(), importOrderDO.getProvinceId(), line);
                importOrderDO.setCityId(cityRegion.getId());
                importOrderDO.setCityName(cityRegion.getRegionName());
            }
            try {
                RegionRpcDTO districtRegion = importOrderRpcCmdExe.getRegionRpcDTO(regionMap,
                    importOrderExcelDTO.getDistrictName(), importOrderDO.getCityId(), line);
                importOrderDO.setDistrictId(districtRegion.getId());
                importOrderDO.setDistrictName(districtRegion.getRegionName());
            } catch (OrderException e) {
                importOrderDO
                    .setAddress(importOrderExcelDTO.getDistrictName() + " " + importOrderExcelDTO.getAddress());
            }
        }
    }

    /**
     * 校验参数非空
     *
     * @param diyFlag
     * @param list
     */
    @SneakyThrows
    private void validExcelParams(boolean diyFlag, List<ImportOrderExcelDTO> list) {
        for (int i = 0; i < list.size(); i++) {
            ImportOrderExcelDTO importOrderExcelDTO = list.get(i);
            Field[] fields = importOrderExcelDTO.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                String columnName = annotation.value()[0];
                String msg = "第{0}行，{1}:不能为空";
                if (!diyFlag && field.getName().startsWith("diy")) {
                    continue;
                }
                if (columnName.startsWith("*") && field.get(importOrderExcelDTO) == null) {
                    throw OrderException.buildException(ErrorCode.P_NOTNULL,
                        MessageFormat.format(msg, i + 2, columnName));
                }
            }
        }
    }

    /**
     * 查询订单 并校验库存等信息
     * 
     * @param qry
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<ImportOrderDTO> listImportOrder(ImportOrderListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<ImportOrderDO> resultList = importOrderMapper.listImportOrderByParams(map);
        PageInfo pageInfo = new PageInfo(resultList);
        if (!CollectionUtils.isEmpty(resultList)) {
            List<ImportOrderDTO> importOrders =
                getImportOrderGoodsDOS(resultList, IMPORT_OPERATION_LIST, null, null, null, null);
            pageInfo.setList(importOrders);
        }
        return pageInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public ImportOrderDetailDTO importOrderDetail(ImportOrderDetailQry qry) {

        Map<Short, OrderTypeDTO> orderTypeMap = importOrderRpcCmdExe.getOrderTypeDTOMap();

        ImportOrderDO importOrder = importOrderMapper.selectByPrimaryKey(qry.getId());
        if (importOrder == null) {
            throw OrderException.buildException(B_ORDER_NOT_EXIST);
        }
        ImportOrderDetailDTO importOrderDetailBean = new ImportOrderDetailDTO();
        importOrderDetailBean.setImportOrderId(importOrder.getId());
        importOrderDetailBean.setOrderSplitFlag(importOrder.getOrderSplitFlag());
        BeanUtil.copyProperties(importOrder, importOrderDetailBean,
            CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        // 获取全局配置
        ShopSettingRpcDTO config = importOrderRpcCmdExe.getSystemConfig();
        importOrderDetailBean.setOnWayAttendEventFlag(Short.valueOf(config.getOnWayAttendEventFlag() + ""));
        importOrderDetailBean.setCustomizedAttendEventFlag(Short.valueOf(config.getCustomizedAttendEventFlag() + ""));
        importOrderDetailBean.setMtoAttendEventFlag(Short.valueOf(config.getMtoAttendEventFlag() + ""));
        List<ImportOrderGoodsDO> importOrderGoodsList = importOrderGoodsMapper.findByImportOrderId(importOrder.getId());
        DistributorRpcDTO info = importOrderRpcCmdExe.getDistributorInfo(importOrder.getDistributorId());
        List<ImportOrderGoodsDO> deleteList = new ArrayList<>();
        // 订单已处理
        List<String> itemCodes = importOrderGoodsList.stream().map(ImportOrderGoodsDO::getItemCode).collect(toList());
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = importOrderRpcCmdExe.validGoodsItemAndGetList(itemCodes);
        Map<Integer, GoodsItemRpcDTO> itemMap = goodsItemRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getId, goodsItemRpcDTO -> goodsItemRpcDTO));
        if (HANDLE_FLAG_1.equals(importOrderDetailBean.getHandleFlag())) {
            String orderId = importOrder.getOrderId();
            String[] strs = orderId.split(",");
            BigDecimal orderAmount = BigDecimal.ZERO;
            BigDecimal goodsAmount = BigDecimal.ZERO;
            BigDecimal distributionMoney = BigDecimal.ZERO;
            BigDecimal goodsServiceAmount = BigDecimal.ZERO;
            StringBuilder orderNo = new StringBuilder();
            if (StringUtils.isNotBlank(importOrder.getOrderId())) {
                for (String str : strs) {
                    Integer id = Integer.parseInt(str);
                    OrderExtendDataDO orderExt = orderExtendDataMapper.getByOrderId(Integer.valueOf(str));
                    if (orderExt == null) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(orderExt.getOrderErpNo())) {
                        orderNo.append(orderExt.getOrderErpNo()).append(",");
                    }
                    OrderDistributorCostDO cost =
                        orderDistributorCostMapper.getByOrderIdAndDistributorId(id, info.getId());
                    orderAmount = orderAmount.add(cost.getPayAmount());
                    goodsAmount = goodsAmount.add(cost.getGoodsAmount());
                    distributionMoney = distributionMoney.add(cost.getDistributionAmount());
                }
            }
            if (StringUtils.isNotBlank(orderNo.toString())) {
                orderNo = new StringBuilder(orderNo.substring(0, orderNo.length() - 1));
            }
            importOrderDetailBean.setOrderAmount(orderAmount);
            importOrderDetailBean.setOrderId(importOrder.getOrderId());
            importOrderDetailBean.setGoodsAmount(goodsAmount);
            importOrderDetailBean.setDistributionMoney(distributionMoney);
            importOrderDetailBean.setOrderNo(orderNo.toString());
            importOrderDetailBean.setOrderCreateTime(importOrder.getOrderCreateTime());
            importOrderDetailBean.setGoodsServiceAmount(goodsServiceAmount);
        } else {
            calculatePromotion(importOrderGoodsList, importOrder, deleteList, itemMap);
        }
        // List<Integer> goodsIds =
        // importOrderGoodsList.stream().map(ImportOrderGoodsDO::getGoodsId).distinct().collect(toList());
        List<Integer> itemIds =
            importOrderGoodsList.stream().map(ImportOrderGoodsDO::getItemId).distinct().collect(toList());
        /// 新柔性定制
        List<Integer> importOrderGoodsIds = importOrderGoodsList.stream()
            .filter(importOrderGoodsDO -> importOrderGoodsDO.getGoodsType().equals((short)3))
            .map(ImportOrderGoodsDO::getId).collect(toList());
        List<ImportOrderGoodsDTO> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(importOrderGoodsList)) {
            // List<ImportOrderDetail> importOrderDetails = new ArrayList<>();
            // Map<Long, ImportOrderDetailDiyBean> importOrderDetailDiyBeanMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(importOrderGoodsIds)) {
                // 柔性定制
                // importOrderDetails = importOrderDetailDBManager.findByImportOrderGoodsIdIn(importOrderGoodsIds);
                // for (ImportOrderDetail importOrderDetail : importOrderDetails) {
                // ImportOrderDetailDiyBean importOrderDetailDiyBean = new ImportOrderDetailDiyBean();
                // if (importOrderDetail.getGenerateImage() == null) {
                // PictureModelMaterialDiy pictureModelMaterialDiy = pictureModelMaterialDiyRepository
                // .findByPictureIdAndMaterialIdAndModelId(importOrderDetail.getPictureId(),
                // importOrderDetail.getMaterialId(), importOrderDetail.getModelId());
                // if (pictureModelMaterialDiy != null) {
                // importOrderDetail.setGenerateImage(pictureModelMaterialDiy.getGenerateImage());
                // importOrderDetail.setImage(pictureModelMaterialDiy.getImage());
                // importOrderDetailDBManager.save(importOrderDetail);
                // }
                // }
                // BeanUtils.copyProperties(importOrderDetail, importOrderDetailDiyBean);
                // importOrderDetailDiyBeanMap.put(importOrderDetail.getImportOrderGoodsId(),
                // importOrderDetailDiyBean);
                // }
            }
            // List<Goods> goodss = goodsDataManager.findGoodsByIdIn(goodsIds);
            // List<GoodsItem> items = goodsDataManager.findItemsByIds(itemIds);
            // Map<Long, Goods> goodsMap = new HashMap<>();
            // Map<Long, GoodsItem> goodsItemMap = new HashMap<>();
            // for (Goods goods : goodss) {
            // goodsMap.put(goods.getId(), goods);
            // }
            // for (GoodsItem goodsItem : items) {
            // goodsItemMap.put(goodsItem.getId(), goodsItem);
            // }
            List<GoodsItemInventorySummaryRpcDTO> stockItemCountList =
                importOrderRpcCmdExe.findItemStocks(itemIds, info.getId());

            Map<Integer, GoodsItemInventorySummaryRpcDTO> stockItemCountMap =
                stockItemCountList.stream().collect(toMap(GoodsItemInventorySummaryRpcDTO::getItemId, k -> k));

            for (ImportOrderGoodsDO importOrderGoods : importOrderGoodsList) {
                ImportOrderGoodsDTO bean = new ImportOrderGoodsDTO();
                BeanUtils.copyProperties(importOrderGoods, bean);
                GoodsItemRpcDTO item = itemMap.get(importOrderGoods.getItemId());
                if (item != null) {
                    bean.setBrandId(item.getBrandId());
                    bean.setSaleStatus(item.getSaleStatus());
                    bean.setMoq(item.getMoq());
                    bean.setAdvanceSaleFlag(item.getAdvanceSaleFlag());
                    bean.setBarCode(item.getBarCode());
                }
                // 新Diy定制
                // ImportOrderDetailDiyBean importOrderDetailDiyBean =
                // importOrderDetailDiyBeanMap.get(importOrderGoods.getId());
                // if (importOrderDetailDiyBean != null) {
                // bean.setNewDiy(importOrderDetailDiyBean);
                // }
                BigDecimal amountTotal =
                    importOrderGoods.getActualPrice().multiply(new BigDecimal(importOrderGoods.getActualOrderCount()));
                amountTotal = amountTotal.setScale(4, RoundingMode.HALF_UP);
                bean.setAmountTotal(amountTotal);
                GoodsItemInventorySummaryRpcDTO itemCount = stockItemCountMap.get(importOrderGoods.getItemId());
                if (itemCount == null) {
                    bean.setMaxCount(0);
                } else {
                    int maxCount = 0;
                    // 订单直发客户且货品不支持直发在途下单
                    if (info.getAutoDelivery() == 1 && Objects.requireNonNull(item).getOnwaySaleFlag() == 0) {
                        maxCount = itemCount.getInStockUsableCount();
                    } else {
                        maxCount = itemCount.getOnWayUsableCount();
                    }
                    if (maxCount <= 0) {
                        maxCount = 0;
                    }
                    bean.setMaxCount(maxCount);
                }
                resultList.add(bean);
            }
        }
        // 订单未处理
        if (HANDLE_FLAG_2.equals(importOrderDetailBean.getHandleFlag())) {
            OrderTypeDTO diyOrderTypeDTO = orderTypeMap.get(SPECIAL_FLAG_4);
            OrderTypeDTO zyTypeDTO = orderTypeMap.get(SPECIAL_FLAG_5);
            OrderTypeDTO mtoTypeDTO = orderTypeMap.get(SPECIAL_FLAG_2);
            DistributorPromitonGroupSeckillRpcDTO seckill =
                importOrderRpcCmdExe.getDistributorPromotionGroupSeckill(info.getId());
            // 不为 定制 直运 mot
            boolean flag1 = !importOrder.getOrderTypeValue().equals(diyOrderTypeDTO.getErpType())
                && !importOrder.getOrderTypeValue().equals(zyTypeDTO.getErpType())
                && !importOrder.getOrderTypeValue().equals(mtoTypeDTO.getErpType());
            // 为定制商品 参加活动
            boolean flag2 = importOrder.getOrderTypeValue().equals(diyOrderTypeDTO.getErpType())
                && config.getCustomizedAttendEventFlag() == 1;
            // 为直运订单 参加活动
            boolean flag3 = importOrder.getOrderTypeValue().equals(zyTypeDTO.getErpType())
                && config.getDirectTransportationEventFlag() == 1;
            // 为MTO订单 参加活动
            boolean flag4 =
                importOrder.getOrderTypeValue().equals(mtoTypeDTO.getErpType()) && config.getMtoAttendEventFlag() == 1;
            boolean flag0 = flag1 || flag2 || flag3 || flag4;
            if (importOrder.getOrderTypeValue() == null
                || importOrder.getOrderTypeValue() != null && flag0 && seckill.getPromotionScope() != 0) {// 是否参与活动
                // 把货品能参加的活动 返回前端，切换
                getGoodsPromotion(resultList, info);
            }

        }
        importOrderDetailBean.setOrderGoods(resultList);
        importOrderMapper.updateByPrimaryKeySelective(importOrder);
        if (!CollectionUtils.isEmpty(deleteList)) {
            List<Integer> ids = deleteList.stream().map(ImportOrderGoodsDO::getId).collect(toList());
            importOrderGoodsMapper.deleteByPrimaryKeys(ids);
        }
        return importOrderDetailBean;
    }

    /**
     * 获取商品的活动 塞进列表 在前端可以替换（这功能 先不实现吧）
     * 
     * @param resultList
     * @param info
     */
    public void getGoodsPromotion(List<ImportOrderGoodsDTO> resultList, DistributorRpcDTO info) {

    }

    /**
     * 重新计算活动商品
     *
     * @param importOrderGoodsList
     * @param itemMap
     */
    public void calculatePromotion(List<ImportOrderGoodsDO> importOrderGoodsList, ImportOrderDO importOrder,
        List<ImportOrderGoodsDO> deleteList, Map<Integer, GoodsItemRpcDTO> itemMap) {
        Integer errorRuleId = null;
        try {
            // 根据分销商获取货品价格列表
            Map<Integer, GoodsItemPriceRpcDTO> salePriceMap =
                importOrderRpcCmdExe.getDistributorPriceMap(importOrderGoodsList, importOrder.getDistributorId());
            int countSum = 0;
            BigDecimal amountSum = BigDecimal.ZERO;

            List<GoodsItemRpcQry> itemRpcs = importOrderGoodsList.stream().map(importOrderGoodsDO -> {
                GoodsItemRpcQry rpc = new GoodsItemRpcQry();
                rpc.setItemId(importOrderGoodsDO.getItemId());
                rpc.setGoodsId(importOrderGoodsDO.getGoodsId());
                return rpc;
            }).collect(toList());
            Map<Integer, GoodsItemPromotionRpcDTO> promotionRpcDTOMap =
                importOrderRpcCmdExe.getPromotionPriceMap(itemRpcs, importOrder.getDistributorId());
            for (int i = 0; i < importOrderGoodsList.size(); i++) {
                ImportOrderGoodsDO importOrderGoods = importOrderGoodsList.get(i);
                try {
                    GoodsItemPromotionRpcDTO promotionRpcDTO = promotionRpcDTOMap.get(importOrderGoods.getItemId());
                    GoodsItemPriceRpcDTO goodsItemPriceRpcDTO = salePriceMap.get(importOrderGoods.getItemId());
                    GoodsItemRpcDTO goodsItemRpcDTO = itemMap.get(importOrderGoods.getItemId());
                    GoodsItemPromotionPriceRpcDTO rpcDTO =
                        importOrderRpcCmdExe.calcGoodsItemPromotionPrice(promotionRpcDTO, goodsItemPriceRpcDTO,
                            goodsItemRpcDTO, importOrder.getDistributorId(), importOrderGoods);
                    BigDecimal salePrice = goodsItemPriceRpcDTO.getSalePrice();
                    BigDecimal actualPrice = salePrice;
                    if (rpcDTO != null) {
                        actualPrice = rpcDTO.getActualPrice();
                        importOrderGoods.setRuleId(rpcDTO.getGoodsPromotionId());
                        importOrderGoods.setDistributorPrice(salePrice);
                        importOrderGoods.setActualPrice(actualPrice);
                        importOrderGoods.setPromotionAmount(salePrice.subtract(actualPrice));
                    } else {
                        importOrderGoods.setRuleId(null);
                        importOrderGoods.setGradeDiscountId(null);
                        importOrderGoods.setOrderRuleId(null);
                        importOrderGoods.setActualPrice(importOrderGoods.getDistributorPrice());
                        importOrderGoods.setPromotionAmount(BigDecimal.ZERO);
                        if (importOrderGoods.getItemType() != null && importOrderGoods.getItemType() == 2) {
                            deleteList.add(importOrderGoods);
                            importOrderGoodsList.remove(importOrderGoods);
                            i--;
                            continue;
                        }
                    }
                    amountSum =
                        amountSum.add(actualPrice.multiply(BigDecimal.valueOf(importOrderGoods.getActualOrderCount())));
                    countSum = countSum + importOrderGoods.getActualOrderCount();
                } catch (Exception e) {
                    errorRuleId = importOrderGoods.getRuleId();
                }
            }
            importOrder.setCountSum(countSum);
            importOrder.setAmountSum(amountSum);
        } catch (Exception e) {
            Iterator<ImportOrderGoodsDO> iterator = importOrderGoodsList.iterator();
            while (iterator.hasNext()) {
                ImportOrderGoodsDO importOrderGoods = iterator.next();
                if (importOrderGoods.getItemType() != null
                    && Short.valueOf(importOrderGoods.getItemType() + "").equals(ITEM_TYPE_2)
                    && importOrderGoods.getRuleId() != null && importOrderGoods.getRuleId().equals(errorRuleId)) {
                    iterator.remove();
                    deleteList.add(importOrderGoods);
                }
            }
            calculatePromotion(importOrderGoodsList, importOrder, deleteList, itemMap);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteImportOrderInfo(ImportOrderIds request) {
        String ids = request.getIds();
        String[] split = ids.split(",");
        List<Integer> importOrderIds = new ArrayList<>();
        for (String id : split) {
            importOrderIds.add(Integer.valueOf(id));
        }
        List<ImportOrderDO> importOrders = importOrderMapper.listImportOrderByIds(importOrderIds);
        for (ImportOrderDO importOrder : importOrders) {
            if (HANDLE_FLAG_1.equals(importOrder.getHandleFlag())) {
                importOrderIds.remove(importOrder.getId());
            }
        }
        // importOrderDataManager.deleteImportOrdersDetail(importOrderIds);
        importOrderGoodsMapper.deleteByImportOrderIds(importOrderIds);
        importOrderMapper.deleteByPrimaryKeys(importOrderIds);
    }

    @Async("asyncTaskExecutor-cpu")
    public void addImportOrders(ImportOrderIds request, Boolean adminFlag, String contactId, String contactName,
        String language, String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        try {
            doAddImportOrders(request, adminFlag, contactId, contactName, language);
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void doAddImportOrders(ImportOrderIds request, Boolean adminFlag, String contactId, String contactName,
        String language) {
        List<Integer> ids = Arrays.stream(request.getIds().split(",")).map(Integer::valueOf).collect(toList());
        List<ImportOrderDO> resultList = importOrderMapper.listImportOrderByIds(ids);
        // 排除已提交成功和提交中的数据
        log.info("resultList1 json :{}", JSON.toJSONString(resultList));
        Iterator<ImportOrderDO> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            ImportOrderDO importOrder = iterator.next();
            if (importOrder.getSubmitStatus() != null && (importOrder.getSubmitStatus().equals(SUBMIT_STATUS_2)
                || importOrder.getSubmitStatus().equals(SUBMIT_STATUS_3))) {
                iterator.remove();
            } else {
                importOrder.setSubmitStatus(SUBMIT_STATUS_2);
            }
        }
        log.info("resultList2 json :{}", JSON.toJSONString(resultList));
        importOrderMapper.batchUpdate(resultList);

        List<Integer> distributorIds =
            resultList.stream().map(ImportOrderDO::getDistributorId).distinct().collect(toList());
        if (request.getDistributorId() != null) {
            if (!(distributorIds.size() == 1 && request.getDistributorId().equals(distributorIds.get(0)))) {
                // 前台用户 提交了不属于自己的订单
                throw OrderException.buildException(B_ORDER_NOT_BELONG_TO_DISTRIBUTOR);
            }
        }
        getImportOrderGoodsDOS(resultList, IMPORT_OPERATION_SUBMIT, adminFlag, contactId, contactName, language);
    }

    /**
     * 公共处理部分 涉及到 查询订单列表 提交订单
     *
     * @param resultList
     * @param operationType
     * @param adminFlag
     * @param contactId
     * @param contactName
     * @param language
     * @return
     */
    private List<ImportOrderDTO> getImportOrderGoodsDOS(List<ImportOrderDO> resultList, Short operationType,
        Boolean adminFlag, String contactId, String contactName, String language) {
        List<ImportOrderDTO> importOrders = new ArrayList<>();
        Map<Short, OrderTypeDTO> orderTypeMap = importOrderRpcCmdExe.getOrderTypeDTOMap();
        // 去重分销商id集合
        List<Integer> distributorIds =
            resultList.stream().map(ImportOrderDO::getDistributorId).distinct().collect(toList());
        // 分销商信息
        Map<Integer, DistributorRpcDTO> distributorMap = importOrderRpcCmdExe.getDistributorInfosMap(distributorIds);
        // importOrderId 集合
        List<Integer> importOrderIds = resultList.stream().map(ImportOrderDO::getId).collect(toList());
        List<ImportOrderGoodsDO> importOrderGoodsList = importOrderGoodsMapper.findByImportOrderIds(importOrderIds);
        // importOrderGoods 集合所有货品去重 编码
        List<String> itemCodes =
            importOrderGoodsList.stream().map(ImportOrderGoodsDO::getItemCode).distinct().collect(toList());
        // 货品编码与 货品
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = null;
        if (operationType.equals(IMPORT_OPERATION_LIST)) {
            goodsItemRpcDTOS = importOrderRpcCmdExe.validGoodsItemAndGetList(itemCodes, false);
        } else {
            goodsItemRpcDTOS = importOrderRpcCmdExe.validGoodsItemAndGetList(itemCodes);
        }
        Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap = goodsItemRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getId, goodsItemRpcDTO -> goodsItemRpcDTO));
        // importOrderId importOrderGoods
        Map<Integer, List<ImportOrderGoodsDO>> importOrderGoodsMap =
            importOrderGoodsList.stream().collect(Collectors.groupingBy(ImportOrderGoodsDO::getImportOrderId));
        // 获取 当前所有分销商 对应map
        List<ImportOrderGoodsDO> changeImportOrderGoodsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resultList)) {
            for (ImportOrderDO importOrder : resultList) {
                boolean b = true;
                ImportOrderDTO bean = new ImportOrderDTO();
                BeanUtil.copyProperties(importOrder, bean,
                    CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                // 总价设置为4位小数
                bean.setAmountSum(bean.getAmountSum().setScale(4, RoundingMode.HALF_UP));
                List<ImportOrderGoodsDO> orderGoods = importOrderGoodsMap.get(importOrder.getId());
                List<Integer> brands = new ArrayList<>();
                String remark = importOrder.getRemind();
                DistributorRpcDTO info = distributorMap.get(importOrder.getDistributorId());
                // 未下单
                if (importOrder.getHandleFlag().equals(HANDLE_FLAG_2)) {
                    if (!CollectionUtils.isEmpty(orderGoods)) {
                        // TODO 查询的时候是可以删除数据的？？？？
                        if (operationType.equals(IMPORT_OPERATION_LIST) && info == null) {
                            importOrderGoodsMapper.deleteByImportOrderId(importOrder.getId());
                            importOrderMapper.deleteByPrimaryKey(importOrder.getId());
                            continue;
                        }
                        List<GoodsItemRpcDTO> goodsItemOnWaySaleFlags =
                            importOrderRpcCmdExe.listGoodsItemByCodes(itemCodes);
                        Map<Integer, GoodsItemRpcDTO> goodsItemOnWaySaleFlagMap = goodsItemOnWaySaleFlags.stream()
                            .collect(toMap(GoodsItemRpcDTO::getId, goodsItemRpcDTO -> goodsItemRpcDTO));

                        List<Integer> itemIds =
                            orderGoods.stream().map(ImportOrderGoodsDO::getItemId).collect(toList());
                        List<GoodsItemInventorySummaryRpcDTO> stockItemCountList =
                            importOrderRpcCmdExe.findItemStocks(itemIds, info.getId());
                        Map<Integer, GoodsItemInventorySummaryRpcDTO> stockItemCountMap = stockItemCountList.stream()
                            .collect(toMap(GoodsItemInventorySummaryRpcDTO::getItemId, k -> k));

                        for (ImportOrderGoodsDO importOrderGoods : orderGoods) {
                            GoodsItemRpcDTO goods = itemRpcDTOMap.get(importOrderGoods.getItemId());
                            brands.add(goods.getBrandId());
                            // if (goods == null) {
                            // if (StringUtils.isNotBlank(remark)) {
                            // remark = remark + ";" + "物料" + importOrderGoods.getItemCode() + "不存在";
                            // } else {
                            // remark = "物料" + importOrderGoods.getItemCode() + "不存在";
                            // }
                            // importOrder.setSubmitStatus(OrderConstant.SubmitStatus4);
                            // continue;
                            // }
                            // 直运订单和MTO不计算缺货数量
                            // TODO
                            String zyType = orderTypeMap.get(SPECIAL_FLAG_5).getErpType();
                            String mtoType = orderTypeMap.get(SPECIAL_FLAG_2).getErpType();
                            if (zyType.equals(importOrder.getOrderTypeValue())
                                || mtoType.equals(importOrder.getOrderTypeValue())) {
                                continue;
                            }
                            if (goods.getGoodsType() == 1) {
                                GoodsItemInventorySummaryRpcDTO itemCount =
                                    stockItemCountMap.get(importOrderGoods.getItemId());
                                if (itemCount == null) {
                                    importOrderGoods.setActualOrderCount(0);
                                    importOrderGoods.setItemLoseCount(importOrderGoods.getItemCount());
                                    importOrderGoods.setInWarehouseCount(0);
                                    importOrderGoods.setOnWayCount(0);
                                    if (StringUtils.isNotBlank(remark)) {
                                        remark = remark + ";" + "货号" + importOrderGoods.getItemCode() + "库存不足";
                                    } else {
                                        remark = "货号" + importOrderGoods.getItemCode() + "库存不足";
                                    }
                                    changeImportOrderGoodsList.add(importOrderGoods);
                                    if (operationType.equals(IMPORT_OPERATION_SUBMIT)) {
                                        importOrder.setSubmitStatus(SUBMIT_STATUS_4);
                                        b = false;
                                    }
                                } else {
                                    Integer inWarehouseCount = 0;
                                    Integer maxCount = 0;
                                    GoodsItemRpcDTO goodsItemOnWaySaleFlag =
                                        goodsItemOnWaySaleFlagMap.get(importOrderGoods.getItemId());
                                    // 订单直发客户且货品不支持直发在途下
                                    if (info.getAutoDelivery() == 1 && goodsItemOnWaySaleFlag.getOnwaySaleFlag() == 0) {
                                        inWarehouseCount = itemCount.getInStockUsableCount();
                                        if (importOrderGoods.getActualOrderCount() > inWarehouseCount) {
                                            if (StringUtils.isNotBlank(remark)) {
                                                remark = remark + ";" + "货号" + importOrderGoods.getItemCode() + "库存不足";
                                            } else {
                                                remark = "货号" + importOrderGoods.getItemCode() + "库存不足";
                                            }
                                            if (operationType.equals(IMPORT_OPERATION_SUBMIT)) {
                                                importOrder.setSubmitStatus(SUBMIT_STATUS_4);
                                                b = false;
                                            }
                                            continue;
                                        }
                                    } else {
                                        inWarehouseCount = itemCount.getInStockUsableCount();
                                    }
                                    maxCount = itemCount.getOnWayUsableCount();
                                    if (inWarehouseCount <= 0) {
                                        inWarehouseCount = 0;
                                    }
                                    if (maxCount <= 0) {
                                        maxCount = 0;
                                    }
                                    if (inWarehouseCount >= importOrderGoods.getActualOrderCount() && !importOrderGoods
                                        .getInWarehouseCount().equals(importOrderGoods.getActualOrderCount())) {
                                        // 在库库存满足
                                        importOrderGoods.setItemLoseCount(0);
                                        importOrderGoods.setInWarehouseCount(importOrderGoods.getActualOrderCount());
                                        importOrderGoods.setOnWayCount(0);
                                        changeImportOrderGoodsList.add(importOrderGoods);
                                    } else if (inWarehouseCount < importOrderGoods.getActualOrderCount()
                                        && maxCount >= importOrderGoods.getActualOrderCount()
                                        && (!importOrderGoods.getInWarehouseCount().equals(inWarehouseCount)
                                            || importOrderGoods
                                                .getOnWayCount() != (importOrderGoods.getActualOrderCount()
                                                    - inWarehouseCount))) {
                                        // 在库数量不足、在库+在途可以满足
                                        if (ON_WAY_SPLIT_FLAG_0
                                            .equals(Short.valueOf(importOrder.getOrderSplitFlag()))) {
                                            // 不拆订单
                                            importOrderGoods.setItemLoseCount(0);
                                            importOrderGoods
                                                .setInWarehouseCount(importOrderGoods.getActualOrderCount());
                                            importOrderGoods.setOnWayCount(0);
                                            changeImportOrderGoodsList.add(importOrderGoods);
                                        } else {
                                            // 拆分订单
                                            importOrderGoods.setItemLoseCount(0);
                                            importOrderGoods.setInWarehouseCount(inWarehouseCount);
                                            importOrderGoods.setOnWayCount(
                                                importOrderGoods.getActualOrderCount() - inWarehouseCount);
                                            changeImportOrderGoodsList.add(importOrderGoods);
                                        }
                                        changeImportOrderGoodsList.add(importOrderGoods);
                                    } else if (inWarehouseCount < importOrderGoods.getActualOrderCount()
                                        && maxCount < importOrderGoods.getActualOrderCount()) {
                                        // 数量不足
                                        if (StringUtils.isNotBlank(remark)) {
                                            remark = remark + ";" + "货号" + importOrderGoods.getItemCode() + "库存不足";
                                        } else {
                                            remark = "货号" + importOrderGoods.getItemCode() + "库存不足";
                                        }
                                        if (importOrderGoods
                                            .getItemLoseCount() != (importOrderGoods.getActualOrderCount()
                                                - maxCount)) {
                                            importOrderGoods
                                                .setItemLoseCount(importOrderGoods.getActualOrderCount() - maxCount);
                                            importOrderGoods.setInWarehouseCount(inWarehouseCount);
                                            importOrderGoods.setOnWayCount(maxCount - inWarehouseCount);
                                            changeImportOrderGoodsList.add(importOrderGoods);
                                        }
                                        importOrder.setRemind(remark);
                                        if (operationType.equals(IMPORT_OPERATION_SUBMIT)) {
                                            importOrder.setSubmitStatus(SUBMIT_STATUS_4);
                                            b = false;
                                        }
                                    }
                                }
                            }
                        }
                        bean.setGoodsType(orderGoods.get(0).getGoodsType());
                    }
                    bean.setRemark(remark);
                    importOrder.setRemind(remark);
                } else { // 已下单
                    bean.setOrderId(importOrder.getOrderId());
                    String orderNo = "";
                    if (StringUtils.isNotBlank(importOrder.getOrderId())) {
                        String strs[] = importOrder.getOrderId().split(",");
                        for (String str : strs) {
                            OrderExtendDataDO orderExt = orderExtendDataMapper.getByOrderId(Integer.valueOf(str));
                            if (orderExt == null) {
                                continue;
                            }
                            if (StringUtils.isNotBlank(orderExt.getOrderErpNo())) {
                                orderNo = orderNo + orderExt.getOrderErpNo() + ",";
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(orderNo)) {
                        orderNo = orderNo.substring(0, orderNo.length() - 1);
                    }
                    bean.setOrderNo(orderNo);
                    bean.setOrderCreateTime(importOrder.getOrderCreateTime());
                }
                // TODO 库存判断完成，进入下单阶段
                if (operationType.equals(IMPORT_OPERATION_SUBMIT) && b) {
                    try {
                        List<OrderInfoDO> order =
                            addOrder(importOrder, info, adminFlag, contactId, contactName, language);
                    } catch (OrderException e) {
                        log.info("创建订单出现异常，异常信息：{}", e.getMsg());
                        importOrder.setRemind(e.getMsg());
                        importOrder.setSubmitStatus(SUBMIT_STATUS_4);
                        importOrderMapper.updateByPrimaryKeySelective(importOrder);
                        continue;
                    } catch (Exception e) {
                        importOrder.setRemind(e.getMessage());
                        importOrder.setSubmitStatus(SUBMIT_STATUS_4);
                        importOrderMapper.updateByPrimaryKeySelective(importOrder);
                        continue;
                    }
                }
                bean.setBrands(brands);
                importOrders.add(bean);
            }
        }
        if (!CollectionUtils.isEmpty(changeImportOrderGoodsList)) {
            for (ImportOrderGoodsDO aDo : changeImportOrderGoodsList) {
                importOrderGoodsMapper.updateByPrimaryKeySelective(aDo);
            }
        }
        return importOrders;
    }

    private List<OrderInfoDO> addOrder(ImportOrderDO importOrder, DistributorRpcDTO info, Boolean adminFlag,
        String contactId, String contactName, String language) {
        Map<String, LogisticsRpcDTO> logisticsMap = importOrderRpcCmdExe.initLogisticsMap();
        List<PlatformRpcDTO> platform = importOrderRpcCmdExe.getPlatform();
        List<ImportOrderGoodsDO> importOrderGoods = importOrderGoodsMapper.findByImportOrderId(importOrder.getId());
        List<String> itemCodes =
            importOrderGoods.stream().map(ImportOrderGoodsDO::getItemCode).distinct().collect(toList());
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = importOrderRpcCmdExe.validGoodsItemAndGetList(itemCodes);
        Map<Short, OrderTypeDTO> orderTypeMap = importOrderRpcCmdExe.getOrderTypeDTOMap();
        String zyType = orderTypeMap.get(SPECIAL_FLAG_5).getErpType();
        String mtoType = orderTypeMap.get(SPECIAL_FLAG_2).getErpType();
        String diyType = orderTypeMap.get(SPECIAL_FLAG_4).getErpType();

        Map<Integer, GoodsItemRpcDTO> itemCodeRpcDTO = goodsItemRpcDTOS.stream()
            .collect(Collectors.toMap(GoodsItemRpcDTO::getId, goodsItemRpcDTO -> goodsItemRpcDTO));
        PlatformRpcDTO importPlatform;
        PlatformRpcDTO elseDO = new PlatformRpcDTO();
        if (adminFlag) {
            // 后台导入
            elseDO.setPlatformNo("2");
            importPlatform = platform.stream().filter(platformRpcDTO -> platformRpcDTO.getName().contains("后台导入"))
                .findFirst().orElse(elseDO);
        } else {
            elseDO.setPlatformNo("4");
            // 前台导入
            importPlatform = platform.stream().filter(platformRpcDTO -> platformRpcDTO.getName().contains("前台导入"))
                .findFirst().orElse(elseDO);
        }
        String userId = info.getId() + "";
        String userName = info.getName();
        OrderInfoCmd cmd = new OrderInfoCmd();
        cmd.setInvoiceFlag(importOrder.getIsInvoice());
        cmd.setCurrencyType(importOrder.getCurrencyType());
        cmd.setPayWay(importOrder.getPayWay());
        cmd.setOnWaySplitFlag(Short.valueOf(importOrder.getOrderSplitFlag()));
        cmd.setRemark(importOrder.getRemark());
        OrderDeliveryCmd deliveryCmd = new OrderDeliveryCmd();
        BeanUtils.copyProperties(importOrder, deliveryCmd);
        cmd.setDelivery(deliveryCmd);
        String distributionName = importOrder.getDistributionName();
        LogisticsRpcDTO logisticsRpcDTO = logisticsMap.get(distributionName);
        OrderLogisticsCmd logisticsCmd = new OrderLogisticsCmd();
        logisticsCmd.setLogisticsId(logisticsRpcDTO.getId());
        logisticsCmd.setLogisticsName(logisticsRpcDTO.getName());
        // 因为定制商品与标品不能一起下单
        Short goodsType = importOrderGoods.get(0).getGoodsType();
        if (goodsType.equals(GOODS_TYPE_2)) {
            // logisticsCmd.setManufactors();
        }
        logisticsCmd.setLogisticsType(goodsType);
        List<OrderLogisticsCmd> orderLogisticsCmds = new ArrayList<>();
        orderLogisticsCmds.add(logisticsCmd);
        cmd.setLogisticss(orderLogisticsCmds);
        // 根据 国家 省市 分销商
        // 算拆开的物流费
        BigDecimal inWarehouseWeight = BigDecimal.ZERO;
        BigDecimal inWarehouseAmount = BigDecimal.ZERO;
        BigDecimal onWayWeight = BigDecimal.ZERO;
        BigDecimal onWayAmount = BigDecimal.ZERO;
        BigDecimal diyWeight = BigDecimal.ZERO;
        BigDecimal diyAmount = BigDecimal.ZERO;
        for (ImportOrderGoodsDO importOrderGood : importOrderGoods) {
            GoodsItemRpcDTO goodsItemRpcDTO = itemCodeRpcDTO.get(importOrderGood.getItemId());
            if (importOrderGood.getGoodsType().equals(GOODS_TYPE_1)) {
                if (Short.valueOf(importOrder.getOrderSplitFlag()).equals(ON_WAY_SPLIT_FLAG_1)) {
                    // 拆
                    inWarehouseWeight = inWarehouseWeight.add(goodsItemRpcDTO.getWeight()
                        .multiply(BigDecimal.valueOf(importOrderGood.getInWarehouseCount())));
                    inWarehouseAmount = inWarehouseAmount.add(importOrderGood.getActualPrice()
                        .multiply(BigDecimal.valueOf(importOrderGood.getInWarehouseCount())));
                    onWayWeight = onWayWeight
                        .add(goodsItemRpcDTO.getWeight().multiply(BigDecimal.valueOf(importOrderGood.getOnWayCount())));
                    onWayAmount = onWayAmount.add(
                        importOrderGood.getActualPrice().multiply(BigDecimal.valueOf(importOrderGood.getOnWayCount())));
                } else {
                    // 不拆
                    inWarehouseWeight = inWarehouseWeight.add(goodsItemRpcDTO.getWeight()
                        .multiply(BigDecimal.valueOf(importOrderGood.getActualOrderCount())));
                    inWarehouseAmount = inWarehouseAmount.add(importOrderGood.getActualPrice()
                        .multiply(BigDecimal.valueOf(importOrderGood.getActualOrderCount())));
                }
            } else if (importOrderGood.getGoodsType().equals(GOODS_TYPE_2)) {
                diyWeight = diyWeight.add(
                    goodsItemRpcDTO.getWeight().multiply(BigDecimal.valueOf(importOrderGood.getActualOrderCount())));
                diyAmount = diyAmount.add(importOrderGood.getActualPrice()
                    .multiply(BigDecimal.valueOf(importOrderGood.getActualOrderCount())));
            }
        }
        LogisticsCalculationRpcQry qry = new LogisticsCalculationRpcQry();
        qry.setLogisticsId(importOrder.getDistributionId());
        qry.setCountryId(importOrder.getCountryId());
        qry.setProvinceId(importOrder.getProvinceId());
        qry.setCityId(importOrder.getCityId());
        BigDecimal distributionAmount = BigDecimal.ZERO;
        BigDecimal orderAmount;
        if (inWarehouseWeight.compareTo(BigDecimal.ZERO) != 0 && inWarehouseAmount.compareTo(BigDecimal.ZERO) != 0) {
            qry.setWeight(inWarehouseWeight.doubleValue());
            qry.setPrice(inWarehouseAmount);
            log.info("在库 物流费计算 json:{}", JSON.toJSONString(qry));
            LogisticsCalculationRpcDTO logisticsCalculation = importOrderRpcCmdExe.getLogisticsCalculation(qry);
            distributionAmount = distributionAmount.add(logisticsCalculation.getCost());
        }
        if (onWayWeight.compareTo(BigDecimal.ZERO) != 0 && onWayAmount.compareTo(BigDecimal.ZERO) != 0) {
            qry.setWeight(onWayWeight.doubleValue());
            qry.setPrice(onWayAmount);
            log.info("在途 物流费计算 json:{}", JSON.toJSONString(qry));
            LogisticsCalculationRpcDTO logisticsCalculation = importOrderRpcCmdExe.getLogisticsCalculation(qry);
            distributionAmount = distributionAmount.add(logisticsCalculation.getCost());
        }
        if (diyWeight.compareTo(BigDecimal.ZERO) != 0 && diyAmount.compareTo(BigDecimal.ZERO) != 0) {
            qry.setWeight(diyWeight.doubleValue());
            qry.setPrice(diyAmount);
            log.info("定制 物流费计算 json:{}", JSON.toJSONString(qry));
            LogisticsCalculationRpcDTO logisticsCalculation = importOrderRpcCmdExe.getLogisticsCalculation(qry);
            distributionAmount = distributionAmount.add(logisticsCalculation.getCost());
        }
        log.info("汇总的物流费：distributionAmount:{}", distributionAmount);
        distributionAmount = distributionAmount.setScale(2, RoundingMode.HALF_UP);
        log.info("物流费保留两位小数：{}", distributionAmount);
        BigDecimal amountSum = importOrder.getAmountSum();
        amountSum = amountSum.setScale(2, RoundingMode.HALF_UP);
        log.info("订单费用保留两位小数：{}", amountSum);
        orderAmount = amountSum.add(distributionAmount);
        log.info("订单费用(包含物流费)：{}", amountSum);
        cmd.setDistributionAmount(distributionAmount);
        cmd.setOrderAmount(orderAmount);
        List<OrderGoodsCmd> goodss = importOrderGoods.stream().map(importOrderGoodsDO -> {
            OrderGoodsCmd goodsCmd = new OrderGoodsCmd();
            goodsCmd.setItemCode(importOrderGoodsDO.getItemCode());
            goodsCmd.setItemType(Short.valueOf(importOrderGoodsDO.getItemType() + ""));
            goodsCmd.setOversoldFlag((short)0);
            // mto 预售订单 库存数值重新设置
            if (importOrder.getOrderTypeValue().equals(mtoType)) {
                goodsCmd.setMtoType((short)1);
                goodsCmd.setItemMtoCount(importOrderGoodsDO.getActualOrderCount());
                goodsCmd.setItemInCount(0);
                goodsCmd.setItemOnWayCount(0);
            } else {
                goodsCmd.setMtoType((short)0);
                goodsCmd.setItemMtoCount(0);
                goodsCmd.setGoodsPromotionId(importOrderGoodsDO.getRuleId());
                goodsCmd.setItemCount(importOrderGoodsDO.getActualOrderCount());
                goodsCmd.setItemInCount(importOrderGoodsDO.getInWarehouseCount());
                goodsCmd.setItemOnWayCount(importOrderGoodsDO.getOnWayCount());
            }
            return goodsCmd;
        }).collect(toList());
        cmd.setGoodss(goodss);
        // 无论开不开票 都传发票信息
        OrderInvoiceCmd orderInvoiceCmd = new OrderInvoiceCmd();
        orderInvoiceCmd.setInvoiceType(importOrder.getInvoiceType());
        DistributorERPRpcDTO distributorERPData = importOrderRpcCmdExe.getDistributorERPData(info.getId());
        DistributorFinancialRpcDO financial = distributorERPData.getFinancial();
        orderInvoiceCmd.setInvoiceTitleType(financial.getTaxType());
        orderInvoiceCmd.setName(financial.getInvoiceTitleName());
        orderInvoiceCmd.setTaxpayerNumber(financial.getTaxpayerNumber());
        orderInvoiceCmd.setBankAccount(financial.getBankAccount());
        orderInvoiceCmd.setBankAccountName(financial.getBankAccountName());
        orderInvoiceCmd.setRegisterPhone(financial.getRegisteredPhone());
        cmd.setInvoice(orderInvoiceCmd);

        log.info("订单导入开始推送订单 cmd :{}", JSON.toJSONString(cmd));
        log.info("订单导入开始推送订单 userId :{}", userId);
        log.info("订单导入开始推送订单 userName :{}", userName);
        log.info("订单导入开始推送订单 contactId :{}", contactId);
        log.info("订单导入开始推送订单 contactName :{}", contactName);
        log.info("订单导入开始推送订单 platform :{}", importPlatform.getPlatformNo());
        log.info("订单导入开始推送订单 language :{}", language);
        List<OrderInfoDO> order = importOrderRpcCmdExe.createOrder(cmd, userId, userName, contactId, contactName,
            importPlatform.getPlatformNo(), language);
        log.info("订单导入开始推送订单 返回值:{}", JSON.toJSONString(order));
        if (!CollectionUtils.isEmpty(order)) {
            importOrder.setHandleFlag(HANDLE_FLAG_1);
            importOrder.setAmountSum(cmd.getOrderAmount());
            importOrder.setOrderCreateTime(order.get(0).getCreateTime());
            importOrder.setSubmitStatus(SUBMIT_STATUS_3);
            String ids = order.stream().map(orderInfoDO -> orderInfoDO.getId() + "").collect(Collectors.joining(","));
            String orderNos = order.stream().map(OrderInfoDO::getOrderNo).collect(Collectors.joining(","));
            importOrder.setOrderId(ids);
            importOrder.setOrderNo(orderNos);
            importOrderMapper.updateByPrimaryKeySelective(importOrder);
        }
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    public void importOrderUpdate(ImportOrderUpdateCmd cmd) {
        // Map<String, LogisticsRpcDTO> logisticsMap = importOrderRpcCmd.initLogisticsMap();
        Map<Short, OrderTypeDTO> orderTypeMap = importOrderRpcCmdExe.getOrderTypeDTOMap();
        ImportOrderDO importOrder = importOrderMapper.selectByPrimaryKey(cmd.getId());
        if (importOrder == null) {
            throw OrderException.buildException(ORDER_NULL);
        }
        if (importOrder.getSubmitStatus() != null && (importOrder.getSubmitStatus().equals(SUBMIT_STATUS_2)
            || importOrder.getSubmitStatus().equals(SUBMIT_STATUS_3))) {
            throw OrderException.buildException(B_IMPORT_SUBMIT_STATUS_ERROR);
        }
        DistributorRpcDTO info = importOrderRpcCmdExe.getDistributorInfo(importOrder.getDistributorId());
        // 分销商结算方式信息
        if (info.getPayWay().equals(CASH_LINE_1) && cmd.getPayWay() == 3) {
            throw OrderException.buildException(B_DISTRIBUTOR_NOT_SUPPORT_INTERVAL_SETTLEMENT);
        }
        importOrder.setPayWay(cmd.getPayWay());
        importOrder.setOrderTypeValue(cmd.getOrderTypeValue());
        List<OrderTypeDTO> orderTypeDTOS = importOrderRpcCmdExe.getOrderTypeDTOList();

        // 更新 配送方式
        LogisticsRpcQry qry = new LogisticsRpcQry();
        qry.setLogisticsId(cmd.getDistributionId());
        qry.setCountryId(cmd.getCountryId());
        qry.setProvinceId(cmd.getProvinceId());
        qry.setCityId(cmd.getCityId());

        OrderTypeDTO orderType =
            orderTypeDTOS.stream().filter(orderTypeDTO -> orderTypeDTO.getErpType().equals(cmd.getOrderTypeValue())
                || orderTypeDTO.getName().equals(cmd.getOrderTypeValue())).findFirst().orElse(null);
        if (orderType != null) {
            importOrder.setOrderTypeName(orderType.getName());
            if (orderType.getSpecialFlag().equals(SPECIAL_FLAG_4)) {
                qry.setUseRange("2");
            } else {
                qry.setUseRange("1");
            }
        }
        importOrder.setDistributionId(cmd.getDistributionId());
        List<LogisticsRpcDTO> logisticsByParams = importOrderRpcCmdExe.getLogisticsByParams(qry);
        if (!CollectionUtils.isEmpty(logisticsByParams) && logisticsByParams.size() == 1) {
            LogisticsRpcDTO logisticsRpcDTO = logisticsByParams.get(0);
            importOrder.setDistributionName(logisticsRpcDTO.getName());
        } else {
            throw OrderException.buildException(B_ORDER_LOGISTICS_ERROR);
        }
        importOrder.setIsInvoice(cmd.getIsInvoice());
        importOrder.setInvoiceType(cmd.getInvoiceType());
        importOrder.setDeliveryType(cmd.getDeliveryType());
        importOrder.setDeliveryTime(cmd.getDeliveryTime());
        importOrder.setUserName(cmd.getUserName());
        importOrder.setZipCode(cmd.getZipCode());
        importOrder.setProvinceId(cmd.getProvinceId());
        if (cmd.getCountryId() == null || cmd.getCountryId() <= 0L) {
            throw OrderException.buildException(ADDRESS_COUNTRY_NAME_NULL);
        }
        importOrder.setCountryId(cmd.getCountryId());
        RegionRpcDTO country = importOrderRpcCmdExe.getRegionById(cmd.getCountryId());
        if (country != null) {
            importOrder.setCountryName(country.getRegionName());
        }
        if (country != null && country.getId().equals(orderConfig.getCountryChina())) {
            if (cmd.getProvinceId() == null || cmd.getProvinceId() <= 0L) {
                throw OrderException.buildException(ADDRESS_PROVINCE_NAME_NULL);
            }
            if (cmd.getCityId() == null || cmd.getCityId() <= 0L) {
                throw OrderException.buildException(ADDRESS_CITY_NAME_NULL);
            }
        }
        if (cmd.getProvinceId() != null && cmd.getProvinceId() > 0) {
            importOrder.setProvinceId(cmd.getProvinceId());
            RegionRpcDTO province = importOrderRpcCmdExe.getRegionById(cmd.getProvinceId());
            if (province != null) {
                importOrder.setProvinceName(province.getRegionName());
            }
        }
        if (cmd.getCityId() != null && cmd.getCityId() > 0) {
            importOrder.setCityId(cmd.getCityId());
            RegionRpcDTO city = importOrderRpcCmdExe.getRegionById(cmd.getCityId());
            if (city != null) {
                importOrder.setCityName(city.getRegionName());
            }
        }
        if (cmd.getDistrictId() != null && cmd.getDistrictId() > 0L) {
            importOrder.setDistrictId(cmd.getDistrictId());
            RegionRpcDTO district = importOrderRpcCmdExe.getRegionById(cmd.getDistrictId());
            if (district != null) {
                importOrder.setDistrictName(district.getRegionName());
            }
        } else {
            importOrder.setDistrictName("");
        }
        if (StringUtils.isBlank(cmd.getAddress())) {
            throw OrderException.buildException(ADDRESS_ADDRESS_NULL);
        }
        importOrder.setAddress(cmd.getAddress());
        importOrder.setMobile(cmd.getMobile());
        importOrder.setPhone(cmd.getPhone());
        importOrder.setRemark(cmd.getRemark());
        if (StringUtils.isNotBlank(cmd.getGoodsDelIds())) {
            List<Integer> delIds =
                Arrays.stream(cmd.getGoodsDelIds().split(",")).map(Integer::valueOf).collect(toList());
            importOrderGoodsMapper.deleteByPrimaryKeys(delIds);
        }
        List<ImportOrderGoodsDO> goodsList = importOrderGoodsMapper.findByImportOrderId(cmd.getId());
        List<ImportOrderGoodsDO> deleteGoodsList = new ArrayList<>();

        String zyType = orderTypeMap.get(SPECIAL_FLAG_5).getErpType();
        String mtoType = orderTypeMap.get(SPECIAL_FLAG_2).getErpType();
        String diyType = orderTypeMap.get(SPECIAL_FLAG_4).getErpType();
        if (!CollectionUtils.isEmpty(goodsList) && !CollectionUtils.isEmpty(cmd.getModifyGoodsList())) {
            Map<Integer, ImportOrderGoodsDO> importOrderGoodsMap = new HashMap<>();
            for (ImportOrderGoodsDO importOrderGoods : goodsList) {
                importOrderGoodsMap.put(importOrderGoods.getId(), importOrderGoods);
            }
            // 货品id和数量
            for (ImportOrderGoodsCmd importOrderGoodsModifyBean : cmd.getModifyGoodsList()) {
                ImportOrderGoodsDO importOrderGoods =
                    importOrderGoodsMap.get(importOrderGoodsModifyBean.getImportOderGoodsId());
                List<GoodsItemRpcDTO> goodsItemRpcDTOS = importOrderRpcCmdExe
                    .validGoodsItemAndGetList(Collections.singletonList(importOrderGoods.getItemCode()));
                if (CollectionUtils.isEmpty(goodsItemRpcDTOS) || goodsItemRpcDTOS.size() != 1) {
                    throw OrderException.buildException(B_GOODS_ITEM_CORRESPONDING_GOODS_IS_NUM_ERROR);
                }
                GoodsItemRpcDTO goodsItem = goodsItemRpcDTOS.get(0);
                importOrderRpcCmdExe.validGoodsItem(null, importOrder.getOrderTypeName(), goodsItem,
                    importOrderGoodsModifyBean.getActualOrderCount(), orderTypeMap);
                importOrderGoods.setActualOrderCount(importOrderGoodsModifyBean.getActualOrderCount());
                if (importOrderGoods.getGoodsType() == 2 || zyType.equals(importOrder.getOrderTypeValue())
                    || mtoType.equals(importOrder.getOrderTypeValue())) {
                    continue;
                }
                List<Integer> itemIds = new ArrayList<>();
                itemIds.add(importOrderGoods.getItemId());
                if (importOrderGoods.getGoodsType() == 1 && !importOrder.getOrderTypeValue().equals(zyType)
                    && !importOrder.getOrderTypeValue().equals(mtoType)
                    && !importOrder.getOrderTypeValue().equals(diyType)) {
                    List<GoodsItemInventorySummaryRpcDTO> stockItemCountList =
                        importOrderRpcCmdExe.findItemStocks(itemIds, info.getId());
                    Map<Integer, GoodsItemInventorySummaryRpcDTO> stockItemCountMap =
                        stockItemCountList.stream().collect(toMap(GoodsItemInventorySummaryRpcDTO::getItemId, k -> k));
                    GoodsItemInventorySummaryRpcDTO itemCount = null;
                    if (!CollectionUtils.isEmpty(stockItemCountList)) {
                        itemCount = stockItemCountList.get(0);
                    }
                    if (itemCount == null) {
                        throw OrderException.buildException(B_GOODS_STOCK_NULL);
                    } else {
                        Integer inWarehouseCount = itemCount.getInStockUsableCount();
                        Integer maxCount = itemCount.getOnWayUsableCount();
                        if (inWarehouseCount <= 0) {
                            inWarehouseCount = 0;
                        }
                        if (inWarehouseCount >= importOrderGoods.getActualOrderCount()) {
                            importOrderGoods.setInWarehouseCount(importOrderGoods.getActualOrderCount());
                            importOrderGoods.setOnWayCount(0);
                        } else if (maxCount >= importOrderGoods.getActualOrderCount()) {
                            importOrderGoods.setInWarehouseCount(inWarehouseCount);
                            importOrderGoods.setOnWayCount(importOrderGoods.getActualOrderCount() - inWarehouseCount);
                        } else {
                            importOrderGoods.setActualOrderCount(maxCount);
                            importOrderGoods.setInWarehouseCount(inWarehouseCount);
                            importOrderGoods.setOnWayCount(importOrderGoods.getActualOrderCount() - inWarehouseCount);
                            // 加判断库存不足、
                            throw OrderException.buildException(B_ORDER_ITEM_STOCK_NOT_ENOUGH);
                        }
                    }
                }
                // 组装拼团货品列表参数
                // ItemCountRequest countRequest = new ItemCountRequest();
                // Long itemCount = importOrderGoodsModifyBean.getActualOrderCount();
                // countRequest.setCount(itemCount.intValue());
                // Long itemId = importOrderGoods.getItemId();
                // countRequest.setItemId(itemId.intValue());
                // requestList.add(countRequest);
            }
            // if (info.getPromotionScope() == null
            // || info.getPromotionScope() - DistributorConstant.PromotionScopeAll == 0
            // || (info.getPromotionScope() - DistributorConstant.PromotionScopeAssign == 0
            // && info.getPromotionTypes().contains(DistributorConstant.PromotionTypeGroup))) {
            // // 校验分销商是否被禁止参加拼团活动
            // //查询分销商所属的业务员信息
            // Admin admin = adminDBManager.findOne(info.getSalesMan());
            // List<GroupGoods> groupGoodsList =
            // iGroupGoods.listStarting(requestList,info.getId(),info.getSalesMan(),info.getGradeId(),admin.getDepartmentId(),currentUser);
            // if(groupGoodsList !=null && groupGoodsList.size()>0){
            // GoodsItem goodsItem = goodsItemDBManager.findById(groupGoodsList.get(0).getItemId().longValue());
            // throw new BaseException(ItemBelongToGroupCode, goodsItem.getItemName()+"属于拼团货品、后台无法导入");
            // }
            // }
            Map<Integer, GoodsItemPriceRpcDTO> salePriceMap;
            try {
                salePriceMap = importOrderRpcCmdExe.getDistributorPriceMap(goodsList, info.getId());
            } catch (Exception e) {
                throw OrderException.buildException("ERROR", e.getLocalizedMessage());
            }
            List<String> itemCodes = goodsList.stream().map(ImportOrderGoodsDO::getItemCode).collect(toList());

            List<GoodsItemRpcDTO> goodsItemRpcDTOS = importOrderRpcCmdExe.validGoodsItemAndGetList(itemCodes);

            Map<String, GoodsItemRpcDTO> itemMap = goodsItemRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemRpcDTO::getItemCode, goodsItemRpcDTO -> goodsItemRpcDTO));

            List<GoodsItemRpcQry> itemRpcs = goodsList.stream().map(importOrderGoodsDO -> {
                GoodsItemRpcQry rpc = new GoodsItemRpcQry();
                rpc.setItemId(importOrderGoodsDO.getItemId());
                rpc.setGoodsId(importOrderGoodsDO.getGoodsId());
                return rpc;
            }).collect(toList());

            Map<Integer, GoodsItemPromotionRpcDTO> promotionRpcDTOMap =
                importOrderRpcCmdExe.getPromotionPriceMap(itemRpcs, importOrder.getDistributorId());
            BigDecimal amountSum = BigDecimal.ZERO;
            for (int i = 0; i < goodsList.size(); i++) {
                ImportOrderGoodsDO importOrderGoods = goodsList.get(i);
                GoodsItemPriceRpcDTO goodsItemPriceRpcDTO = salePriceMap.get(importOrderGoods.getItemId());
                BigDecimal salePrice = goodsItemPriceRpcDTO.getSalePrice();
                GoodsItemPromotionRpcDTO promotionRpcDTO = promotionRpcDTOMap.get(importOrderGoods.getItemId());
                GoodsItemRpcDTO goodsItemRpcDTO = itemMap.get(importOrderGoods.getItemCode());
                GoodsItemPromotionPriceRpcDTO rpcDTO = importOrderRpcCmdExe.calcGoodsItemPromotionPrice(promotionRpcDTO,
                    goodsItemPriceRpcDTO, goodsItemRpcDTO, importOrder.getDistributorId(), importOrderGoods);
                BigDecimal actualPrice = salePrice;
                if (rpcDTO != null) {
                    actualPrice = rpcDTO.getActualPrice();
                    importOrderGoods.setRuleId(rpcDTO.getGoodsPromotionId());
                    importOrderGoods.setDistributorPrice(salePrice);
                    importOrderGoods.setActualPrice(actualPrice);
                    importOrderGoods.setPromotionAmount(salePrice.subtract(actualPrice));
                } else {
                    importOrderGoods.setRuleId(null);
                    importOrderGoods.setGradeDiscountId(null);
                    importOrderGoods.setOrderRuleId(null);
                    importOrderGoods.setActualPrice(importOrderGoods.getDistributorPrice());
                    importOrderGoods.setPromotionAmount(BigDecimal.ZERO);
                    if (importOrderGoods.getItemType() != null && importOrderGoods.getItemType() == 2) {
                        deleteGoodsList.add(importOrderGoods);
                        goodsList.remove(importOrderGoods);
                        i--;
                        continue;
                    }
                }
                amountSum =
                    amountSum.add(actualPrice.multiply(BigDecimal.valueOf(importOrderGoods.getActualOrderCount())));
                importOrder.setAmountSum(amountSum);
            }
        }

        Integer countSum = 0;
        BigDecimal amountSum = BigDecimal.ZERO;
        for (ImportOrderGoodsDO aDo : goodsList) {
            countSum = countSum + aDo.getActualOrderCount();
            amountSum = amountSum.add(aDo.getActualPrice().multiply(BigDecimal.valueOf(aDo.getActualOrderCount())));
        }
        importOrder.setCountSum(countSum);
        importOrder.setAmountSum(amountSum);
        importOrder.setRemind("");
        importOrderMapper.updateByPrimaryKeySelective(importOrder);
        for (ImportOrderGoodsDO aDo : goodsList) {
            importOrderGoodsMapper.updateByPrimaryKeySelective(aDo);
        }
    }
}
