package com.bat.distributor.service.user.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.distributor.api.role.dto.data.RoleDTO;
import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialBrandCategoryDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialDO;
import com.bat.distributor.dao.platform.dataobject.WxPlatformDO;
import com.bat.distributor.dao.role.dataobject.RoleDO;
import com.bat.distributor.service.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.distributor.api.user.dto.user.*;
import com.bat.distributor.api.user.dto.user.data.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.UserGoodsRpcDTO;

public class UserConvertor {

    public static UserLoginDTO toUserDTO(DistributorDO distributorDO) {
        UserLoginDTO dto = new UserLoginDTO();
        BeanUtils.copyProperties(distributorDO, dto);
        dto.setUserName(distributorDO.getName());
        return dto;
    }

    public static ContactDTO toContactDTO(DistributorContactsDO contactsDO) {
        ContactDTO dto = new ContactDTO();
        BeanUtils.copyProperties(contactsDO, dto);
        return dto;
    }

    public static DistributorDO toDistributorDO(UserOneLevelApplyCmd cmd) {
        DistributorDO distributorDO = new DistributorDO();
        BeanUtils.copyProperties(cmd, distributorDO);
        Date date = new Date(System.currentTimeMillis());
        distributorDO.setApplyType(Constant.APPLY_TYPE_2);
        distributorDO.setApplyStatus(Constant.APPLY_STATUS_1);
        distributorDO.setApplyTime(date);
        distributorDO.setProfileStatus(Constant.PROFILE_STATUS_0);
        distributorDO.setFreezeStatus(Constant.FREEZE_STATUS_1);
        distributorDO.setTreeNode(1);
        distributorDO.setCreateTime(date);
        distributorDO.setUpdateTime(date);
        return distributorDO;
    }

    public static DistributorDO toDistributorDO(UserNextLevelApplyCmd cmd) {
        DistributorDO distributorDO = new DistributorDO();
        BeanUtils.copyProperties(cmd, distributorDO);
        Date date = new Date(System.currentTimeMillis());
        distributorDO.setApplyType(Constant.APPLY_TYPE_3);
        distributorDO.setApplyStatus(Constant.APPLY_STATUS_1);
        distributorDO.setApplyTime(date);
        distributorDO.setProfileStatus(Constant.PROFILE_STATUS_0);
        distributorDO.setFreezeStatus(Constant.FREEZE_STATUS_1);
        distributorDO.setCreateTime(date);
        distributorDO.setUpdateTime(date);
        return distributorDO;
    }

    public static DistributorContactsDO toDistributorContactsDO(UserOneLevelApplyCmd cmd, Integer distributorId) {
        DistributorContactsDO contactsDO = new DistributorContactsDO();
        contactsDO.setDistributorId(distributorId);
        contactsDO.setPassword(cmd.getPassword());
        contactsDO.setName(cmd.getAccountName());
        if (StringUtils.isNotBlank(cmd.getPhone())) {
            contactsDO.setPhone(cmd.getPhone());
        }
        if (StringUtils.isNotBlank(cmd.getEmail())) {
            contactsDO.setEmail(cmd.getEmail());
        }
        contactsDO.setOwnerFlag(Constant.OWNER_FLAG_1);
        contactsDO.setSex(Constant.SEX_0);
        contactsDO.setFreezeStatus(Constant.FREEZE_STATUS_1);
        Date date = new Date(System.currentTimeMillis());
        contactsDO.setCreateTime(date);
        contactsDO.setUpdateTime(date);
        return contactsDO;
    }

    /**
     * 添加分销商信息
     * 
     * @param cmd
     * @param distributorId
     * @return
     */
    public static DistributorAddressDO toDistributorAddressDO(UserOneLevelApplyCmd cmd, Integer distributorId) {
        DistributorAddressDO addressDO = new DistributorAddressDO();
        BeanUtils.copyProperties(cmd, addressDO);
        addressDO.setDistributorId(distributorId);
        addressDO.setAddressType(Constant.ADDRESS_TYPE_1);
        Date date = new Date(System.currentTimeMillis());
        addressDO.setCreateTime(date);
        addressDO.setUpdateTime(date);
        return addressDO;
    }

