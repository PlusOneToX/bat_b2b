package com.bat.goods.service.category;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.goods.service.category.executor.CategoryCmdExe;
import com.bat.goods.service.category.executor.CategoryQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.category.CategoryServiceI;
import com.bat.goods.api.category.dto.CategoryCmd;
import com.bat.goods.api.category.dto.CategoryId;
import com.bat.goods.api.category.dto.CategoryListQry;
import com.bat.goods.api.category.dto.CategoryOpenCmd;
import com.bat.goods.api.category.dto.data.CategoryDTO;

@Service
public class CategoryServiceImpl implements CategoryServiceI {

    @Resource
    private CategoryCmdExe categoryCmdExe;

    @Resource
    private CategoryQryExe categoryQryExe;

    @Override
    public void createCategory(CategoryCmd cmd) {
        categoryCmdExe.createCategory(cmd);
    }

    @Override
    public void updateCategory(CategoryCmd cmd) {
        categoryCmdExe.updateCategory(cmd);
    }

    @Override
    public void openCategory(CategoryOpenCmd cmd) {
        categoryCmdExe.openCategory(cmd);
    }

    @Override
    public PageInfo<CategoryDTO> listCategory(CategoryListQry categoryListQry) {
        return categoryQryExe.executeList(categoryListQry);
    }

    @Override
    public void deleteCategory(CategoryId cmd) {
        categoryCmdExe.deleteCategory(cmd);
    }

    @Override
    public CategoryDTO getCategory(CategoryId categoryId) {
        return categoryQryExe.execute(categoryId);
    }

}