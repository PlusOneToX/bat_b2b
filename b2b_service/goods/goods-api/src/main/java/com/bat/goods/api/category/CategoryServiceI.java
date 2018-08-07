package com.bat.goods.api.category;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.category.dto.CategoryCmd;
import com.bat.goods.api.category.dto.CategoryId;
import com.bat.goods.api.category.dto.CategoryListQry;
import com.bat.goods.api.category.dto.CategoryOpenCmd;
import com.bat.goods.api.category.dto.data.CategoryDTO;

public interface CategoryServiceI {
    /**
     * 添加品类
     * 
     * @param categoryCmd
     * @return
     */
    public void createCategory(CategoryCmd categoryCmd);

    /**
     * 更新品类
     * 
     * @param categoryCmd
     * @return
     */
    public void updateCategory(CategoryCmd categoryCmd);

    /**
     * 更新品类状态
     * 
     * @param cmd
     * @return
     */
    public void openCategory(CategoryOpenCmd cmd);

    /**
     * 获取品类列表（分页）
     * 
     * @param categoryListQry
     * @return
     */
    public PageInfo<CategoryDTO> listCategory(CategoryListQry categoryListQry);

    /**
     * 根据ID删除品类
     * 
     * @param cmd
     * @return
     */
    public void deleteCategory(CategoryId cmd);

    /**
     * 根据品类id获取详情
     * 
     * @param categoryId
     * @return
     */
    public CategoryDTO getCategory(CategoryId categoryId);

}
