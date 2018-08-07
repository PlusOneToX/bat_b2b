package com.bat.system.web.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.user.dto.*;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.user.MenuService;
import com.bat.system.api.user.dto.*;
import com.bat.system.api.user.dto.data.MenuDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/12 12:15
 */
@Api(tags = "系统模块-菜单后台接口", value = "AdminMenuController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/menu")
public class AdminMenuController extends BaseController {

    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    @ApiOperation(value = "查询菜单列表(分页)")
    public Response<PageInfo<MenuDTO>> listMenu(@Valid MenuQry qry) {
        PageInfo pageInfo = menuService.listMenu(qry);
        return Response.of(pageInfo);
    }

    @GetMapping("/po/list/tree")
    @ApiOperation(value = "查询菜单列表(不分页)")
    public Response<List<MenuManagerSyncDTO>> listMenuTree() {
        return Response.of(menuService.listManagerTree());
    }

    @PutMapping("/po/sync")
    @ApiOperation(value = "根据前端代码json 同步菜单(仅限迁移代码时调用) 目前仅限层级为2")
    public Response syncMenu(@RequestBody List<MenuCreateCmd> map) {
        menuService.syncMenu(map);
        return Response.buildSuccess();
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个菜单")
    public Response<MenuDTO> getMenu(@Valid MenuId id) {
        return Response.of(menuService.getMenuById(id.getId()));
    }

    @GetMapping("/ids")
    @ApiOperation(value = "通过IDS查询多个菜单")
    public Response<List<MenuDTO>> getMenuByIds(@Valid MenuIds ids) {
        List<MenuDTO> collect =
            ids.getIds().stream().map(integer -> menuService.getMenuById(integer)).collect(Collectors.toList());
        return Response.of(collect);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMenu, value = CommonLogTypeConstantDTO.AdminMenuAdd)
    @PostMapping()
    @ApiOperation(value = "添加菜单")
    public Response createMenu(@RequestBody @Valid MenuCreateCmd cmd) {
        menuService.createMenu(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMenu,
        value = CommonLogTypeConstantDTO.AdminMenuUpdate)
    @PutMapping()
    @ApiOperation(value = "更新菜单")
    public Response updateMenu(@RequestBody @Valid MenuUpdateCmd cmd) {
        menuService.updateMenu(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminMenu,
        value = CommonLogTypeConstantDTO.AdminMenuDeleteById)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除菜单")
    public Response deleteMenu(@RequestBody @Valid MenuId id) {
        menuService.deleteMenuById(id.getId());
        return Response.buildSuccess();
    }

}
