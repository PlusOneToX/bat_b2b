package com.bat.goods.service.category.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.category.convertor.CategoryConvertor;
import com.bat.goods.service.common.CommonRpcQryExe;
import com.bat.goods.service.common.Constant;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.category.dto.CategoryId;
import com.bat.goods.api.category.dto.CategoryListQry;
import com.bat.goods.api.category.dto.data.CategoryDTO;
import com.bat.goods.dao.category.CategoryDistributorGroupRelevanceMapper;
import com.bat.goods.dao.category.CategoryMapper;
import com.bat.goods.dao.category.dataobject.CategoryDO;

@Component
public class CategoryQryExe {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private CommonRpcQryExe commonRpcQryExe;
    @Resource
    private CategoryDistributorGroupRelevanceMapper categoryDistributorGroupRelevanceMapper;

    /**
     * 查询品类列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<CategoryDTO> executeList(CategoryListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CategoryDO> categoryDOList = categoryMapper.listCategory(qryMap);
        PageInfo pageInfo = new PageInfo(categoryDOList);
        List<CategoryDTO> categoryDTOList = CategoryConvertor.toCategoryDTOList(pageInfo.getList());
        pageInfo.setList(categoryDTOList);
        return pageInfo;
    }

    /**
     * 根据品类ID查询品类详情
     * 
     * @param categoryId
     * @return
     */
    public CategoryDTO execute(CategoryId categoryId) {
        CategoryDO categoryDO = categoryMapper.getById(categoryId.getId());
        if (categoryDO == null) {
            throw GoodsException.buildException(ErrorCode.B_CATEGORY_NULL);
        }
        CategoryDTO categoryDTO = CategoryConvertor.toCategoryDTO(categoryDO);
        getCategoryScopeIds(categoryDTO);
        getCategoryScopeNoIds(categoryDTO);
        return categoryDTO;
    }

    /**
     * 获取品类可视关联关系
     * 
     * @param categoryDTO
     */
    public void getCategoryScopeIds(CategoryDTO categoryDTO) {
        Integer categoryId = categoryDTO.getId();
        if (categoryDTO.getDistributorScope() == null) {
            return;
        }
        if (categoryDTO.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE)) {
            categoryDTO.setScalePriceIds(categoryMapper.listCategoryScalePriceRelevanceId(categoryId));
        } else if (categoryDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorIds = categoryMapper.listCategoryDistributorRelevanceId(categoryId);
            List<DistributorNameRpcDTO> distributorNameRpcDTOS =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorIds);
            categoryDTO.setDistributors(
                CategoryConvertor.toCategoryDistributorScopeDTOList(distributorIds, distributorNameRpcDTOS));
        } else if (categoryDTO.getDistributorScope().equals(Constant.SCOPE_DEPARTMENT)) {
            categoryDTO.setDepartmentIds(categoryMapper.listCategoryDepartmentRelevanceId(categoryId));
        } else if (categoryDTO.getDistributorScope().equals(Constant.SCOPE_ADMIN)) {
            categoryDTO.setAdminIds(categoryMapper.listCategoryAdminRelevanceId(categoryId));
        } else if (categoryDTO.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
            List<Integer> distributorGroupIds =
                categoryDistributorGroupRelevanceMapper.listDistributorGroupIdByCategoryId(categoryId);
            List<DistributorGroupRpcDTO> groupRpcDTOS =
                commonRpcQryExe.getDistributorGroupByDistributorGroupIds(distributorGroupIds);
            categoryDTO.setDistributorGroups(
                CategoryConvertor.toCategoryDistributorGroupDTOList(distributorGroupIds, groupRpcDTOS));
        }
    }

    /**
     * 获取品类不可视关联关系
     * 
     * @param categoryDTO
     */
    public void getCategoryScopeNoIds(CategoryDTO categoryDTO) {
        Integer categoryId = categoryDTO.getId();
        if (categoryDTO.getDistributorScopeNo() == null) {
            return;
        }
        if (categoryDTO.getDistributorScopeNo().equals(Constant.SCOPE_SCALE_PRICE)) {
            categoryDTO.setScalePriceNoIds(categoryMapper.listCategoryScalePriceRelevanceIdNo(categoryId));
        } else if (categoryDTO.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorNoIds = categoryMapper.listCategoryDistributorRelevanceIdNo(categoryId);
            List<DistributorNameRpcDTO> distributorNameRpcDTOS =
                commonRpcQryExe.getDistributorNameByDistributorIds(distributorNoIds);
            categoryDTO.setDistributorNos(
                CategoryConvertor.toCategoryDistributorScopeDTOList(distributorNoIds, distributorNameRpcDTOS));
        } else if (categoryDTO.getDistributorScopeNo().equals(Constant.SCOPE_DEPARTMENT)) {
            categoryDTO.setDepartmentNoIds(categoryMapper.listCategoryDepartmentRelevanceIdNo(categoryId));
        } else if (categoryDTO.getDistributorScopeNo().equals(Constant.SCOPE_ADMIN)) {
            categoryDTO.setAdminNoIds(categoryMapper.listCategoryAdminRelevanceIdNo(categoryId));
        }
    }

}
