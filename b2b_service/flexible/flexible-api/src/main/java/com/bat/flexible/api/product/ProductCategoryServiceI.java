package com.bat.flexible.api.product;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.product.dto.ProductCategoryCmd;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.product.dto.ProductCategoryPageQry;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;

import java.util.List;

public interface ProductCategoryServiceI {
    ProductCategoryDO getById(Integer categoryId);


    Response create(ProductCategoryCmd productCategoryCmd, AdminResponse currentAdmin);

    Response update(ProductCategoryCmd productCategoryCmd, AdminResponse currentAdmin);

    PageInfo<ProductCategoryCmd> page(ProductCategoryPageQry categoryPageQry);

    List<ProductCategoryDO> listByCondition(String content);

    Response<ProductCategoryCmd> detailById(Integer id);

    List<ProductCategoryCmd> listDTOUsable();
}
