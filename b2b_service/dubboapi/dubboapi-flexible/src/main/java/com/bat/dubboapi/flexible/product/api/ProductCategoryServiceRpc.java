package com.bat.dubboapi.flexible.product.api;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.product.dto.ProductCategoryRpcQryDTO;

public interface ProductCategoryServiceRpc {

    /**
     * 根据材质id或者材质编码查询产品类型
     * @param materialId
     * @param materialNo
     * @return
     */
    Response<ProductCategoryRpcQryDTO> getByMaterialIdOrMaterialNo(Integer materialId,String materialNo);
}
