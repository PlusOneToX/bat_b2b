package com.bat.flexible.web.model;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.ModelQry;
import com.bat.flexible.dao.model.co.ModelTreeCO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flexible/v1/web/user/u/model")
@Api(tags = "型号前台管理接口")
public class UserModelController extends BaseController {

    @Autowired
    private ModelServiceI modelServiceI;

    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询型号树形")
    public Response<List<ModelTreeCO>> tree(ModelQry modelQry){
        List<ModelTreeCO> list = modelServiceI.tree(modelQry);
        return Response.of(list);
    }

    @GetMapping(value = "/treeNew")
    @ApiOperation(value = "查询型号树形")
    public Response<List<ModelTreeCO>> treeNew(ModelQry modelQry){
        List<ModelTreeCO> list = modelServiceI.treeNew(modelQry);
        return Response.of(list);
    }


    @GetMapping(value = "/getOneByNetworkModel")
    @ApiOperation(value = "根据网络类型查询")
    public Response<ModelDO> tree(@RequestParam(value = "networkModel",required = false)String networkModel){
        ModelDO modelDO = modelServiceI.getOneByNetworkModel(networkModel);
        return Response.of(modelDO);
    }
}
