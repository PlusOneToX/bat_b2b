package com.bat.thirdparty.quanyi.controller;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.base.BaseController;
import com.bat.dubboapi.financial.common.Response;
import com.bat.thirdparty.quanyi.api.AdminQuanyiServiceI;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiCancelCmd;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiLogQry;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiQry;
import com.bat.thirdparty.quanyi.api.dto.ThirdQuanyiUnCancelCmd;
import com.bat.thirdparty.quanyi.dao.co.ThirdQuanyiLogPageCO;
import com.bat.thirdparty.quanyi.dao.co.ThirdQuanyiPageCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 沙漠
 */
@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/quanyi")
@Api(tags = "权益列表接口")
public class AdminQuanyiController extends BaseController {

    @Resource
    private AdminQuanyiServiceI adminQuanyiServiceI;

    @GetMapping("/page")
    @ApiOperation(value="分页查询")
    public Response<PageInfo<List<ThirdQuanyiPageCO>>> page(ThirdQuanyiQry thirdQuanyiQry) {
        PageInfo<List<ThirdQuanyiPageCO>> pageInfo = adminQuanyiServiceI.page(thirdQuanyiQry);
        return Response.of(pageInfo);
    }

    @GetMapping("/log/page")
    @ApiOperation(value="查询权益日志")
    public Response<PageInfo<List<ThirdQuanyiLogPageCO>>> logPage(@Valid ThirdQuanyiLogQry thirdQuanyiLogQry) {
        PageInfo<List<ThirdQuanyiLogPageCO>> pageInfo = adminQuanyiServiceI.logPage(thirdQuanyiLogQry);
        return Response.of(pageInfo);
    }


    @PutMapping("cancel")
    @ApiOperation(value="对当前权益进行作废（取消）")
    public Response cancel(@Valid @RequestBody ThirdQuanyiCancelCmd cmd) {
        adminQuanyiServiceI.cancel(cmd);
        return Response.buildSuccess();
    }

    @PutMapping("unCancel")
    @ApiOperation(value="撤销对当前权益进行作废（撤销取消）")
    public Response unCancel(@Valid @RequestBody ThirdQuanyiUnCancelCmd cmd) {
        adminQuanyiServiceI.unCancel(cmd);
        return Response.buildSuccess();
    }

}
