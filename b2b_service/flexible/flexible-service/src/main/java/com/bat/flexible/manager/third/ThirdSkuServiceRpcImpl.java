package com.bat.flexible.manager.third;


import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.third.ThirdSkuRelevanceServiceI;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.material.MaterialConstant;
import com.bat.flexible.manager.third.executor.ThirdCourierContrastQryExe;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;
import com.bat.dubboapi.flexible.third.api.ThirdSkuServiceRpc;
import com.bat.dubboapi.flexible.third.dto.MolejiCaseCmd;
import com.bat.dubboapi.flexible.third.dto.MolejiLogisticsCmd;
import com.bat.dubboapi.flexible.third.dto.ThirdSkuRelevanceRpcDTO;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.dao.third.dataobject.ThirdCourierContrastDO;
import com.bat.flexible.dao.third.dataobject.ThirdSkuRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.material.MaterialErrorCode;
import com.bat.flexible.manager.error.model.ModelMaterialRelevanceErrorCode;
import com.bat.flexible.manager.error.third.ThirdCourierContrastErrorCode;
import com.bat.flexible.manager.error.third.ThirdSkuRelevanceErrorCode;
import com.bat.flexible.manager.model.executor.ModelMaterialRelevanceQryExe;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService
public class ThirdSkuServiceRpcImpl implements ThirdSkuServiceRpc {

    @Autowired
    private ThirdCourierContrastQryExe thirdCourierContrastQryExe;

    @Autowired
    private ThirdSkuRelevanceServiceI thirdSkuRelevanceServiceI;

    @Autowired
    private ModelMaterialRelevanceQryExe modelMaterialRelevanceQryExe;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private ModelServiceI modelServiceI;



