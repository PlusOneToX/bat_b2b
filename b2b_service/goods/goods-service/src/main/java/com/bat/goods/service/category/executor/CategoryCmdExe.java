package com.bat.goods.service.category.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.category.convertor.CategoryConvertor;
import com.bat.goods.service.common.Constant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.category.dto.CategoryCmd;
import com.bat.goods.api.category.dto.CategoryId;
import com.bat.goods.api.category.dto.CategoryOpenCmd;
import com.bat.goods.dao.category.CategoryDistributorGroupRelevanceMapper;
import com.bat.goods.dao.category.CategoryMapper;
import com.bat.goods.dao.category.dataobject.CategoryDO;

@Component
public class CategoryCmdExe {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryRpcCmdExe rpcCmdExe;

    @Resource
    private CategoryDistributorGroupRelevanceMapper distributorGroupRelevanceMapper;

    /**
     * 创建品类
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(CategoryCmd cmd) {
        CategoryDO categoryDO = CategoryConvertor.toCategoryDo(cmd, Constant.OPERATION_TYPE_1);
        categoryMapper.createCategory(categoryDO);
        saveCategoryScope(categoryDO.getId(), cmd);
    }

    /**
     * 更新品类
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryCmd cmd) {
        CategoryDO categoryDO = CategoryConvertor.toCategoryDo(cmd, Constant.OPERATION_TYPE_2);
        categoryMapper.updateCategory(categoryDO);
        deleteCategoryScope(categoryDO.getId());
        saveCategoryScope(categoryDO.getId(), cmd);
    }

    /**
     * 更新品类状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openCategory(CategoryOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(Constant.OPEN_NO)) {
            // 停用品类前，确保品类下的商品都已转移或删除
            Integer count = categoryMapper.getCategoryGoodsCount(cmd.getId());
            if (count > 0) {
                throw GoodsException.buildException(ErrorCode.B_CATEGORY_GOODSNOTNULL);
            }
        }
        categoryMapper.openCategory(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 删除品类
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(CategoryId cmd) {
        CategoryDO categoryDO = categoryMapper.getById(cmd.getId());
        if (categoryDO == null) {
            throw GoodsException.buildException(ErrorCode.B_CATEGORY_NULL);
        }
        // 停用的品类才允许删除
        if (!categoryDO.getOpenFlag().equals(Constant.OPEN_NO)) {
            throw GoodsException.buildException(ErrorCode.B_CATEGORY_DELETE_OPEN_ERROR);
        }
        // TODO 删除品类关联数据
        categoryMapper.deleteCategory(cmd.getId());
    }

    /**
     * 保存品类可视和不可视关系
     * 
     * @param categoryId
     */
    public void saveCategoryScope(Integer categoryId, CategoryCmd cmd) {
        List list = CategoryConvertor.toCategoryRelevance(categoryId, cmd);// 可视关系
        if (list != null && list.size() > 0) {
            Short distributorScope = cmd.getDistributorScope();
            if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
                categoryMapper.createCategoryScalePriceRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
                categoryMapper.createCategoryDistributorRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
                categoryMapper.createCategoryDepartmentRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
                categoryMapper.createCategoryAdminRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
                distributorGroupRelevanceMapper.insertList(list);
            }
        }
        List listNo = CategoryConvertor.toCategoryRelevanceNO(categoryId, cmd);// 不可视关系
        if (listNo != null && listNo.size() > 0) {
            Short distributorScopeNo = cmd.getDistributorScopeNo();
            if (distributorScopeNo.equals(Constant.SCOPE_SCALE_PRICE)) {
                categoryMapper.createCategoryScalePriceRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_DISTRIBUTOR)) {
                categoryMapper.createCategoryDistributorRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_DEPARTMENT)) {
                categoryMapper.createCategoryDepartmentRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_ADMIN)) {
                categoryMapper.createCategoryAdminRelevanceNoList(listNo);
            }
        }
        // 更新分销商品类可视范围
        rpcCmdExe.distributorCategoryRelevance(categoryId, cmd);
    }

    /**
     * 删除品类可视和不可视关系
     * 
     * @param categoryId
     */
    public void deleteCategoryScope(Integer categoryId) {
        categoryMapper.deleteCategoryScalePriceRelevance(categoryId);
        categoryMapper.deleteCategoryScalePriceRelevanceNo(categoryId);
        categoryMapper.deleteCategoryDistributorRelevance(categoryId);
        categoryMapper.deleteCategoryDistributorRelevanceNo(categoryId);
        categoryMapper.deleteCategoryDepartmentRelevance(categoryId);
        categoryMapper.deleteCategoryDepartmentRelevanceNo(categoryId);
        categoryMapper.deleteCategoryAdminRelevance(categoryId);
        categoryMapper.deleteCategoryAdminRelevanceNo(categoryId);
        distributorGroupRelevanceMapper.deleteByCategoryId(categoryId);
    }

}
