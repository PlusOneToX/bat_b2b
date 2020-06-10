package com.bat.flexible.web.burying;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.burying.BuryingPointServiceI;
import com.bat.flexible.api.burying.dto.BuryingPointCmd;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/flexible/v1/web/user/buryingPoint")
@Api(tags = "埋点接口")
public class BuryingPointController {

    @Autowired
    private BuryingPointServiceI buryingPointServiceI;


    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.BuryingPoint, value = CommonLogTypeConstantDTO.BuryingPointAdd)
    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody BuryingPointCmd buryingPointCmd){
        return buryingPointServiceI.create(buryingPointCmd);
    }
}
