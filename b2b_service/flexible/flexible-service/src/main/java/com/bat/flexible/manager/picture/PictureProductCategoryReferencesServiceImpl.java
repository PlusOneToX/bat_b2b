package com.bat.flexible.manager.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.picture.PictureProductCategoryRelevanceServiceI;
import com.bat.flexible.manager.picture.executor.PictureProductCategoryReferencesCmdExe;
import com.bat.flexible.manager.picture.executor.PictureProductCategoryReferencesQryExe;
import com.bat.flexible.manager.product.executor.ProductCategoryQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.product.dto.ProductCategoryRelaDTO;
import com.bat.flexible.dao.picture.dataobject.PictureProductCategoryRelevanceDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PictureProductCategoryReferencesServiceImpl implements PictureProductCategoryRelevanceServiceI {


    @Autowired
    private ProductCategoryQryExe productCategoryQryExe;

    @Autowired
    private PictureProductCategoryReferencesQryExe pictureProductCategoryReferencesQryExe;

    @Autowired
    private PictureProductCategoryReferencesCmdExe pictureProductCategoryReferencesCmdExe;

    @Transactional
    @Override
    public void saveRela(Integer pictureId, Short modelScope, Boolean isAdd, AdminResponse adminResponse, List<Integer> categoryIdList) {
        if(modelScope - PictureConstant.MODEL_SCOPE_MODEL_CATEGORY !=0){
            //非指定型号类别
            return;
        }
        if(categoryIdList !=null && categoryIdList.size()>0){
            List<ProductCategoryDO> categoryDOList = productCategoryQryExe.listByCategoryIdList(categoryIdList);
        }
        //处理编辑
        dealWithByUpdate(pictureId, isAdd, categoryIdList);
        if(categoryIdList !=null && categoryIdList.size()>0){
            categoryIdList.stream().forEach(categoryId -> {
                PictureProductCategoryRelevanceDO referencesDO = new PictureProductCategoryRelevanceDO();
                referencesDO.setCategoryId(categoryId);
                referencesDO.setPictureId(pictureId);
                setAdminMsg(referencesDO,adminResponse);
                pictureProductCategoryReferencesCmdExe.create(referencesDO);
            });
        }
    }

    @Override
    public List<ProductCategoryRelaDTO> listByPictureId(Integer pictureId) {
        List<PictureProductCategoryRelevanceDO> relevanceDOList = pictureProductCategoryReferencesQryExe.listByPictureId(pictureId);
        List<ProductCategoryRelaDTO> dtoList = new ArrayList<>();
        relevanceDOList.stream().forEach(pictureProductCategoryRelevanceDO -> {
            ProductCategoryRelaDTO relaSimpleDTO  = new ProductCategoryRelaDTO();
            relaSimpleDTO.setId(pictureProductCategoryRelevanceDO.getId());
            relaSimpleDTO.setProductCategoryId(pictureProductCategoryRelevanceDO.getCategoryId());
            //处理redis数据
            ProductCategoryDO productCategoryDO = productCategoryQryExe.getById(pictureProductCategoryRelevanceDO.getCategoryId());
            relaSimpleDTO.setName(productCategoryDO.getName());
            relaSimpleDTO.setEnglishName(productCategoryDO.getEnglishName());
            dtoList.add(relaSimpleDTO);
        });
        return dtoList;
    }

    @Override
    public List<PictureProductCategoryRelevanceDO> listByCondition(Integer pictureId, Integer categoryId,List<Integer> pictureIdList) {
        return pictureProductCategoryReferencesQryExe.listByCondition(pictureId,categoryId,pictureIdList);
    }

    /**
     * 删除关联关系
     * @param productCategoryId 产品类型id
     * @param pictureId 图片id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer productCategoryId, Integer pictureId) {
        if(pictureId ==null&&productCategoryId ==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        List<PictureProductCategoryRelevanceDO> pictureProductCategoryRelevanceDOS = pictureProductCategoryReferencesQryExe.listByCondition(pictureId, productCategoryId,null);
        if(pictureProductCategoryRelevanceDOS ==null || pictureProductCategoryRelevanceDOS.size()==0){
            return ;
        }
        pictureProductCategoryRelevanceDOS.stream().forEach(pictureProductCategoryRelevanceDO -> {
            pictureProductCategoryReferencesCmdExe.deleteById(pictureProductCategoryRelevanceDO.getId());
        });
    }

    /**
     * 处理修改的
     * @param pictureId
     * @param isAdd
     * @param categoryIdList
     */
    private void dealWithByUpdate(Integer pictureId, Boolean isAdd, List<Integer> categoryIdList) {
        if(isAdd){
            return;
        }
        List<PictureProductCategoryRelevanceDO> pictureProductCategoryRelevanceDOList = pictureProductCategoryReferencesQryExe.listByPictureId(pictureId);
        if(pictureProductCategoryRelevanceDOList !=null && pictureProductCategoryRelevanceDOList.size()>0){

            for(int x=0;x<pictureProductCategoryRelevanceDOList.size();x++){
                if(categoryIdList !=null && categoryIdList.size()>0){
                    for(int y=0;y<categoryIdList.size();y++){
                        if(pictureProductCategoryRelevanceDOList.get(x).getCategoryId() - categoryIdList.get(y)==0){
                            //没有变化
                            pictureProductCategoryRelevanceDOList.remove(x);
                            x--;
                            categoryIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
            }

        }
        if(pictureProductCategoryRelevanceDOList !=null && pictureProductCategoryRelevanceDOList.size()>0){
            //已被删除
            pictureProductCategoryRelevanceDOList.stream().forEach(pictureProductCategoryRelevanceDO -> {
                pictureProductCategoryReferencesCmdExe.deleteById(pictureProductCategoryRelevanceDO.getId());
            });
        }
    }

    private void setAdminMsg(PictureProductCategoryRelevanceDO pictureProductCategoryRelevanceDO, AdminResponse adminResponse) {
        if(pictureProductCategoryRelevanceDO.getId() ==null){
            pictureProductCategoryRelevanceDO.setCreateTime(new Date());
            pictureProductCategoryRelevanceDO.setCreateUserId(adminResponse.getId());
            pictureProductCategoryRelevanceDO.setCreateUserName(adminResponse.getUserName());
        }

    }
}
