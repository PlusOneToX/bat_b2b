package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.ColumnService;
import com.bat.system.api.storesetting.dto.*;
import com.bat.system.api.storesetting.dto.data.ColumnDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页栏目
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页栏目后台接口", value = "AdminColumnController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/column")
public class AdminColumnController extends BaseController {

    @Resource
    private ColumnService columnService;

    @GetMapping("/list")
    @ApiOperation(value = "查询栏目列表(分页)")
    public Response<PageInfo<ColumnDTO>> listColumn(@Valid ColumnQry qry) {
        return Response.of(columnService.listColumn(qry));
    }

    @GetMapping("")
    @ApiOperation(value = "通过ID查询单个栏目")
    public Response<ColumnDTO> getColumn(@Valid ColumnId id) {
        return Response.of(columnService.getColumn(id.getId()));
    }

    @PutMapping("/clearanceGoodsStoreColumn")
    @ApiOperation(value = "一键导入清仓商品（栏目）")
    public Response clearanceGoodsStoreColumn(@Valid @RequestBody ColumnId id) {
        columnService.clearanceGoodsStoreColumn(id.getId());
        return Response.buildSuccess();
    }

    @PutMapping("/clearGoodsStoreColumn")
    @ApiOperation(value = "一键清除商品（栏目）")
    public Response clearGoodsStoreColumn(@Valid @RequestBody ColumnId id) {
        columnService.clearGoodsStoreColumn(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminColumn,
        value = CommonLogTypeConstantDTO.AdminColumnAdd)
    @PostMapping()
    @ApiOperation(value = "添加栏目")
    public Response createColumn(@RequestBody @Valid ColumnCreateCmd cmd) {
        columnService.createColumn(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminColumn,
        value = CommonLogTypeConstantDTO.AdminColumnUpdate)
    @PutMapping()
    @ApiOperation(value = "更新栏目")
    public Response updateColumn(@RequestBody @Valid ColumnUpdateCmd cmd) {
        columnService.updateColumn(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminColumn,
        value = CommonLogTypeConstantDTO.AdminColumnRelease)
    @PutMapping("/release")
    @ApiOperation(value = "发布栏目")
    public Response releaseColumn(@RequestBody @Valid ColumnReleaseCmd cmd) {
        columnService.releaseColumn(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminColumn,
        value = CommonLogTypeConstantDTO.AdminColumnDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除栏目")
    public Response deleteColumn(@RequestBody @Valid ColumnId id) {
        columnService.deleteColumnById(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminColumn,
        value = CommonLogTypeConstantDTO.AdminColumnDeleteByIds)
    @DeleteMapping("/ids")
    @ApiOperation(value = "通过IDS删除栏目")
    public Response deleteColumnByIds(@RequestBody @Valid ColumnIds ids) {
        ids.getIds().forEach(integer -> columnService.deleteColumnById(integer));
        return Response.buildSuccess();
    }
}
