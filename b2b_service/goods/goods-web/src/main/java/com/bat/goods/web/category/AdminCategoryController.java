package com.bat.goods.web.category;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.category.CategoryServiceI;
import com.bat.goods.api.category.dto.CategoryCmd;
import com.bat.goods.api.category.dto.CategoryId;
import com.bat.goods.api.category.dto.CategoryListQry;
import com.bat.goods.api.category.dto.CategoryOpenCmd;
import com.bat.goods.api.category.dto.data.CategoryDTO;
import com.bat.goods.web.annotation.SysLog;
import com.bat.goods.web.constants.CommonLogTypeConstantDTO;
import com.bat.goods.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "品类后台接口", value = "AdminCategoryController")
@RestController
@RequestMapping("/goods/v1/web/admin/category")
public class AdminCategoryController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);

    @Resource
    private CategoryServiceI categoryService;

    @ApiOperation(value = "根据搜索条件查找品类列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<CategoryDTO>> listCategory(@Valid CategoryListQry qry) {
        PageInfo<CategoryDTO> pageInfo = categoryService.listCategory(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找品类列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<CategoryDTO>> listCategoryPo(@Valid CategoryListQry qry) {
        PageInfo<CategoryDTO> pageInfo = categoryService.listCategory(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据品类id获取品类详情")
    @GetMapping()
    public Response<CategoryDTO> getCategory(@Valid CategoryId qry) {
        CategoryDTO category = categoryService.getCategory(qry);
        return Response.of(category);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryAdd)
    @ApiOperation(value = "添加品类")
    @PostMapping()
    public Response createCategory(@RequestBody @Valid CategoryCmd cmd) {
        categoryService.createCategory(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryUpdate)
    @ApiOperation(value = "更新品类")
    @PutMapping()
    public Response updateCategory(@RequestBody @Valid CategoryCmd cmd) {
        categoryService.updateCategory(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryUpdateStatus)
    @ApiOperation(value = "更新品类状态")
    @PutMapping(value = "/open")
    public Response openCategory(@RequestBody @Valid CategoryOpenCmd cmd) {
        categoryService.openCategory(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCategory, value = CommonLogTypeConstantDTO.AdminCategoryDelete)
    @ApiOperation(value = "根据品类id删除品类")
    @DeleteMapping()
    public Response deleteCategory(@RequestBody @Valid CategoryId qry) {
        categoryService.deleteCategory(qry);
        return Response.buildSuccess();
    }

}
