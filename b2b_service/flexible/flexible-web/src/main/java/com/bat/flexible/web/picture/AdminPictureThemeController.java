package com.bat.flexible.web.picture;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.picture.PictureThemeServiceI;
import com.bat.flexible.api.picture.dto.theme.PictureThemeDTO;
import com.bat.flexible.api.picture.dto.theme.PictureThemePageQry;
import com.bat.flexible.api.picture.dto.theme.PictureThemeRelaRequest;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.picture.dataobject.PictureThemeDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 沙漠
 */
@RestController
@RequestMapping(value = "/flexible/v1/web/admin/u/pictureTheme")
@Api(tags = "官方主题后台接口")
public class AdminPictureThemeController {

    @Autowired
    private PictureThemeServiceI pictureThemeServiceI;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<PictureThemeDO>> page(PictureThemePageQry pictureThemePageQry) {
        PageInfo<PictureThemeDO> pageInfo = pictureThemeServiceI.page(pictureThemePageQry);
        return Response.of(pageInfo);
    }

    @GetMapping
    @ApiOperation(value = "根据id查询详情")
    public Response get(@Valid FlexibleIdDTO flexibleIdDTO) {
        PictureThemeDTO pictureThemeDTO = pictureThemeServiceI.getById(flexibleIdDTO.getId());
        return Response.of(pictureThemeDTO);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureTheme, value = CommonLogTypeConstantDTO.AdminPictureThemeAdd)
    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@RequestBody PictureThemeDO pictureTheme) {
        pictureTheme = pictureThemeServiceI.create(pictureTheme);
        return Response.of(pictureTheme);
    }

    /**
     * 主题与分类进行关联
     * @param pictureThemeRelaRequest
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureTheme, value = CommonLogTypeConstantDTO.AdminPictureThemeRelation)
    @PostMapping("/relation")
    @ApiOperation(value = "主题与分类进行关联")
    public Response relation(@RequestBody PictureThemeRelaRequest pictureThemeRelaRequest) {
       return   pictureThemeServiceI.relation(pictureThemeRelaRequest);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureTheme, value = CommonLogTypeConstantDTO.AdminPictureThemeUpdate)
    @PutMapping
    @ApiOperation(value = "修改")
    public Response update(@RequestBody PictureThemeDO pictureTheme) {
        pictureTheme = pictureThemeServiceI.update(pictureTheme);
        return Response.of(pictureTheme);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminPictureTheme, value = CommonLogTypeConstantDTO.AdminPictureThemeDelete)
    @DeleteMapping
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        return  pictureThemeServiceI.delete(flexibleIdDTO.getId());
    }
}
