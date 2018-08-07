package com.bat.distributor.service.category;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.distributor.api.category.CategoryServiceI;
import com.bat.distributor.api.category.dto.CategoryCmd;
import com.bat.distributor.api.category.dto.CategoryId;
import com.bat.distributor.api.category.dto.CategoryListQry;
import com.bat.distributor.api.category.dto.CategoryOpenCmd;
import com.bat.distributor.api.category.dto.data.CategoryDTO;
import com.bat.distributor.service.category.executor.CategoryCmdExe;
import com.bat.distributor.service.category.executor.CategoryQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

@Service
public class CategoryServiceImpl implements CategoryServiceI {

    @Resource
    private CategoryCmdExe cmdExe;

    @Resource
    private CategoryQryExe qryExe;

    @Override
    public void createCategory(CategoryCmd cmd) {
        cmdExe.createCategory(cmd);
    }

    @Override
    public void updateCategory(CategoryCmd cmd) {
        cmdExe.updateCategory(cmd);
    }

    @Override
    public void openCategory(CategoryOpenCmd cmd) {
        cmdExe.openCategory(cmd);
    }

    @Override
    public PageInfo<CategoryDTO> listCategory(CategoryListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public void deleteCategory(CategoryId cmd) {
        cmdExe.deleteCategory(cmd);
    }

    @Override
    public CategoryDTO getCategory(CategoryId qry) {
        return qryExe.execute(qry);
    }

}