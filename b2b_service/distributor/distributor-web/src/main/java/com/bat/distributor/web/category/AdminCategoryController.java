package com.bat.distributor.web.category;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.category.CategoryServiceI;
import com.bat.distributor.api.category.dto.CategoryCmd;
import com.bat.distributor.api.category.dto.CategoryId;
import com.bat.distributor.api.category.dto.CategoryListQry;
import com.bat.distributor.api.category.dto.CategoryOpenCmd;
import com.bat.distributor.api.category.dto.data.CategoryDTO;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商类别后台接口", value = "AdminCategoryController")
@RestController
@RequestMapping("/distributor/v1/web/admin/category")
public class AdminCategoryController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);

    @Resource
    private CategoryServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商类别列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<CategoryDTO>> listCategory(@Valid CategoryListQry qry) {
        PageInfo<CategoryDTO> pageInfo = service.listCategory(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商类别列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<CategoryDTO>> listCategoryPo(@Valid CategoryListQry qry) {
        PageInfo<CategoryDTO> pageInfo = service.listCategory(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商类别id获取分销商类别详情")
    @GetMapping()
    public Response<CategoryDTO> getCategory(@Valid CategoryId qry) {
        CategoryDTO dto = service.getCategory(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryAdd)
    @ApiOperation(value = "添加分销商类别")
    @PostMapping()
    public Response createCategory(@RequestBody @Valid CategoryCmd cmd) {
        service.createCategory(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryUpdate)
    @ApiOperation(value = "更新分销商类别")
    @PutMapping()
    public Response updateCategory(@RequestBody @Valid CategoryCmd cmd) {
        service.updateCategory(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryUpdateStatus)
    @ApiOperation(value = "更新分销商类别状态")
    @PutMapping(value = "/open")
    public Response openCategory(@RequestBody @Valid CategoryOpenCmd cmd) {
        service.openCategory(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryDeleteById)
    @ApiOperation(value = "根据分销商类别id删除分销商类别")
    @DeleteMapping()
    public Response deleteCategory(@RequestBody @Valid CategoryId cmd) {
        service.deleteCategory(cmd);
        return Response.buildSuccess();
    }

}
