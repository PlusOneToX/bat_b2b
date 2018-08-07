package com.bat.goods.service.brand.convertor;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.goods.dao.brand.dataobject.*;
import com.bat.goods.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.goods.api.brand.dto.BrandCmd;
import com.bat.goods.api.brand.dto.data.BrandDTO;
import com.bat.goods.api.brand.dto.data.BrandDistributorGroupDTO;
import com.bat.goods.api.brand.dto.data.BrandDistributorScopeDTO;
import com.bat.goods.dao.brand.dataobject.*;

public class BrandConvertor {
    /**
     * 品牌领域类和数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static BrandDO toBrandDo(BrandCmd cmd, Short operationType) {
        BrandDO brandDO = new BrandDO();
        BeanUtils.copyProperties(cmd, brandDO);
        Date date = new Date(System.currentTimeMillis());
        if (operationType.equals(Constant.OPERATION_TYPE_1)) {
            brandDO.setId(null);
        }
        brandDO.setCreateTime(date);
        brandDO.setUpdateTime(date);
        return brandDO;
    }

    public static List<BrandDTO> toBrandDTOList(List<BrandDO> brandDOList) {
        List<BrandDTO> brandDTOList = new ArrayList<>();
        brandDOList.forEach(brandDO -> {
            BrandDTO brandDTO = new BrandDTO();
            BeanUtils.copyProperties(brandDO, brandDTO);
            brandDTOList.add(brandDTO);
        });
        return brandDTOList;
    }

    public static BrandDTO toBrandDTO(BrandDO brandDO) {
        BrandDTO brandDTO = new BrandDTO();
        BeanUtils.copyProperties(brandDO, brandDTO);
        return brandDTO;
    }

    /**
     * 获取品牌可视关系列表
     * 
     * @param brandId
     * @param cmd
     * @return
     */
    public static List toBrandRelevance(Integer brandId, BrandCmd cmd) {
        List<Integer> scalePriceIds = cmd.getScalePriceIds();
        List<Integer> distributorIds = cmd.getDistributorIds();
        List<Integer> departmentIds = cmd.getDepartmentIds();
        List<Integer> adminIds = cmd.getAdminIds();
        List<Integer> distributorGroupIds = cmd.getDistributorGroupIds();
        if (cmd.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceIds)) {
            List<BrandScalePriceRelevanceDO> scalePriceRelevanceDOS = new ArrayList<>();
            scalePriceIds.forEach(id -> {
                BrandScalePriceRelevanceDO scalePriceRelevanceDO = new BrandScalePriceRelevanceDO();
                scalePriceRelevanceDO.setBrandId(brandId);
                scalePriceRelevanceDO.setScalePriceId(id);
                scalePriceRelevanceDOS.add(scalePriceRelevanceDO);
            });
            return scalePriceRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributorIds)) {
            List<BrandDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
            distributorIds.forEach(id -> {
                BrandDistributorRelevanceDO distributorRelevanceDO = new BrandDistributorRelevanceDO();
                distributorRelevanceDO.setBrandId(brandId);
                distributorRelevanceDO.setDistributorId(id);
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
            return distributorRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentIds)) {
            List<BrandDepartmentRelevanceDO> departmentRelevanceDOS = new ArrayList<>();
            departmentIds.forEach(id -> {
                BrandDepartmentRelevanceDO departmentRelevanceDO = new BrandDepartmentRelevanceDO();
                departmentRelevanceDO.setBrandId(brandId);
                departmentRelevanceDO.setDepartmentId(id);
                departmentRelevanceDOS.add(departmentRelevanceDO);
            });
            return departmentRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminIds)) {
            List<BrandAdminRelevanceDO> adminRelevanceDOS = new ArrayList<>();
            adminIds.forEach(id -> {
                BrandAdminRelevanceDO adminRelevanceDO = new BrandAdminRelevanceDO();
                adminRelevanceDO.setBrandId(brandId);
                adminRelevanceDO.setAdminId(id);
                adminRelevanceDOS.add(adminRelevanceDO);
            });
            return adminRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)
            && !CollectionUtils.isEmpty(distributorGroupIds)) {
            List<BrandDistributorGroupRelevanceDO> distributorGroupRelevanceDOS = new ArrayList<>();
            distributorGroupIds.forEach(id -> {
                BrandDistributorGroupRelevanceDO distributorGroupRelevanceDO = new BrandDistributorGroupRelevanceDO();
                distributorGroupRelevanceDO.setBrandId(brandId);
                distributorGroupRelevanceDO.setDistributorGroupId(id);
                distributorGroupRelevanceDOS.add(distributorGroupRelevanceDO);
            });
            return distributorGroupRelevanceDOS;
        } else {
            return null;
        }
    }

    /**
     * 获取品牌不可视关系列表
     * 
     * @param brandId
     * @param cmd
     * @return
     */
    public static List toBrandRelevanceNO(Integer brandId, BrandCmd cmd) {
        List<Integer> scalePriceNoIds = cmd.getScalePriceNoIds();
        List<Integer> distributorNoIds = cmd.getDistributorNoIds();
        List<Integer> departmentNoIds = cmd.getDepartmentNoIds();
        List<Integer> adminNoIds = cmd.getAdminNoIds();
        if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceNoIds)) {
            List<BrandScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS = new ArrayList<>();
            scalePriceNoIds.forEach(id -> {
                BrandScalePriceRelevanceNoDO scalePriceRelevanceNoDO = new BrandScalePriceRelevanceNoDO();
                scalePriceRelevanceNoDO.setBrandId(brandId);
                scalePriceRelevanceNoDO.setScalePriceId(id);
                scalePriceRelevanceNoDOS.add(scalePriceRelevanceNoDO);
            });
            return scalePriceRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)
            && !CollectionUtils.isEmpty(distributorNoIds)) {
            List<BrandDistributorRelevanceNoDO> distributorRelevanceNoDOS = new ArrayList<>();
            distributorNoIds.forEach(id -> {
                BrandDistributorRelevanceNoDO distributorRelevanceNoDO = new BrandDistributorRelevanceNoDO();
                distributorRelevanceNoDO.setBrandId(brandId);
                distributorRelevanceNoDO.setDistributorId(id);
                distributorRelevanceNoDOS.add(distributorRelevanceNoDO);
            });
            return distributorRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentNoIds)) {
            List<BrandDepartmentRelevanceNoDO> departmentRelevanceNoDOS = new ArrayList<>();
            departmentNoIds.forEach(id -> {
                BrandDepartmentRelevanceNoDO departmentRelevanceNoDO = new BrandDepartmentRelevanceNoDO();
                departmentRelevanceNoDO.setBrandId(brandId);
                departmentRelevanceNoDO.setDepartmentId(id);
                departmentRelevanceNoDOS.add(departmentRelevanceNoDO);
            });
            return departmentRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminNoIds)) {
            List<BrandAdminRelevanceNoDO> adminRelevanceNoDOS = new ArrayList<>();
            adminNoIds.forEach(id -> {
                BrandAdminRelevanceNoDO adminRelevanceNoDO = new BrandAdminRelevanceNoDO();
                adminRelevanceNoDO.setBrandId(brandId);
                adminRelevanceNoDO.setAdminId(id);
                adminRelevanceNoDOS.add(adminRelevanceNoDO);
            });
            return adminRelevanceNoDOS;
        } else {
            return null;
        }
    }

    public static List<UserBrandRpcDTO> toUserBrandRpcDTOList(List<UserBrandDO> brandDOS) {
        List<UserBrandRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(brandDOS)) {
            brandDOS.forEach(brandDO -> {
                UserBrandRpcDTO dto = new UserBrandRpcDTO();
                BeanUtils.copyProperties(brandDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static UserBrandRpcDTO toUserBrandRpcDTO(BrandDO brandDO) {
        if (brandDO != null) {
            UserBrandRpcDTO rpcDTO = new UserBrandRpcDTO();
            BeanUtils.copyProperties(brandDO, rpcDTO);
            return rpcDTO;
        }
        return null;
    }

    public static List<BrandDistributorScopeDTO> toBrandDistributorScopeDTOList(List<Integer> distributorIds,
        List<DistributorNameRpcDTO> distributorNameRpcDTOS) {
        List<BrandDistributorScopeDTO> distributorScopeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorIds)) {
            Map<Integer, DistributorNameRpcDTO> distributorNameRpcDTOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(distributorNameRpcDTOS)) {
                distributorNameRpcDTOMap.putAll(distributorNameRpcDTOS.stream().collect(
                    Collectors.toMap(DistributorNameRpcDTO::getId, distributorNameRpcDTO -> distributorNameRpcDTO)));
            }
            distributorIds.forEach(distributorId -> {
                BrandDistributorScopeDTO distributorScopeDTO = new BrandDistributorScopeDTO();
                distributorScopeDTO.setDistributorId(distributorId);
                DistributorNameRpcDTO nameRpcDTO = distributorNameRpcDTOMap.get(distributorId);
                if (nameRpcDTO != null) {
                    distributorScopeDTO.setName(nameRpcDTO.getName());
                    distributorScopeDTO.setCompanyName(nameRpcDTO.getCompanyName());
                }
                distributorScopeDTOS.add(distributorScopeDTO);
            });
        }
        return distributorScopeDTOS;
    }

    public static List<BrandDistributorGroupDTO> toBrandDistributorGroupDTOList(List<Integer> distributorGroupIds,
        List<DistributorGroupRpcDTO> groupRpcDTOS) {
        List<BrandDistributorGroupDTO> distributorGroupDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorGroupIds)) {
            Map<Integer, DistributorGroupRpcDTO> groupRpcDTOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(groupRpcDTOS)) {
                groupRpcDTOMap.putAll(groupRpcDTOS.stream()
                    .collect(Collectors.toMap(DistributorGroupRpcDTO::getId, groupRpcDTO -> groupRpcDTO)));
            }
            distributorGroupIds.forEach(distributorGroupId -> {
                BrandDistributorGroupDTO distributorGroupDTO = new BrandDistributorGroupDTO();
                distributorGroupDTO.setDistributorGroupId(distributorGroupId);
                DistributorGroupRpcDTO groupRpcDTO = groupRpcDTOMap.get(distributorGroupId);
                if (groupRpcDTO != null) {
                    distributorGroupDTO.setName(groupRpcDTO.getName());
                    distributorGroupDTO.setErpGroupNo(groupRpcDTO.getErpGroupNo());
                }
                distributorGroupDTOS.add(distributorGroupDTO);
            });
        }
        return distributorGroupDTOS;
    }

}
