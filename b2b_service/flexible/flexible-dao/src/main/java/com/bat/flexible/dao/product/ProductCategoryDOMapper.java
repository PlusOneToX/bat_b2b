package com.bat.flexible.dao.product;

import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCategoryDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductCategoryDO record);

    int insertSelective(ProductCategoryDO record);

    ProductCategoryDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductCategoryDO record);

    int updateByPrimaryKey(ProductCategoryDO record);

    List<ProductCategoryDO> listByCategoryIdList(@Param("categoryIdList") List<Integer> categoryIdList);

    List<ProductCategoryDO> listByCondtion(@Param("content") String content,@Param("openFlag")Short openFlag);

 
}