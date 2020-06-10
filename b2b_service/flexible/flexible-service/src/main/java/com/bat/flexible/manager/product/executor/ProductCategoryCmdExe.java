package com.bat.flexible.manager.product.executor;

import com.bat.flexible.dao.product.ProductCategoryDOMapper;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProductCategoryCmdExe {

    @Autowired
    private ProductCategoryDOMapper productCategoryDOMapper;


    public void create(ProductCategoryDO productCategoryDO) {
        productCategoryDOMapper.insert(productCategoryDO);
    }

    public void update(ProductCategoryDO productCategoryDO) {
        productCategoryDOMapper.updateByPrimaryKey(productCategoryDO);
    }
}
