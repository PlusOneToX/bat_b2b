package com.bat.distributor.api.category;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.category.dto.CategoryCmd;
import com.bat.distributor.api.category.dto.CategoryId;
import com.bat.distributor.api.category.dto.CategoryListQry;
import com.bat.distributor.api.category.dto.CategoryOpenCmd;
import com.bat.distributor.api.category.dto.data.CategoryDTO;

public interface CategoryServiceI {
    /**
     * 添加分销商类别
     * 
     * @param cmd
     * @return
     */
    public void createCategory(CategoryCmd cmd);

    /**
     * 更新分销商类别
     * 
     * @param cmd
     * @return
     */
    public void updateCategory(CategoryCmd cmd);

    /**
     * 更新分销商类别状态
     * 
     * @param cmd
     * @return
     */
    public void openCategory(CategoryOpenCmd cmd);

    /**
     * 获取分销商类别列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<CategoryDTO> listCategory(CategoryListQry qry);

    /**
     * 根据ID删除分销商类别
     * 
     * @param cmd
     * @return
     */
    public void deleteCategory(CategoryId cmd);

    /**
     * 根据分销商类别id获取详情
     * 
     * @param qry
     * @return
     */
    public CategoryDTO getCategory(CategoryId qry);

}
