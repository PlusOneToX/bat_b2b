package com.bat.flexible.web.picture;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.dto.PictureCategoryCmd;
import com.bat.flexible.api.picture.dto.PictureCategoryDetailQry;
import com.bat.flexible.api.picture.dto.page.PictureCategoryPageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.picture.co.PictureCategoryPageCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/flexible/v1/web/admin/u")
@Api(value = "AdminPictureCatgoryController",tags = "图片分类后台管理接口")
public class AdminPictureCatgoryController extends BaseController {

    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureCatgory, value = CommonLogTypeConstantDTO.AdminPictureCatgoryAdd)
    @PostMapping(value = "/p/pictureCategory")
    @ApiOperation(value = "新增图片分类")
    public Response create(@Valid @RequestBody PictureCategoryCmd pictureCategoryCmd){
        return pictureCategoryServiceI.create(pictureCategoryCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureCatgory, value = CommonLogTypeConstantDTO.AdminPictureCatgoryUpdate)
    @PutMapping(value = "/p/pictureCategory")
    @ApiOperation(value = "修改图片分类")
    public Response update(@Valid @RequestBody PictureCategoryCmd pictureCategoryCmd){
        return pictureCategoryServiceI.update(pictureCategoryCmd,getCurrentAdmin());
    }

    @GetMapping(value = "/p/pictureCategory")
    @ApiOperation(value = "查看图片详情")
    public Response<PictureCategoryDetailQry> detailById(@ApiParam(value ="图片分类id")@RequestParam(value="id",required = false)Integer id){
        return pictureCategoryServiceI.detailById(id);
    }


    @GetMapping(value = {"/p/pictureCategory/page","/po/pictureCategory/page"})
    @ApiOperation(value = "分页查询图片分类")
    public Response<PageInfo<PictureCategoryPageCO>> page(@Valid PictureCategoryPageQry categoryPageQry){
        return pictureCategoryServiceI.page(categoryPageQry);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureCatgory, value = CommonLogTypeConstantDTO.AdminPictureCatgoryUpOrDown)
    @PutMapping(value = "/p/pictureCategory/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response upOrDown(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){
        return pictureCategoryServiceI.upOrDown(flexibleUpOrDownDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureCatgory, value = CommonLogTypeConstantDTO.AdminPictureCatgoryUpdateStatus)
    @PutMapping(value = "/p/pictureCategory/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return pictureCategoryServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureCatgory, value = CommonLogTypeConstantDTO.AdminPictureCatgoryDelete)
    @DeleteMapping(value = "/p/pictureCategory")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return pictureCategoryServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }
}
