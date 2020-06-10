package com.bat.flexible.web.font;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.font.FontServiceI;
import com.bat.flexible.api.font.dto.FontCmd;
import com.bat.flexible.api.font.dto.FontPageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;

import com.bat.flexible.dao.font.co.FontCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/flexible/v1/web/admin/u")
@Api(tags = "字体后台接口")
public class FontController extends BaseController {

    @Autowired
    private FontServiceI fontServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Font, value = CommonLogTypeConstantDTO.FontAdd)
    @PostMapping(value = "/p/font")
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody FontCmd fontCmd){
        return fontServiceI.create(fontCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Font, value = CommonLogTypeConstantDTO.FontUpdate)
    @PutMapping(value = "/p/font")
    @ApiOperation(value = "修改")
    public Response update(@Valid @RequestBody FontCmd fontCmd){
        return fontServiceI.update(fontCmd,getCurrentAdmin());
    }

    @GetMapping("/p/font/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<FontCO>> page(@Valid FontPageQry fontPageQry){
        PageInfo<FontCO> pageInfo = fontServiceI.page(fontPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping("/p/font")
    @ApiOperation(value = "详情")
    public Response<FontCmd> detailById(@Valid FlexibleIdDTO flexibleIdDTO){
        FontCmd fontCmd  = fontServiceI.detailById(flexibleIdDTO.getId());
        return Response.of(fontCmd);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Font, value = CommonLogTypeConstantDTO.FontUpOrDown)
    @PutMapping(value = "/p/font/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response upOrDown(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){
        return fontServiceI.upOrDown(flexibleUpOrDownDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Font, value = CommonLogTypeConstantDTO.FontDelete)
    @DeleteMapping(value = "/p/font")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return fontServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Font, value = CommonLogTypeConstantDTO.FontUpdateOpenFlag)
    @PutMapping(value = "/p/font/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return fontServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }
}
