package com.bat.order.service.cart.convertor;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.order.service.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorCustomerPriceDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemRpcQry;
import com.bat.dubboapi.promotion.dto.data.*;
import com.bat.order.api.cart.dto.ShoppingCartCmd;
import com.bat.order.api.cart.dto.ShoppingCartDiyCmd;
import com.bat.order.api.cart.dto.ShoppingCustomerCartCmd;
import com.bat.order.api.cart.dto.data.*;
import com.bat.order.dao.cart.dataobject.ShoppingCartCustomerDO;
import com.bat.order.dao.cart.dataobject.ShoppingCartDistributorDO;
import com.bat.order.dao.cart.dataobject.ShoppingCartDiyCustomerDO;
import com.bat.order.dao.cart.dataobject.ShoppingCartDiyDistributorDO;

@Component
public class ShoppingCartConvertor {

    public static ShoppingCartDistributorDO toShoppingCartDistributorDO(ShoppingCartCmd cmd, String userId,
        String language) {
        ShoppingCartDistributorDO shoppingCartDistributorDO = new ShoppingCartDistributorDO();
        BeanUtils.copyProperties(cmd, shoppingCartDistributorDO);
        shoppingCartDistributorDO.setDistributorId(Integer.valueOf(userId));
        shoppingCartDistributorDO.setOpenFlag(Constant.OPEN_FLAG_1);
        shoppingCartDistributorDO.setLanguage(language);
        Date date = new Date(System.currentTimeMillis());
        shoppingCartDistributorDO.setCreateTime(date);
        shoppingCartDistributorDO.setUpdateTime(date);
        return shoppingCartDistributorDO;
    }

