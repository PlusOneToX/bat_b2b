package com.bat.flexible.manager.product.executor;

import com.alibaba.fastjson.JSON;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.product.ProductCategoryDOMapper;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.label.LabelErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductCategoryQryExe {

    @Autowired
    private ProductCategoryDOMapper productCategoryDOMapper;




    public ProductCategoryDO getById(Integer id) {
        ProductCategoryDO productCategoryDO = productCategoryDOMapper.selectByPrimaryKey(id);
        if(productCategoryDO ==null){
           throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
        }
        return productCategoryDO;
    }

    public List<ProductCategoryDO> listByCategoryIdList(List<Integer> categoryIdList) {
        List<ProductCategoryDO> categoryDOList = productCategoryDOMapper.listByCategoryIdList(categoryIdList);
        if(categoryDOList==null || categoryDOList.size()==0){
            String msg = MessageUtils.get(LabelErrorCode.LABEL_ERROR_MSG_CODE)+"【"+ JSON.toJSONString(categoryIdList)+"】"+MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
            throw new FlexibleCustomException(msg);
        }
        if(categoryIdList.size() - categoryIdList.size()==0){
            return categoryDOList;
        }
        categoryIdList.stream().forEach(categoryId -> {
            Boolean flag = false;
            for(int x=0;x<categoryDOList.size();x++){
                if(categoryDOList.get(x).getId() - categoryId ==0){
                    flag=true;
                    break;
                }
            }
            if(flag){
                String msg = MessageUtils.get(LabelErrorCode.LABEL_ERROR_MSG_CODE)+"【"+ categoryId+"】"+MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_ERROR);
                throw new FlexibleCustomException(msg);
            }
        });
        return categoryDOList;
    }

    public List<ProductCategoryDO> listByCondition(String content,Short openFlag) {
        return productCategoryDOMapper.listByCondtion(content,openFlag);
    }

  
}
