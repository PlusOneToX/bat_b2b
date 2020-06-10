package com.bat.flexible.web.canva;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.canva.CanvaServiceI;
import com.bat.flexible.api.canva.dto.UserNoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/flexible/v1/web/user/canva")
@Api(tags = "canva接口")
public class CanvaController {

    @Autowired
    private CanvaServiceI canvaServiceI;

    @GetMapping("/token")
    @ApiOperation(value = "获取canva令牌")
    public Response getCanvaToken(@Valid UserNoDTO userNoDTO) {
       String token = canvaServiceI.getToken(userNoDTO.getUserNo());
        return Response.of(token);
    }
}
