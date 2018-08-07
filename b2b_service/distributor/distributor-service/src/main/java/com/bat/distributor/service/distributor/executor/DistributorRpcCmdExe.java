package com.bat.distributor.service.distributor.executor;

import static com.bat.distributor.service.common.Constant.OPERATION_TYPE_3;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.service.distributor.convertor.DistributorConvertor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.service.Message.MessageSendService;
import com.bat.dubboapi.distributor.distributor.dto.ChangeBrandRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorChangeBrandRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.goods.brand.api.GoodsBrandServiceRpc;
import com.bat.dubboapi.goods.brand.dto.BrandDistributorRpcCmd;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.BrandScaleRpc;
import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.promotion.dto.data.PromotionDistributorRpcDTO;
import com.bat.dubboapi.system.check.api.SystemCheckServiceRpc;
import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/5/6 14:12
 */
@Slf4j
@Component
public class DistributorRpcCmdExe {

    @DubboReference(check = false, timeout = 5000)
    private GoodsBrandServiceRpc brandServiceRpc;
    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;
    @DubboReference(check = false, timeout = 5000)
    private PromotionServiceDistributorRpc promotionServiceDistributorRpc;

    @Resource
    private DistributorBrandRelevanceMapper brandRelevanceMapper;
    @Resource
    private DistributorCategoryRelevanceMapper categoryRelevanceMapper;
    @Resource
    private DistributorGoodsRelevanceMapper goodsRelevanceMapper;
    @Resource
    private DistributorOneScalePriceMapper oneScalePriceMapper;
    @Resource
    private DistributorPromotionRelevanceMapper promotionRelevanceMapper;
    @Resource
    private DistributorGroupSeckillRelevanceMapper groupSeckillRelevanceMapper;
    @Resource
    private DistributorExtendDataMapper distributorExtendDataMapper;

    @Resource
    private MessageSendService sendService;

    @DubboReference(check = false, timeout = 5000)
    private SystemCheckServiceRpc systemCheckServiceRpc;

    @Resource
    private DistributorGoodsPromotionRelevanceMapper goodsPromotionRelevanceMapper;