    public static List<ShoppingCartDistributorDO> toShoppingCartDistributorDOList(List<ShoppingCartCmd> cmds,
        String userId, String language) {
        List<ShoppingCartDistributorDO> shoppingCartDistributorDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                shoppingCartDistributorDOS.add(toShoppingCartDistributorDO(cmd, userId, language));
            });
        }
        return shoppingCartDistributorDOS;
    }

    public static ShoppingCartDiyDistributorDO toShoppingCartDiyDistributorDO(Integer cartId, ShoppingCartDiyCmd cmd) {
        ShoppingCartDiyDistributorDO diyDistributorDO = new ShoppingCartDiyDistributorDO();
        BeanUtils.copyProperties(cmd, diyDistributorDO);
        diyDistributorDO.setOpenFlag(Constant.OPEN_FLAG_1);
        diyDistributorDO.setShoppingCartDistributorId(cartId);
        Date date = new Date(System.currentTimeMillis());
        diyDistributorDO.setCreateTime(date);
        diyDistributorDO.setUpdateTime(date);
        return diyDistributorDO;
    }

    public static ShoppingCartCustomerDO toShoppingCartCustomerDO(ShoppingCartCmd cmd, String userId,
        String distributorId, String language) {
        ShoppingCartCustomerDO shoppingCartCustomerDO = new ShoppingCartCustomerDO();
        BeanUtils.copyProperties(cmd, shoppingCartCustomerDO);
        shoppingCartCustomerDO.setCustomerId(Integer.valueOf(userId));
        shoppingCartCustomerDO.setDistributorId(Integer.valueOf(distributorId));
        shoppingCartCustomerDO.setOpenFlag(Constant.OPEN_FLAG_1);
        shoppingCartCustomerDO.setLanguage(language);
        Date date = new Date(System.currentTimeMillis());
        shoppingCartCustomerDO.setCreateTime(date);
        shoppingCartCustomerDO.setUpdateTime(date);
        return shoppingCartCustomerDO;
    }

    public static ShoppingCartCustomerDO toShoppingCartCustomerDO(ShoppingCustomerCartCmd cmd, String userId,
        String distributorId, String language, GoodsItemRpcDTO rpcDTO) {
        ShoppingCartCustomerDO shoppingCartCustomerDO = new ShoppingCartCustomerDO();
        BeanUtils.copyProperties(cmd, shoppingCartCustomerDO);
        shoppingCartCustomerDO.setCustomerId(Integer.valueOf(userId));
        shoppingCartCustomerDO.setDistributorId(Integer.valueOf(distributorId));
        shoppingCartCustomerDO.setOpenFlag(Constant.OPEN_FLAG_1);
        shoppingCartCustomerDO.setLanguage(language);
        BeanUtils.copyProperties(rpcDTO, shoppingCartCustomerDO);
        shoppingCartCustomerDO.setId(null);
        if (StringUtils.isNotBlank(rpcDTO.getItemImg())) {
            shoppingCartCustomerDO.setImageUrl(rpcDTO.getImageUrl1());
        } else {
            shoppingCartCustomerDO.setImageUrl(rpcDTO.getItemImg());
        }
        if (StringUtils.isNotBlank(language) && language.equals(Constant.LANGUAGE_EN)) {
            shoppingCartCustomerDO.setGoodsName(rpcDTO.getGoodsNameEn());
            shoppingCartCustomerDO.setColorName(rpcDTO.getColorNameEn());
            shoppingCartCustomerDO.setSpecsName(rpcDTO.getSpecsNameEn());
            shoppingCartCustomerDO.setItemName(rpcDTO.getItemNameEn());
            if (StringUtils.isNotBlank(rpcDTO.getItemImg())) {
                shoppingCartCustomerDO.setImageUrl(rpcDTO.getImageUrl1en());
            }
        }
        shoppingCartCustomerDO.setItemId(rpcDTO.getId());
        Date date = new Date(System.currentTimeMillis());
        shoppingCartCustomerDO.setCreateTime(date);
        shoppingCartCustomerDO.setUpdateTime(date);
        return shoppingCartCustomerDO;
    }

    public static List<ShoppingCartCustomerDO> toShoppingCartCustomerDOList(List<ShoppingCartCmd> cmds, String userId,
        String distributorId, String language) {
        List<ShoppingCartCustomerDO> cartCustomerDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                cartCustomerDOS.add(toShoppingCartCustomerDO(cmd, userId, distributorId, language));
            });
        }
        return cartCustomerDOS;
    }

    public static ShoppingCartDiyCustomerDO toShoppingCartDiyCustomerDO(Integer cartId, ShoppingCartDiyCmd cmd) {
        ShoppingCartDiyCustomerDO cartDiyCustomerDO = new ShoppingCartDiyCustomerDO();
        BeanUtils.copyProperties(cmd, cartDiyCustomerDO);
        cartDiyCustomerDO.setOpenFlag(Constant.OPEN_FLAG_1);
        cartDiyCustomerDO.setShoppingCartCustomerId(cartId);
        Date date = new Date(System.currentTimeMillis());
        cartDiyCustomerDO.setCreateTime(date);
        cartDiyCustomerDO.setUpdateTime(date);
        return cartDiyCustomerDO;
    }

    public static List<GoodsItemRpcQry>
        toGoodsItemRpcQryByDistributorList(List<ShoppingCartDistributorDO> shoppingCartDistributorDOS) {
        List<GoodsItemRpcQry> rpcQryList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shoppingCartDistributorDOS)) {
            shoppingCartDistributorDOS.forEach(shoppingCartDistributorDO -> {
                GoodsItemRpcQry rpcQry = new GoodsItemRpcQry();
                rpcQry.setGoodsId(shoppingCartDistributorDO.getGoodsId());
                rpcQry.setItemId(shoppingCartDistributorDO.getItemId());
                rpcQryList.add(rpcQry);
            });
        }
        return rpcQryList.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsItemRpcQry::getItemId))),
                ArrayList::new));
    }

    public static List<GoodsItemRpcQry>
        toGoodsItemRpcQryByCustomerList(List<ShoppingCartCustomerDO> shoppingCartCustomerDOS) {
        List<GoodsItemRpcQry> rpcQryList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shoppingCartCustomerDOS)) {
            shoppingCartCustomerDOS.forEach(shoppingCartCustomerDO -> {
                GoodsItemRpcQry rpcQry = new GoodsItemRpcQry();
                rpcQry.setGoodsId(shoppingCartCustomerDO.getGoodsId());
                rpcQry.setItemId(shoppingCartCustomerDO.getItemId());
                rpcQryList.add(rpcQry);
            });
        }
        return rpcQryList.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GoodsItemRpcQry::getItemId))),
                ArrayList::new));
    }

    public static List<ShoppingCartDTO> toShoppingCartDTOByDistributorList(
        List<ShoppingCartDistributorDO> shoppingCartDistributorDOS,
        List<ShoppingCartDiyDistributorDO> shoppingCartDiyDistributorDOS, List<GoodsItemPromotionRpcDTO> rpcDTOS,
        List<Integer> promotionRuleIds, List<Integer> groupSeckillIds, List<GoodsItemPriceRpcDTO> priceRpcDTOS,
        List<GoodsItemBoxRpcDTO> boxRpcDTOS) {
        List<ShoppingCartDTO> cartDTOS = new ArrayList<>();
        Map<Integer, ShoppingCartDiyDistributorDO> diyDistributorDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(shoppingCartDiyDistributorDOS)) {
            diyDistributorDOMap.putAll(shoppingCartDiyDistributorDOS.stream()
                .collect(Collectors.toMap(ShoppingCartDiyDistributorDO::getShoppingCartDistributorId,
                    cartDiyDistributorDO -> cartDiyDistributorDO)));
        }
        Map<Integer, GoodsItemPromotionRpcDTO> rpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOMap
                .putAll(rpcDTOS.stream().collect(Collectors.toMap(GoodsItemPromotionRpcDTO::getId, rpcDTO -> rpcDTO)));
        }
        Map<Integer, List<GoodsItemBoxRpcDTO>> boxRpcDTOListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(boxRpcDTOS)) {
            boxRpcDTOListMap
                .putAll(boxRpcDTOS.stream().collect(Collectors.groupingBy(GoodsItemBoxRpcDTO::getGoodsItemId)));
        }
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(priceRpcDTOS)) {
            priceRpcDTOMap.putAll(priceRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO)));
        }
        if (!CollectionUtils.isEmpty(shoppingCartDistributorDOS)) {
            shoppingCartDistributorDOS.forEach(shoppingCartDistributorDO -> {
                ShoppingCartDTO cartDTO = new ShoppingCartDTO();
                BeanUtils.copyProperties(shoppingCartDistributorDO, cartDTO);
                cartDTOS.add(cartDTO);
                if (cartDTO.getItemType() == null || cartDTO.getItemType().equals(Constant.ITEM_TYPE_1)) {
                    ShoppingCartDiyDistributorDO diyDistributorDO = diyDistributorDOMap.get(cartDTO.getId());
                    if (diyDistributorDO != null) {
                        ShoppingCartDiyDTO diy = new ShoppingCartDiyDTO();
                        BeanUtils.copyProperties(diyDistributorDO, diy);
                        cartDTO.setDiy(diy);
                    }
                    GoodsItemPromotionRpcDTO rpcDTO = rpcDTOMap.get(cartDTO.getItemId());
                    if (rpcDTO != null) {
                        // 促销活动处理
                        List<PromotionRpcDTO> promotions = rpcDTO.getPromotions();
                        if (!CollectionUtils.isEmpty(promotions)) {
                            List<PromotionDTO> promotionDTOS = new ArrayList<>();
                            cartDTO.setPromotions(promotionDTOS);
                            promotions.forEach(promotion -> {
                                PromotionDTO promotionDTO = new PromotionDTO();
                                BeanUtils.copyProperties(promotion, promotionDTO);
                                promotionDTOS.add(promotionDTO);
                                List<PromotionRuleRpcDTO> rules = promotion.getRules();
                                if (!CollectionUtils.isEmpty(rules)) {
                                    List<PromotionRuleDTO> ruleDTOS = new ArrayList<>();
                                    promotionDTO.setRules(ruleDTOS);
                                    rules.forEach(rule -> {
                                        PromotionRuleDTO ruleDTO = new PromotionRuleDTO();
                                        BeanUtils.copyProperties(rule, ruleDTO);
                                        ruleDTOS.add(ruleDTO);
                                        promotionRuleIds.add(ruleDTO.getId());
                                        List<PromotionRuleConditionRpcDTO> conditions = rule.getConditions();
                                        if (!CollectionUtils.isEmpty(conditions)) {
                                            List<PromotionRuleConditionDTO> conditionDTOS = new ArrayList<>();
                                            ruleDTO.setConditions(conditionDTOS);
                                            conditions.forEach(condition -> {
                                                PromotionRuleConditionDTO conditionDTO =
                                                    new PromotionRuleConditionDTO();
                                                BeanUtils.copyProperties(condition, conditionDTO);
                                                conditionDTOS.add(conditionDTO);
                                                List<PromotionRuleConditionPresentRpcDTO> presents =
                                                    condition.getPresents();
                                                // if (!CollectionUtils.isEmpty(presents)) {
                                                // List<PromotionRuleConditionPresentDTO> presentDTOS =
                                                // new ArrayList<>();
                                                // conditionDTO.setPresents(presentDTOS);
                                                // presents.forEach(present -> {
                                                // PromotionRuleConditionPresentDTO presentDTO =
                                                // new PromotionRuleConditionPresentDTO();
                                                // BeanUtils.copyProperties(present, presentDTO);
                                                // presentDTOS.add(presentDTO);
                                                // });
                                                // }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                        // 拼团秒杀处理
                        List<GoodsItemGroupSeckillRpcDTO> groupSeckills = rpcDTO.getGroupSeckills();
                        if (!CollectionUtils.isEmpty(groupSeckills)) {
                            List<GroupSeckillDTO> groupSeckillDTOS = new ArrayList<>();
                            cartDTO.setGroupSeckills(groupSeckillDTOS);
                            groupSeckills.forEach(groupSeckill -> {
                                GroupSeckillDTO groupSeckillDTO = new GroupSeckillDTO();
                                BeanUtils.copyProperties(groupSeckill, groupSeckillDTO);
                                groupSeckillDTOS.add(groupSeckillDTO);
                                groupSeckillIds.add(groupSeckillDTO.getGroupSeckillId());
                            });
                        }
                    }
                }
                // 处理价格
                GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(cartDTO.getItemId());
                if (priceRpcDTO != null) {
                    cartDTO.setRetailPrice(priceRpcDTO.getRetailPrice());
                    cartDTO.setSalePrice(priceRpcDTO.getSalePrice());
                }
                // 处理装箱数据
                List<GoodsItemBoxRpcDTO> boxRpcDTOList = boxRpcDTOListMap.get(cartDTO.getItemId());
                if (!CollectionUtils.isEmpty(boxRpcDTOList)) {
                    List<GoodsItemBoxDTO> boxDTOS = new ArrayList<>();
                    cartDTO.setBoxs(boxDTOS);
                    boxRpcDTOList.forEach(boxRpcDTO -> {
                        GoodsItemBoxDTO boxDTO = new GoodsItemBoxDTO();
                        BeanUtils.copyProperties(boxRpcDTO, boxDTO);
                        boxDTOS.add(boxDTO);
                    });
                }
            });
        }
        return cartDTOS;
    }

    public static List<ShoppingCartDTO> toShoppingCartDTOByCustomerList(
        List<ShoppingCartCustomerDO> shoppingCartCustomerDOS,
        List<ShoppingCartDiyCustomerDO> shoppingCartDiyCustomerDOS, List<GoodsItemPromotionRpcDTO> rpcDTOS,
        List<Integer> promotionRuleIds, List<Integer> groupSeckillIds,
        List<DistributorCustomerPriceDTO> customerPriceDTOS) {
        List<ShoppingCartDTO> cartDTOS = new ArrayList<>();
        Map<Integer, ShoppingCartDiyCustomerDO> diyCustomerDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(shoppingCartDiyCustomerDOS)) {
            diyCustomerDOMap.putAll(shoppingCartDiyCustomerDOS.stream().collect(Collectors
                .toMap(ShoppingCartDiyCustomerDO::getShoppingCartCustomerId, cartDiyCustomerDO -> cartDiyCustomerDO)));
        }
        Map<Integer, GoodsItemPromotionRpcDTO> rpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOMap
                .putAll(rpcDTOS.stream().collect(Collectors.toMap(GoodsItemPromotionRpcDTO::getId, rpcDTO -> rpcDTO)));
        }
        Map<Integer, DistributorCustomerPriceDTO> customerPriceDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(customerPriceDTOS)) {
            customerPriceDTOMap.putAll(customerPriceDTOS.stream().collect(
                Collectors.toMap(DistributorCustomerPriceDTO::getItemId, customerPriceDTO -> customerPriceDTO)));
        }
        if (!CollectionUtils.isEmpty(shoppingCartCustomerDOS)) {
            shoppingCartCustomerDOS.forEach(shoppingCartCustomerDO -> {
                ShoppingCartDTO cartDTO = new ShoppingCartDTO();
                BeanUtils.copyProperties(shoppingCartCustomerDO, cartDTO);
                cartDTOS.add(cartDTO);
                if (cartDTO.getItemType() == null || cartDTO.getItemType().equals(Constant.ITEM_TYPE_1)) {
                    ShoppingCartDiyCustomerDO diyCustomerDO = diyCustomerDOMap.get(cartDTO.getId());
                    if (diyCustomerDO != null) {
                        ShoppingCartDiyDTO diy = new ShoppingCartDiyDTO();
                        BeanUtils.copyProperties(diyCustomerDO, diy);
                        cartDTO.setDiy(diy);
                    }
                    GoodsItemPromotionRpcDTO rpcDTO = rpcDTOMap.get(cartDTO.getItemId());
                    if (rpcDTO != null) {
                        // 促销活动处理
                        List<PromotionRpcDTO> promotions = rpcDTO.getPromotions();
                        if (!CollectionUtils.isEmpty(promotions)) {
                            List<PromotionDTO> promotionDTOS = new ArrayList<>();
                            cartDTO.setPromotions(promotionDTOS);
                            promotions.forEach(promotion -> {
                                PromotionDTO promotionDTO = new PromotionDTO();
                                BeanUtils.copyProperties(promotion, promotionDTO);
                                promotionDTOS.add(promotionDTO);
                                List<PromotionRuleRpcDTO> rules = promotion.getRules();
                                if (!CollectionUtils.isEmpty(rules)) {
                                    List<PromotionRuleDTO> ruleDTOS = new ArrayList<>();
                                    promotionDTO.setRules(ruleDTOS);
                                    rules.forEach(rule -> {
                                        PromotionRuleDTO ruleDTO = new PromotionRuleDTO();
                                        BeanUtils.copyProperties(rule, ruleDTO);
                                        ruleDTOS.add(ruleDTO);
                                        promotionRuleIds.add(ruleDTO.getId());
                                        List<PromotionRuleConditionRpcDTO> conditions = rule.getConditions();
                                        if (!CollectionUtils.isEmpty(conditions)) {
                                            List<PromotionRuleConditionDTO> conditionDTOS = new ArrayList<>();
                                            ruleDTO.setConditions(conditionDTOS);
                                            conditions.forEach(condition -> {
                                                PromotionRuleConditionDTO conditionDTO =
                                                    new PromotionRuleConditionDTO();
                                                BeanUtils.copyProperties(condition, conditionDTO);
                                                conditionDTOS.add(conditionDTO);
                                                List<PromotionRuleConditionPresentRpcDTO> presents =
                                                    condition.getPresents();
                                                // if (!CollectionUtils.isEmpty(presents)) {
                                                // List<PromotionRuleConditionPresentDTO> presentDTOS =
                                                // new ArrayList<>();
                                                // conditionDTO.setPresents(presentDTOS);
                                                // presents.forEach(present -> {
                                                // PromotionRuleConditionPresentDTO presentDTO =
                                                // new PromotionRuleConditionPresentDTO();
                                                // BeanUtils.copyProperties(present, presentDTO);
                                                // presentDTOS.add(presentDTO);
                                                // });
                                                // }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                        // 拼团秒杀处理
                        List<GoodsItemGroupSeckillRpcDTO> groupSeckills = rpcDTO.getGroupSeckills();
                        if (!CollectionUtils.isEmpty(groupSeckills)) {
                            List<GroupSeckillDTO> groupSeckillDTOS = new ArrayList<>();
                            cartDTO.setGroupSeckills(groupSeckillDTOS);
                            groupSeckills.forEach(groupSeckill -> {
                                GroupSeckillDTO groupSeckillDTO = new GroupSeckillDTO();
                                BeanUtils.copyProperties(groupSeckill, groupSeckillDTO);
                                groupSeckillDTOS.add(groupSeckillDTO);
                                groupSeckillIds.add(groupSeckillDTO.getGroupSeckillId());
                            });
                        }
                    }
                }
                // 价格处理
                DistributorCustomerPriceDTO priceRpcDTO = customerPriceDTOMap.get(cartDTO.getItemId());
                if (priceRpcDTO != null) {
                    cartDTO.setSalePrice(priceRpcDTO.getPrice());
                }
            });
        }
        return cartDTOS;
    }

    public static void toShoppingCartDTOList(List<GoodsItemRpcDTO> itemRpcDTOS, List<ShoppingCartDTO> cartDTOS,
        String language) {
        if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
            Map<Integer, GoodsItemRpcDTO> itemRpcDTOMap =
                itemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getId, itemRpcDTO -> itemRpcDTO));
            cartDTOS.forEach(cartDTO -> {
                GoodsItemRpcDTO goodsItemRpcDTO = itemRpcDTOMap.get(cartDTO.getItemId());
                if (goodsItemRpcDTO != null) {
                    if (language.equals(Constant.LANGUAGE_EN)) {
                        cartDTO.setGoodsName(goodsItemRpcDTO.getGoodsNameEn());
                        cartDTO.setItemName(goodsItemRpcDTO.getItemNameEn());
                        cartDTO.setSpecsName(goodsItemRpcDTO.getSpecsNameEn());
                        cartDTO.setColorName(goodsItemRpcDTO.getColorNameEn());
                        cartDTO.setImageUrl(goodsItemRpcDTO.getImageUrl1en());
                    } else {
                        cartDTO.setGoodsName(goodsItemRpcDTO.getGoodsName());
                        cartDTO.setItemName(goodsItemRpcDTO.getItemName());
                        cartDTO.setSpecsName(goodsItemRpcDTO.getSpecsName());
                        cartDTO.setColorName(goodsItemRpcDTO.getColorName());
                        cartDTO.setImageUrl(goodsItemRpcDTO.getImageUrl1());
                    }
                    if (StringUtils.isNotBlank(goodsItemRpcDTO.getItemImg())) {
                        cartDTO.setImageUrl(goodsItemRpcDTO.getItemImg());
                    }
                }
            });
        }
    }

}
