package com.bat.flexible.manager.model.validtor;

import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.model.dto.ModelCmd;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelErrorCode;
import com.bat.flexible.manager.error.model.ModelMaterialRelevanceErrorCode;
import com.bat.flexible.manager.model.executor.ModelQryExe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelValidtor {

    @Autowired
    private ModelQryExe modelQryExe;

    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;
    /**
     * 校验型号是否最终可用
     * @param modelId
     */
    public ModelDO validModelIsLast(Integer modelId,ModelDO modelDO){
        if(modelDO ==null){
            modelDO = modelQryExe.getById(modelId);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(modelDO.getAtLastTrademark())){
            throw new FlexibleCustomException(ModelErrorCode.M_MODEL_NOT_LAST_TRADEMARK);
        }
        if(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO.equals(modelDO.getOpenFlag())){
            String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)+MessageUtils.get(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE);
            throw new FlexibleCustomException(msg);
        }
        return modelDO;
    }

    /**
     * 参数校验
     * @param modelCmd
     * @param isAdd
     */
    public void checkParam(ModelCmd modelCmd, Boolean isAdd) {
        if(modelCmd.getId() !=null && isAdd){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(modelCmd.getAtLastTrademark()) && StringUtils.isBlank(modelCmd.getModelNo())){
            //最低级型号编码不能为空
            throw new FlexibleCustomException(ModelErrorCode.M_MODEL_NO_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_YES.equals(modelCmd.getAtLastTrademark()) &&
                (modelCmd.getMaterialRelevanceDTOList() ==null || modelCmd.getMaterialRelevanceDTOList().size()==0)){
            //型号关联的材质列表不能为空(最终型号)
            throw new FlexibleCustomException(ModelErrorCode.M_MODEL_MATERIAL_RELA_NULL);
        }
        if(FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(modelCmd.getAtLastTrademark()) &&
                (modelCmd.getMaterialRelevanceDTOList() !=null && modelCmd.getMaterialRelevanceDTOList().size()>0)){
            //品牌型号（非最终型号不能关联材质）
            throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_MODEL_MATERIAL_RELA_FORBID_BY_NOT_LAST);
        }

        if(StringUtils.isNotBlank(modelCmd.getModelNo())){
            //判断材质编码是否重复
            ModelDO modelDO = modelQryExe.getByModelNo(modelCmd.getModelNo(),false);
            if(modelDO !=null && ( isAdd || (!isAdd && modelDO.getId() - modelCmd.getId() !=0))){
                throw new FlexibleCustomException(ModelErrorCode.M_MODEL_NO_ONLY);
            }
        }
        //校验产品类型
        productCategoryServiceI.getById(modelCmd.getCategoryId());
        //校验材质的关联列表
        modelMaterialRelevanceServiceI.checkModelMaterialRelevanceList(true,modelCmd.getMaterialRelevanceDTOList(),isAdd,modelCmd.getId());
    }

}
