package com.bat.goods.service.goods.executor;

import static com.bat.goods.service.brand.executor.ErrorCode.B_GOODS_BRAND_DISTRIBUTOR_ERROR;
import static com.bat.goods.service.goods.executor.ErrorCode.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.dataobject.BrandDO;
import com.bat.goods.dao.goods.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.service.common.Constant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.goods.goods.dto.*;
import com.bat.dubboapi.order.order.api.ShoppingCartServiceRpc;
import com.bat.dubboapi.warehouse.stock.api.WarehouseStockServiceRpc;
import com.bat.dubboapi.warehouse.stock.dto.GoodsItemErpDTO;
import com.bat.dubboapi.warehouse.stock.dto.GoodsMsgRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.dao.goods.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.service.goods.convertor.GoodsConvertor;

@Component
public class GoodsRpcCmdExe {

    @Resource
    private GoodsStoreMapper storeMapper;

    @Resource
    private GoodsStoreMobileMapper mobileMapper;

    @DubboReference(check = false, timeout = 30000)
    private DistributorServiceRpc distributorServiceRpc;

    @Resource
    private GoodsMinMaxPriceMapper minMaxPriceMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GoodsPromotionPriceMapper promotionPriceMapper;

    @DubboReference(check = false, timeout = 30000)
    private WarehouseStockServiceRpc warehouseStockServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private ShoppingCartServiceRpc shoppingCartServiceRpc;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsRpcCmdExe.class);

    /**
     * 新增更新栏目和商品关联关系
     * 
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertUpdateGoodsStoreColumn(List<GoodsStoreColumnRpcCmd> list) {
        List<GoodsStoreColumnRpcCmd> addCmds = list.stream()
            .filter(goodsStoreColumnRpcCmd -> goodsStoreColumnRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_1))
            .collect(Collectors.toList());
        List<GoodsStoreColumnRpcCmd> updateCmds = list.stream()
            .filter(goodsStoreColumnRpcCmd -> goodsStoreColumnRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_2))
            .collect(Collectors.toList());
        List<GoodsStoreColumnRpcCmd> deletes = list.stream()
            .filter(goodsStoreColumnRpcCmd -> goodsStoreColumnRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_3))
            .collect(Collectors.toList());
        List<GoodsStoreColumnRpcCmd> topCmds = list.stream()
            .filter(goodsStoreColumnRpcCmd -> goodsStoreColumnRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_4))
            .collect(Collectors.toList());
        storeMapper.updateGoodsStoreColumnSort(addCmds.size());
        if (!CollectionUtils.isEmpty(addCmds)) {
            try {
                storeMapper.createGoodsStoreColumnList(GoodsConvertor.toGoodsStoreColumnDOList(addCmds));
            } catch (DuplicateKeyException e) {
                throw GoodsException.buildException(B_GOODS_RPC_COLUMN_GOODS_ERROR);
            }
        }
        List<GoodsStoreColumnDO> updateStoreColumnDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(updateCmds)) {
            updateStoreColumnDOS.addAll(GoodsConvertor.toGoodsStoreColumnDOList(updateCmds));
        }
        if (!CollectionUtils.isEmpty(topCmds)) {
            updateStoreColumnDOS.addAll(GoodsConvertor.toGoodsStoreColumnDOList(topCmds));
        }
        if (!CollectionUtils.isEmpty(updateStoreColumnDOS)) {
            storeMapper.updateGoodsStoreColumnList(updateStoreColumnDOS);
        }
        if (!CollectionUtils.isEmpty(deletes)) {
            storeMapper.deleteGoodsStoreColumnList(GoodsConvertor.toGoodsStoreColumnDOList(deletes));
        }
    }

    /**
     * 新增更新板块和商品关联关系
     * 
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertUpdateGoodsStoreSection(List<GoodsStoreSectionRpcCmd> list) {
        List<GoodsStoreSectionRpcCmd> addCmds = list.stream()
            .filter(goodsStoreSectionRpcCmd -> goodsStoreSectionRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_1))
            .collect(Collectors.toList());
        List<GoodsStoreSectionRpcCmd> updateCmds = list.stream()
            .filter(goodsStoreSectionRpcCmd -> goodsStoreSectionRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_2))
            .collect(Collectors.toList());
        List<GoodsStoreSectionRpcCmd> deletes = list.stream()
            .filter(goodsStoreSectionRpcCmd -> goodsStoreSectionRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_3))
            .collect(Collectors.toList());
        List<GoodsStoreSectionRpcCmd> topCmds = list.stream()
            .filter(goodsStoreSectionRpcCmd -> goodsStoreSectionRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_4))
            .collect(Collectors.toList());
        storeMapper.updateGoodsStoreSectionSort(addCmds.size());
        if (!CollectionUtils.isEmpty(addCmds)) {
            try {
                storeMapper.createGoodsStoreSectionList(GoodsConvertor.toGoodsStoreSectionDOList(addCmds));
            } catch (DuplicateKeyException e) {
                throw GoodsException.buildException(B_GOODS_RPC_SECTION_GOODS_ERROR);
            }
        }
        List<GoodsStoreSectionDO> updateStoreSectionDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(updateCmds)) {
            updateStoreSectionDOS.addAll(GoodsConvertor.toGoodsStoreSectionDOList(updateCmds));
        }
        if (!CollectionUtils.isEmpty(topCmds)) {
            updateStoreSectionDOS.addAll(GoodsConvertor.toGoodsStoreSectionDOList(topCmds));
        }
        if (!CollectionUtils.isEmpty(updateStoreSectionDOS)) {
            storeMapper.updateGoodsStoreSectionList(updateStoreSectionDOS);
        }
        if (!CollectionUtils.isEmpty(deletes)) {
            storeMapper.deleteGoodsStoreSectionList(GoodsConvertor.toGoodsStoreSectionDOList(deletes));
        }
    }

    /**
     * 新增更新移动端配置和商品关联关系
     *
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertUpdateGoodsStoreMobile(List<GoodsStoreMobileRpcCmd> list) {
        List<GoodsStoreMobileRpcCmd> addCmds = list.stream()
            .filter(goodsStoreMobileRpcCmd -> goodsStoreMobileRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_1))
            .collect(Collectors.toList());
        List<GoodsStoreMobileRpcCmd> updateCmds = list.stream()
            .filter(goodsStoreMobileRpcCmd -> goodsStoreMobileRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_2))
            .collect(Collectors.toList());
        List<GoodsStoreMobileRpcCmd> deletes = list.stream()
            .filter(goodsStoreMobileRpcCmd -> goodsStoreMobileRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_3))
            .collect(Collectors.toList());
        List<GoodsStoreMobileRpcCmd> topCmds = list.stream()
            .filter(goodsStoreMobileRpcCmd -> goodsStoreMobileRpcCmd.getOperationType().equals(Constant.OPERATION_TYPE_4))
            .collect(Collectors.toList());
        mobileMapper.updateGoodsStoreMobileSort(addCmds.size());
        if (!CollectionUtils.isEmpty(addCmds)) {
            try {
                mobileMapper.createGoodsStoreMobileList(GoodsConvertor.toGoodsStoreMobileDOList(addCmds));
            } catch (DuplicateKeyException e) {
                throw GoodsException.buildException(B_GOODS_RPC_MOBILE_GOODS_ERROR);
            }
        }
        List<GoodsStoreMobileDO> updateStoreMobileDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(updateCmds)) {
            updateStoreMobileDOS.addAll(GoodsConvertor.toGoodsStoreMobileDOList(updateCmds));
        }
        if (!CollectionUtils.isEmpty(topCmds)) {
            updateStoreMobileDOS.addAll(GoodsConvertor.toGoodsStoreMobileDOList(topCmds));
        }
        if (!CollectionUtils.isEmpty(updateStoreMobileDOS)) {
            mobileMapper.updateGoodsStoreMobileList(updateStoreMobileDOS);
        }
        if (!CollectionUtils.isEmpty(deletes)) {
            mobileMapper.deleteGoodsStoreMobileList(GoodsConvertor.toGoodsStoreMobileDOList(deletes));
        }
    }

    /**
     * 根据栏目id删除所有商品关联关系
     * 
     * @param columnId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoodsStoreColumnByColumnId(Integer columnId) {
        storeMapper.deleteGoodsStoreColumnByColumnId(columnId);
    }

    /**
     * 根据板块id删除所有商品关联关系
     * 
     * @param sectionId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoodsStoreColumnBySectionId(Integer sectionId) {
        storeMapper.deleteGoodsStoreSectionBySectionId(sectionId);
    }

    /**
     * 根据移动端配置id删除所有商品关联关系
     *
     * @param mobileId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoodsStoreMobileByMobileId(Integer mobileId) {
        mobileMapper.deleteGoodsStoreMobileByMobileId(mobileId);
    }

    /**
     * 更新分销商对应商品的可视范围
     *
     * @param goodsId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorGoodsRelevance(Integer goodsId, Short distributorScope, List<Integer> scalePriceIds,
                                          List<Integer> distributorIds, BrandDO brandDO, List<Integer> departmentIds, List<Integer> adminIds,
                                          Short distributorScopeNo, List<Integer> distributorNoIds, List<Integer> departmentNoIds,
                                          List<Integer> scalePriceNoIds, List<Integer> adminNoIds, List<Integer> distributorGroupIds,
                                          List<Integer> brandDistributorIds) {
        // 可视关系
        List<Integer> addDistributorIds = new ArrayList<>();
        Response<List<Integer>> listResponse = null;
        if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
            listResponse = distributorServiceRpc.listDistributorIdByScalePriceIds(scalePriceIds, brandDO.getId());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributorIds)) {
            addDistributorIds.addAll(distributorIds);
        } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(departmentIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdBySalesIdsAndOneTreeNode(adminIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
            listResponse = distributorServiceRpc.listIdByDistributorGroupIdsAndOneTreeNode(distributorGroupIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                addDistributorIds.addAll(listResponse.getData());
            }
        }
        // 品牌非全部可视情况，品牌可视优先
        if (brandDO != null && !brandDO.getDistributorScope().equals(Constant.SCOPE_ALL)) {
            addDistributorIds =
                addDistributorIds.stream().filter(addDistributorId -> brandDistributorIds.contains(addDistributorId))
                    .collect(Collectors.toList());
        }
        if (listResponse != null && !listResponse.isSuccess()) {
            throw GoodsException.buildException(B_GOODS_BRAND_DISTRIBUTOR_ERROR);
        }
        // 不可视关系
        List<Integer> delDistributorIds = new ArrayList<>();
        if (distributorScopeNo != null && distributorScopeNo.equals(Constant.SCOPE_SCALE_PRICE)) {
            listResponse = distributorServiceRpc.listDistributorIdByScalePriceIds(scalePriceNoIds, brandDO.getId());
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScopeNo != null && distributorScopeNo.equals(Constant.SCOPE_DISTRIBUTOR)) {
            delDistributorIds.addAll(distributorNoIds);
        } else if (distributorScopeNo != null && distributorScopeNo.equals(Constant.SCOPE_DEPARTMENT)) {
            listResponse = distributorServiceRpc.listDistributorIdByDepartmentIds(departmentNoIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        } else if (distributorScopeNo != null && distributorScopeNo.equals(Constant.SCOPE_ADMIN)) {
            listResponse = distributorServiceRpc.listDistributorIdByDefaultScalePriceIds(adminNoIds);
            if (listResponse.isSuccess() && !CollectionUtils.isEmpty(listResponse.getData())) {
                delDistributorIds.addAll(listResponse.getData());
            }
        }
        // 剔除不可视范围
        addDistributorIds =
            addDistributorIds.stream().filter(distributorId -> !delDistributorIds.contains(distributorId)).distinct()
                .collect(Collectors.toList());
        Response response = distributorServiceRpc.distributorGoodsRelevanceByGoodsId(addDistributorIds, goodsId);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(B_GOODS_BRAND_DISTRIBUTOR_ERROR);
        }
    }

    /**
     * 根据促销活动计算商品最佳价格
     * 
     * @param goodsPromotionRpcCmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void goodsPromotionPriceByPromotion(GoodsPromotionRpcCmd goodsPromotionRpcCmd) {
        Integer promotionId = goodsPromotionRpcCmd.getId();
        List<GoodsPromotionRuleRpcCmd> rules = goodsPromotionRpcCmd.getRules();
        List<GoodsPromotionPriceDO> priceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rules)) {
            rules.forEach(rule -> {
                // 商品
                DecimalFormat df = new DecimalFormat("0.00");
                if (rule.getRuleType().equals(Constant.RULE_TYPE_2) && !CollectionUtils.isEmpty(rule.getIds())
                    && !CollectionUtils.isEmpty(rule.getConditions())) {
                    // 商品最小最大价格
                    List<GoodsMinMaxPriceDO> minMaxPriceDOS =
                        minMaxPriceMapper.listByGoodsIdsAndScalePrice(rule.getIds());
                    rule.getConditions().forEach(condition -> {
                        minMaxPriceDOS.forEach(minMaxPriceDO -> {
                            BigDecimal minPrice = minMaxPriceDO.getMinPrice();
                            BigDecimal maxPrice = minMaxPriceDO.getMaxPrice();
                            // 计算优惠价格
                            Optional<GoodsPromotionPriceDO> optional =
                                priceDOS.stream()
                                    .filter(priceDO -> priceDO.getGoodsId().equals(minMaxPriceDO.getGoodsId())
                                        && priceDO.getScalePriceId().equals(minMaxPriceDO.getScalePriceId()))
                                    .findFirst();
                            GoodsPromotionPriceDO priceDO = null;
                            if (optional != null && optional.isPresent()) {
                                priceDO = optional.get();
                            } else {
                                priceDO = new GoodsPromotionPriceDO();
                                priceDO.setGoodsId(minMaxPriceDO.getGoodsId());
                                priceDO.setSpecialFlag(Constant.SPECIAL_FLAG_1);
                                priceDO.setPromotionId(promotionId);
                                priceDO.setScalePriceId(minMaxPriceDO.getScalePriceId());
                                priceDO.setMinPrice(minPrice);
                                priceDO.setMaxPrice(maxPrice);
                                priceDOS.add(priceDO);
                            }
                            Double promotionMinPrice = 0.00;
                            Double promotionMaxPrice = 0.00;
                            if (condition.getReduceOrPresent().equals(Constant.CONDITION_PRESENT)) {
                                priceDO.setReduceOrPresent(Constant.CONDITION_PRESENT);
                                return;
                            }
                            if (condition.getReduceType().equals(Constant.REDUCE_TYPE_1)) {// 1减免
                                if (rule.getMoneyOrCount().equals(Constant.MONEY_COUNT_1)) {// 金额
                                    promotionMinPrice = minPrice.doubleValue() - condition.getReduction().doubleValue()
                                        * minPrice.doubleValue() / condition.getOneBuyMoney().doubleValue();
                                    promotionMaxPrice = maxPrice.doubleValue() - condition.getReduction().doubleValue()
                                        * maxPrice.doubleValue() / condition.getOneBuyMoney().doubleValue();
                                } else if (rule.getMoneyOrCount().equals(Constant.MONEY_COUNT_2)) {// 数量
                                    promotionMinPrice = minPrice.doubleValue()
                                        - condition.getReduction().doubleValue() / condition.getOneBuyCount();
                                    promotionMaxPrice = maxPrice.doubleValue()
                                        - condition.getReduction().doubleValue() / condition.getOneBuyCount();
                                }
                            } else {
                                promotionMinPrice =
                                    minPrice.doubleValue() * condition.getDiscount().doubleValue() * 0.01;
                                promotionMaxPrice =
                                    maxPrice.doubleValue() * condition.getDiscount().doubleValue() * 0.01;
                            }
                            if (priceDO.getPromotionMinPrice() == null || (priceDO.getPromotionMinPrice() != null
                                && priceDO.getPromotionMinPrice().doubleValue() > promotionMinPrice)) {
                                priceDO.setPromotionMinPrice(new BigDecimal(df.format(promotionMinPrice)));
                            }
                            if (priceDO.getPromotionMaxPrice() == null || (priceDO.getPromotionMaxPrice() != null
                                && priceDO.getPromotionMaxPrice().doubleValue() < promotionMaxPrice)) {
                                priceDO.setPromotionMaxPrice(new BigDecimal(df.format(promotionMaxPrice)));
                            }
                        });
                    });
                } else if (rule.getRuleType().equals(Constant.RULE_TYPE_3) && !CollectionUtils.isEmpty(rule.getIds())
                    && !CollectionUtils.isEmpty(rule.getConditions())) { // 货品
                    List<GoodsItemScalePriceDO> scalePriceDOS =
                        goodsMapper.listGoodsItemScalePriceByGoodsItemIds(rule.getIds());
                    rule.getConditions().forEach(condition -> {
                        List<GoodsConditionSpecialRpcCmd> specials = condition.getSpecials();
                        scalePriceDOS.forEach(scalePriceDO -> {
                            Double promotionPrice = null;
                            BigDecimal price = scalePriceDO.getPrice();
                            Optional<GoodsPromotionPriceDO> optional = priceDOS.stream()
                                .filter(priceDO -> priceDO.getGoodsId().equals(scalePriceDO.getGoodsId())
                                    && priceDO.getScalePriceId().equals(scalePriceDO.getGoodsItemGradeId()))
                                .findFirst();
                            GoodsPromotionPriceDO priceDO = null;
                            if (optional != null && optional.isPresent()) {
                                priceDO = optional.get();
                            } else {
                                priceDO = new GoodsPromotionPriceDO();
                                priceDO.setGoodsId(scalePriceDO.getGoodsId());
                                priceDO.setPromotionId(promotionId);
                                priceDO.setScalePriceId(scalePriceDO.getGoodsItemGradeId());
                                priceDO.setMinPrice(price);
                                priceDO.setMaxPrice(price);
                                priceDOS.add(priceDO);
                            }
                            if (condition.getSpecialFlag().equals(Constant.SPECIAL_FLAG_1)
                                && !CollectionUtils.isEmpty(specials)) {// 特价情况
                                priceDO.setSpecialFlag(Constant.SPECIAL_FLAG_1);
                                Optional<GoodsConditionSpecialRpcCmd> specialOptional = specials.stream()
                                    .filter(special -> special.getItemId().equals(scalePriceDO.getGoodsItemId()))
                                    .findFirst();
                                if (specialOptional == null || !specialOptional.isPresent()) {
                                    return;
                                }
                                GoodsConditionSpecialRpcCmd specialRpcCmd = specialOptional.get();
                                promotionPrice = specialRpcCmd.getSpecialPrice().doubleValue();
                            } else if (condition.getSpecialFlag().equals(Constant.SPECIAL_FLAG_0)) {// 非特价情况
                                priceDO.setSpecialFlag(Constant.SPECIAL_FLAG_0);
                                if (condition.getReduceOrPresent().equals(Constant.CONDITION_PRESENT)) {// 赠品
                                    priceDO.setReduceOrPresent(Constant.CONDITION_PRESENT);
                                    return;
                                }
                                if (condition.getReduceType().equals(Constant.REDUCE_TYPE_1)) {// 1减免
                                    if (rule.getMoneyOrCount().equals(Constant.MONEY_COUNT_1)) {// 金额
                                        promotionPrice = price.doubleValue() - condition.getReduction().doubleValue()
                                            * price.doubleValue() / condition.getOneBuyMoney().doubleValue();
                                    } else if (rule.getMoneyOrCount().equals(Constant.MONEY_COUNT_2)) {// 数量
                                        promotionPrice = price.doubleValue()
                                            - condition.getReduction().doubleValue() / condition.getOneBuyCount();
                                    }
                                } else {
                                    promotionPrice = price.doubleValue() * condition.getDiscount().doubleValue() * 0.01;
                                }
                            }
                            if (promotionPrice != null
                                && (priceDO.getPromotionMinPrice() == null || (priceDO.getPromotionMinPrice() != null
                                    && priceDO.getPromotionMinPrice().doubleValue() > promotionPrice))) {
                                priceDO.setPromotionMinPrice(new BigDecimal(df.format(promotionPrice)));
                                priceDO.setMinPrice(price);
                            }
                            if (promotionPrice != null
                                && (priceDO.getPromotionMaxPrice() == null || (priceDO.getPromotionMaxPrice() != null
                                    && priceDO.getPromotionMaxPrice().doubleValue() < promotionPrice))) {
                                priceDO.setPromotionMaxPrice(new BigDecimal(df.format(promotionPrice)));
                                priceDO.setMaxPrice(price);
                            }
                        });
                    });
                }
            });
            if (!CollectionUtils.isEmpty(priceDOS)) {
                promotionPriceMapper.insertList(priceDOS);
            }
        }
    }

    /**
     * 根据拼团秒杀活动计算商品最佳价格
     * 
     * @param groupSeckillRpcCmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void goodsPromotionPriceByGroupSeckill(GoodsGroupSeckillRpcCmd groupSeckillRpcCmd) {
        Integer groupSeckillId = groupSeckillRpcCmd.getId();
        Short groupSeckillType = groupSeckillRpcCmd.getGroupSeckillType();
        List<GoodsGroupSeckillGoodsRpcCmd> goodsRpcCmds = groupSeckillRpcCmd.getGoods();
        if (!CollectionUtils.isEmpty(goodsRpcCmds)) {
            DecimalFormat df = new DecimalFormat("0.00");
            List<Integer> goodsItemIds =
                goodsRpcCmds.stream().map(GoodsGroupSeckillGoodsRpcCmd::getItemId).collect(Collectors.toList());
            List<GoodsItemScalePriceDO> scalePriceDOS = goodsMapper.listGoodsItemScalePriceByGoodsItemIds(goodsItemIds);
            List<GoodsPromotionPriceDO> priceDOS = new ArrayList<>();
            scalePriceDOS.forEach(scalePriceDO -> {
                BigDecimal promotionPrice = null;
                BigDecimal price = scalePriceDO.getPrice();
                Optional<GoodsPromotionPriceDO> optional =
                    priceDOS.stream().filter(priceDO -> priceDO.getGoodsId().equals(scalePriceDO.getGoodsId())
                        && priceDO.getScalePriceId().equals(scalePriceDO.getGoodsItemGradeId())).findFirst();
                GoodsPromotionPriceDO priceDO = null;
                if (optional != null && optional.isPresent()) {
                    priceDO = optional.get();
                } else {
                    priceDO = new GoodsPromotionPriceDO();
                    priceDO.setGoodsId(scalePriceDO.getGoodsId());
                    priceDO.setGroupSeckillId(groupSeckillId);
                    priceDO.setGroupSeckillType(groupSeckillType);
                    priceDO.setScalePriceId(scalePriceDO.getGoodsItemGradeId());
                    priceDO.setMinPrice(price);
                    priceDO.setMaxPrice(price);
                    priceDOS.add(priceDO);
                }
                Optional<GoodsGroupSeckillGoodsRpcCmd> cmdOptional = goodsRpcCmds.stream()
                    .filter(goodsRpcCmd -> goodsRpcCmd.getItemId().equals(scalePriceDO.getGoodsItemId())).findFirst();
                if (cmdOptional != null && cmdOptional.isPresent()) {
                    promotionPrice = cmdOptional.get().getGroupSeckillPrice();
                }
                if (promotionPrice != null
                    && (priceDO.getPromotionMinPrice() == null || (priceDO.getPromotionMinPrice() != null
                        && priceDO.getPromotionMinPrice().doubleValue() > promotionPrice.doubleValue()))) {
                    priceDO.setPromotionMinPrice(new BigDecimal(df.format(promotionPrice)));
                    priceDO.setMinPrice(price);
                }
                if (promotionPrice != null
                    && (priceDO.getPromotionMaxPrice() == null || (priceDO.getPromotionMaxPrice() != null
                        && priceDO.getPromotionMaxPrice().doubleValue() < promotionPrice.doubleValue()))) {
                    priceDO.setPromotionMaxPrice(new BigDecimal(df.format(promotionPrice)));
                    priceDO.setMaxPrice(price);
                }
            });
            if (!CollectionUtils.isEmpty(priceDOS)) {
                promotionPriceMapper.insertList(priceDOS);
            }
        }
    }

    public void deletePromotionPriceByPromotionId(Integer promotionId) {
        promotionPriceMapper.deleteByPromotionId(promotionId);
    }

    public void deletePromotionPriceByGroupSeckillId(Integer groupSeckillId) {
        promotionPriceMapper.deleteByGroupSeckillId(groupSeckillId);
    }

    /**
     * 新增商品/货品、初始化货品库存
     * 
     * @param goodsItems
     */
    public void initGoodsStock(List<GoodsItemDO> goodsItems) {
        GoodsMsgRpcDTO rpcDTO = new GoodsMsgRpcDTO();
        List<GoodsItemErpDTO> goodsItemErpDTOS = new ArrayList<>();
        rpcDTO.setItemErpDTOList(goodsItemErpDTOS);
        goodsItems.forEach(goodsItem -> {
            rpcDTO.setGoodsId(goodsItem.getGoodsId());
            GoodsItemErpDTO goodsItemErpDTO = new GoodsItemErpDTO();
            goodsItemErpDTO.setItemCode(goodsItem.getItemCode());
            goodsItemErpDTO.setItemErpId(goodsItem.getItemErpId());
            goodsItemErpDTO.setItemId(goodsItem.getId());
            goodsItemErpDTO.setItemName(goodsItem.getItemName());
            goodsItemErpDTOS.add(goodsItemErpDTO);
        });
        com.bat.dubboapi.warehouse.common.Response response = warehouseStockServiceRpc.initGoodsStock(rpcDTO);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 删除货品、移除库存(根据货品Ids)
     * 
     * @param itemIds
     */
    public void deleteGoodsStockByItemIds(List<Integer> itemIds) {
        com.bat.dubboapi.warehouse.common.Response response = warehouseStockServiceRpc.deleteGoodsStock(itemIds);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 删除货品、移除库存(根据商品Ids)
     * 
     * @param goodsIds
     */
    public void deleteGoodsStockByGoodsIds(List<Integer> goodsIds) {
        com.bat.dubboapi.warehouse.common.Response response =
            warehouseStockServiceRpc.deleteGoodsStockByGoodsIds(goodsIds);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据商品Ids更新购物车商品状态
     * 
     * @param goodsIds
     * @param openFlag
     */
    public void changeGoodsOpenFlag(List<Integer> goodsIds, Short openFlag) {
        com.bat.dubboapi.order.common.Response response =
            shoppingCartServiceRpc.changeGoodsOpenFlag(goodsIds, openFlag);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 根据货品Ids更新购物车商品状态
     *
     * @param itemIds
     * @param openFlag
     */
    public void changeGoodsOpenFlagByItemIds(List<Integer> itemIds, Short openFlag) {
        com.bat.dubboapi.order.common.Response response =
            shoppingCartServiceRpc.changeGoodsOpenFlagByItemIds(itemIds, openFlag);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 一键导入清仓商品（栏目）
     * 
     * @param columnId
     */
    public void clearanceGoodsStoreColumn(Integer columnId) {
        // 先清除已有商品，再重新批量导入商品
        storeMapper.deleteGoodsStoreColumnByColumnId(columnId);
        List<Integer> goodsIds = goodsMapper.listClearanceGoodsId();
        List<GoodsStoreColumnDO> goodsStoreColumnDOS = GoodsConvertor.toGoodsStoreColumnDOList(goodsIds, columnId);
        if (!CollectionUtils.isEmpty(goodsStoreColumnDOS)) {
            storeMapper.createGoodsStoreColumnList(goodsStoreColumnDOS);
        }
    }

    /**
     * 一键清除商品（栏目）
     * 
     * @param columnId
     */
    public void clearGoodsStoreColumn(Integer columnId) {
        storeMapper.deleteGoodsStoreColumnByColumnId(columnId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsItemSaleStatusByIds(List<Integer> ids, Short saleStatus) {
        String logName = "";
        if (saleStatus == Constant.SALE_STATUS_1.shortValue()) {
            logName = "下架";
        }
        if (saleStatus == Constant.SALE_STATUS_3.shortValue()) {
            logName = "上架";
        }
        if (saleStatus != Constant.SALE_STATUS_3.shortValue() && saleStatus != Constant.SALE_STATUS_1.shortValue()) {
            LOGGER.info("不是上下架状态；提前退出；状态值:{}", saleStatus);
            return;
        }
        LOGGER.info("开始" + logName + "货品:{}", ids);
        goodsMapper.updateSaleStatusByItemIds(ids, saleStatus);
        // 根据货品查出商品信息
        List<Integer> goodsIds = goodsMapper.listGoodsIdsByGoodsItemIds(ids);
        LOGGER.info("当前" + logName + "货品对应的商品ids:{}", goodsIds);
        if (goodsIds.size() > 0) {
            // 对于上架；只要满足有一个货品既可以上架
            if (saleStatus == Constant.SALE_STATUS_3.shortValue()) {
                List<Integer> upperGoodIds = goodsMapper.listGoodsByIdsAndGoodsItemSaleStatus(goodsIds, saleStatus);
                LOGGER.info("当前已上架商品ids:{}", upperGoodIds);
                if (upperGoodIds.size() > 0) {
                    goodsMapper.updateGoodsSaleStatusByGoodsIds(upperGoodIds, saleStatus);
                }
            }
            // 对于下架；需要每个货品都下架；商品才能下架
            if (saleStatus == Constant.SALE_STATUS_1.shortValue()) {
                // 根据id集合与货品上下架状态查找货品其他状态的的商品
                List<Integer> notGoodIds = goodsMapper.listGoodsByIdsAndGoodsItemNotSaleStatus(goodsIds, saleStatus);
                LOGGER.info("当前非下架商品ids:{}", notGoodIds);
                // 从goodsIds过滤掉notGoodIds
                List<Integer> leftGoodsIds =
                    goodsIds.stream().filter(a -> !notGoodIds.contains(a)).collect(Collectors.toList());
                LOGGER.info("当前已下架商品ids:{}", leftGoodsIds);
                if (leftGoodsIds.size() > 0) {
                    goodsMapper.updateGoodsSaleStatusByGoodsIds(leftGoodsIds, saleStatus);
                }
            }
        }
    }
}