    /**
     * 更新品牌与分销商之间的关系
     *
     * @param scalePriceDOS
     * @return
     */
    public void brandDistributorRelevance(List<DistributorOneScalePriceDO> scalePriceDOS, Integer distributorId) {
        // List<DistributorOneScalePriceDO> oneScalePriceDOS = scalePriceDOS.stream()
        // .filter(scalePriceDO -> scalePriceDO.getBrandId() != null && scalePriceDO.getBrandId() != 0
        // && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0)
        // && scalePriceDO.getOperationType() != null && !scalePriceDO.getOperationType().equals(OPERATION_TYPE_2))
        // .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            List<BrandDistributorRpcCmd> cmds =
                DistributorConvertor.toBrandDistributorRpcCmd(scalePriceDOS, distributorId);
            brandServiceRpc.brandDistributorRelevanceByDistributorId(cmds, distributorId);
        }
    }

    /**
     * 根据品牌可视范围调整分销商可视品牌关系
     *
     * @param distributorIds
     * @param brandId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorBrandRelevanceByBrandId(List<Integer> distributorIds, Integer brandId) {
        List<DistributorBrandRelevanceDO> relevanceDOS1 = brandRelevanceMapper.listByDistributorIds(distributorIds);
        List<DistributorBrandRelevanceDO> relevanceDOS2 =
            brandRelevanceMapper.listByBrandId("," + String.valueOf(brandId) + ",");
        List<DistributorBrandRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorBrandRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视品牌 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            List<DistributorBrandRelevanceDO> updateOrDeleteRelevanceDOS =
                relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<Integer> brandIds = Arrays
                        .stream(
                            relevanceDO.getBrandIds().substring(1, relevanceDO.getBrandIds().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    brandIds = brandIds.stream().filter(id -> !id.equals(brandId)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(brandIds)) {
                        relevanceDO.setBrandIds("," + StringUtils.join(brandIds, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                    distributorIds.remove(relevanceDO.getDistributorId());
                });
            }
        }
        /** 添加分销商可视品牌 */
        Map<Integer, DistributorBrandRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream()
                .collect(Collectors.toMap(DistributorBrandRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorBrandRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                String brandIdStr1 = "," + String.valueOf(brandId) + ",";
                String brandIdStr2 = String.valueOf(brandId) + ",";
                if (relevanceDO1 != null && !relevanceDO1.getBrandIds().contains(brandIdStr1)) {
                    relevanceDO1.setBrandIds(relevanceDO1.getBrandIds() + brandIdStr2);
                    updateRelevanceDOS.add(relevanceDO1);
                } else if (relevanceDO1 == null) {
                    DistributorBrandRelevanceDO relevanceDO = new DistributorBrandRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setBrandIds(brandIdStr1);
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            brandRelevanceMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            brandRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            brandRelevanceMapper.deleteByIds(deleteIds);
        }
        /** 更新分销商品牌等级(包括删除、新增，初始默认品牌价格等级为默认价格等级) */
        List<DistributorOneScalePriceDO> scalePriceDOS = oneScalePriceMapper.listByBrandId(brandId);
        List<Integer> oldDistributorIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            oldDistributorIds.addAll(
                scalePriceDOS.stream().map(DistributorOneScalePriceDO::getDistributorId).collect(Collectors.toList()));
        }
        List<Integer> addDistributorIds = distributorIds.stream()
            .filter(distributorId -> !oldDistributorIds.contains(distributorId)).collect(Collectors.toList());
        List<Integer> delDistributorIds = oldDistributorIds.stream()
            .filter(distributorId -> !distributorIds.contains(distributorId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(delDistributorIds)) {
            oneScalePriceMapper.deleteByDistributorIdsAndBrandId(delDistributorIds, brandId);
        }
        if (!CollectionUtils.isEmpty(addDistributorIds)) {
            List<DistributorOneScalePriceDO> addScalePriceDOS = new ArrayList<>();
            List<DistributorOneScalePriceDO> defaultScalePriceDOS =
                oneScalePriceMapper.listDefaultByDistributorIds(addDistributorIds);
            addDistributorIds.forEach(addDistributorId -> {
                Optional<DistributorOneScalePriceDO> defaultScalePrice = defaultScalePriceDOS.stream()
                    .filter(defaultScalePriceDO -> defaultScalePriceDO.getDistributorId().equals(addDistributorId))
                    .findFirst();
                DistributorOneScalePriceDO oneScalePriceDO = new DistributorOneScalePriceDO();
                oneScalePriceDO.setDistributorId(addDistributorId);
                oneScalePriceDO.setBrandId(brandId);
                oneScalePriceDO.setScalePriceId(defaultScalePrice.get().getScalePriceId());
                oneScalePriceDO.setDistributionScalePriceId(oneScalePriceDO.getScalePriceId());
                addScalePriceDOS.add(oneScalePriceDO);
            });
            oneScalePriceMapper.insertList(addScalePriceDOS);
        }
    }

    /**
     * 根据品类可视范围调整分销商可视品类关系
     *
     * @param distributorIds
     * @param categoryId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorCategoryRelevanceByCategoryId(List<Integer> distributorIds, Integer categoryId) {
        List<DistributorCategoryRelevanceDO> relevanceDOS1 =
            categoryRelevanceMapper.listByDistributorIds(distributorIds);
        List<DistributorCategoryRelevanceDO> relevanceDOS2 =
            categoryRelevanceMapper.listByCategoryId("," + String.valueOf(categoryId) + ",");
        List<DistributorCategoryRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorCategoryRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视品类 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            List<DistributorCategoryRelevanceDO> updateOrDeleteRelevanceDOS =
                relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<
                        Integer> brandIds =
                            Arrays
                                .stream(relevanceDO.getCategoryIds()
                                    .substring(1, relevanceDO.getCategoryIds().length() - 1).split(","))
                                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    brandIds = brandIds.stream().filter(id -> !id.equals(categoryId)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(brandIds)) {
                        relevanceDO.setCategoryIds("," + StringUtils.join(brandIds, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                    distributorIds.remove(relevanceDO.getDistributorId());
                });
            }
        }
        /** 添加分销商可视品类 */
        Map<Integer, DistributorCategoryRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream().collect(
                Collectors.toMap(DistributorCategoryRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorCategoryRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                String categoryIdStr1 = "," + String.valueOf(categoryId) + ",";
                String categoryIdStr2 = String.valueOf(categoryId) + ",";
                if (relevanceDO1 != null && !relevanceDO1.getCategoryIds().contains(categoryIdStr1)) {
                    relevanceDO1.setCategoryIds(relevanceDO1.getCategoryIds() + categoryIdStr2);
                    updateRelevanceDOS.add(relevanceDO1);
                } else if (relevanceDO1 == null) {
                    DistributorCategoryRelevanceDO relevanceDO = new DistributorCategoryRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setCategoryIds(categoryIdStr1);
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            categoryRelevanceMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            categoryRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            categoryRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 根据商品可视范围调整分销商可视商品关系
     *
     * @param distributorIds
     * @param goodsId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorGoodsRelevanceByGoodsId(List<Integer> distributorIds, Integer goodsId) {
        List<DistributorGoodsRelevanceDO> relevanceDOS1 = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorIds)) {
            relevanceDOS1 = goodsRelevanceMapper.listByDistributorIds(distributorIds);
        }
        List<DistributorGoodsRelevanceDO> relevanceDOS2 =
            goodsRelevanceMapper.listByGoodsId("," + String.valueOf(goodsId) + ",");
        List<DistributorGoodsRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorGoodsRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视商品 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            List<DistributorGoodsRelevanceDO> updateOrDeleteRelevanceDOS =
                relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<Integer> goodsIds = Arrays
                        .stream(
                            relevanceDO.getGoodsIds().substring(1, relevanceDO.getGoodsIds().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    goodsIds = goodsIds.stream().filter(id -> !id.equals(goodsId)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(goodsIds)) {
                        relevanceDO.setGoodsIds("," + StringUtils.join(goodsIds, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                });
            }
        }
        /** 添加分销商可视商品 */
        Map<Integer, DistributorGoodsRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream()
                .collect(Collectors.toMap(DistributorGoodsRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorGoodsRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                String goodsIdStr1 = "," + String.valueOf(goodsId) + ",";
                String goodsIdStr2 = String.valueOf(goodsId) + ",";
                if (relevanceDO1 != null && !relevanceDO1.getGoodsIds().contains(goodsIdStr1)) {
                    relevanceDO1.setGoodsIds(relevanceDO1.getGoodsIds() + goodsIdStr2);
                    updateRelevanceDOS.add(relevanceDO1);
                } else if (relevanceDO1 == null) {
                    DistributorGoodsRelevanceDO relevanceDO = new DistributorGoodsRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setGoodsIds(goodsIdStr1);
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            goodsRelevanceMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            goodsRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            goodsRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 更新分销商品牌可视
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeDistributorBrandRelevance(DistributorChangeBrandRpcCmd cmd) {
        DistributorBrandRelevanceDO relevanceDO = brandRelevanceMapper.selectByDistributorId(cmd.getDistributorId());
        List<ChangeBrandRpcCmd> brandCmds = cmd.getBrandCmds();
        List<Integer> brandIds = new ArrayList<>();
        if (relevanceDO != null) {
            brandIds.addAll(
                Arrays.stream(relevanceDO.getBrandIds().substring(1, relevanceDO.getBrandIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(brandCmds)) {
            brandCmds.forEach(brandCmd -> {
                if (brandCmd.getOperationType().equals(OPERATION_TYPE_3)) {
                    brandIds.remove(brandCmd.getBrandId());
                } else if (!(brandIds.contains(brandCmd.getBrandId()))) {
                    brandIds.add(brandCmd.getBrandId());
                }
            });
        }
        if (CollectionUtils.isEmpty(brandIds) && relevanceDO != null) {
            brandRelevanceMapper.deleteByDistributorId(relevanceDO.getDistributorId());
        } else if (!CollectionUtils.isEmpty(brandIds)) {
            if (null == relevanceDO) {
                relevanceDO = new DistributorBrandRelevanceDO();
            }
            relevanceDO.setDistributorId(cmd.getDistributorId());
            relevanceDO.setBrandIds("," + StringUtils.join(brandIds, ",") + ",");
            if (relevanceDO.getId() == null) {
                brandRelevanceMapper.insert(relevanceDO);
            } else {
                brandRelevanceMapper.updateByPrimaryKey(relevanceDO);
            }
        }
    }

    /**
     * 刷新分销商品牌可视
     * 
     * @param brandIds
     * @param distributorId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorBrandRelevance(List<Integer> brandIds, Integer distributorId) {
        DistributorBrandRelevanceDO relevanceDO = brandRelevanceMapper.selectByDistributorId(distributorId);
        if (CollectionUtils.isEmpty(brandIds)) {
            if (relevanceDO != null) {
                brandRelevanceMapper.deleteByPrimaryKey(relevanceDO.getId());
            }
        } else {
            if (relevanceDO == null) {
                relevanceDO = new DistributorBrandRelevanceDO();
                relevanceDO.setDistributorId(distributorId);
            }
            relevanceDO.setBrandIds("," + StringUtils.join(brandIds, ",") + ",");
            if (relevanceDO.getId() == null) {
                brandRelevanceMapper.insert(relevanceDO);
            } else {
                brandRelevanceMapper.updateByPrimaryKey(relevanceDO);
            }
        }
    }

    /**
     * 根据促销活动可视范围调整分销商可视促销活动关系
     * 
     * @param distributorIds
     * @param promotionId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorPromotionRelevanceByPromotionId(List<Integer> distributorIds, Integer promotionId) {
        log.info("根据促销活动可视范围调整分销商可视促销活动关 promotionId:{},distributorIds:{}", promotionId, distributorIds);
        List<DistributorPromotionRelevanceDO> relevanceDOS1 =
            promotionRelevanceMapper.listByDistributorIds(distributorIds);
        List<DistributorPromotionRelevanceDO> relevanceDOS2 =
            promotionRelevanceMapper.listByPromotionId("," + String.valueOf(promotionId) + ",");
        List<DistributorPromotionRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorPromotionRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视活动 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            List<DistributorPromotionRelevanceDO> updateOrDeleteRelevanceDOS =
                relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<Integer> promotionIds =
                        Arrays
                            .stream(relevanceDO.getPromotionIds()
                                .substring(1, relevanceDO.getPromotionIds().length() - 1).split(","))
                            .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    promotionIds =
                        promotionIds.stream().filter(id -> !id.equals(promotionId)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(promotionIds)) {
                        relevanceDO.setPromotionIds("," + StringUtils.join(promotionIds, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                    distributorIds.remove(relevanceDO.getDistributorId());
                });
            }
        }
        /** 添加分销商可视促销活动 */
        Map<Integer, DistributorPromotionRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream().collect(
                Collectors.toMap(DistributorPromotionRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorPromotionRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                if (relevanceDO1 != null) {
                    relevanceDO1.setPromotionIds(relevanceDO1.getPromotionIds() + String.valueOf(promotionId) + ",");
                    updateRelevanceDOS.add(relevanceDO1);
                } else {
                    DistributorPromotionRelevanceDO relevanceDO = new DistributorPromotionRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setPromotionIds("," + String.valueOf(promotionId) + ",");
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            promotionRelevanceMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            promotionRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            promotionRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 根据拼团秒杀可视范围调整分销商可视拼团秒杀关系
     *
     * @param distributorIds
     * @param groupSeckillId
     */
    @Transactional(rollbackFor = Exception.class)
    public void distributorGroupSeckillRelevanceByGroupSeckillId(List<Integer> distributorIds, Integer groupSeckillId) {
        List<DistributorGroupSeckillRelevanceDO> relevanceDOS1 =
            groupSeckillRelevanceMapper.listByDistributorIds(distributorIds);
        List<DistributorGroupSeckillRelevanceDO> relevanceDOS2 =
            groupSeckillRelevanceMapper.listByGroupSeckillId("," + String.valueOf(groupSeckillId) + ",");
        List<DistributorGroupSeckillRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorGroupSeckillRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视拼团秒杀 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            List<DistributorGroupSeckillRelevanceDO> updateOrDeleteRelevanceDOS =
                relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<Integer> groupSeckillIds = Arrays
                        .stream(relevanceDO.getGroupSeckillIds()
                            .substring(1, relevanceDO.getGroupSeckillIds().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    groupSeckillIds =
                        groupSeckillIds.stream().filter(id -> !id.equals(groupSeckillId)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(groupSeckillIds)) {
                        relevanceDO.setGroupSeckillIds("," + StringUtils.join(groupSeckillIds, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                    distributorIds.remove(relevanceDO.getDistributorId());
                });
            }
        }
        /** 添加分销商可视拼团秒杀 */
        Map<Integer, DistributorGroupSeckillRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream().collect(
                Collectors.toMap(DistributorGroupSeckillRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorGroupSeckillRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                if (relevanceDO1 != null) {
                    relevanceDO1
                        .setGroupSeckillIds(relevanceDO1.getGroupSeckillIds() + String.valueOf(groupSeckillId) + ",");
                    updateRelevanceDOS.add(relevanceDO1);
                } else {
                    DistributorGroupSeckillRelevanceDO relevanceDO = new DistributorGroupSeckillRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setGroupSeckillIds("," + String.valueOf(groupSeckillId) + ",");
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            groupSeckillRelevanceMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            groupSeckillRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            groupSeckillRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 根据促销活动id删除分销商可视促销活动关系
     * 
     * @param promotionId
     */
    public void deletePromotionRelevanceByPromotionId(Integer promotionId) {
        List<DistributorPromotionRelevanceDO> relevanceDOS =
            promotionRelevanceMapper.listByPromotionId("," + String.valueOf(promotionId) + ",");
        List<DistributorPromotionRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relevanceDOS)) {
            relevanceDOS.forEach(relevanceDO -> {
                List<
                    Integer> promotionIds =
                        Arrays
                            .stream(relevanceDO.getPromotionIds()
                                .substring(1, relevanceDO.getPromotionIds().length() - 1).split(","))
                            .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                promotionIds = promotionIds.stream().filter(id -> !id.equals(promotionId)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(promotionIds)) {
                    relevanceDO.setPromotionIds("," + StringUtils.join(promotionIds, ",") + ",");
                    updateRelevanceDOS.add(relevanceDO);
                } else {
                    deleteIds.add(relevanceDO.getId());
                }
            });
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            promotionRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            promotionRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 根据拼团秒杀id删除分销商可视拼团秒杀关系
     * 
     * @param groupSeckillId
     */
    public void deleteGroupSeckillRelevanceByGroupSeckillId(Integer groupSeckillId) {
        List<DistributorGroupSeckillRelevanceDO> relevanceDOS =
            groupSeckillRelevanceMapper.listByGroupSeckillId("," + String.valueOf(groupSeckillId) + ",");
        List<DistributorGroupSeckillRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relevanceDOS)) {
            relevanceDOS.forEach(relevanceDO -> {
                List<Integer> groupSeckillIds =
                    Arrays
                        .stream(relevanceDO.getGroupSeckillIds()
                            .substring(1, relevanceDO.getGroupSeckillIds().length() - 1).split(","))
                        .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                groupSeckillIds =
                    groupSeckillIds.stream().filter(id -> !id.equals(groupSeckillId)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(groupSeckillIds)) {
                    relevanceDO.setGroupSeckillIds("," + StringUtils.join(groupSeckillIds, ",") + ",");
                    updateRelevanceDOS.add(relevanceDO);
                } else {
                    deleteIds.add(relevanceDO.getId());
                }
            });
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            groupSeckillRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            groupSeckillRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 更新分销商erp数据
     * 
     * @param cmd
     */
    public void updateDistributorErpData(DistributorErpRpcCmd cmd) {
        DistributorExtendDataDO extendDataDO = distributorExtendDataMapper.getByDistributorId(cmd.getDistributorId());
        DistributorConvertor.toUpdateDistributorExtendDataDO(extendDataDO, cmd);
        distributorExtendDataMapper.updateByPrimaryKey(extendDataDO);
        sendService.distributorLogPackage(cmd.getDistributorId(), "更新分销商erp数据", "更新成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 根据分销商刷新分销商商品可视范围
     * 
     * @param distributorId
     * @param scalePriceDOS
     * @param salesId
     * @param departmentId
     */
    public List<Integer> updateVisibleGoodsIdsByDistributor(Integer distributorId,
        List<DistributorOneScalePriceDO> scalePriceDOS, Integer salesId, Integer departmentId,
        String distributorGroupIds) {
        //根据价格等级查询品牌
        List<BrandScaleRpc> brandScaleRpcs = DistributorConvertor.toBrandScaleRpcList(scalePriceDOS);
        //筛选品牌id
        List<Integer> brandIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(brandScaleRpcs)) {
            brandIds = brandScaleRpcs.stream().map(BrandScaleRpc::getBrandId).collect(Collectors.toList());
        }
        Response<List<Integer>> response = goodsServiceRpc.getVisibleGoodsIdsByDistributor(distributorId,
            brandScaleRpcs, salesId, departmentId, brandIds, distributorGroupIds);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 根据分销商获取可视活动数据
     * 
     * @param distributorId
     * @param scalePriceId
     * @param salesId
     * @param departmentId
     * @return
     */
    public PromotionDistributorRpcDTO updateVisiblePromotionIdsByDistributor(Integer distributorId,
        Integer scalePriceId, Integer salesId, Integer departmentId) {
        com.bat.dubboapi.promotion.common.Response<PromotionDistributorRpcDTO> response =
            promotionServiceDistributorRpc.getVisiblePromotionIdsByDistributor(distributorId, scalePriceId, salesId,
                departmentId);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 获取是否需要审批
     *
     * @param ext
     * @return
     */
    public CheckConfigDetailRpcDTO getCheckConfigDetail(Short ext) {
        com.bat.dubboapi.system.common.Response<CheckConfigDetailRpcDTO> response =
            systemCheckServiceRpc.getCheckConfigDetail(ext);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
        }
    }

    /**
     * 获取分销商审批流
     *
     * @param ext
     * @return
     */
    public CheckConfigDetailRpcDTO getCheckFlows(Short ext) {
        com.bat.dubboapi.system.common.Response<CheckConfigDetailRpcDTO> response =
            systemCheckServiceRpc.getCheckConfigDetail(ext);
        if (response != null && response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /***
     * <h2>更新商品推广的可视范围</h2>
     * @param distributorIds
     * @param id
     */
    public void distributorGoodsPromotionRelevanceByGoodsId(List<Integer> distributorIds, Integer id) {
        //根据分销商id查询分销商与商品推广关联记录
        List<DistributorGoodsPromotionRelevanceDO> relevanceDOS1 = goodsPromotionRelevanceMapper.listByDistributorIds(distributorIds);

        //根据商品推广id查询该商品与所有分销商推广的记录
        List<DistributorGoodsPromotionRelevanceDO> relevanceDOS2 =
                goodsPromotionRelevanceMapper.listByGoodsPromotionId(id);

        List<DistributorGoodsPromotionRelevanceDO> addRelevanceDOS = new ArrayList<>();
        List<DistributorGoodsPromotionRelevanceDO> updateRelevanceDOS = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        /** 删除分销商可视商品推广 */
        if (!CollectionUtils.isEmpty(relevanceDOS2)) {
            //过滤出需要修改的DistributorGoodsPromotionRelevanceDO
            List<DistributorGoodsPromotionRelevanceDO> updateOrDeleteRelevanceDOS =
                    relevanceDOS2.stream().filter(relevanceDO -> !distributorIds.contains(relevanceDO.getDistributorId()))
                            .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(updateOrDeleteRelevanceDOS)) {
                updateOrDeleteRelevanceDOS.forEach(relevanceDO -> {
                    List<Integer> goodsPromotionId = Arrays
                            .stream(
                                    relevanceDO.getGoodsPromotionId().substring(1, relevanceDO.getGoodsPromotionId().length() - 1).split(","))
                            .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    goodsPromotionId = goodsPromotionId.stream().filter(gpid -> !gpid.equals(id)).collect(Collectors.toList());

                    if (!CollectionUtils.isEmpty(goodsPromotionId)) {
                        relevanceDO.setGoodsPromotionId("," + StringUtils.join(goodsPromotionId, ",") + ",");
                        updateRelevanceDOS.add(relevanceDO);
                    } else {
                        deleteIds.add(relevanceDO.getId());
                    }
                });
            }
        }
        /** 添加分销商可视商品 */
        Map<Integer, DistributorGoodsPromotionRelevanceDO> relevanceDOS1Map = new HashMap<>();
        if (!CollectionUtils.isEmpty(relevanceDOS1)) {
            relevanceDOS1Map.putAll(relevanceDOS1.stream()
                    .collect(Collectors.toMap(DistributorGoodsPromotionRelevanceDO::getDistributorId, relevanceDO -> relevanceDO)));
        }
        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                DistributorGoodsPromotionRelevanceDO relevanceDO1 = relevanceDOS1Map.get(distributorId);
                String goodsIdStr = String.valueOf(id) + ",";
                if (relevanceDO1 != null && !relevanceDO1.getGoodsPromotionId().contains(goodsIdStr)) {
                    relevanceDO1.setGoodsPromotionId(relevanceDO1.getGoodsPromotionId() + goodsIdStr);
                    updateRelevanceDOS.add(relevanceDO1);
                } else if (relevanceDO1 == null) {
                    DistributorGoodsPromotionRelevanceDO relevanceDO = new DistributorGoodsPromotionRelevanceDO();
                    relevanceDO.setDistributorId(distributorId);
                    relevanceDO.setGoodsPromotionId("," + goodsIdStr);
                    addRelevanceDOS.add(relevanceDO);
                }
            });
        }
        if (!CollectionUtils.isEmpty(addRelevanceDOS)) {
            goodsPromotionRelevanceMapper.insertList(addRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(updateRelevanceDOS)) {
            goodsPromotionRelevanceMapper.updateList(updateRelevanceDOS);
        }
        if (!CollectionUtils.isEmpty(deleteIds)) {
            goodsPromotionRelevanceMapper.deleteByIds(deleteIds);
        }
    }

    public List<Integer> goodsAreVisibleByDepId(Integer departmentID) {
        Response<List<Integer>> listResponse = goodsServiceRpc.goodsAreVisibleByDepId(departmentID);
        if (listResponse.isSuccess()){
            return listResponse.getData();
        }
        return null;
    }
}