    @Override
    public Response<MolejiLogisticsCmd> trandformLogistics(Integer distributorId, String expressCode, Long expressTime, String expressNo, String otherOrderNo) {
        ThirdCourierContrastDO contrastDO =thirdCourierContrastQryExe.getByDistributorIdAndDistributionKdnCode(distributorId, expressCode);
        if(contrastDO ==null){
            return Response.buildFailure(ThirdCourierContrastErrorCode.THIRD_DISTRIBUTOR_COURIER_CONTRAST_NULL, MessageUtils.get(ThirdCourierContrastErrorCode.THIRD_DISTRIBUTOR_COURIER_CONTRAST_NULL));
        }
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(contrastDO.getOpenFlag())){
            String errorMessage = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_THIRD_COURIER_CONTRAST)+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE);
            return Response.buildFailure(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE, errorMessage);
        }
        MolejiLogisticsCmd molejiLogisticsCmd = new MolejiLogisticsCmd();
        molejiLogisticsCmd.setDeliveryTime(expressTime);
        molejiLogisticsCmd.setTimestamp(System.currentTimeMillis());

        molejiLogisticsCmd.setOrderNo(otherOrderNo);
        molejiLogisticsCmd.setShipperCode(contrastDO.getDistributorShipperCode());
        molejiLogisticsCmd.setShipperName(contrastDO.getDistributorShipperName());
        molejiLogisticsCmd.setTrackingNo(expressNo);

        //String checkSignature = Sha1Handler.encryption(signStr);
        return Response.of(molejiLogisticsCmd);
    }

    /**
     * 转换摩乐吉下单明细
     * @param caseCmdList 摩乐吉传的明细列表是jsonArray、需要转换
     * @param distributorId
     * @return
     */
    @Override
    public Response<List<OrderGoodsDiyParamDTO>> transformOrderSync(List<MolejiCaseCmd> caseCmdList, Integer distributorId) {

        List<OrderGoodsDiyParamDTO> customInfoList = new ArrayList<>();
        BigDecimal payAmount = BigDecimal.ZERO;
        for(int x=0;x<caseCmdList.size();x++){
            OrderGoodsDiyParamDTO orderGoodsDiyParamDTO = new OrderGoodsDiyParamDTO();
            MolejiCaseCmd molejiCaseCmd =caseCmdList.get(x);

            String sku = molejiCaseCmd.getSku();
           /* if(StringUtils.isBlank(sku)){
                throw new ApiException(ThirdSkuOpenErrorConstant.SkuNullError.getMsg());
            }
            if(sku.length() -  ThirdSkuRelaConstants.SkuLengthMoleji !=0){
                throw new ApiException(ThirdSkuOpenErrorConstant.SkuLengthError.getMsg());
            }*/
            /**
             * 摩乐吉的sku长度是12
             */
            String thirdMaterialCategory = sku.substring(0,1);
            String thirdBrandNo = sku.substring(1,4);
            String thirdModelNo = sku.substring(4,8);
            String thirdMaterialNo = sku.substring(8,10);
            String colorNo = sku.substring(10,12);
            ThirdSkuRelevanceDO thirdSkuRelevanceDO = thirdSkuRelevanceServiceI.getByDistributorIdAndThirdSkuNo(distributorId,sku);
            if(thirdSkuRelevanceDO ==null){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+MessageUtils.get(ThirdSkuRelevanceErrorCode.THIRD_SKU_NO_ERROR));
            }
            if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(thirdSkuRelevanceDO.getOpenFlag())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE));
            }

            ModelMaterialRelevanceDO modelMaterialRelevanceDO = modelMaterialRelevanceQryExe.getByModelIdAndMaterialId(thirdSkuRelevanceDO.getModelId(),thirdSkuRelevanceDO.getMaterialId(),true);
            if(modelMaterialRelevanceDO ==null){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+MessageUtils.get(ModelMaterialRelevanceErrorCode.M_MODEL_MATERIAL_NO_CORRELATION));
            }

            if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(modelMaterialRelevanceDO.getOpenFlag())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE));
            }
           /* if(StringUtils.isBlank(molejiCaseCmd.getPrevImgUrl())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+ThirdSkuOpenErrorConstant.PrevImgUrlNullError.getMsg());
            }
            if(StringUtils.isBlank(molejiCaseCmd.getImgUrl())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+ThirdSkuOpenErrorConstant.ImgUrlNullError.getMsg());
            }*/
            //打印图
            orderGoodsDiyParamDTO.setGenerateImage(molejiCaseCmd.getImgUrl());
            //预览图
            orderGoodsDiyParamDTO.setPreviewImage(molejiCaseCmd.getPrevImgUrl());
            MaterialDO materialDO = materialServiceI.getById(thirdSkuRelevanceDO.getMaterialId());
            if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(materialDO.getOpenFlag())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+
                        MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE)
                );
            }
            orderGoodsDiyParamDTO.setMaterialId(materialDO.getId());
            orderGoodsDiyParamDTO.setMaterialName(materialDO.getName());
            orderGoodsDiyParamDTO.setManufactors(materialDO.getManufactor());

            ModelDO modelDO = modelServiceI.getById(thirdSkuRelevanceDO.getModelId());
            if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(modelDO.getOpenFlag())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+
                        MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE)
                        );
            }
            orderGoodsDiyParamDTO.setModelId(modelDO.getId());
            orderGoodsDiyParamDTO.setModelName(modelDO.getName());
            ModelDO parentModel =  modelServiceI.getById(modelDO.getParentId());
            orderGoodsDiyParamDTO.setBrandId(parentModel.getId());
            orderGoodsDiyParamDTO.setBrandName(parentModel.getName());
            if(MaterialConstant.ALLOW_UPLOAD_FLAG_NO.equals(materialDO.getAllowUploadFlag())){
                throw FlexibleDubboApiException.buildException("【"+sku+"】"+materialDO.getName()+
                        MessageUtils.get(MaterialErrorCode.M_MATERIAL_FORBIN_UPLOAD_ERROR)
                );
            }
            orderGoodsDiyParamDTO.setItemCode(materialDO.getItemCode());

            orderGoodsDiyParamDTO.setItemCount(molejiCaseCmd.getQuantity());
            orderGoodsDiyParamDTO.setPictureId(0);


            customInfoList.add(orderGoodsDiyParamDTO);
        }
        return Response.of(customInfoList);
    }

    @Override
    public Response<ThirdSkuRelevanceRpcDTO> findSkuRelevance(Integer distributorId, String thirdSkuNo) {
        ThirdSkuRelevanceDO thirdSkuRelevanceDO = thirdSkuRelevanceServiceI.getByDistributorIdAndThirdSkuNo(distributorId, thirdSkuNo);
        if(thirdSkuRelevanceDO==null){
            return Response.of(null);
        }
        ThirdSkuRelevanceRpcDTO thirdSkuRelevanceRpcDTO=new ThirdSkuRelevanceRpcDTO();
        BeanUtils.copyProperties(thirdSkuRelevanceDO,thirdSkuRelevanceRpcDTO);
        MaterialDO materialDO = materialServiceI.getById(thirdSkuRelevanceDO.getMaterialId());
        if(materialDO!=null){
            thirdSkuRelevanceRpcDTO.setMaterialName(materialDO.getName());
        }
        return Response.of(thirdSkuRelevanceRpcDTO);
    }
}