    /**
     * 添加收货地址
     * 
     * @param cmd
     * @return
     */
    public static DistributorAddressDO toDistributorAddressDO(UserAddressCmd cmd) {
        DistributorAddressDO addressDO = new DistributorAddressDO();
        BeanUtils.copyProperties(cmd, addressDO);
        Date date = new Date(System.currentTimeMillis());
        addressDO.setCreateTime(date);
        addressDO.setUpdateTime(date);
        return addressDO;
    }

    public static DistributorContactsDO toDistributorContactsDO(UserNextLevelApplyCmd cmd, Integer distributorId) {
        DistributorContactsDO contactsDO = new DistributorContactsDO();
        contactsDO.setDistributorId(distributorId);
        contactsDO.setPassword(cmd.getPassword());
        contactsDO.setName(cmd.getAccountName());
        if (StringUtils.isNotBlank(cmd.getPhone())) {
            contactsDO.setPhone(cmd.getPhone());
        }
        if (StringUtils.isNotBlank(cmd.getEmail())) {
            contactsDO.setEmail(cmd.getEmail());
        }
        contactsDO.setOwnerFlag(Constant.OWNER_FLAG_1);
        contactsDO.setSex(Constant.SEX_0);
        contactsDO.setFreezeStatus(Constant.FREEZE_STATUS_1);
        Date date = new Date(System.currentTimeMillis());
        contactsDO.setCreateTime(date);
        contactsDO.setUpdateTime(date);
        return contactsDO;
    }

    public static DistributorContactsDO toDistributorContactsDO(UserContactCmd cmd) {
        DistributorContactsDO contactsDO = new DistributorContactsDO();
        BeanUtils.copyProperties(cmd, contactsDO);
        contactsDO.setFreezeStatus(Constant.FREEZE_STATUS_1);
        Date date = new Date(System.currentTimeMillis());
        contactsDO.setCreateTime(date);
        contactsDO.setUpdateTime(date);
        return contactsDO;
    }

    public static DistributorExtendDataDO toDistributorExtendDataDO(UserOneLevelApplyCmd cmd, Integer distributorId) {
        DistributorExtendDataDO extendDataDO = new DistributorExtendDataDO();
        extendDataDO.setDistributorId(distributorId);
        extendDataDO.setDistributionFlag(Constant.DISTRIBUTION_FLAG_0);
        extendDataDO.setDistributionMode(Constant.DISTRIBUTION_MODE_1);
        extendDataDO.setDistributionAutoFlag(Constant.AUTO_FLAG_1);
        extendDataDO.setCustomerFlag(Constant.CUSTOMER_FLAG_0);
        extendDataDO.setCustomerMode(Constant.CUSTOMER_MODE_1);
        extendDataDO.setDistributionPromotionFlag(Constant.DISTRIBUTION_PROMOTION_FLAG_0);
        extendDataDO.setCertNo(cmd.getCertNo());
        extendDataDO.setErpFlag(Constant.ERP_FLAG_1);
        extendDataDO.setErpFlag(Constant.ERP_BALANCE_FLAG_0);
        extendDataDO.setComment(cmd.getComment());
        Date date = new Date(System.currentTimeMillis());
        extendDataDO.setCreateTime(date);
        extendDataDO.setUpdateTime(date);
        return extendDataDO;
    }

    public static DistributorExtendDataDO toDistributorExtendDataDO(UserNextLevelApplyCmd cmd, Integer distributorId) {
        DistributorExtendDataDO extendDataDO = new DistributorExtendDataDO();
        extendDataDO.setDistributorId(distributorId);
        extendDataDO.setDistributionFlag(Constant.DISTRIBUTION_FLAG_0);
        extendDataDO.setDistributionMode(Constant.DISTRIBUTION_MODE_1);
        extendDataDO.setDistributionAutoFlag(Constant.AUTO_FLAG_1);
        extendDataDO.setCustomerFlag(Constant.CUSTOMER_FLAG_0);
        extendDataDO.setCustomerMode(Constant.CUSTOMER_MODE_1);
        extendDataDO.setDistributionPromotionFlag(Constant.DISTRIBUTION_PROMOTION_FLAG_0);
        extendDataDO.setErpFlag(Constant.ERP_FLAG_0);
        extendDataDO.setErpFlag(Constant.ERP_BALANCE_FLAG_0);
        Date date = new Date(System.currentTimeMillis());
        extendDataDO.setCreateTime(date);
        extendDataDO.setUpdateTime(date);
        return extendDataDO;
    }

