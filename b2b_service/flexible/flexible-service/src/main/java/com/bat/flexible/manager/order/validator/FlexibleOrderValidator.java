package com.bat.flexible.manager.order.validator;

import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.material.validator.MaterialValidtor;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnCodeQry;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.error.material.MaterialErrorCode;
import com.bat.flexible.manager.error.picture.PictureErrorCode;
import com.bat.flexible.manager.model.executor.ModelQryExe;
import com.bat.flexible.manager.model.validtor.ModelValidtor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 柔性第三方下单参数校验
 */
@Component
public class FlexibleOrderValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexibleOrderValidator.class);

    @Autowired
    private MaterialValidtor materialValidtor;

    @Autowired
    private ModelValidtor modelValidtor;

    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private ModelQryExe modelQryExe;

    @Autowired
    private PictureServiceI pictureServiceI;

    /**
     * 参数校验、基于ID
     * @param orderDetailBaseOnIdQryList
     * @return
     */
    public List<OrderGoodsDiyParamDTO> validThirdOrderParamBaseId(List<OrderDetailBaseOnIdQry> orderDetailBaseOnIdQryList) {
        List<OrderGoodsDiyParamDTO> list = new ArrayList<>();
        for(int x=0;x<orderDetailBaseOnIdQryList.size();x++){
            OrderDetailBaseOnIdQry orderDetailBaseOnIdQry = orderDetailBaseOnIdQryList.get(x);
            OrderGoodsDiyParamDTO diyParamDTO = new OrderGoodsDiyParamDTO();
            //校验材质
            MaterialDO materialDO =materialValidtor.validMaterialIsLast(orderDetailBaseOnIdQry.getMaterialId(),null);
            if(!materialDO.getItemCode().equals(orderDetailBaseOnIdQry.getSkuNo())){
                LOGGER.error("第三方传过来的sku编码与材质编码不匹配、第三方编码是{}，实际材质对应的80码是{}",orderDetailBaseOnIdQry.getSkuNo(),materialDO.getItemCode());
                throw FlexibleDubboApiException.buildException(MaterialErrorCode.G_ITEM_CODE_ERROR);
            }
            ProductCategoryDO productCategoryDO = productCategoryServiceI.getById(materialDO.getCategoryId());
            diyParamDTO.setCategoryId(productCategoryDO.getId());
            diyParamDTO.setCategoryName(productCategoryDO.getName());
            diyParamDTO.setMaterialId(materialDO.getId());
            diyParamDTO.setMaterialName(materialDO.getName());
            diyParamDTO.setManufactors(materialDO.getManufactor());
            //处理型号(要求不做校验)
            //ModelDO modelDO = modelValidtor.validModelIsLast(orderDetailBaseOnIdQry.getModelId());
            diyParamDTO.setModelId(orderDetailBaseOnIdQry.getModelId());
            diyParamDTO.setModelName(orderDetailBaseOnIdQry.getModelName());
            if(StringUtils.isBlank(orderDetailBaseOnIdQry.getModelName())){
                ModelDO modelDO = modelQryExe.getById(orderDetailBaseOnIdQry.getModelId());
                diyParamDTO.setModelName(modelDO.getName());
            }
            diyParamDTO.setBrandId(orderDetailBaseOnIdQry.getBrandId());

            diyParamDTO.setBrandName(orderDetailBaseOnIdQry.getBrandName());
            if(StringUtils.isBlank(orderDetailBaseOnIdQry.getBrandName())){
                ModelDO modelDO = modelQryExe.getById(orderDetailBaseOnIdQry.getModelId());
                diyParamDTO.setBrandName(modelDO.getName());
            }
            //要求不做校验
            //modelMaterialRelevanceServiceI.getByModelIdAndMaterialId(orderDetailBaseOnIdQry.getModelId(),orderDetailBaseOnIdQry.getMaterialId(),true);
            diyParamDTO.setPictureId(orderDetailBaseOnIdQry.getPictureId());
            if(PictureConstant.PICTURE_ID_USER_UPLOAD.equals(diyParamDTO.getPictureId())){
                //
                diyParamDTO.setPictureName(PictureConstant.PICTURE_NAME_USER_UPLOAD);
            }else {
                PictureDO pictureDO = pictureServiceI.getById(diyParamDTO.getPictureId());
                diyParamDTO.setPictureName(pictureDO.getName());
            }
            diyParamDTO.setGenerateImage(orderDetailBaseOnIdQry.getGenerateImage());
            diyParamDTO.setPreviewImage(orderDetailBaseOnIdQry.getImage());
            list.add(diyParamDTO);
        }
        return list;
    }

    public List<OrderGoodsDiyParamDTO> validThirdOrderParamBaseOnCode(List<OrderDetailBaseOnCodeQry> orderDetailBaseOnCodeQryList) {
        List<OrderGoodsDiyParamDTO> list = new ArrayList<>();
        for(int x=0;x<orderDetailBaseOnCodeQryList.size();x++){
            OrderDetailBaseOnCodeQry orderDetailBaseOnCodeQry = orderDetailBaseOnCodeQryList.get(x);
            OrderGoodsDiyParamDTO diyParamDTO = new OrderGoodsDiyParamDTO();
            //校验材质
            MaterialDO materialDO =materialServiceI.getByMaterialNo(orderDetailBaseOnCodeQry.getMaterialNo());
            materialValidtor.validMaterialIsLast(null,materialDO);
            diyParamDTO.setItemCode(materialDO.getItemCode());
            ProductCategoryDO productCategoryDO = productCategoryServiceI.getById(materialDO.getCategoryId());
            diyParamDTO.setCategoryId(productCategoryDO.getId());
            diyParamDTO.setCategoryName(productCategoryDO.getName());
            diyParamDTO.setMaterialId(materialDO.getId());
            diyParamDTO.setMaterialName(materialDO.getName());
            diyParamDTO.setManufactors(materialDO.getManufactor());
            //处理型号(要求不做校验)
            ModelDO modelDO = modelQryExe.getByModelNo(orderDetailBaseOnCodeQry.getModelNo(),true);
            diyParamDTO.setModelId(modelDO.getId());
            diyParamDTO.setModelName(modelDO.getName());
            ModelDO parentModelDO = modelQryExe.getById(modelDO.getParentId());
            diyParamDTO.setBrandId(parentModelDO.getId());
            diyParamDTO.setBrandName(parentModelDO.getName());
            //要求不做校验
            //modelMaterialRelevanceServiceI.getByModelIdAndMaterialId(orderDetailBaseOnIdQry.getModelId(),orderDetailBaseOnIdQry.getMaterialId(),true);
            if(PictureConstant.PICTURE_CODE_USER_UPLOAD.equals(orderDetailBaseOnCodeQry.getPictureNo())){
                diyParamDTO.setPictureId(PictureConstant.PICTURE_ID_USER_UPLOAD);
                diyParamDTO.setPictureName(PictureConstant.PICTURE_NAME_USER_UPLOAD);
            }else{
                PictureDO pictureDO = pictureServiceI.getByCode(orderDetailBaseOnCodeQry.getPictureNo());
                if (pictureDO ==null){
                    throw FlexibleDubboApiException.buildException(PictureErrorCode.P_PICTURE_CODE_ERROR);
                }
                diyParamDTO.setPictureId(pictureDO.getId());
                diyParamDTO.setPictureName(pictureDO.getName());
            }
            diyParamDTO.setGenerateImage(orderDetailBaseOnCodeQry.getGenerateImage());
            diyParamDTO.setPreviewImage(orderDetailBaseOnCodeQry.getImage());
            list.add(diyParamDTO);
        }
        return list;
    }
}
