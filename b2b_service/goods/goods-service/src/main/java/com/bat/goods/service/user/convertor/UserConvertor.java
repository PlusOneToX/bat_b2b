package com.bat.goods.service.user.convertor;

import static com.bat.goods.service.user.executor.ErrorCode.B_GOODS_USER_SCALE_PRICE_NULL;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorScalePriceControlRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorSpecialGoodsRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.OneScalePriceRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.user.dto.UserGoodsItemId;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.dao.classify.dataobject.ClassifyDO;
import com.bat.goods.dao.goods.dataobject.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/29 11:27
 */
public class UserConvertor {

    public static List<UserClassifyDTO> toUserClassifyDTOList(List<ClassifyDO> classifyDOS) {
        List<UserClassifyDTO> dtos = new ArrayList<>();
        classifyDOS.forEach(classifyDO -> {
            UserClassifyDTO dto = new UserClassifyDTO();
            BeanUtils.copyProperties(classifyDO, dto);
            dtos.add(dto);
        });
        return dtos;
    }

    public static List<UserGoodsListDTO> toUserGoodsListDTOList(List<UserGoodsListDO> userGoodsListDOS,
                                                                Short collectionFlag) {
        List<UserGoodsListDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userGoodsListDOS)) {
            userGoodsListDOS.forEach(userGoodsListDO -> {
                UserGoodsListDTO dto = new UserGoodsListDTO();
                BeanUtils.copyProperties(userGoodsListDO, dto);
                if (collectionFlag != null && collectionFlag.equals(Constant.COLLECTION_FLAG_1)) {
                    dto.setCollectionFlag(collectionFlag);
                }
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static void toUserGoodsListTagDTOList(List<UserGoodsListDTO> userGoodsListDTOS,
        List<GoodsTagDO> goodsTagDOS) {
        Map<Integer, List<GoodsTagDO>> goodsTagDOSMap =
            goodsTagDOS.stream().collect(Collectors.groupingBy(GoodsTagDO::getGoodsId));
        userGoodsListDTOS.forEach(userGoodsListDO -> {
            List<GoodsTagDO> tagDOS = goodsTagDOSMap.get(userGoodsListDO.getId());
            if (!CollectionUtils.isEmpty(tagDOS)) {
                List<UserTagDTO> tagDTOS = new ArrayList<>();
                userGoodsListDO.setTags(tagDTOS);
                tagDOS.forEach(tagDO -> {
                    UserTagDTO tagDTO = new UserTagDTO();
                    BeanUtils.copyProperties(tagDO, tagDTO);
                    tagDTOS.add(tagDTO);
                });
            }
        });
    }

    public static List<UserGoodsPriceDTO> toUserGoodsPriceDTOList(
        DistributorScalePriceControlRpcDTO distributorScalePrice, Map<Integer, GoodsBrandCategoryDO> brandCategoryDOMap,
        List<Integer> goodsIds, List<OneScalePriceRpcDTO> oneScalePrices, List<GoodsMinMaxPriceDO> minMaxPriceDOS,
        List<GoodsPromotionPriceDO> promotionPriceDOS, List<GoodsItemScalePriceDO> goodsItemScalePriceDOS) {
        List<GoodsMinMaxPriceDO> goodsPriceDOS = new ArrayList<>();
        List<UserGoodsPriceDTO> dtos = new ArrayList<>();
        // 特价时，需处理特价
        Map<Integer, List<DistributorSpecialGoodsRpcDTO>> specialsMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(distributorScalePrice.getSpecials())) {
            specialsMap.putAll(distributorScalePrice.getSpecials().stream()
                .collect(Collectors.groupingBy(DistributorSpecialGoodsRpcDTO::getGoodsId)));
        }
        goodsIds.forEach(goodsId -> {
            GoodsBrandCategoryDO goodsBrandCategoryDO = brandCategoryDOMap.get(goodsId);
            // 商品没有设置品牌和品类时直接跳过
            if (goodsBrandCategoryDO == null) {
                return;
            }
            OneScalePriceRpcDTO oneScalePrice = getDistibutorOneScalePriceRpcDTO(goodsBrandCategoryDO, oneScalePrices);
            Optional<GoodsMinMaxPriceDO> minMaxPriceOptional = minMaxPriceDOS.stream()
                .filter(minMaxPriceDO -> minMaxPriceDO.getGoodsId().equals(goodsId)
                    && ((distributorScalePrice.getTreeNode() == 1
                        && minMaxPriceDO.getScalePriceId().equals(oneScalePrice.getScalePriceId()))
                        || (distributorScalePrice.getTreeNode() > 1
                            && minMaxPriceDO.getScalePriceId().equals(oneScalePrice.getDistributionScalePriceId()))))
                .findFirst();
            // 能获取对应商品价格时，如果多级分销商情况还需级数层级价格
            if (minMaxPriceOptional != null && minMaxPriceOptional.isPresent()) {
                GoodsMinMaxPriceDO minMaxPriceDO = minMaxPriceOptional.get();
                goodsPriceDOS.add(minMaxPriceDO);
                // 有特价时需处理特价，但不一定是特价最优，需做商品里的所有货品做判断
                List<DistributorSpecialGoodsRpcDTO> specials = specialsMap.get(minMaxPriceDO.getGoodsId());
                if (!CollectionUtils.isEmpty(specials) && !CollectionUtils.isEmpty(goodsItemScalePriceDOS)) {
                    Map<Integer, DistributorSpecialGoodsRpcDTO> specialMap = specials.stream()
                        .collect(Collectors.toMap(DistributorSpecialGoodsRpcDTO::getGoodsItemId, special -> special));
                    List<GoodsItemScalePriceDO> scalePriceDOS = goodsItemScalePriceDOS.stream().filter(
                        goodsItemScalePriceDO -> goodsItemScalePriceDO.getGoodsId().equals(minMaxPriceDO.getGoodsId())
                            && goodsItemScalePriceDO.getGoodsItemGradeId().equals(minMaxPriceDO.getScalePriceId()))
                        .collect(Collectors.toList());
                    // 重新计算最大最小值
                    if (!CollectionUtils.isEmpty(scalePriceDOS)) {
                        scalePriceDOS.forEach(scalePriceDO -> {
                            DistributorSpecialGoodsRpcDTO special = specialMap.get(scalePriceDO.getGoodsItemId());
                            // 如果特价不为空，则把特价作为等级价(会员价)
                            if (special != null) {
                                scalePriceDO.setPrice(special.getDistributorPrice());
                            }
                        });
                        List<BigDecimal> prices = scalePriceDOS.stream()
                            .filter(goodsItemScalePriceDO -> goodsItemScalePriceDO.getPrice() != null
                                && goodsItemScalePriceDO.getPrice().compareTo(new BigDecimal(0)) != 0)
                            .map(GoodsItemScalePriceDO::getPrice).collect(toList());
                        Optional<BigDecimal> max = prices.stream().max(Comparator.comparing(Function.identity()));
                        Optional<BigDecimal> min = prices.stream().min(Comparator.comparing(Function.identity()));
                        minMaxPriceDO.setMinPrice(min.get());
                        minMaxPriceDO.setMaxPrice(max.get());
                    }
                }
            }
        });
        goodsPriceDOS.forEach(goodsPriceDO -> {
            UserGoodsPriceDTO dto = new UserGoodsPriceDTO();
            BeanUtils.copyProperties(goodsPriceDO, dto);
            dto.setPromotionType(Constant.PROMOTION_TYPE_0);
            if (!CollectionUtils.isEmpty(promotionPriceDOS)) {
                List<GoodsPromotionPriceDO> priceDOS = promotionPriceDOS.stream()
                    .filter(promotionPriceDO -> promotionPriceDO.getGoodsId().equals(goodsPriceDO.getGoodsId())
                        && promotionPriceDO.getScalePriceId().equals(goodsPriceDO.getScalePriceId()))
                    .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(priceDOS)) {
                    priceDOS.forEach(priceDO -> {
                        if (priceDO.getReduceOrPresent() != null
                            && priceDO.getReduceOrPresent().equals(Constant.SECKILL_FLAG_1)) {
                            dto.setPresentFlag(Constant.SECKILL_FLAG_1);
                        } else {
                            // 活动最小值
                            if (dto.getPromotionMinPrice() == null || (priceDO.getPromotionMinPrice() != null && dto
                                .getPromotionMinPrice().doubleValue() > priceDO.getPromotionMinPrice().doubleValue())) {
                                dto.setMinPrice(priceDO.getMinPrice());
                                dto.setPromotionMinPrice(priceDO.getPromotionMinPrice());
                                if (priceDO.getPromotionId() != null) {
                                    dto.setPromotionType(Constant.PROMOTION_TYPE_1);
                                } else if (priceDO.getGroupSeckillId() != null) {
                                    if (priceDO.getGroupSeckillType().equals(Constant.GROUP_SECKILL_TYPE_1)) {
                                        dto.setPromotionType(Constant.PROMOTION_TYPE_2);
                                    } else {
                                        dto.setPromotionType(Constant.PROMOTION_TYPE_3);
                                    }
                                }
                            }
                            // 活动最大值
                            if (dto.getPromotionMaxPrice() == null || (priceDO.getPromotionMaxPrice() != null && dto
                                .getPromotionMaxPrice().doubleValue() < priceDO.getPromotionMaxPrice().doubleValue())) {
                                dto.setMaxPrice(priceDO.getMaxPrice());
                                dto.setPromotionMaxPrice(priceDO.getPromotionMaxPrice());
                            }
                        }
                    });
                }
            }
            if (dto.getMinPrice() != null) {
                dto.setMinPrice(dto.getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (dto.getMaxPrice() != null) {
                dto.setMaxPrice(dto.getMaxPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (dto.getPromotionMinPrice() != null) {
                dto.setPromotionMinPrice(dto.getPromotionMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            if (dto.getPromotionMaxPrice() != null) {
                dto.setPromotionMaxPrice(dto.getPromotionMaxPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
            dtos.add(dto);
        });
        return dtos;
    }

    public static List<UserGoodsItemPriceDTO> toUserGoodsItemPriceDTOList(
        DistributorScalePriceControlRpcDTO distributorScalePrice, List<UserGoodsItemId> goodsItems,
        Map<Integer, GoodsBrandCategoryDO> brandCategoryDOMap, List<GoodsItemScalePriceDO> scalePriceDOS,
        List<GoodsItemScalePriceDO> retailPriceDOS, List<OneScalePriceRpcDTO> oneScalePrices,
        List<DistributorSpecialGoodsRpcDTO> specials) {
        List<UserGoodsItemPriceDTO> dtos = new ArrayList<>();
        Map<Integer, GoodsItemScalePriceDO> retailPriceDOMap = new HashMap<>();
        Map<Integer, DistributorSpecialGoodsRpcDTO> specialGoodsRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(retailPriceDOS)) {
            retailPriceDOMap.putAll(retailPriceDOS.stream()
                .collect(Collectors.toMap(GoodsItemScalePriceDO::getGoodsItemId, scalePriceDO -> scalePriceDO)));
        }
        if (!CollectionUtils.isEmpty(specials)) {
            specialGoodsRpcDTOMap.putAll(specials.stream()
                .collect(Collectors.toMap(DistributorSpecialGoodsRpcDTO::getGoodsItemId, special -> special)));
        }
        Map<Integer, List<GoodsItemScalePriceDO>> scalePriceListMap =
            scalePriceDOS.stream().collect(Collectors.groupingBy(GoodsItemScalePriceDO::getGoodsItemId));
        goodsItems.forEach(goodsItem -> {
            UserGoodsItemPriceDTO dto = new UserGoodsItemPriceDTO();
            dto.setGoodsId(goodsItem.getGoodsId());
            dto.setItemId(goodsItem.getItemId());
            dtos.add(dto);
            // 建议零售价
            if (retailPriceDOMap.size() > 0) {
                GoodsItemScalePriceDO retailPriceDO = retailPriceDOMap.get(goodsItem.getItemId());
                if (retailPriceDO != null) {
                    dto.setRetailPrice(retailPriceDO.getPrice());
                }
            }
            // 先看是否有特价
            DistributorSpecialGoodsRpcDTO specialGoods = specialGoodsRpcDTOMap.get(goodsItem.getItemId());
            if (specialGoods != null) {
                dto.setSalePrice(specialGoods.getDistributorPrice());
            } else {
                // 会员价
                List<GoodsItemScalePriceDO> scalePriceDOList = scalePriceListMap.get(goodsItem.getItemId());
                // 获取一级分销商对应的商品价格
                GoodsBrandCategoryDO goodsBrandCategoryDO = brandCategoryDOMap.get(goodsItem.getGoodsId());
                if (!CollectionUtils.isEmpty(scalePriceDOList) && goodsBrandCategoryDO != null) {
                    OneScalePriceRpcDTO oneScalePrice =
                        getDistibutorOneScalePriceRpcDTO(goodsBrandCategoryDO, oneScalePrices);
                    Optional<GoodsItemScalePriceDO> scalePriceDOOptional = scalePriceDOList.stream()
                        .filter(scalePriceDO -> (distributorScalePrice.getTreeNode() == 1
                            && scalePriceDO.getGoodsItemGradeId().equals(oneScalePrice.getScalePriceId()))
                            || (distributorScalePrice.getTreeNode() > 1 && scalePriceDO.getGoodsItemGradeId()
                                .equals(oneScalePrice.getDistributionScalePriceId())))
                        .findFirst();
                    if (scalePriceDOOptional != null && scalePriceDOOptional.isPresent()) {
                        dto.setSalePrice(scalePriceDOOptional.get().getPrice());
                    }
                }
            }
        });
        return dtos;
    }

    /**
     * 根据商品品牌品类关系获取一级分销商价格等级
     * 
     * @param goodsBrandCategoryDO
     * @param oneScalePrices
     * @return
     */
    public static OneScalePriceRpcDTO getDistibutorOneScalePriceRpcDTO(GoodsBrandCategoryDO goodsBrandCategoryDO,
        List<OneScalePriceRpcDTO> oneScalePrices) {
        Optional<OneScalePriceRpcDTO> first = null;
        if (goodsBrandCategoryDO.getCategoryId() != null) {
            first = oneScalePrices.stream()
                .filter(oneScalePriceRpcDTO -> oneScalePriceRpcDTO.getBrandId() != null
                    && oneScalePriceRpcDTO.getBrandId().equals(goodsBrandCategoryDO.getBrandId())
                    && oneScalePriceRpcDTO.getCategoryId() != null
                    && oneScalePriceRpcDTO.getCategoryId().equals(goodsBrandCategoryDO.getCategoryId()))
                .findFirst();
        }
        if (first == null || !first.isPresent()) {
            first = oneScalePrices.stream()
                .filter(oneScalePriceRpcDTO -> oneScalePriceRpcDTO.getBrandId() != null
                    && oneScalePriceRpcDTO.getBrandId().equals(goodsBrandCategoryDO.getBrandId())
                    && (oneScalePriceRpcDTO.getCategoryId() == null || oneScalePriceRpcDTO.getCategoryId() == 0))
                .findFirst();
        }
        if (first == null || !first.isPresent()) {
            first = oneScalePrices.stream()
                .filter(oneScalePriceRpcDTO -> (oneScalePriceRpcDTO.getBrandId() == null
                    || oneScalePriceRpcDTO.getBrandId() == 0)
                    && (oneScalePriceRpcDTO.getCategoryId() == null || oneScalePriceRpcDTO.getCategoryId() == 0))
                .findFirst();
        }
        if (first == null || !first.isPresent()) {
            throw GoodsException.buildException(B_GOODS_USER_SCALE_PRICE_NULL);
        }
        OneScalePriceRpcDTO oneScalePrice = first.get();
        return oneScalePrice;
    }

    public static UserGoodsDTO toUserGoodsDTO(UserGoodsDO userGoodsDO) {
        UserGoodsDTO dto = new UserGoodsDTO();
        BeanUtils.copyProperties(userGoodsDO, dto);
        return dto;
    }

    public static List<UserTagDTO> toUserTagDTOList(List<GoodsTagDO> userTagDOS) {
        List<UserTagDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userTagDOS)) {
            userTagDOS.forEach(userTagDO -> {
                UserTagDTO dto = new UserTagDTO();
                BeanUtils.copyProperties(userTagDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<UserParamDTO> toUserParamDTOList(List<GoodsParamDO> userParamDOS) {
        List<UserParamDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userParamDOS)) {
            userParamDOS.forEach(userParamDO -> {
                UserParamDTO dto = new UserParamDTO();
                BeanUtils.copyProperties(userParamDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<UserGoodsItemDTO> toUserGoodsItemDTO(List<UserGoodsItemDO> goodsItemDOS,
                                                            List<GoodsItemBoxDO> goodsItemBoxDOS, List<GoodsItemSpecsColorListDO> specsColorListDOS) {
        List<UserGoodsItemDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemDOS)) {
            goodsItemDOS.forEach(goodsItemDO -> {
                UserGoodsItemDTO dto = new UserGoodsItemDTO();
                BeanUtils.copyProperties(goodsItemDO, dto);
                List<GoodsItemBoxDO> boxDOS = goodsItemBoxDOS.stream()
                    .filter(goodsItemBoxDO -> goodsItemBoxDO.getGoodsItemId().equals(dto.getId()))
                    .collect(Collectors.toList());
                dto.setBoxs(toUserGoodsItemBoxDTOList(boxDOS));
                List<GoodsItemSpecsColorListDO> specsColorListDOList = specsColorListDOS.stream()
                    .filter(specsColorListDO -> specsColorListDO.getGoodsItemId().equals(dto.getId()))
                    .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(specsColorListDOList)) {
                    specsColorListDOList.forEach(specsColorListDO -> {
                        if (specsColorListDO.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_1)) {
                            dto.setSpecsName(specsColorListDO.getAttributeValueName());
                            dto.setSpecsNameEn(specsColorListDO.getAttributeValueNameEn());
                        } else {
                            dto.setColorName(specsColorListDO.getAttributeValueName());
                            dto.setColorNameEn(specsColorListDO.getAttributeValueNameEn());
                        }
                    });
                }
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<UserGoodsItemBoxDTO> toUserGoodsItemBoxDTOList(List<GoodsItemBoxDO> goodsItemBoxDOS) {
        List<UserGoodsItemBoxDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemBoxDOS)) {
            goodsItemBoxDOS.forEach(goodsItemBoxDO -> {
                UserGoodsItemBoxDTO dto = new UserGoodsItemBoxDTO();
                BeanUtils.copyProperties(goodsItemBoxDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static GoodsDistributorCollectionDO toGoodsDistributorCollectionDO(Integer goodsId, Integer distributorId) {
        GoodsDistributorCollectionDO collectionDO = new GoodsDistributorCollectionDO();
        Date date = new Date(System.currentTimeMillis());
        collectionDO.setCreateTime(date);
        collectionDO.setDistributorId(distributorId);
        collectionDO.setGoodsId(goodsId);
        return collectionDO;
    }
}
