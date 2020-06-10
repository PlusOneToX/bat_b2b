package com.bat.flexible.web.font;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.font.FontServiceI;
import com.bat.flexible.dao.font.co.FontCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/flexible/v1/web/user/u/font")
@Api(tags = "字体前台接口")
public class UserFontController extends BaseController {

    @Autowired
    private FontServiceI fontServiceI;


    @GetMapping("/listUsable")
    @ApiOperation(value = "列表查询可用字体")
    public Response<List<FontCO>> listUsable(){
        List<FontCO> list = fontServiceI.listUsable();
        return Response.of(list);
    }


}
