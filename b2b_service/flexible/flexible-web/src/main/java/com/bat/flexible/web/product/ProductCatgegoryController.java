package com.bat.flexible.web.product;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.product.dto.ProductCategoryCmd;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.product.dto.ProductCategoryPageQry;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="/flexible/v1/web/admin/u")
@Api(tags = "产品类型后台管理接口")
public class ProductCatgegoryController extends BaseController {

    @Autowired
    private ProductCategoryServiceI productCategoryServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ProductCatgegory, value = CommonLogTypeConstantDTO.ProductCatgegoryAdd)
    @PostMapping(value = "/p/productCategory")
    @ApiOperation(value = "新增产品类型")
    public Response create(@Valid @RequestBody ProductCategoryCmd productCategoryCmd){
        return productCategoryServiceI.create(productCategoryCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ProductCatgegory, value = CommonLogTypeConstantDTO.ProductCatgegoryUpdate)
    @PutMapping(value = "/p/productCategory")
    @ApiOperation(value = "编辑产品类型")
    public Response update(@Valid @RequestBody ProductCategoryCmd productCategoryCmd){
        return productCategoryServiceI.update(productCategoryCmd,getCurrentAdmin());
    }


    @GetMapping(value = {"/p/productCategory/page","/po/productCategory/page"})
    @ApiOperation(value="分页")
    public Response<PageInfo<ProductCategoryCmd>> page(@Valid ProductCategoryPageQry categoryPageQry){
        PageInfo<ProductCategoryCmd> pageInfo =  productCategoryServiceI.page(categoryPageQry);
        return Response.of(pageInfo);
    }
    @GetMapping(value = "/p/productCategory/detail")
    @ApiOperation(value="查询详情")
    public Response<ProductCategoryCmd> detailById(@Valid FlexibleIdDTO flexibleIdDTO){
        return productCategoryServiceI.detailById(flexibleIdDTO.getId());
    }

    @GetMapping(value = {"/po/productCategory/listUsable"})
    @ApiOperation(value="获取所有启用的产品类型列表"  )
    public Response<List<ProductCategoryCmd>> listUsable(){
        List<ProductCategoryCmd> list =  productCategoryServiceI.listDTOUsable();
        return Response.of(list);
    }
}
