package com.bat.goods.web.brand;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.brand.BrandServiceI;
import com.bat.goods.api.brand.dto.BrandCmd;
import com.bat.goods.api.brand.dto.BrandId;
import com.bat.goods.api.brand.dto.BrandListQry;
import com.bat.goods.api.brand.dto.BrandOpenCmd;
import com.bat.goods.api.brand.dto.data.BrandDTO;
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

@Api(tags = "品牌后台接口", value = "AdminBrandController")
@RestController
@RequestMapping("/goods/v1/web/admin/brand")
public class AdminBrandController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminBrandController.class);

    @Resource
    private BrandServiceI brandService;

    @ApiOperation(value = "根据搜索条件查找品牌列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<BrandDTO>> listBrand(@Valid BrandListQry qry) {
        PageInfo<BrandDTO> pageInfo = brandService.listBrand(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找品牌列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<BrandDTO>> listBrandPo(@Valid BrandListQry qry) {
        PageInfo<BrandDTO> pageInfo = brandService.listBrand(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据品牌id获取品牌详情")
    @GetMapping()
    public Response<BrandDTO> getBrand(@Valid BrandId qry) {
        BrandDTO dto = brandService.getBrand(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBrand, value = CommonLogTypeConstantDTO.AdminBrandAdd)
    @ApiOperation(value = "添加品牌")
    @PostMapping()
    public Response createBrand(@RequestBody @Valid BrandCmd brandCmd) {
        brandService.createBrand(brandCmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBrand, value = CommonLogTypeConstantDTO.AdminBrandUpdate)
    @ApiOperation(value = "更新品牌")
    @PutMapping()
    public Response updateBrand(@RequestBody @Valid BrandCmd brandCmd) {
        brandService.updateBrand(brandCmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBrand, value = CommonLogTypeConstantDTO.AdminBrandUpdateStatus)
    @ApiOperation(value = "更新品牌状态")
    @PutMapping(value = "/open")
    public Response openBrand(@RequestBody @Valid BrandOpenCmd cmd) {
        brandService.openBrand(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminBrand, value = CommonLogTypeConstantDTO.AdminBrandDelete)
    @ApiOperation(value = "根据品牌id删除品牌")
    @DeleteMapping()
    public Response deleteBrand(@RequestBody @Valid BrandId cmd) {
        brandService.deleteBrand(cmd);
        return Response.buildSuccess();
    }

}