    public static List<ContactDTO> toDistributorContactsDTOList(List<DistributorContactsDO> contactsDOS) {
        List<ContactDTO> contactsDTOS = new ArrayList<>();
        contactsDOS.forEach(contactsDO -> {
            ContactDTO contactsDTO = new ContactDTO();
            BeanUtils.copyProperties(contactsDO, contactsDTO);
            contactsDTOS.add(contactsDTO);
        });
        return contactsDTOS;
    }

    public static UserInfoDTO toUserInfoDTO(DistributorDO distributorDO, DistributorFinancialDO financialDO,
                                            List<DistributorAddressDO> addressDOS, List<DistributorContactsDO> contactsDOS,
                                            DistributorExtendDataDO extendDataDO) {
        UserInfoDTO dto = new UserInfoDTO();
        BeanUtils.copyProperties(distributorDO, dto);
        if (financialDO != null) {
            UserFinancialDTO financialDTO = new UserFinancialDTO();
            BeanUtils.copyProperties(financialDO, financialDTO);
            dto.setFinancial(financialDTO);
        }
        if (!CollectionUtils.isEmpty(addressDOS)) {
            BeanUtils.copyProperties(addressDOS.get(0), dto);
        }
        if (!CollectionUtils.isEmpty(contactsDOS)) {
            DistributorContactsDO contactsDO = contactsDOS.get(0);
            dto.setUserName(contactsDO.getName());
            dto.setPhone(contactsDO.getPhone());
            dto.setEmail(contactsDO.getEmail());
        }
        if (extendDataDO != null) {
            dto.setCertNo(extendDataDO.getCertNo());
        }
        return dto;
    }

