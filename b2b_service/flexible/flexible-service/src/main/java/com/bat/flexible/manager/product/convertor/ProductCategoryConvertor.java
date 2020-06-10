package com.bat.flexible.manager.product.convertor;

import com.bat.flexible.api.product.dto.ProductCategoryCmd;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;

import java.util.List;

public class ProductCategoryConvertor {

    public static List<ProductCategoryCmd> toProductCategoryCmdList(List<ProductCategoryDO> doList){
        if(doList==null || doList.size()==0){
            return null;
        }
        return BeanUtils.copyList(doList,ProductCategoryCmd.class);
    }

    public static ProductCategoryCmd toProductCategoryCmd(ProductCategoryDO productCategoryDO){
        if(productCategoryDO==null ){
            return null;
        }
        ProductCategoryCmd categoryCmd =BeanUtils.copy(productCategoryDO,ProductCategoryCmd.class);
        return categoryCmd;
    }
}
