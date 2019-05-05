package com.bat.promotion.service.groupseckill.convertor;

import static com.bat.promotion.service.common.Constant.*;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.dao.groupseckill.dataobject.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.GoodsGroupSeckillGoodsRpcCmd;
import com.bat.dubboapi.goods.goods.dto.GoodsGroupSeckillRpcCmd;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDistributorScopeDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillGoodsDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillListDTO;
import com.bat.promotion.dao.groupseckill.dataobject.*;
import com.bat.promotion.mq.dto.GoodsSaleDTO;
import com.bat.promotion.service.common.CommonErrorCode;
import com.bat.promotion.api.base.MessageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/21 14:35
 */
@Slf4j
public class GroupSeckillConvertor {

    public static GroupSeckillDO toGroupSeckillDO(GroupSeckillCmd cmd, Date date) {
        GroupSeckillDO groupSeckillDO = new GroupSeckillDO();
        BeanUtils.copyProperties(cmd, groupSeckillDO);
        groupSeckillDO.setCreateTime(date);
        groupSeckillDO.setUpdateTime(date);
        return groupSeckillDO;
    }

    public static List toGroupSeckillRelevance(Integer groupSeckillId, GroupSeckillCmd cmd) {
        List<Integer> scalePriceIds = cmd.getScalePriceIds();
        List<GroupSeckillDistributorScopeCmd> distributors = cmd.getDistributors();
        List<Integer> departmentIds = cmd.getDepartmentIds();
        List<Integer> adminIds = cmd.getAdminIds();
        if (cmd.getDistributorScope().equals(SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceIds)) {
            List<GroupSeckillScalePriceRelevanceDO> scalePriceRelevanceDOS = new ArrayList<>();
            scalePriceIds.forEach(id -> {
                GroupSeckillScalePriceRelevanceDO scalePriceRelevanceDO = new GroupSeckillScalePriceRelevanceDO();
                scalePriceRelevanceDO.setGroupSeckillId(groupSeckillId);
                scalePriceRelevanceDO.setScalePriceId(id);
                scalePriceRelevanceDOS.add(scalePriceRelevanceDO);
            });
            return scalePriceRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributors)) {
            List<GroupSeckillDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
            distributors.forEach(distributor -> {
                GroupSeckillDistributorRelevanceDO distributorRelevanceDO = new GroupSeckillDistributorRelevanceDO();
                distributorRelevanceDO.setGroupSeckillId(groupSeckillId);
                distributorRelevanceDO.setDistributorId(distributor.getDistributorId());
                distributorRelevanceDO.setName(distributor.getName());
                distributorRelevanceDO.setCompanyName(distributor.getCompanyName());
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
            return distributorRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentIds)) {
            List<GroupSeckillDepartmentRelevanceDO> departmentRelevanceDOS = new ArrayList<>();
            departmentIds.forEach(id -> {
                GroupSeckillDepartmentRelevanceDO departmentRelevanceDO = new GroupSeckillDepartmentRelevanceDO();
                departmentRelevanceDO.setGroupSeckillId(groupSeckillId);
                departmentRelevanceDO.setDepartmentId(id);
                departmentRelevanceDOS.add(departmentRelevanceDO);
            });
            return departmentRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminIds)) {
            List<GroupSeckillAdminRelevanceDO> adminRelevanceDOS = new ArrayList<>();
            adminIds.forEach(id -> {
                GroupSeckillAdminRelevanceDO adminRelevanceDO = new GroupSeckillAdminRelevanceDO();
                adminRelevanceDO.setGroupSeckillId(groupSeckillId);
                adminRelevanceDO.setAdminId(id);
                adminRelevanceDOS.add(adminRelevanceDO);
            });
            return adminRelevanceDOS;
        } else {
            return null;
        }
    }

    public static List toGroupSeckillRelevanceNO(Integer groupSeckillId, GroupSeckillCmd cmd) {
        List<Integer> scalePriceNoIds = cmd.getScalePriceNoIds();
        List<Integer> distributorNoIds = cmd.getDistributorNoIds();
        List<Integer> departmentNoIds = cmd.getDepartmentNoIds();
        List<Integer> adminNoIds = cmd.getAdminNoIds();
        if (cmd.getDistributorScopeNo().equals(SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceNoIds)) {
            List<GroupSeckillScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS = new ArrayList<>();
            scalePriceNoIds.forEach(id -> {
                GroupSeckillScalePriceRelevanceNoDO scalePriceRelevanceNoDO = new GroupSeckillScalePriceRelevanceNoDO();
                scalePriceRelevanceNoDO.setGroupSeckillId(groupSeckillId);
                scalePriceRelevanceNoDO.setScalePriceId(id);
                scalePriceRelevanceNoDOS.add(scalePriceRelevanceNoDO);
            });
            return scalePriceRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_DISTRIBUTOR)
            && !CollectionUtils.isEmpty(distributorNoIds)) {
            List<GroupSeckillDistributorRelevanceNoDO> distributorRelevanceNoDOS = new ArrayList<>();
            distributorNoIds.forEach(id -> {
                GroupSeckillDistributorRelevanceNoDO distributorRelevanceNoDO =
                    new GroupSeckillDistributorRelevanceNoDO();
                distributorRelevanceNoDO.setGroupSeckillId(groupSeckillId);
                distributorRelevanceNoDO.setDistributorId(id);
                distributorRelevanceNoDOS.add(distributorRelevanceNoDO);
            });
            return distributorRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentNoIds)) {
            List<GroupSeckillDepartmentRelevanceNoDO> departmentRelevanceNoDOS = new ArrayList<>();
            departmentNoIds.forEach(id -> {
                GroupSeckillDepartmentRelevanceNoDO departmentRelevanceNoDO = new GroupSeckillDepartmentRelevanceNoDO();
                departmentRelevanceNoDO.setGroupSeckillId(groupSeckillId);
                departmentRelevanceNoDO.setDepartmentId(id);
                departmentRelevanceNoDOS.add(departmentRelevanceNoDO);
            });
            return departmentRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminNoIds)) {
            List<GroupSeckillAdminRelevanceNoDO> adminRelevanceNoDOS = new ArrayList<>();
            adminNoIds.forEach(id -> {
                GroupSeckillAdminRelevanceNoDO adminRelevanceNoDO = new GroupSeckillAdminRelevanceNoDO();
                adminRelevanceNoDO.setGroupSeckillId(groupSeckillId);
                adminRelevanceNoDO.setAdminId(id);
                adminRelevanceNoDOS.add(adminRelevanceNoDO);
            });
            return adminRelevanceNoDOS;
        } else {
            return null;
        }
    }

    public static List<GroupSeckillGoodsDO> toGroupSeckillGoodsDOList(Integer groupSeckillId,
        List<GroupSeckillGoodsCmd> cmds) {
        List<GroupSeckillGoodsDO> goodsDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                GroupSeckillGoodsDO goodsDO = new GroupSeckillGoodsDO();
                BeanUtils.copyProperties(cmd, goodsDO);
                if (goodsDO.getMaxNum() == null) {
                    goodsDO.setMaxNum(0);
                }
                if (goodsDO.getMinNum() == null) {
                    goodsDO.setMinNum(0);
                }
                if (goodsDO.getRealSum() == null) {
                    goodsDO.setRealSum(0);
                }
                if (goodsDO.getVirtualSum() == null) {
                    goodsDO.setVirtualSum(0);
                }
                goodsDO.setGroupSeckillId(groupSeckillId);
                goodsDOS.add(goodsDO);
            });
        }
        return goodsDOS;
    }

    public static List<GroupSeckillListDTO> toGroupSeckillListDTOList(List<GroupSeckillDO> groupSeckillDOS) {
        List<GroupSeckillListDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupSeckillDOS)) {
            groupSeckillDOS.forEach(groupSeckillDO -> {
                GroupSeckillListDTO dto = new GroupSeckillListDTO();
                BeanUtils.copyProperties(groupSeckillDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static GroupSeckillDTO toGroupSeckillDTO(GroupSeckillDO groupSeckillDO) {
        GroupSeckillDTO dto = new GroupSeckillDTO();
        BeanUtils.copyProperties(groupSeckillDO, dto);
        return dto;
    }

    public static List<GroupSeckillDistributorScopeDTO>
        toGroupSeckillDistributorScopeDTOList(List<GroupSeckillDistributorRelevanceDO> distributorRelevanceDOS) {
        List<GroupSeckillDistributorScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorRelevanceDOS)) {
            distributorRelevanceDOS.forEach(distributorRelevanceDO -> {
                GroupSeckillDistributorScopeDTO dto = new GroupSeckillDistributorScopeDTO();
                BeanUtils.copyProperties(distributorRelevanceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GroupSeckillDistributorScopeDTO>
        toGroupSeckillDistributorScopeDTOListForRpc(List<DistributorRpcDTO> rpcDTOS) {
        List<GroupSeckillDistributorScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOS.forEach(rpcDTO -> {
                GroupSeckillDistributorScopeDTO dto = new GroupSeckillDistributorScopeDTO();
                BeanUtils.copyProperties(rpcDTO, dto);
                dto.setDistributorId(rpcDTO.getId());
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GroupSeckillGoodsDTO> toGroupSeckillGoodsDTOList(List<GroupSeckillGoodsDO> goodsDOS,
        List<GoodsItemRpcDTO> itemRpcDTOS) {
        List<GroupSeckillGoodsDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsDOS)) {
            goodsDOS.forEach(goodsDO -> {
                GroupSeckillGoodsDTO dto = new GroupSeckillGoodsDTO();
                BeanUtils.copyProperties(goodsDO, dto);
                Optional<GoodsItemRpcDTO> optional =
                    itemRpcDTOS.stream().filter(itemRpcDTO -> itemRpcDTO.getId().equals(dto.getItemId())).findFirst();
                if (optional != null && optional.isPresent()) {
                    GoodsItemRpcDTO rpcDTO = optional.get();
                    dto.setGoodsName(rpcDTO.getGoodsName());
                    dto.setItemName(rpcDTO.getItemName());
                    dto.setColorName(rpcDTO.getColorName());
                    dto.setSpecsName(rpcDTO.getSpecsName());
                    dto.setSpecsName(rpcDTO.getBarCode());
                }
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GroupSeckillStatusDO> toGroupSeckillStatusDOList(List<Integer> ids, Short groupSeckillStatus) {
        List<GroupSeckillStatusDO> statusDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            ids.forEach(id -> {
                GroupSeckillStatusDO statusDO = new GroupSeckillStatusDO();
                Date date = new Date(System.currentTimeMillis());
                statusDO.setUpdateTime(date);
                statusDO.setId(id);
                statusDO.setGroupSeckillStatus(groupSeckillStatus);
                statusDOS.add(statusDO);
            });
        }
        return statusDOS;
    }

    public static GoodsGroupSeckillRpcCmd toGoodsGroupSeckillRpcCmd(Integer groupSeckillId, GroupSeckillCmd cmd) {
        GoodsGroupSeckillRpcCmd rpcCmd = new GoodsGroupSeckillRpcCmd();
        rpcCmd.setGroupSeckillType(cmd.getGroupSeckillType());
        rpcCmd.setId(groupSeckillId);
        List<GoodsGroupSeckillGoodsRpcCmd> goodss = new ArrayList<>();
        rpcCmd.setGoods(goodss);
        cmd.getGoods().forEach(goods -> {
            GoodsGroupSeckillGoodsRpcCmd goodsRpcCmd = new GoodsGroupSeckillGoodsRpcCmd();
            goodsRpcCmd.setItemId(goods.getItemId());
            goodsRpcCmd.setGroupSeckillPrice(goodsRpcCmd.getGroupSeckillPrice());
            goodss.add(goodsRpcCmd);
        });
        return rpcCmd;
    }

    public static List<GroupSeckillSortDO> toGroupSeckillSortDOList(List<GroupSeckillSortCmd> cmds) {
        List<GroupSeckillSortDO> sortDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                GroupSeckillSortDO sortDO = new GroupSeckillSortDO();
                BeanUtils.copyProperties(cmd, sortDO);
                Date date = new Date(System.currentTimeMillis());
                sortDO.setUpdateTime(date);
                sortDOS.add(sortDO);
            });
        }
        return sortDOS;
    }

    public static List<GroupSeckillGoodsSortDO> toGroupSeckillGoodsSortDOList(List<GroupSeckillGoodsSortCmd> cmds) {
        List<GroupSeckillGoodsSortDO> sortDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                GroupSeckillGoodsSortDO sortDO = new GroupSeckillGoodsSortDO();
                BeanUtils.copyProperties(cmd, sortDO);
                Date date = new Date(System.currentTimeMillis());
                sortDO.setUpdateTime(date);
                sortDOS.add(sortDO);
            });
        }
        return sortDOS;
    }

    public static List<GoodsItemPromotionPriceRpcDTO> toGoodsItemPromotionPriceRpcDTOList(
        List<GroupSeckillDO> groupSeckillDOS, List<GoodsItemPriceRpcQry> goodsSpellGroupPrices,
        List<GroupSeckillGoodsDO> groupSeckillGoodsDOS, Date time) {
        List<GoodsItemPromotionPriceRpcDTO> rpcDTOS = new ArrayList<>();
        Map<Integer, GroupSeckillGoodsDO> groupSeckillGoodsDOMap = groupSeckillGoodsDOS.stream()
            .collect(Collectors.toMap(GroupSeckillGoodsDO::getItemId, groupSeckillGoodsDO -> groupSeckillGoodsDO));
        Map<Integer, GroupSeckillDO> groupSeckillDOMap =
            groupSeckillDOS.stream().collect(Collectors.toMap(GroupSeckillDO::getId, groupSeckillDO -> groupSeckillDO));
        for (GoodsItemPriceRpcQry goodsSpellGroupPrice : goodsSpellGroupPrices) {
            GoodsItemPromotionPriceRpcDTO rpcDTO = new GoodsItemPromotionPriceRpcDTO();
            BeanUtils.copyProperties(goodsSpellGroupPrice, rpcDTO);
            GroupSeckillGoodsDO goodsDO = groupSeckillGoodsDOMap.get(rpcDTO.getItemId());
            GroupSeckillDO groupSeckillDO = groupSeckillDOMap.get(rpcDTO.getSpellGroupId());
            rpcDTO.setExistFlag(goodsDO.getExistFlag());
            rpcDTO.setMtoFlag(goodsDO.getMtoFlag());
            if (time != null && ((groupSeckillDO.getGroupSeckillStatus().equals(GROUP_SECKILL_STATUS_4)
                && groupSeckillDO.getUpdateTime().getTime() < time.getTime())
                || time.getTime() > groupSeckillDO.getEndTime().getTime())) {
                rpcDTO.setFlag(CommonErrorCode.B_PROMOTION_EXPIRE_ERROR);
                try {
                    // 哎呀！活动:{0}已结束或未开始，商品将按原价计算，是否继续下单购买呢！
                    String format = MessageFormat.format(MessageUtils.get(CommonErrorCode.B_PROMOTION_EXPIRE_ERROR_MSG),
                        groupSeckillDO.getName());
                    log.error(format);
                    rpcDTO.setMsg(format);
                } catch (Exception e) {
                    rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_EXPIRE_ERROR));
                }
            } else if (goodsDO.getMinNum() != null && goodsDO.getMinNum() > 0
                && rpcDTO.getItemCount().intValue() < goodsDO.getMinNum().intValue()) {
                rpcDTO.setFlag(CommonErrorCode.B_PROMOTION_GROUP_SECKILL_MIN_NUM_ERROR);
                try {
                    // 哎呀！拼团:{0},{1}秒杀数量:{2}未达到最小起批量:{3}，请您重新调整拼团数量！
                    String format = MessageFormat.format(
                        MessageUtils.get(CommonErrorCode.B_PROMOTION_GROUP_SECKILL_MIN_NUM_ERROR_MSG),
                        groupSeckillDO.getName(), goodsDO.getItemCode(), rpcDTO.getItemCount(), goodsDO.getMinNum());
                    log.error(format);
                    rpcDTO.setMsg(format);
                } catch (Exception e) {
                    rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_GROUP_SECKILL_MIN_NUM_ERROR));
                }
            } else if (goodsDO.getMaxNum() != null && goodsDO.getMaxNum() > 0
                && groupSeckillDO.getGroupSeckillType().equals(GROUP_SECKILL_TYPE_1)
                && goodsDO.getMaxNum() - goodsDO.getRealSum() < rpcDTO.getItemCount().intValue()) {
                rpcDTO.setFlag(CommonErrorCode.B_PROMOTION_GROUP_MAX_NUM_ERROR);
                try {
                    // 哎呀！拼团:{0},{1}数量:{2}已超过可拼数量:{3}，请您重新调整拼团数量！
                    String format =
                        MessageFormat.format(MessageUtils.get(CommonErrorCode.B_PROMOTION_GROUP_MAX_NUM_ERROR_MSG),
                            groupSeckillDO.getName(), goodsDO.getItemCode(), rpcDTO.getItemCount(),
                            (Math.max(goodsDO.getMaxNum() - goodsDO.getRealSum(), 0)));
                    log.error(format);
                    rpcDTO.setMsg(format);
                } catch (Exception e) {
                    rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_GROUP_MAX_NUM_ERROR));
                }
            } else if (goodsDO.getMaxNum() != null && goodsDO.getMaxNum() > 0
                && groupSeckillDO.getGroupSeckillType().equals(GROUP_SECKILL_TYPE_2)
                && goodsDO.getMaxNum() < rpcDTO.getItemCount().intValue()) {
                rpcDTO.setFlag(CommonErrorCode.B_PROMOTION_SECKILL_MAX_NUM_ERROR);
                try {
                    // 哎呀！秒杀:{0},{1}数量:{2}已超过可秒数量:{3}，请您重新调整秒杀数量！
                    String format = MessageFormat.format(
                        MessageUtils.get(CommonErrorCode.B_PROMOTION_SECKILL_MAX_NUM_ERROR_MSG),
                        groupSeckillDO.getName(), goodsDO.getItemCode(), rpcDTO.getItemCount(), goodsDO.getMaxNum());
                    log.error(format);
                    rpcDTO.setMsg(format);
                } catch (Exception e) {
                    rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_SECKILL_MAX_NUM_ERROR));
                }
            } else {
                rpcDTO.setFlag(CommonErrorCode.B_PROMOTION_SUCCESS);
                rpcDTO.setMsg(MessageUtils.get(CommonErrorCode.B_PROMOTION_SUCCESS));
                rpcDTO.setActualPrice(goodsDO.getGroupSeckillPrice());
                // goodsDO.setRealSum(goodsDO.getRealSum() + goodsSpellGroupPrice.getItemCount());
            }
            rpcDTOS.add(rpcDTO);
        }
        return rpcDTOS;
    }

    public static List<GroupSeckillGoodsDO> toGroupSeckillGoodsDOList(List<GoodsSaleDTO> saleDTOS,
        List<GroupSeckillGoodsDO> goodsDOS) {
        List<GroupSeckillGoodsDO> changeGoodsDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleDTOS) && !CollectionUtils.isEmpty(goodsDOS)) {
            saleDTOS.forEach(saleDTO -> {
                Optional<GroupSeckillGoodsDO> first =
                    goodsDOS.stream().filter(goodsDO -> goodsDO.getItemId().equals(saleDTO.getItemId())
                        && goodsDO.getGroupSeckillId().equals(saleDTO.getSpellGroupId())).findFirst();
                if (first.isPresent() && saleDTO.getSaleNum() != null) {
                    GroupSeckillGoodsDO goodsDO = first.get();
                    if (goodsDO.getRealSum() == null) {
                        goodsDO.setRealSum(0);
                    }
                    if (saleDTO.getChangeType().equals(CHANGE_TYPE_1)) {
                        goodsDO.setRealSum(goodsDO.getRealSum() + saleDTO.getSaleNum());
                    } else {
                        goodsDO.setRealSum(goodsDO.getRealSum() - saleDTO.getSaleNum());
                    }
                    if (goodsDO.getRealSum() < 0) {
                        goodsDO.setRealSum(0);
                    }
                    changeGoodsDOS.add(goodsDO);
                }
            });
        }
        return changeGoodsDOS;
    }

}
