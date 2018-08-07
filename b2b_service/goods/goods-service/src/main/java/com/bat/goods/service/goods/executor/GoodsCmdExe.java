package com.bat.goods.service.goods.executor;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.goods.api.goods.dto.*;
import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.brand.dataobject.BrandDO;
import com.bat.goods.dao.goods.GoodsDistributorGroupRelevanceMapper;
import com.bat.goods.dao.goods.GoodsMinMaxPriceMapper;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.manager.Tenant.TenantContext;
import com.bat.goods.manager.mq.dto.GoodsSaleDTO;
import com.bat.goods.service.common.Constant;
import com.bat.goods.service.goods.convertor.GoodsConvertor;
import com.bat.goods.service.message.MessageSendService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.dubboapi.order.order.api.OrderServiceRpc;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.dao.goods.GoodsMapper;
import com.bat.goods.dao.goods.dataobject.*;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GoodsCmdExe {

    @Resource
    private GoodsMapper mapper;

    @Resource
    private GoodsRpcCmdExe rpcCmdExe;

    @Resource
    private GoodsRpcQryExe rpcQryExe;

    @Resource
    private GoodsMinMaxPriceMapper minMaxPriceMapper;

    @Resource
    private GoodsDistributorGroupRelevanceMapper distributorGroupRelevanceMapper;

    @Resource
    private BrandMapper brandMapper;

    @DubboReference(check = false, timeout = 5000)
    private OrderServiceRpc orderServiceRpc;

    @Resource
    private MessageSendService sendService;
    @CreateCache(name = "goods.service.", cacheType = CacheType.BOTH, expire = 300)
    private Cache<String, Object> cache;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCmdExe.class);

    /**
     * 创建商品
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createGoods(GoodsCmd cmd) {
        GoodsDO goodsDO = GoodsConvertor.toGoodsDo(cmd, Constant.OPERATION_TYPE_1);
        mapper.createGoods(goodsDO);
        saveGoodsScope(goodsDO.getId(), cmd);
        saveGoodsRelevance(goodsDO.getId(), cmd);
        saveGoodsItemList(goodsDO.getId(), cmd.getGoodsItems(), Constant.OPERATION_TYPE_1);
    }

    /**
     * 保持商品关系数据（包括品牌、品类、商品、标签和商品属性等）
     * 
     * @param goodsId
     * @param cmd
     */
    private void saveGoodsRelevance(Integer goodsId, GoodsCmd cmd) {
        mapper.createGoodsClassifyRelevanceList(
            GoodsConvertor.toGoodsClassifyRelevanceDOList(goodsId, cmd.getClassifyIds()));
        if (!CollectionUtils.isEmpty(cmd.getTagIds())) {
            mapper.createGoodsTagRelevanceList(GoodsConvertor.toGoodsTagRelevanceDOList(goodsId, cmd.getTagIds()));
        }
        if (!CollectionUtils.isEmpty(cmd.getParamIds())) {
            mapper
                .createGoodsParamRelevanceList(GoodsConvertor.toGoodsParamRelevanceDOList(goodsId, cmd.getParamIds()));
        }
    }

    /**
     * 保存商品可视和不可视关系
     *
     * @param goodsId
     */
    private void saveGoodsScope(Integer goodsId, GoodsCmd cmd) {
        BrandDO brandDO = brandMapper.getById(cmd.getBrandId());
        Short brandDistributorScope = brandDO.getDistributorScope();
        Short distributorScope = cmd.getDistributorScope();
        List<Integer> brandDistributorIds = new ArrayList<>();
        if (!brandDistributorScope.equals(Constant.SCOPE_ALL)) {
            if (distributorScope.equals(Constant.SCOPE_ALL)) {
                throw GoodsException.buildException(ErrorCode.B_GOODS_DISTRIBUTOR_SCOPE_ALL_ERROR);
            }
            brandDistributorIds = rpcQryExe.getDistributorIdsByBrandId(brandDO.getId());
        }
        // 可视关系
        List list = GoodsConvertor.toGoodsRelevance(goodsId, cmd, brandDistributorScope, brandDistributorIds);
        if (list != null && list.size() > 0) {
            if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
                mapper.createGoodsScalePriceRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
                mapper.createGoodsDistributorRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
                mapper.createGoodsDepartmentRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
                mapper.createGoodsAdminRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
                distributorGroupRelevanceMapper.insertList(list);
            }
        }
        // 不可视关系
        List listNo = GoodsConvertor.toGoodsRelevanceNO(goodsId, cmd);
        if (listNo != null && listNo.size() > 0) {
            Short distributorScopeNo = cmd.getDistributorScopeNo();
            if (distributorScopeNo.equals(Constant.SCOPE_SCALE_PRICE)) {
                mapper.createGoodsScalePriceRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_DISTRIBUTOR)) {
                mapper.createGoodsDistributorRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_DEPARTMENT)) {
                mapper.createGoodsDepartmentRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_ADMIN)) {
                mapper.createGoodsAdminRelevanceNoList(listNo);
            }
        }
        // 更新分销商商品可视数据
        rpcCmdExe.distributorGoodsRelevance(goodsId, cmd.getDistributorScope(), cmd.getScalePriceIds(),
            cmd.getDistributorIds(), brandDO, cmd.getDepartmentIds(), cmd.getAdminIds(), cmd.getDistributorScopeNo(),
            cmd.getDistributorNoIds(), cmd.getDepartmentNoIds(), cmd.getScalePriceNoIds(), cmd.getAdminNoIds(),
            cmd.getDistributorGroupIds(), brandDistributorIds);
    }

    /**
     * 保存或更新货品（SKU）
     * 
     * @param goodsId
     * @param cmds
     */
    private void saveGoodsItemList(Integer goodsId, List<GoodsItemCmd> cmds, Short operationType) {
        List<GoodsItemCmd> list =
            cmds.stream().filter(cmd -> !cmd.getOperationType().equals(Constant.OPERATION_TYPE_3)).collect(toList());
        if (CollectionUtils.isEmpty(list)) {
            throw GoodsException.buildException(ErrorCode.P_GOODS_SKU_NULL);
        }
        Map<Integer, List<GoodsItemScalePriceDO>> scalePriceDOSMap = new HashMap<>();
        List<Integer> deleteIds = new ArrayList<>();
        List<GoodsItemDO> addGoodsItems = new ArrayList<>();
        List<Integer> changeItemIds = new ArrayList<>();
        cmds.forEach(item -> {
            Short itemOperationType = item.getOperationType();
            if (itemOperationType.equals(Constant.OPERATION_TYPE_3)) {
                // TODO 判断是否允许删除
                mapper.deleteGoodsItemScalePriceByGoodsItemId(item.getId());
                mapper.deleteGoodsItemSpecsColorByGoodsItemId(item.getId());
                mapper.deleteGoodsItemBoxByGoodsItemId(item.getId());
                mapper.deleteGoodsItemDataByGoodsItemId(item.getId());
                // TODO 删除其他相关基础数据（非业务数据），包括库存数据
                mapper.deleteGoodsItemById(item.getId());
                deleteIds.add(item.getId());
            } else {
                GoodsItemDO afterGoodsItemDO = GoodsConvertor.toGoodsItemDO(goodsId, item);
                if (itemOperationType.equals(Constant.OPERATION_TYPE_1)) {
                    mapper.createGoodsItem(afterGoodsItemDO);
                    addGoodsItems.add(afterGoodsItemDO);
                } else {
                    GoodsItemDO beforeGoodsItemDO = mapper.getGoodsItemById(item.getId());
                    if (beforeGoodsItemDO.getSaleTime() != null) {
                        afterGoodsItemDO.setSaleTime(beforeGoodsItemDO.getSaleTime());
                    }
                    mapper.updateGoodsItem(afterGoodsItemDO);
                    changeItemIds.add(afterGoodsItemDO.getId());
                }
                List<GoodsItemSpecsColorDO> specsColorDOS = GoodsConvertor
                    .toGoodsItemSpecsColorDOList(afterGoodsItemDO.getId(), item.getSpecsId(), item.getColorId());
                if (!CollectionUtils.isEmpty(specsColorDOS)) {
                    mapper.createGoodsItemSpecsColorList(specsColorDOS);
                }
                // 先删除历史的商品等级价格，再保持新商品等级价格
                List<GoodsItemScalePriceDO> scalePriceDOS = GoodsConvertor.toGoodsItemScalePriceDOList(goodsId,
                    afterGoodsItemDO.getId(), item.getScalePrices());
                mapper.deleteGoodsItemScalePriceByGoodsItemId(afterGoodsItemDO.getId());
                mapper.createGoodsItemScalePriceList(scalePriceDOS);
                // 组装价格等级
                scalePriceDOS.forEach(scalePriceDO -> {
                    List<GoodsItemScalePriceDO> tmpScalePriceDOS =
                        scalePriceDOSMap.get(scalePriceDO.getGoodsItemGradeId());
                    if (tmpScalePriceDOS == null) {
                        tmpScalePriceDOS = new ArrayList<>();
                        scalePriceDOSMap.put(scalePriceDO.getGoodsItemGradeId(), tmpScalePriceDOS);
                    }
                    tmpScalePriceDOS.add(scalePriceDO);
                });
                if (!CollectionUtils.isEmpty(item.getBoxs()) && itemOperationType.equals(Constant.OPERATION_TYPE_1)) {
                    mapper.createGoodsItemBoxList(
                        GoodsConvertor.toGoodsItemBoxDOList(afterGoodsItemDO.getId(), item.getBoxs()));
                }
                if (item.getData() != null && itemOperationType.equals(Constant.OPERATION_TYPE_1)) {
                    mapper.createGoodsItemData(
                        GoodsConvertor.toGoodsItemDataDO(afterGoodsItemDO.getId(), item.getData()));
                }
            }
        });
        // 商品新增或删除货品时，统计商品对应价格等级最低最高价格
        if (operationType.equals(Constant.OPERATION_TYPE_1) || !CollectionUtils.isEmpty(deleteIds)) {
            updateGoodsMinMaxPriceData(scalePriceDOSMap, goodsId);
        }
        // 初始化货品库存
        if (!CollectionUtils.isEmpty(addGoodsItems)) {
            rpcCmdExe.initGoodsStock(addGoodsItems);
        }
        // 删除货品、移除库存、更新购物车商品数据为失效
        if (!CollectionUtils.isEmpty(deleteIds)) {
            rpcCmdExe.deleteGoodsStockByItemIds(deleteIds);
            rpcCmdExe.changeGoodsOpenFlagByItemIds(deleteIds, Constant.OPEN_NO);
        }
        // 发送更新货品自动上下架消息
        if (!CollectionUtils.isEmpty(changeItemIds)) {
            sendService.goodsItemSaleStatus(changeItemIds);
        }
    }

    /**
     * 统计商品对应价格等级最低最高价格
     * 
     * @param scalePriceDOMap
     * @param goodsId
     */
    private void updateGoodsMinMaxPriceData(Map<Integer, List<GoodsItemScalePriceDO>> scalePriceDOMap,
        Integer goodsId) {
        minMaxPriceMapper.deleteByGoodsId(goodsId);
        List<GoodsMinMaxPriceDO> minMaxPriceDOS = new ArrayList<>();
        List<GoodsItemScalePriceDO> allScalePriceDO = new ArrayList<>();
        /** 获取所有最大最小（分等级） */
        scalePriceDOMap.forEach((k, v) -> {
            List<BigDecimal> prices = v.stream()
                .filter(goodsItemScalePriceDO -> goodsItemScalePriceDO.getPrice() != null
                    && goodsItemScalePriceDO.getPrice().compareTo(new BigDecimal(0)) != 0)
                .map(GoodsItemScalePriceDO::getPrice).collect(toList());
            if (!CollectionUtils.isEmpty(prices)) {
                GoodsMinMaxPriceDO minMaxPriceDO = new GoodsMinMaxPriceDO();
                Optional<BigDecimal> max = prices.stream().max(Comparator.comparing(Function.identity()));
                Optional<BigDecimal> min = prices.stream().min(Comparator.comparing(Function.identity()));
                minMaxPriceDO.setGoodsId(goodsId);
                minMaxPriceDO.setScalePriceId(k);
                minMaxPriceDO.setMaxPrice(max.get());
                minMaxPriceDO.setMinPrice(min.get());
                minMaxPriceDOS.add(minMaxPriceDO);
            }
            allScalePriceDO.addAll(v);
        });
        /** 获取所有最大最小（不分等级） */
        GoodsMinMaxPriceDO minMaxPriceDO = new GoodsMinMaxPriceDO();
        minMaxPriceDO.setScalePriceId(0);
        minMaxPriceDO.setGoodsId(goodsId);
        if (!CollectionUtils.isEmpty(minMaxPriceDOS)) {
            Optional<BigDecimal> max = minMaxPriceDOS.stream().map(GoodsMinMaxPriceDO::getMaxPrice)
                .max(Comparator.comparing(Function.identity()));
            Optional<BigDecimal> min = minMaxPriceDOS.stream().map(GoodsMinMaxPriceDO::getMinPrice)
                .min(Comparator.comparing(Function.identity()));
            minMaxPriceDO.setMaxPrice(max.get());
            minMaxPriceDO.setMinPrice(min.get());
        } else {
            minMaxPriceDO.setMinPrice(BigDecimal.ZERO);
            minMaxPriceDO.setMaxPrice(BigDecimal.ZERO);
        }
        minMaxPriceDOS.add(minMaxPriceDO);
        minMaxPriceMapper.createList(minMaxPriceDOS);
    }

    /**
     * 更新商品
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(GoodsCmd cmd) {
        GoodsDO beforeGoodsDO = mapper.getGoodsById(cmd.getId());
        GoodsDO afterGoodsDO = GoodsConvertor.toGoodsDo(cmd, Constant.OPERATION_TYPE_2);
        if (beforeGoodsDO.getSaleTime() != null) {
            afterGoodsDO.setSaleTime(beforeGoodsDO.getSaleTime());
        }
        mapper.updateGoods(afterGoodsDO);
        deleteGoodsRelevance(afterGoodsDO.getId());
        saveGoodsRelevance(afterGoodsDO.getId(), cmd);
        deleteGoodsScope(afterGoodsDO.getId());
        saveGoodsScope(afterGoodsDO.getId(), cmd);
        saveGoodsItemList(afterGoodsDO.getId(), cmd.getGoodsItems(), Constant.OPERATION_TYPE_2);
    }

    /**
     * 删除商品关联关系
     * 
     * @param goodsId
     */
    private void deleteGoodsRelevance(Integer goodsId) {
        mapper.deleteGoodsClassifyRelevanceByGoodsId(goodsId);
        mapper.deleteGoodsTagRelevanceByGoodsId(goodsId);
        mapper.deleteGoodsParamRelevanceByGoodsId(goodsId);
    }

    /**
     * 删除商品可视和不可视关系
     *
     * @param goodsId
     */
    private void deleteGoodsScope(Integer goodsId) {
        mapper.deleteGoodsScalePriceRelevanceByGoodsId(goodsId);
        mapper.deleteGoodsScalePriceRelevanceNoByGoodsId(goodsId);
        mapper.deleteGoodsDistributorRelevanceByGoodsId(goodsId);
        mapper.deleteGoodsDistributorRelevanceNoByGoodsId(goodsId);
        mapper.deleteGoodsDepartmentRelevanceByGoodsId(goodsId);
        mapper.deleteGoodsDepartmentRelevanceNoByGoodsId(goodsId);
        mapper.deleteGoodsAdminRelevanceByGoodsId(goodsId);
        mapper.deleteGoodsAdminRelevanceNoByGoodsId(goodsId);
        distributorGroupRelevanceMapper.deleteByGoodsId(goodsId);
    }

    /**
     * 上下架商品（SPU）
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void saleStatusGoods(GoodsSaleStatusCmd cmd) {
        GoodsDO goodsDO = mapper.getGoodsById(cmd.getId());
        if (goodsDO == null) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        // 先上下架所有的货品（SKU） TODO 看是否有审批机制
        if (cmd.getSaleStatus().equals(Constant.SALE_STATUS_3) && goodsDO.getSaleTime() == null) {
            Date date = new Date(System.currentTimeMillis());
            goodsDO.setSaleTime(date);
        }
        mapper.changeGoodsItemSaleStatusByGoodsId(cmd.getId(), cmd.getSaleStatus());
        mapper.changeGoodsSaleStatusById(cmd.getId(), cmd.getSaleStatus(), goodsDO.getSaleTime());
        // 修改购物车所有货品的状态
        Integer cartItemStatus = cmd.getSaleStatus() == 1 ? 0 : 1;
        orderServiceRpc.updateShoppingCartItemStatus(cmd.getId(), cartItemStatus);
    }

    /**
     * 上下架货品（SKU）
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void saleStatusGoodsItem(GoodsItemSaleStatusCmd cmd) {
        GoodsItemDO goodsItemDO = mapper.getGoodsItemById(cmd.getId());
        if (goodsItemDO == null) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        // TODO 看是否有审批机制
        // 货品下架时，如果商品对应的所有货品都下架时商品也需下架
        if (cmd.getSaleStatus().equals(Constant.SALE_STATUS_1)) {
            Integer count = mapper.listGoodsItemCountBySaleStatusNotInAndGoodsItemId(cmd.getId(), cmd.getSaleStatus());
            if (count == null || count.intValue() == 0 || count == 1) {
                mapper.changeGoodsItemAndGoodsSaleStatusByGoodsItemId(cmd.getId(), cmd.getSaleStatus());
            } else {
                mapper.changeGoodsItemSaleStatusById(cmd.getId(), cmd.getSaleStatus());
            }
        } else if (cmd.getSaleStatus().equals(Constant.SALE_STATUS_3)) {
            mapper.changeGoodsItemAndGoodsSaleStatusByGoodsItemId(cmd.getId(), cmd.getSaleStatus());
        }
    }

    /**
     * 冻结解冻商品
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void freezeStatusGoods(GoodsFreezeStatusCmd cmd) {
        GoodsDO goodsDO = mapper.getGoodsById(cmd.getId());
        if (goodsDO == null) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        // 下架的商品才允许冻结
        if (!goodsDO.getSaleStatus().equals(Constant.SALE_STATUS_1)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_FREEZE_STATUS_ERROR);
        }
        mapper.changeGoodsFreezeStatusById(cmd.getId(), cmd.getFreezeStatus());
    }

    /**
     * 删除商品
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoods(GoodsId cmd) {
        GoodsDO goodsDO = mapper.getGoodsById(cmd.getId());
        if (goodsDO == null) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        /** 冻结的商品才允许删除 */
        if (!goodsDO.getFreezeStatus().equals(Constant.FREEZE_STATUS_2)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_DELETE_ERROR);
        }
        deleteGoodsItemByGoodsId(cmd.getId());
        deleteGoodsRelevance(cmd.getId());
        deleteGoodsScope(cmd.getId());
        deleteGoodsData(cmd.getId());
        mapper.deleteGoods(cmd.getId());
    }

    /**
     * 删除商品数据
     * 
     * @param goodsId
     */
    private void deleteGoodsData(Integer goodsId) {
        // 删除商品中每个价格等级最小和最大价数据
        mapper.deleteGoodsMinMaxPriceByGoodsId(goodsId);
        // 删除商品中每个价格等级最小和最大活动价数据
        mapper.deleteGoodsPromotionPriceByGoodsId(goodsId);
        // 删除商品扩展数据
        mapper.deleteGoodsSaleDataByGoodsId(goodsId);
        // 删除货品在库库存是否缺货标记数据
        mapper.deleteGoodsStockFlagByGoodsId(goodsId);
        // 删除栏目商品关联数据
        mapper.deleteGoodsStoreColumnByGoodsId(goodsId);
        // 删除移动端商店配置商品关联数据
        mapper.deleteGoodsStoreMobileByGoodsId(goodsId);
        // 删除板块商品关联数据
        mapper.deleteGoodsStoreSectionByGoodsId(goodsId);
    }

    /**
     * 根据商品id删除货品（SKU）
     * 
     * @param goodsId
     */
    private void deleteGoodsItemByGoodsId(Integer goodsId) {
        List<Integer> goodsItemIds = mapper.listGoodsItemIdByGoodsId(goodsId);
        // TODO 判断是否允许删除(如果订单测试验证)
        // 如果订单未关闭且还有未发货商品，不允许删除
        mapper.deleteGoodsItemBoxByGoodsItemIds(goodsItemIds);
        mapper.deleteGoodsItemScalePriceByGoodsItemIds(goodsItemIds);
        mapper.deleteGoodsItemSpecsColorByGoodsItemIds(goodsItemIds);
        mapper.deleteGoodsItemDataByGoodsItemIds(goodsItemIds);
        mapper.deleteGoodsItemByGoodsId(goodsId);
        List<Integer> goodsIds = new ArrayList<>();
        goodsIds.add(goodsId);
        // 删除库存数据（包括缓存）
        rpcCmdExe.deleteGoodsStockByGoodsIds(goodsIds);
        // 更新购物车商品数据为失效
        rpcCmdExe.changeGoodsOpenFlag(goodsIds, Constant.OPEN_NO);
        // 删除分销商商品可视数据
        rpcCmdExe.distributorGoodsRelevance(goodsId, Constant.SCOPE_DISTRIBUTOR, null, null, null, null, null, null, null, null,
            null, null, null, new ArrayList<>());

    }

    /**
     * 设置是否按装箱数量售卖
     * 
     * @param cmds
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsItemBoxs(List<GoodsItemBoxCmd> cmds) {
        List<GoodsItemBoxDO> goodsItemBoxDOS = GoodsConvertor.toGoodsItemBoxDOList(cmds);
        List<GoodsItemBoxDO> beforeGoodsItemBoxDOS = mapper.listGoodsItemBoxByGoodsItemId(cmds.get(0).getGoodsItemId());
        List<GoodsItemBoxDO> changeGoodsItemBoxDOS = new ArrayList<>();
        goodsItemBoxDOS.forEach(goodsItemBoxDO -> {
            if (goodsItemBoxDO.getDefaultFlag().equals(Constant.DEFAULT_FLAG_1)) {
                if (!CollectionUtils.isEmpty(beforeGoodsItemBoxDOS)) {
                    Optional<GoodsItemBoxDO> first = beforeGoodsItemBoxDOS.stream()
                        .filter(beforeGoodsItemBoxDO -> beforeGoodsItemBoxDO.getDefaultFlag().equals(Constant.DEFAULT_FLAG_1))
                        .findFirst();
                    if (first != null && first.isPresent()) {
                        GoodsItemBoxDO changeBox = first.get();
                        if (!changeBox.getGoodsItemId().equals(goodsItemBoxDO.getGoodsItemId())) {
                            changeBox.setDefaultFlag(Constant.DEFAULT_FLAG_0);
                            changeGoodsItemBoxDOS.add(changeBox);
                        }
                    }
                }
            }
        });
        goodsItemBoxDOS.addAll(changeGoodsItemBoxDOS);
        mapper.updateGoodsItemBoxs(goodsItemBoxDOS);
    }

    /**
     * 批量同步商品价格
     * 
     * @param cmd
     */
    public void syncBatchGoodsPrice(BaseIds cmd) {
        List<GoodsItemSyncDO> goodsItemSyncDOS = mapper.listGoodsItemSyncByGoodsIds(cmd.getIds());
        if (CollectionUtils.isEmpty(goodsItemSyncDOS)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        List<GoodsItemPriceRpcDTO> rpcDTOS = rpcQryExe.listGoodsItemPriceErp(goodsItemSyncDOS);
        LOGGER.info("指定商品同步价格数据:{}", JSONObject.toJSONString(rpcDTOS));
        List<GoodsItemScalePriceDO> scalePriceDOS = mapper.listGoodsItemScalePriceByGoodsIds(cmd.getIds());
        updateGoodsPrice(goodsItemSyncDOS, rpcDTOS, scalePriceDOS, Constant.UPDATE_GOODS_PRICE_ORIGIN_APPOINT);
    }

    /**
     * 全量同步商品价格
     */
    public void syncAllGoodsPriceMessage() {
        String syncGoodsFlag = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsPrice");
        // 价格正在同步
        if (StringUtils.isNotBlank(syncGoodsFlag)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_SYNCING_PRICE);
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsPrice", "syncAllGoodsPrice", 300,
                TimeUnit.SECONDS);
        }
        sendService.syncAllGoodsPrice();
    }

    /**
     * 全量同步商品价格
     */
    @Async
    public void syncAllGoodsPrice(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        String syncGoodsFlagIng = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsPriceIng");
        // 正在全量同步货品装箱信息
        if (StringUtils.isNotBlank(syncGoodsFlagIng)) {
            return;
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsPriceIng", "syncAllGoodsPriceIng", 300,
                TimeUnit.SECONDS);
        }
        try {
            Integer page = 1;
            Integer count = 100;
            List<GoodsItemSyncDO> goodsItemSyncDOS = mapper.listGoodsItemSync((page - 1) * count, count);
            while (!CollectionUtils.isEmpty(goodsItemSyncDOS)) {
                List<GoodsItemPriceRpcDTO> rpcDTOS = rpcQryExe.listGoodsItemPriceErp(goodsItemSyncDOS);
                List<Integer> goodsIds =
                    goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getGoodsId).distinct().collect(toList());
                List<GoodsItemScalePriceDO> scalePriceDOS = mapper.listGoodsItemScalePriceByGoodsIds(goodsIds);
                updateGoodsPrice(goodsItemSyncDOS, rpcDTOS, scalePriceDOS, Constant.UPDATE_GOODS_PRICE_ORIGIN_ALL);
                page++;
                goodsItemSyncDOS = mapper.listGoodsItemSync((page - 1) * count, count);
            }
        } finally {
            // 不管是否成功都结束同步标识
            if (StringUtils.isBlank(syncGoodsFlagIng)) {
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsPrice");
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsPriceIng");
            }
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 更新商品价格
     *
     * @param rpcDTOS
     * @param scalePriceDOS
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsPrice(List<GoodsItemSyncDO> goodsItemSyncDOS, List<GoodsItemPriceRpcDTO> rpcDTOS,
        List<GoodsItemScalePriceDO> scalePriceDOS, Short origin) {
        List<GoodsItemScalePriceDO> addScalePriceDOS = new ArrayList<>();
        List<GoodsItemScalePriceDO> updateScalePriceDOS = new ArrayList<>();
        List<GoodsItemSyncDO> updateGoodsItemSyncDOS = new ArrayList<>();
        GoodsConvertor.toUpdateGoodsPrice(goodsItemSyncDOS, rpcDTOS, scalePriceDOS, updateGoodsItemSyncDOS,
            addScalePriceDOS, updateScalePriceDOS);
        // 更新默认价和成本价
        if (!CollectionUtils.isEmpty(updateGoodsItemSyncDOS)) {
            mapper.updateListGoodsItemSync(updateGoodsItemSyncDOS);
        }
        // 更新货品价格
        if (!CollectionUtils.isEmpty(updateScalePriceDOS)) {
            mapper.updateListGoodsItemScalePrice(updateScalePriceDOS);
        }
        // 新增货品价格
        if (!CollectionUtils.isEmpty(addScalePriceDOS)) {
            mapper.createGoodsItemScalePriceList(addScalePriceDOS);
            scalePriceDOS.addAll(addScalePriceDOS);
        }
        // 更新商品最大最小价格
        // 如果在商品详情中更新 不管阶梯价一不一致 直接更新
        if (origin.equals(Constant.UPDATE_GOODS_PRICE_ORIGIN_APPOINT)) {
            List<Integer> goodsIds = goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getGoodsId).collect(toList());
            List<GoodsItemScalePriceDO> goodsItemScalePriceDOS = mapper.listGoodsItemScalePriceByGoodsIds(goodsIds);
            updateGoodsPrice(goodsItemScalePriceDOS);
        } else {
            // 批量更新
            if (!CollectionUtils.isEmpty(updateScalePriceDOS) || !CollectionUtils.isEmpty(addScalePriceDOS)) {
                updateScalePriceDOS.addAll(addScalePriceDOS);
                // 通过更新集合（阶梯价不全） 商品id 反查阶梯价（补全）
                List<Integer> goodsIds =
                    updateScalePriceDOS.stream().map(GoodsItemScalePriceDO::getGoodsId).collect(toList());
                List<GoodsItemScalePriceDO> updateScalePriceDOSFixBug =
                    mapper.listGoodsItemScalePriceByGoodsIds(goodsIds);
                // addScalePriceDOS.addAll(updateScalePriceDOS);
                updateGoodsPrice(updateScalePriceDOSFixBug);
            }
        }
    }

    /**
     * 更新商品最小最大价格
     * 
     * @param scalePriceDOS
     */
    private void updateGoodsPrice(List<GoodsItemScalePriceDO> scalePriceDOS) {
        List<Integer> goodsIds =
            scalePriceDOS.stream().map(GoodsItemScalePriceDO::getGoodsId).distinct().collect(toList());
        goodsIds.forEach(goodsId -> {
            Map<Integer, List<GoodsItemScalePriceDO>> scalePriceDOSMap =
                scalePriceDOS.stream().filter(scalePriceDO -> scalePriceDO.getGoodsId().equals(goodsId))
                    .collect(Collectors.groupingBy(GoodsItemScalePriceDO::getGoodsItemGradeId));
            updateGoodsMinMaxPriceData(scalePriceDOSMap, goodsId);
        });
    }

    /**
     * 同步货品信息
     * 
     * @param cmd
     */
    public void syncBatchGoodsItem(BaseIds cmd) {
        List<GoodsItemDO> goodsItemDOS = mapper.listGoodsItemByGoodsIds(cmd.getIds());
        if (CollectionUtils.isEmpty(goodsItemDOS)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        List<Integer> itemErpIds = goodsItemDOS.stream().map(GoodsItemDO::getItemErpId).collect(toList());
        List<GoodsItemErpRpcDTO> itemErpRpcDTOS = rpcQryExe.listGoodsItemErp(itemErpIds);
        if (!CollectionUtils.isEmpty(itemErpRpcDTOS)) {

        }
        updateGoodsItem(goodsItemDOS, itemErpRpcDTOS);
    }

    /**
     * 全量同步货品信息
     */
    public void syncAllGoodsItemMessage() {
        String syncGoodsFlag = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsItem");
        // 价格正在同步
        if (StringUtils.isNotBlank(syncGoodsFlag)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_SYNCING_INFO);
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsItem", "syncAllGoodsItem", 300,
                TimeUnit.SECONDS);
        }
        sendService.syncAllGoodsItem();
    }

    /**
     * 全量同步商品信息
     */
    @Async
    public void syncAllGoodsItem(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        String syncGoodsFlagIng = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemIng");
        // 正在全量同步货品装箱信息
        if (StringUtils.isNotBlank(syncGoodsFlagIng)) {
            return;
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemIng", "syncAllGoodsItemIng", 300,
                TimeUnit.SECONDS);
        }
        try {
            Integer page = 1;
            Integer count = 100;
            List<GoodsItemDO> goodsItemDOS = mapper.listGoodsItemPage((page - 1) * count, count);
            while (!CollectionUtils.isEmpty(goodsItemDOS)) {
                List<Integer> itemErpIds = goodsItemDOS.stream().map(GoodsItemDO::getItemErpId).collect(toList());
                List<GoodsItemErpRpcDTO> itemErpRpcDTOS = rpcQryExe.listGoodsItemErp(itemErpIds);
                updateGoodsItem(goodsItemDOS, itemErpRpcDTOS);
                page++;
                goodsItemDOS = mapper.listGoodsItemPage((page - 1) * count, count);
            }
        } finally {
            // 不管是否成功都结束同步标识
            if (StringUtils.isBlank(syncGoodsFlagIng)) {
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsItem");
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemIng");
            }
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 更新商品信息
     * 
     * @param goodsItemDOS
     * @param itemErpRpcDTOS
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsItem(List<GoodsItemDO> goodsItemDOS, List<GoodsItemErpRpcDTO> itemErpRpcDTOS) {
        List<GoodsItemDO> updateGoodsItemDOS = GoodsConvertor.toUpdateGoodsItem(goodsItemDOS, itemErpRpcDTOS);
        if (!CollectionUtils.isEmpty(updateGoodsItemDOS)) {
            mapper.updateGoodsItemList(updateGoodsItemDOS);
            // TODO 发送商品货品更新信息
        }
    }

    /**
     * 同步货品装箱信息
     *
     * @param cmd
     */
    public void syncBatchGoodsItemBox(BaseIds cmd) {
        List<GoodsItemSyncDO> goodsItemSyncDOS = mapper.listGoodsItemSyncByGoodsIds(cmd.getIds());
        if (CollectionUtils.isEmpty(goodsItemSyncDOS)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_NUll);
        }
        List<Integer> itemErpIds = goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getItemErpId).collect(toList());
        List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS = rpcQryExe.listGoodsItemBoxErp(itemErpIds);
        List<Integer> goodsItemIds = goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getId).collect(toList());
        List<GoodsItemBoxDO> goodsItemBoxDOS = mapper.listGoodsItemBoxByGoodsItemIds(goodsItemIds);
        updateGoodsItemBoxs(goodsItemSyncDOS, goodsItemBoxDOS, goodsItemBoxRpcDTOS);
    }

    /**
     * 全量同步货品装箱信息
     */
    public void syncAllGoodsItemBoxMessage() {
        String syncGoodsFlag = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemBox");
        // 价格正在同步
        if (StringUtils.isNotBlank(syncGoodsFlag)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_SYNCING_BOX);
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemBox", "syncAllGoodsItemBox", 300,
                TimeUnit.SECONDS);
        }
        sendService.syncAllGoodsItemBox();
    }

    /**
     * 全量同步货品装箱信息
     */
    @Async
    public void syncAllGoodsItemBox(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        String syncGoodsFlagIng = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemBoxIng");
        // 正在全量同步货品装箱信息
        if (StringUtils.isNotBlank(syncGoodsFlagIng)) {
            return;
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemBoxIng", "syncAllGoodsItemBoxIng", 300,
                TimeUnit.SECONDS);
        }
        try {
            Integer page = 1;
            Integer count = 100;
            List<GoodsItemSyncDO> goodsItemSyncDOS = mapper.listGoodsItemSync((page - 1) * count, count);
            while (!CollectionUtils.isEmpty(goodsItemSyncDOS)) {
                List<Integer> itemErpIds =
                    goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getItemErpId).collect(toList());
                List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS = rpcQryExe.listGoodsItemBoxErp(itemErpIds);
                List<Integer> goodsItemIds = goodsItemSyncDOS.stream().map(GoodsItemSyncDO::getId).collect(toList());
                List<GoodsItemBoxDO> goodsItemBoxDOS = mapper.listGoodsItemBoxByGoodsItemIds(goodsItemIds);
                updateGoodsItemBoxs(goodsItemSyncDOS, goodsItemBoxDOS, goodsItemBoxRpcDTOS);
                page++;
                goodsItemSyncDOS = mapper.listGoodsItemSync((page - 1) * count, count);
            }
        } finally {
            // 不管是否成功都结束同步标识
            if (StringUtils.isBlank(syncGoodsFlagIng)) {
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemBox");
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsItemBoxIng");
            }
            TenantContext.removeTenantNo();
        }
    }

    /**
     * 更新货品装箱信息
     *
     * @param goodsItemBoxDOS
     * @param goodsItemBoxRpcDTOS
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsItemBoxs(List<GoodsItemSyncDO> goodsItemSyncDOS, List<GoodsItemBoxDO> goodsItemBoxDOS,
        List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS) {
        List<GoodsItemBoxDO> updateGoodsItemBoxDOS = new ArrayList<>();
        List<GoodsItemBoxDO> addGoodsItemBoxDOS = new ArrayList<>();
        GoodsConvertor.toUpdateGoodsItemBox(goodsItemSyncDOS, goodsItemBoxDOS, goodsItemBoxRpcDTOS,
            updateGoodsItemBoxDOS, addGoodsItemBoxDOS);
        if (!CollectionUtils.isEmpty(updateGoodsItemBoxDOS)) {
            mapper.updateGoodsItemBoxs(updateGoodsItemBoxDOS);
        }
        if (!CollectionUtils.isEmpty(addGoodsItemBoxDOS)) {
            mapper.createGoodsItemBoxList(addGoodsItemBoxDOS);
        }
    }

    /**
     * 刷新商品最小最大价格
     */
    @Transactional(rollbackFor = Exception.class)
    public void allGoodsPrice() {
        List<Integer> goodsIds = mapper.allGoodsIds();
        List<GoodsItemScalePriceDO> scalePriceDOS = mapper.listGoodsItemScalePriceByGoodsIds(goodsIds);
        updateGoodsPrice(scalePriceDOS);
    }

    /**
     * 全量刷新商品可视范围
     */
    public void syncAllGoodsScopeMessage() {
        String syncGoodsFlag = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsScope");
        // 价格正在同步
        if (StringUtils.isNotBlank(syncGoodsFlag)) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_SYNCING_SCOPE);
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsScope", "syncAllGoodsScope", 300,
                TimeUnit.SECONDS);
        }
        sendService.syncAllGoodsScope();
    }

    /**
     * 刷新商品可视范围
     */
    @Async
    public void syncAllGoodsScope(String tenantNo) {
        TenantContext.setTenantNo(tenantNo);
        try {
            syncAllGoodsScope();
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncAllGoodsScope() {
        String syncGoodsFlagIng = (String)cache.get(TenantContext.getTenantNo() + ":" + "syncAllGoodsScopeIng");
        // 正在刷新商品可视范围
        if (StringUtils.isNotBlank(syncGoodsFlagIng)) {
            return;
        } else {
            cache.put(TenantContext.getTenantNo() + ":" + "syncAllGoodsScopeIng", "syncAllGoodsScopeIng", 300,
                TimeUnit.SECONDS);
        }
        try {
            List<GoodsScopeDO> goodsScopeDOS = mapper.allGoodsScope();
            goodsScope(goodsScopeDOS);
        } finally {
            // 不管是否成功都结束同步标识
            if (StringUtils.isBlank(syncGoodsFlagIng)) {
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsScope");
                cache.remove(TenantContext.getTenantNo() + ":" + "syncAllGoodsScopeIng");
            }
        }
    }

    /**
     * 刷新分销商商品可视范围
     * 
     * @param goodsScopeDOS
     */
    public void goodsScope(List<GoodsScopeDO> goodsScopeDOS) {
        if (!CollectionUtils.isEmpty(goodsScopeDOS)) {
            goodsScopeDOS.forEach(goodsScopeDO -> {
                BrandDO brandDO = brandMapper.getById(goodsScopeDO.getBrandId());
                Short brandDistributorScope = brandDO.getDistributorScope();
                List<Integer> brandDistributorIds = new ArrayList<>();
                if (!brandDistributorScope.equals(Constant.SCOPE_ALL)) {
                    brandDistributorIds = rpcQryExe.getDistributorIdsByBrandId(brandDO.getId());
                }
                List<Integer> scalePriceIds = new ArrayList<>();
                List<Integer> distributorIds = new ArrayList<>();
                List<Integer> departmentIds = new ArrayList<>();
                List<Integer> adminIds = new ArrayList<>();
                List<Integer> distributorNoIds = new ArrayList<>();
                List<Integer> departmentNoIds = new ArrayList<>();
                List<Integer> scalePriceNoIds = new ArrayList<>();
                List<Integer> adminNoIds = new ArrayList<>();
                List<Integer> distributorGroupIds = new ArrayList<>();
                if (goodsScopeDO.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE)) {
                    scalePriceIds = mapper.listGoodsScalePriceRelevanceId(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR)) {
                    distributorIds = mapper.listGoodsDistributorRelevanceId(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScope().equals(Constant.SCOPE_DEPARTMENT)) {
                    departmentIds = mapper.listGoodsDepartmentRelevanceId(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScope().equals(Constant.SCOPE_ADMIN)) {
                    adminIds = mapper.listGoodsAdminRelevanceId(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
                    distributorGroupIds = mapper.listGoodsDistributorGroupRelevanceId(goodsScopeDO.getId());
                }
                if (goodsScopeDO.getDistributorScopeNo().equals(Constant.SCOPE_SCALE_PRICE)) {
                    scalePriceNoIds = mapper.listGoodsScalePriceRelevanceIdNo(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)) {
                    distributorNoIds = mapper.listGoodsDistributorRelevanceIdNo(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScopeNo().equals(Constant.SCOPE_DEPARTMENT)) {
                    departmentNoIds = mapper.listGoodsDepartmentRelevanceIdNo(goodsScopeDO.getId());
                } else if (goodsScopeDO.getDistributorScopeNo().equals(Constant.SCOPE_ADMIN)) {
                    adminNoIds = mapper.listGoodsAdminRelevanceIdNo(goodsScopeDO.getId());
                }
                rpcCmdExe.distributorGoodsRelevance(goodsScopeDO.getId(), goodsScopeDO.getDistributorScope(),
                    scalePriceIds, distributorIds, brandDO, departmentIds, adminIds,
                    goodsScopeDO.getDistributorScopeNo(), distributorNoIds, departmentNoIds, scalePriceNoIds,
                    adminNoIds, distributorGroupIds, brandDistributorIds);
            });
        }
    }

    /**
     * 根据品牌id更新分销商商品可视范围
     * 
     * @param brandId
     */
    @Async
    public void syncBrandGoodsScope(String tenantNo, Integer brandId) {
        TenantContext.setTenantNo(tenantNo);
        try {
            syncBrandGoodsScope(brandId);
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncBrandGoodsScope(Integer brandId) {
        List<GoodsScopeDO> goodsScopeDOS = mapper.listGoodsScopeByBrandId(brandId);
        goodsScope(goodsScopeDOS);
    }

    /**
     * 更新商品销量
     * 
     * @param saleDTOS
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsSaleData(List<GoodsSaleDTO> saleDTOS) {
        List<Integer> goodsIds = saleDTOS.stream().map(GoodsSaleDTO::getGoodsId).distinct().collect(toList());
        List<GoodsSaleDataDO> goodsSaleDataDOS = mapper.listGoodsSaleDataByGoodsIds(goodsIds);
        List<GoodsSaleDataDO> updateGoodsSaleDataDOS = new ArrayList<>();
        List<GoodsSaleDataDO> addGoodsSaleDataDOS = new ArrayList<>();
        GoodsConvertor.toGoodsSaleDataDOList(goodsSaleDataDOS, updateGoodsSaleDataDOS, addGoodsSaleDataDOS, saleDTOS);
        if (!CollectionUtils.isEmpty(updateGoodsSaleDataDOS)) {
            mapper.updateListGoodsSaleData(updateGoodsSaleDataDOS);
        }
        if (!CollectionUtils.isEmpty(addGoodsSaleDataDOS)) {
            mapper.createGoodsSaleDataList(addGoodsSaleDataDOS);
        }
    }
}
