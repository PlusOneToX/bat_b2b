package com.bat.flexible.manager.product;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.material.executor.MaterialQryExe;
import com.bat.flexible.manager.product.executor.ProductCategoryQryExe;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.product.api.ProductCategoryServiceRpc;
import com.bat.dubboapi.flexible.product.dto.ProductCategoryRpcQryDTO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ProductCategoryServiceRpcImpl implements ProductCategoryServiceRpc {

    @Autowired
    private MaterialQryExe materialQryExe;

    @Autowired
    private ProductCategoryQryExe productCategoryQryExe;

    @Override
    public Response<ProductCategoryRpcQryDTO> getByMaterialIdOrMaterialNo(Integer materialId, String materialNo) {
        MaterialDO materialDO = null;
        if(materialId !=null){
            materialDO = materialQryExe.getById(materialId);
        }else{
            //不校验
            materialDO = materialQryExe.getByMaterialNo(materialNo,false);
        }
        ProductCategoryDO productCategoryDO = productCategoryQryExe.getById(materialDO.getCategoryId());

        return Response.of(BeanUtils.copy(productCategoryDO,ProductCategoryRpcQryDTO.class));
    }
}
