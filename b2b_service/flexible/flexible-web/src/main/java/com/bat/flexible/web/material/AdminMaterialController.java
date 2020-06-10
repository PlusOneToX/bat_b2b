package com.bat.flexible.web.material;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.material.dto.*;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.*;
import com.bat.flexible.dao.material.co.MaterialPageCO;
import com.bat.flexible.dao.material.co.MaterialSimpleTreeCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value ="/flexible/v1/web/admin/u")
@Api(tags = "材质后台接口",value = "MaterialController")
public class AdminMaterialController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMaterialController.class);

    @Autowired
    private MaterialServiceI materialServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMaterial, value = CommonLogTypeConstantDTO.AdminMaterialAdd)
    @PostMapping(value = "/p/material")
    @ApiOperation(value = "新增材质")
    public Response create(@Valid @RequestBody MaterialSaveCmd materialSaveCmd){

        return materialServiceI.create(materialSaveCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMaterial, value = CommonLogTypeConstantDTO.AdminMaterialUpdate)
    @PutMapping(value = "/p/material")
    @ApiOperation(value = "修改材质")
    public Response update(@Valid @RequestBody MaterialSaveCmd materialSaveCmd){
        return materialServiceI.update(materialSaveCmd,getCurrentAdmin());
    }

    @GetMapping(value = "/p/material")
    @ApiOperation(value = "查询材质详情")
    public Response<MaterialDetailQry> detailById(@ApiParam(value = "材质id")@RequestParam(value = "id",required = false)Integer id){

        Response<MaterialDetailQry> response = materialServiceI.detailById(id);

        return response;
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMaterial, value = CommonLogTypeConstantDTO.AdminMaterialUpOrDown)
    @PutMapping(value = "/p/material/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response upOrDown(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){
        return materialServiceI.upOrDown(flexibleUpOrDownDTO,getCurrentAdmin());
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMaterial, value = CommonLogTypeConstantDTO.AdminMaterialUpdateStatus)
    @PutMapping(value = "/p/material/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return materialServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMaterial, value = CommonLogTypeConstantDTO.AdminMaterialDelete)
    @DeleteMapping(value = "/p/material")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return materialServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }
    @GetMapping(value = {"/p/material/page","/po/material/page"})
    @ApiOperation(value = "分页查询材质")
    public Response<PageInfo<MaterialPageCO>> page(@Valid MaterialPageQry materialPageQry){
        PageInfo<MaterialPageCO> pageInfo = materialServiceI.page(materialPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = {"/p/material/listLowest","/po/material/listLowest"})
    @ApiOperation(value = "查询最底级材质")
    public Response<PageInfo<MaterialSimpleDTO>> listLowest(@Valid MaterialTreeAdminQry materialTreeAdminQry){
        materialTreeAdminQry.setAtLastTrademark((short)1);
        PageInfo<MaterialSimpleDTO> pageInfo = materialServiceI.pageWithoutTree(materialTreeAdminQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = {"/p/material/treeAdmin","/po/material/treeAdmin"})
    @ApiOperation(value = "后台查询材质树")
    public Response<List<MaterialSimpleTreeCO>> treeAdmin(MaterialTreeAdminQry materialTreeAdminQry){

        return materialServiceI.treeAdmin(materialTreeAdminQry);
    }

    @GetMapping(value = {"/p/material/pageWithoutTree","/po/material/pageWithoutTree"})
    @ApiOperation(value = "后台分页查询材质列表（非树形）")
    public Response<PageInfo<MaterialSimpleDTO>> pageWithoutTree(MaterialTreeAdminQry materialTreeAdminQry){

        PageInfo<MaterialSimpleDTO> pageInfo = materialServiceI.pageWithoutTree(materialTreeAdminQry);
        return Response.of(pageInfo);
    }
    @GetMapping(value = {"/p/material/listWithoutTree","/po/material/listWithoutTree"})
    @ApiOperation(value = "后台列表查询材质列表（非树形、非分页）")
    public Response<List<MaterialSimpleDTO>> listWithoutTree(MaterialTreeAdminQry materialTreeAdminQry){

        List<MaterialSimpleDTO> list = materialServiceI.listWithoutTree(materialTreeAdminQry);
        return Response.of(list);
    }



}
