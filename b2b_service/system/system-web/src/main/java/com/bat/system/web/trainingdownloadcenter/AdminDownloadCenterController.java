package com.bat.system.web.trainingdownloadcenter;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.trainingdownloadcenter.DownloadCenterService;
import com.bat.system.api.trainingdownloadcenter.dto.*;
import com.bat.system.api.trainingdownloadcenter.dto.data.DownloadCenterDTO;
import com.bat.system.web.annotation.SysLog;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import com.bat.system.web.constants.CommonLogTypeConstantDTO;
import com.bat.system.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.trainingdownloadcenter.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-下载中心后台接口", value = "AdminDownloadCenterController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/downloadCenter")
public class AdminDownloadCenterController extends BaseController {

    @Resource
    private DownloadCenterService downloadCenterService;

    @GetMapping("")
    @ApiOperation(value = "查询下载中心通过ID")
    public Response<DownloadCenterDTO> getDownloadCenterById(@Valid DownloadCenterId id) {
        return Response.of(downloadCenterService.getDownloadCenterById(id.getId()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询下载中心通过父ID(分页)")
    public Response<PageInfo<DownloadCenterDTO>> listDownloadCenterByParentId(@Valid DownloadCenterQry qry) {
        return Response.of(downloadCenterService.listDownloadCenterByParentId(qry));
    }

    @GetMapping("/list/menu")
    @ApiOperation(value = "查询下载中心通过父ID(不分页)")
    public Response<List<DownloadCenterDTO>> listDownloadCenterMenuByParentId(@Valid DownloadCenterMenuQry qry) {
        return Response.of(downloadCenterService.listDownloadCenterMenuByParentId(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDownloadCenter, value = CommonLogTypeConstantDTO.AdminDownloadCenterAdd)
    @PostMapping("")
    @ApiOperation(value = "新增下载中心")
    public Response createDownloadCenter(@RequestBody @Valid DownloadCenterCreateCmd cmd) {
        downloadCenterService.createDownloadCenter(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDownloadCenter, value = CommonLogTypeConstantDTO.AdminDownloadCenterUpdate)
    @PutMapping("")
    @ApiOperation(value = "更新下载中心")
    public Response updateDownloadCenter(@RequestBody @Valid DownloadCenterUpdateCmd cmd) {
        downloadCenterService.updateDownloadCenter(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDownloadCenter, value = CommonLogTypeConstantDTO.AdminDownloadCenterUp)
    @PutMapping("/sort/up")
    @ApiOperation(value = "上移")
    public Response sortDownloadCenterUp(@RequestBody @Valid DownloadCenterId id) {
        downloadCenterService.sortDownloadCenterUp(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDownloadCenter, value = CommonLogTypeConstantDTO.AdminDownloadCenterDown)
    @PutMapping("/sort/down")
    @ApiOperation(value = "下移")
    public Response sortDownloadCenterDown(@RequestBody @Valid DownloadCenterId id) {
        downloadCenterService.sortDownloadCenterDown(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminDownloadCenter, value = CommonLogTypeConstantDTO.AdminDownloadCenterDelete)
    @DeleteMapping("")
    @ApiOperation(value = "删除下载中心")
    public Response deleteDownloadCenter(@RequestBody @Valid DownloadCenterId id) {
        downloadCenterService.deleteDownloadCenter(id.getId());
        return Response.buildSuccess();
    }

}