    public static List<UserAddressDTO> toUserAddressDTOList(List<DistributorAddressDO> addressDOS) {
        List<UserAddressDTO> addressDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addressDOS)) {
            addressDOS.forEach(addressDO -> {
                UserAddressDTO addressDTO = new UserAddressDTO();
                BeanUtils.copyProperties(addressDO, addressDTO);
                addressDTOS.add(addressDTO);
            });
        }
        return addressDTOS;
    }

    public static List<RoleDTO> toRoleDTOList(List<RoleDO> doList) {
        List<RoleDTO> dtoList = new ArrayList<>();
        doList.forEach(roleDO -> {
            RoleDTO dto = new RoleDTO();
            BeanUtils.copyProperties(roleDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static List<DistributorTreePathDO> toNextTreePathDOList(List<DistributorTreePathDO> treePathDOS,
        DistributorDO distributorDO, DistributorDO nextDistributorDO) {
        List<DistributorTreePathDO> nextTreePathDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(treePathDOS)) {
            treePathDOS.forEach(treePathDO -> {
                DistributorTreePathDO distributorTreePathDO = new DistributorTreePathDO();
                BeanUtils.copyProperties(treePathDO, distributorTreePathDO);
                distributorTreePathDO.setDistributorDescendantId(nextDistributorDO.getId());
                distributorTreePathDO.setTreeNode(treePathDO.getTreeNode() + 1);
                nextTreePathDOS.add(distributorTreePathDO);
            });
        }
        DistributorTreePathDO distributorTreePathDO = new DistributorTreePathDO();
        distributorTreePathDO.setDistributorAncestorId(distributorDO.getId());
        distributorTreePathDO.setDistributorDescendantId(nextDistributorDO.getId());
        distributorTreePathDO.setTreeNode(1);
        nextTreePathDOS.add(distributorTreePathDO);
        return nextTreePathDOS;
    }

    public static List<UserNextScalePriceDTO> toNextScalePriceDTOList(List<NextScalePriceDO> nextScalePriceDOS) {
        List<UserNextScalePriceDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nextScalePriceDOS)) {
            nextScalePriceDOS.forEach(nextScalePriceDO -> {
                UserNextScalePriceDTO dto = new UserNextScalePriceDTO();
                BeanUtils.copyProperties(nextScalePriceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static NextScalePriceDO toNextScalePriceDO(UserNextScalePriceCmd cmd) {
        NextScalePriceDO nextScalePriceDO = new NextScalePriceDO();
        BeanUtils.copyProperties(cmd, nextScalePriceDO);
        Date date = new Date(System.currentTimeMillis());
        nextScalePriceDO.setCreateTime(date);
        nextScalePriceDO.setUpdateTime(date);
        return nextScalePriceDO;
    }

    public static NextScalePriceSpecialDO toNextScalePriceSpecialDO(UserNextScalePriceSpecialCmd cmd) {
        NextScalePriceSpecialDO scalePriceSpecialDO = new NextScalePriceSpecialDO();
        BeanUtils.copyProperties(cmd, scalePriceSpecialDO);
        return scalePriceSpecialDO;
    }

    public static List<NextScalePriceSpecialBrandCategoryDO> toNextScalePriceSpecialBrandCategoryDO(
        List<UserNextScalePriceSpecialBrandCategoryCmd> cmds, Integer nextScalePriceSpecialId) {
        List<NextScalePriceSpecialBrandCategoryDO> brandCategoryDOS = new ArrayList<>();
        cmds.forEach(cmd -> {
            NextScalePriceSpecialBrandCategoryDO brandCategoryDO = new NextScalePriceSpecialBrandCategoryDO();
            BeanUtils.copyProperties(cmd, brandCategoryDO);
            brandCategoryDO.setNextScalePriceSpecialId(nextScalePriceSpecialId);
            brandCategoryDOS.add(brandCategoryDO);
        });
        return brandCategoryDOS;
    }

    public static UserNextScalePriceDTO toUserNextScalePriceDTO(NextScalePriceDO nextScalePriceDO) {
        UserNextScalePriceDTO dto = new UserNextScalePriceDTO();
        BeanUtils.copyProperties(nextScalePriceDO, dto);
        return dto;
    }

    public static UserNextScalePriceSpecialDTO
        toUserNextScalePriceSpecialDTO(NextScalePriceSpecialDO nextScalePriceSpecialDO) {
        UserNextScalePriceSpecialDTO dto = new UserNextScalePriceSpecialDTO();
        BeanUtils.copyProperties(nextScalePriceSpecialDO, dto);
        return dto;
    }

    public static List<UserNextScalePriceSpecialBrandCategoryDTO>
        toUserNextScalePriceSpecialBrandCategoryDTOList(List<NextScalePriceSpecialBrandCategoryDO> brandCategoryDOS) {
        List<UserNextScalePriceSpecialBrandCategoryDTO> dtos = new ArrayList<>();
        brandCategoryDOS.forEach(brandCategoryDO -> {
            UserNextScalePriceSpecialBrandCategoryDTO dto = new UserNextScalePriceSpecialBrandCategoryDTO();
            BeanUtils.copyProperties(brandCategoryDO, dto);
            dtos.add(dto);
        });
        return dtos;
    }

    public static List<UserNextListDTO> toUserNextListDTOList(List<UerNextListDO> uerNextListDOS) {
        List<UserNextListDTO> dtos = new ArrayList<>();
        uerNextListDOS.forEach(uerNextListDO -> {
            UserNextListDTO dto = new UserNextListDTO();
            BeanUtils.copyProperties(uerNextListDO, dto);
            dtos.add(dto);
        });
        return dtos;
    }

    public static UserNextDTO toUserNextDTO(DistributorNextDO distributorNextDO) {
        UserNextDTO dto = new UserNextDTO();
        BeanUtils.copyProperties(distributorNextDO, dto);
        return dto;
    }

    public static List<UserBrandDTO> toUserBrandDTOList(List<UserBrandRpcDTO> userBrandRpcDTOS) {
        List<UserBrandDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userBrandRpcDTOS)) {
            userBrandRpcDTOS.forEach(userBrandRpcDTO -> {
                UserBrandDTO dto = new UserBrandDTO();
                BeanUtils.copyProperties(userBrandRpcDTO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<UserGoodsDTO> toUserGoodsDTOList(List<UserGoodsRpcDTO> userGoodsRpcDTOS) {
        List<UserGoodsDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userGoodsRpcDTOS)) {
            userGoodsRpcDTOS.forEach(userGoodsRpcDTO -> {
                UserGoodsDTO dto = new UserGoodsDTO();
                BeanUtils.copyProperties(userGoodsRpcDTO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static DistributorBrandRelevanceNoDO toDistributorBrandRelevanceNoDO(List<Integer> brandIds,
        Integer distributorId, DistributorBrandRelevanceNoDO relevanceNoDO) {
        if (relevanceNoDO == null) {
            relevanceNoDO = new DistributorBrandRelevanceNoDO();
        }
        relevanceNoDO.setDistributorId(distributorId);
        relevanceNoDO.setBrandIds(
            "," + String.join(",", brandIds.stream().map(String::valueOf).collect(Collectors.toList())) + ",");
        return relevanceNoDO;
    }

    public static DistributorGoodsRelevanceNoDO toDistributorGoodsRelevanceNoDO(List<Integer> goodsIds,
        Integer distributorId, DistributorGoodsRelevanceNoDO relevanceNoDO) {
        if (relevanceNoDO == null) {
            relevanceNoDO = new DistributorGoodsRelevanceNoDO();
        }
        relevanceNoDO.setDistributorId(distributorId);
        relevanceNoDO.setGoodsIds(
            "," + String.join(",", goodsIds.stream().map(String::valueOf).collect(Collectors.toList())) + ",");
        return relevanceNoDO;
    }

    public static DistributorBusinessDO toDistributorBusinessDO(DistributorBusinessDO maxBusinessDO,
        DistributorBusinessDO minBusinessDO, DistributorExtendDataDO minExtendDataDO, Integer distributorId) {
        DistributorBusinessDO nextBusinessDO = new DistributorBusinessDO();
        Date date = new Date(System.currentTimeMillis());
        nextBusinessDO.setCreateTime(date);
        nextBusinessDO.setUpdateTime(date);
        nextBusinessDO.setDistributorId(distributorId);
        nextBusinessDO.setSalesId(maxBusinessDO.getSalesId());
        nextBusinessDO.setCoordinatorId(maxBusinessDO.getCoordinatorId());
        nextBusinessDO.setAutoDelivery(maxBusinessDO.getAutoDelivery());
        nextBusinessDO.setOnWayFlag(maxBusinessDO.getOnWayFlag());
        nextBusinessDO.setCanExportPrice(Constant.CAN_EXPORT_PRICE_0);
        if (minExtendDataDO.getDistributionPromotionFlag().equals(Constant.DISTRIBUTION_PROMOTION_FLAG_1)) {
            nextBusinessDO.setPromotionScope(minBusinessDO.getPromotionScope());
            nextBusinessDO.setPromotionTypes(minBusinessDO.getPromotionTypes());
        } else {
            nextBusinessDO.setPromotionScope(Constant.PROMOTION_SCOPE_0);
        }
        nextBusinessDO.setRxShopSwitch(maxBusinessDO.getRxShopSwitch());
        nextBusinessDO.setLogisticsSmsSwitch(maxBusinessDO.getLogisticsSmsSwitch());
        return nextBusinessDO;
    }

    public static List<UserWxPlatformListDTO> toUserWxPlatformListDTOList(List<WxPlatformDO> wxPlatformDOS) {
        List<UserWxPlatformListDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(wxPlatformDOS)) {
            wxPlatformDOS.forEach(wxPlatformDO -> {
                UserWxPlatformListDTO dto = new UserWxPlatformListDTO();
                BeanUtils.copyProperties(wxPlatformDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }
}
