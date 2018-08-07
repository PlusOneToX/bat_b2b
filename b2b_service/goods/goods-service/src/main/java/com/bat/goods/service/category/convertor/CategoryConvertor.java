package com.bat.goods.service.category.convertor;

import static com.bat.goods.service.common.Constant.*;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.goods.dao.category.dataobject.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.goods.api.category.dto.CategoryCmd;
import com.bat.goods.api.category.dto.data.CategoryDTO;
import com.bat.goods.api.category.dto.data.CategoryDistributorGroupDTO;
import com.bat.goods.api.category.dto.data.CategoryDistributorScopeDTO;
import com.bat.goods.dao.category.dataobject.*;

public class CategoryConvertor {
    /**
     * 品类领域类和数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static CategoryDO toCategoryDo(CategoryCmd cmd, Short operationType) {
        CategoryDO categoryDO = new CategoryDO();
        BeanUtils.copyProperties(cmd, categoryDO);
        Date date = new Date(System.currentTimeMillis());
        if (operationType.equals(OPERATION_TYPE_1)) {
            categoryDO.setId(null);
        }
        categoryDO.setCreateTime(date);
        categoryDO.setUpdateTime(date);
        return categoryDO;
    }

    public static List<CategoryDTO> toCategoryDTOList(List<CategoryDO> categoryDOList) {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryDOList.forEach(categoryDO -> {
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(categoryDO, categoryDTO);
            categoryDTOList.add(categoryDTO);
        });
        return categoryDTOList;
    }

    public static CategoryDTO toCategoryDTO(CategoryDO categoryDO) {
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(categoryDO, categoryDTO);
        return categoryDTO;
    }

    /**
     * 获取品类可视关系列表
     * 
     * @param categoryId
     * @param cmd
     * @return
     */
    public static List toCategoryRelevance(Integer categoryId, CategoryCmd cmd) {
        List<Integer> scalePriceIds = cmd.getScalePriceIds();
        List<Integer> distributorIds = cmd.getDistributorIds();
        List<Integer> departmentIds = cmd.getDepartmentIds();
        List<Integer> adminIds = cmd.getAdminIds();
        List<Integer> distributorGroupIds = cmd.getDistributorGroupIds();
        if (cmd.getDistributorScope().equals(SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceIds)) {
            List<CategoryScalePriceRelevanceDO> scalePriceRelevanceDOS = new ArrayList<>();
            scalePriceIds.forEach(id -> {
                CategoryScalePriceRelevanceDO scalePriceRelevanceDO = new CategoryScalePriceRelevanceDO();
                scalePriceRelevanceDO.setCategoryId(categoryId);
                scalePriceRelevanceDO.setScalePriceId(id);
                scalePriceRelevanceDOS.add(scalePriceRelevanceDO);
            });
            return scalePriceRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributorIds)) {
            List<CategoryDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
            distributorIds.forEach(id -> {
                CategoryDistributorRelevanceDO distributorRelevanceDO = new CategoryDistributorRelevanceDO();
                distributorRelevanceDO.setCategoryId(categoryId);
                distributorRelevanceDO.setDistributorId(id);
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
            return distributorRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentIds)) {
            List<CategoryDepartmentRelevanceDO> departmentRelevanceDOS = new ArrayList<>();
            departmentIds.forEach(id -> {
                CategoryDepartmentRelevanceDO departmentRelevanceDO = new CategoryDepartmentRelevanceDO();
                departmentRelevanceDO.setCategoryId(categoryId);
                departmentRelevanceDO.setDepartmentId(id);
                departmentRelevanceDOS.add(departmentRelevanceDO);
            });
            return departmentRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminIds)) {
            List<CategoryAdminRelevanceDO> adminRelevanceDOS = new ArrayList<>();
            adminIds.forEach(id -> {
                CategoryAdminRelevanceDO adminRelevanceDO = new CategoryAdminRelevanceDO();
                adminRelevanceDO.setCategoryId(categoryId);
                adminRelevanceDO.setAdminId(id);
                adminRelevanceDOS.add(adminRelevanceDO);
            });
            return adminRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(SCOPE_DISTRIBUTOR_GROUP)
            && !CollectionUtils.isEmpty(distributorGroupIds)) {
            List<CategoryDistributorGroupRelevanceDO> distributorGroupRelevanceDOS = new ArrayList<>();
            distributorGroupIds.forEach(id -> {
                CategoryDistributorGroupRelevanceDO distributorGroupRelevanceDO =
                    new CategoryDistributorGroupRelevanceDO();
                distributorGroupRelevanceDO.setCategoryId(categoryId);
                distributorGroupRelevanceDO.setDistributorGroupId(id);
                distributorGroupRelevanceDOS.add(distributorGroupRelevanceDO);
            });
            return distributorGroupRelevanceDOS;
        } else {
            return null;
        }
    }

    /**
     * 获取品类不可视关系列表
     * 
     * @param categoryId
     * @param cmd
     * @return
     */
    public static List toCategoryRelevanceNO(Integer categoryId, CategoryCmd cmd) {
        List<Integer> scalePriceNoIds = cmd.getScalePriceNoIds();
        List<Integer> distributorNoIds = cmd.getDistributorNoIds();
        List<Integer> departmentNoIds = cmd.getDepartmentNoIds();
        List<Integer> adminNoIds = cmd.getAdminNoIds();
        if (cmd.getDistributorScopeNo().equals(SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceNoIds)) {
            List<CategoryScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS = new ArrayList<>();
            scalePriceNoIds.forEach(id -> {
                CategoryScalePriceRelevanceNoDO scalePriceRelevanceNoDO = new CategoryScalePriceRelevanceNoDO();
                scalePriceRelevanceNoDO.setCategoryId(categoryId);
                scalePriceRelevanceNoDO.setScalePriceId(id);
                scalePriceRelevanceNoDOS.add(scalePriceRelevanceNoDO);
            });
            return scalePriceRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_DISTRIBUTOR)
            && !CollectionUtils.isEmpty(distributorNoIds)) {
            List<CategoryDistributorRelevanceNoDO> distributorRelevanceNoDOS = new ArrayList<>();
            distributorNoIds.forEach(id -> {
                CategoryDistributorRelevanceNoDO distributorRelevanceNoDO = new CategoryDistributorRelevanceNoDO();
                distributorRelevanceNoDO.setCategoryId(categoryId);
                distributorRelevanceNoDO.setDistributorId(id);
                distributorRelevanceNoDOS.add(distributorRelevanceNoDO);
            });
            return distributorRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentNoIds)) {
            List<CategoryDepartmentRelevanceNoDO> departmentRelevanceNoDOS = new ArrayList<>();
            departmentNoIds.forEach(id -> {
                CategoryDepartmentRelevanceNoDO departmentRelevanceNoDO = new CategoryDepartmentRelevanceNoDO();
                departmentRelevanceNoDO.setCategoryId(categoryId);
                departmentRelevanceNoDO.setDepartmentId(id);
                departmentRelevanceNoDOS.add(departmentRelevanceNoDO);
            });
            return departmentRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminNoIds)) {
            List<CategoryAdminRelevanceNoDO> adminRelevanceNoDOS = new ArrayList<>();
            adminNoIds.forEach(id -> {
                CategoryAdminRelevanceNoDO adminRelevanceNoDO = new CategoryAdminRelevanceNoDO();
                adminRelevanceNoDO.setCategoryId(categoryId);
                adminRelevanceNoDO.setAdminId(id);
                adminRelevanceNoDOS.add(adminRelevanceNoDO);
            });
            return adminRelevanceNoDOS;
        } else {
            return null;
        }
    }

    public static List<CategoryDistributorScopeDTO> toCategoryDistributorScopeDTOList(List<Integer> distributorIds,
        List<DistributorNameRpcDTO> distributorNameRpcDTOS) {
        List<CategoryDistributorScopeDTO> distributorScopeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorIds)) {
            Map<Integer, DistributorNameRpcDTO> distributorNameRpcDTOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(distributorNameRpcDTOS)) {
                distributorNameRpcDTOMap.putAll(distributorNameRpcDTOS.stream().collect(
                    Collectors.toMap(DistributorNameRpcDTO::getId, distributorNameRpcDTO -> distributorNameRpcDTO)));
            }
            distributorIds.forEach(distributorId -> {
                CategoryDistributorScopeDTO distributorScopeDTO = new CategoryDistributorScopeDTO();
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

    public static List<CategoryDistributorGroupDTO> toCategoryDistributorGroupDTOList(List<Integer> distributorGroupIds,
        List<DistributorGroupRpcDTO> groupRpcDTOS) {
        List<CategoryDistributorGroupDTO> distributorGroupDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorGroupIds)) {
            Map<Integer, DistributorGroupRpcDTO> groupRpcDTOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(groupRpcDTOS)) {
                groupRpcDTOMap.putAll(groupRpcDTOS.stream()
                    .collect(Collectors.toMap(DistributorGroupRpcDTO::getId, groupRpcDTO -> groupRpcDTO)));
            }
            distributorGroupIds.forEach(distributorGroupId -> {
                CategoryDistributorGroupDTO distributorGroupDTO = new CategoryDistributorGroupDTO();
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
