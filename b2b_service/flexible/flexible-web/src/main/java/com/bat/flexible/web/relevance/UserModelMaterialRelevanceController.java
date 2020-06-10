package com.bat.flexible.web.relevance;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/flexible/v1/web/user/u/modelMaterialRelevance")
@Api(tags = "型号材质关联前台管理接口")
public class UserModelMaterialRelevanceController extends BaseController {

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;

    @GetMapping(value = "/getByModelIdAndMaterialId")
    @ApiOperation(value = "根据型号id")
    public Response<ModelMaterialRelevanceDO> getByModelIdAndMaterialId(
            @ApiParam(value = "型号id",name = "modelId")@RequestParam(value = "modelId",required = false)Integer modelId,
            @ApiParam(value = "材质id",name = "materialId")@RequestParam(value = "materialId",required = false)Integer materialId
    ){
        return modelMaterialRelevanceServiceI.getByModelIdAndMaterialId(modelId,materialId,false);
    }
}
