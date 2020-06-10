package com.bat.flexible.web.model;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.ModelCmd;
import com.bat.flexible.api.model.dto.ModelDetailQry;
import com.bat.flexible.api.model.dto.ModelPageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.model.co.ModelPageCO;
import com.bat.flexible.dao.model.co.ModelSimpleCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/flexible/v1/web/admin/u")
@Api(tags = "型号后台管理接口")
public class AdminModelController extends BaseController {

    @Autowired
    private ModelServiceI modelServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModel, value = CommonLogTypeConstantDTO.AdminModelAdd)
    @PostMapping(value = "/p/model")
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody ModelCmd modelCmd){
        return modelServiceI.create(modelCmd,getCurrentAdmin()) ;
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModel, value = CommonLogTypeConstantDTO.AdminModelUpdate)
    @PutMapping(value = "/p/model")
    @ApiOperation(value = "修改")
    public Response update(@Valid @RequestBody ModelCmd modelCmd){
        return modelServiceI.update(modelCmd,getCurrentAdmin()) ;
    }

    @GetMapping(value = "/p/model")
    @ApiOperation(value = "查看型号详情")
    public Response<ModelDetailQry> detailById(
            @Valid FlexibleIdDTO flexibleIdDTO
    ){
        return modelServiceI.detailById(flexibleIdDTO.getId());
    }

    @GetMapping(value = {"/p/model/page","/po/model/page"})
    @ApiOperation(value = "分页查询型号列表")
    public Response<PageInfo<ModelPageCO>> page(
            @Valid ModelPageQry modelPageQry
            ){
        PageInfo<ModelPageCO> pageInfo = modelServiceI.page(modelPageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModel, value = CommonLogTypeConstantDTO.AdminModelUpOrDown)
    @PutMapping(value = "/p/model/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response upOrDown(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){
        return modelServiceI.upOrDown(flexibleUpOrDownDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModel, value = CommonLogTypeConstantDTO.AdminModelUpdateOpenFlag)
    @PutMapping(value = "/p/model/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return modelServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModel, value = CommonLogTypeConstantDTO.AdminModelDelete)
    @DeleteMapping(value = "/p/model")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return modelServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModel, value = CommonLogTypeConstantDTO.AdminModelTop)
    @PutMapping(value = "/p/model/top")
    @ApiOperation(value = "置顶")
    public Response top(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return modelServiceI.top(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    @GetMapping(value = {"/p/model/pageSimpleCOByCondition","/po/model/pageSimpleCOByCondition"})
    @ApiOperation(value = "分页查询（非树形）")
    public Response<PageInfo<ModelSimpleCO>> pageSimpleCOByCondition(@Valid ModelPageQry modelPageQry){
        PageInfo<ModelSimpleCO> pageInfo = modelServiceI.pageSimpleCO(modelPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "测试")
    public Response test(){
        return modelServiceI.test();
    }
}
