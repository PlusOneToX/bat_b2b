package com.bat.flexible.api.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.product.dto.ProductCategoryRelaDTO;
import com.bat.flexible.dao.picture.dataobject.PictureProductCategoryRelevanceDO;

import java.util.List;

public interface PictureProductCategoryRelevanceServiceI {
    void saveRela(Integer pictureId, Short modelScope, Boolean isAdd, AdminResponse adminResponse,  List<Integer> categoryIdList);

    List<ProductCategoryRelaDTO> listByPictureId(Integer pictureId);


    List<PictureProductCategoryRelevanceDO> listByCondition(Integer pictureId,Integer categoryId,List<Integer> pictureIdList);

    /**
     * 删除关联关系
     * @param productCategoryId 产品类型id
     * @param pictureId 图片id
     */
    void delete(Integer productCategoryId, Integer pictureId);
}
