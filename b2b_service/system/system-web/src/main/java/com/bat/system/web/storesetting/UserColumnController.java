package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.ColumnService;
import com.bat.system.api.storesetting.dto.ColumnId;
import com.bat.system.api.storesetting.dto.ColumnQry;
import com.bat.system.api.storesetting.dto.data.ColumnDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页栏目
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页栏目前台接口", value = "UserColumnController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/column")
public class UserColumnController extends BaseController {

    @Resource
    private ColumnService columnService;

    @GetMapping("/list")
    @ApiOperation(value = "查询栏目列表(不分页)")
    public Response<PageInfo<ColumnDTO>> listColumn(@Valid ColumnQry qry) {
        return Response.of(columnService.listColumn(qry));
    }

    @GetMapping("")
    @ApiOperation(value = "通过ID查询单个栏目")
    public Response<ColumnDTO> getColumn(@Valid ColumnId id) {
        return Response.of(columnService.getColumn(id.getId()));
    }
}
