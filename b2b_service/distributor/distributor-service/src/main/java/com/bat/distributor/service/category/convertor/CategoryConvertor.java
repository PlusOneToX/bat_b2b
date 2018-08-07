package com.bat.distributor.service.category.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.distributor.api.category.dto.CategoryCmd;
import com.bat.distributor.api.category.dto.data.CategoryDTO;
import com.bat.distributor.dao.category.dataobject.CategoryDO;
import org.springframework.beans.BeanUtils;

public class CategoryConvertor {
    /**
     * 分销商类别数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static CategoryDO toCategoryDo(CategoryCmd cmd) {
        CategoryDO categoryDO = new CategoryDO();
        BeanUtils.copyProperties(cmd, categoryDO);
        Date date = new Date(System.currentTimeMillis());
        categoryDO.setCreateTime(date);
        categoryDO.setUpdateTime(date);
        return categoryDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param doList
     * @return
     */
    public static List<CategoryDTO> toCategoryDTOList(List<CategoryDO> doList) {
        List<CategoryDTO> dtoList = new ArrayList<>();
        doList.forEach(categoryDO -> {
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(categoryDO, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    /**
     * do转dto
     * 
     * @param categoryDO
     * @return
     */
    public static CategoryDTO toCategoryDTO(CategoryDO categoryDO) {
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(categoryDO, dto);
        return dto;
    }

}
