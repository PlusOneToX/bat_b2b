package com.bat.distributor.service.category.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.category.dto.CategoryId;
import com.bat.distributor.api.category.dto.CategoryListQry;
import com.bat.distributor.api.category.dto.data.CategoryDTO;
import com.bat.distributor.dao.category.CategoryMapper;
import com.bat.distributor.dao.category.dataobject.CategoryDO;
import com.bat.distributor.service.category.convertor.CategoryConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Component
public class CategoryQryExe {

    @Resource
    private CategoryMapper mapper;

    /**
     * 查询分销商类别列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<CategoryDTO> executeList(CategoryListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CategoryDO> tagDOList = mapper.listCategory(qryMap);
        PageInfo pageInfo = new PageInfo(tagDOList);
        List<CategoryDTO> tagDTOList = CategoryConvertor.toCategoryDTOList(pageInfo.getList());
        pageInfo.setList(tagDTOList);
        return pageInfo;
    }

    /**
     * 根据分销商类别ID查询分销商类别详情
     * 
     * @param qry
     * @return
     */
    public CategoryDTO execute(CategoryId qry) {
        CategoryDO categoryDO = mapper.selectByPrimaryKey(qry.getId());
        if (categoryDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CATEGORY_NULL);
        }
        CategoryDTO dto = CategoryConvertor.toCategoryDTO(categoryDO);
        return dto;
    }
}
