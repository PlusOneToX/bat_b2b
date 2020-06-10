package com.bat.flexible.manager.model;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.model.api.ModelServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ModelDTORpcQry;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.manager.error.model.ModelErrorCode;
import com.bat.flexible.manager.model.executor.ModelQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ModelServiceRpcImpl implements ModelServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelServiceRpcImpl.class);

    @Autowired
    private ModelQryExe modelQryExe;

    @Override
    public Response<ModelDTORpcQry> getByModelIdOrModelNo(Integer modelId, String modelNo) {
        if(modelId ==null && StringUtils.isBlank(modelNo)){
            LOGGER.info("获取型号信息、型号id和型号编码均为空");
            throw new FlexibleCustomException(ModelErrorCode.M_MODEL_ID_AND_MODEL_NO_ALL_NULL);
        }
        ModelDO modelDO = null;
        if(modelId !=null){
            modelDO = modelQryExe.getById(modelId);
        }else {
            modelDO = modelQryExe.getByModelNo(modelNo,true);
        }
        ModelDTORpcQry modelDTORpcQry = BeanUtils.copy(modelDO,ModelDTORpcQry.class);

        if(modelDO.getParentId()> FlexibleCommonConstant.COMMON_PARENT_ID){
            ModelDO parentDO = modelQryExe.getById(modelDO.getParentId());
            modelDTORpcQry.setParentEnglishName(parentDO.getEnglishName());
            modelDTORpcQry.setParentModelNo(parentDO.getModelNo());
            modelDTORpcQry.setParentName(parentDO.getName());
        }
        return Response.of(modelDTORpcQry);
    }
}
