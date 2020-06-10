package com.bat.flexible.web.distributor;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.distributor.cooperation.FlexibleDistributorCooperationServiceI;
import com.bat.flexible.api.distributor.cooperation.dto.FlexibleDistributorCooperationCmd;
import com.bat.flexible.api.distributor.cooperation.dto.FlexibleDistributorCooperationQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/flexible/v1/web/admin/u")
@Api(tags = "柔性合作的分销商后台管理接口")
public class FlexibleDistributorCooperationController extends BaseController {

    @Autowired
    private FlexibleDistributorCooperationServiceI flexibleDistributorCooperationServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.FlexibleDistributorCooperation, value = CommonLogTypeConstantDTO.FlexibleDistributorCooperationAdd)
    @PostMapping(value = "/p/flexibleDistributorCooperation")
    @ApiOperation(value = "新增合作关系")
    public Response create(@Valid @RequestBody FlexibleDistributorCooperationCmd flexDistributorCooperationCmd){
        return flexibleDistributorCooperationServiceI.create(flexDistributorCooperationCmd,getCurrentAdmin());
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.FlexibleDistributorCooperation, value = CommonLogTypeConstantDTO.FlexibleDistributorCooperationUpdate)
    @PutMapping(value = "/p/flexibleDistributorCooperation")
    @ApiOperation(value = "修改合作关系")
    public Response update(@Valid @RequestBody FlexibleDistributorCooperationCmd flexDistributorCooperationCmd){
        return flexibleDistributorCooperationServiceI.update(flexDistributorCooperationCmd,getCurrentAdmin());
    }

    @GetMapping(value = {"/p/flexibleDistributorCooperation/page","/p/flexibleDistributorCooperation/page"})
    @ApiOperation(value = "合作关系分页")
    public Response page(@Valid FlexibleDistributorCooperationQry flexDistributorCooperationQry){
        PageInfo<FlexibleDistributorCooperationCmd> pageInfo = flexibleDistributorCooperationServiceI.page(flexDistributorCooperationQry,getCurrentAdmin());
        return Response.of(pageInfo);
    }

    @GetMapping(value = {"/p/flexibleDistributorCooperation/listUsable","/po/flexibleDistributorCooperation/listUsable"})
    @ApiOperation(value = "查询可用的分销商合作列表")
    public Response listUsable(){
        return flexibleDistributorCooperationServiceI.listUsable();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.FlexibleDistributorCooperation, value = CommonLogTypeConstantDTO.FlexibleDistributorCooperationUpdateOpenFlag)
    @PutMapping(value = "/p/flexibleDistributorCooperation/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return flexibleDistributorCooperationServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.FlexibleDistributorCooperation, value = CommonLogTypeConstantDTO.FlexibleDistributorCooperationDelete)
    @DeleteMapping(value = "/p/flexibleDistributorCooperation")
    @ApiOperation(value = "删除")
    public Response deleteById(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return flexibleDistributorCooperationServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }
}
