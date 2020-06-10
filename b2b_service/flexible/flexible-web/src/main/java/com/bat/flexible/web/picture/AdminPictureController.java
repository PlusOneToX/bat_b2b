package com.bat.flexible.web.picture;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureCmd;
import com.bat.flexible.api.picture.dto.PictureQry;
import com.bat.flexible.api.picture.dto.page.DistributorPictureQry;
import com.bat.flexible.api.picture.dto.page.PicturePageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.picture.co.DistributorPictureCO;
import com.bat.flexible.dao.picture.co.PicturePageCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/flexible/v1/web/admin/u")
@Api(tags = "图片后台管理接口",value = "AdminPictureController")
public class AdminPictureController extends BaseController {

    @Autowired
    private PictureServiceI pictureServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPicture, value = CommonLogTypeConstantDTO.AdminPictureAdd)
    @ApiOperation(value = "新增图片")
    @PostMapping(value = "/p/picture")
    public Response create(@Valid @RequestBody PictureCmd pictureCmd){
        return pictureServiceI.create(pictureCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPicture, value = CommonLogTypeConstantDTO.AdminPictureUpdate)
    @ApiOperation(value = "编辑图片")
    @PutMapping(value = "/p/picture")
    public Response update(@Valid @RequestBody PictureCmd pictureCmd){
        return pictureServiceI.update(pictureCmd,getCurrentAdmin());
    }

    @ApiOperation(value = "查看图片详情")
    @GetMapping(value = "/p/picture")
    public Response<PictureQry> detailById(
           @Valid FlexibleIdDTO flexibleIdDTO){
        return pictureServiceI.detailById(flexibleIdDTO.getId());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPicture, value = CommonLogTypeConstantDTO.AdminPictureUpOrDown)
    @PutMapping(value = "/p/picture/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response upOrDown(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){
        return pictureServiceI.upOrDown(flexibleUpOrDownDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPicture, value = CommonLogTypeConstantDTO.AdminPictureUpdateStatus)
    @PutMapping(value = "/p/picture/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return pictureServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPicture, value = CommonLogTypeConstantDTO.AdminPictureDelete)
    @DeleteMapping(value = "/p/picture")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return pictureServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPicture, value = CommonLogTypeConstantDTO.AdminPictureTop)
    @PutMapping(value = "/p/picture/top")
    @ApiOperation(value = "置顶")
    public Response top(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return pictureServiceI.top(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    @GetMapping(value = {"/p/picture/page","/po/picture/page"})
    @ApiOperation(value = "分页查询图片列表")
    public Response<PageInfo<PicturePageCO>> page(@Valid PicturePageQry picturePageQry){
        PageInfo<PicturePageCO> pageInfo =  pictureServiceI.page(picturePageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = {"/p/picture/pageByDistributor","/po/picture/pageByDistributor"})
    @ApiOperation(value = "分页查询分销商图片列表")
    public Response<PageInfo<DistributorPictureCO>> pageByDistributor(@Valid DistributorPictureQry distributorPictureQry){

        PageInfo<DistributorPictureCO> pageInfo = pictureServiceI.pagePictureByDistributor(distributorPictureQry);
        return Response.of(pageInfo);
    }
}
