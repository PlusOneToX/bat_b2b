package com.bat.system.web.trainingdownloadcenter;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.trainingdownloadcenter.DownloadCenterService;
import com.bat.system.api.trainingdownloadcenter.dto.DownloadCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.data.DownloadCenterDTO;
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
 * @description:
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-下载中心前台接口", value = "UserDownloadCenterController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/downloadCenter")
public class UserDownloadCenterController extends BaseController {

    @Resource
    private DownloadCenterService downloadCenterService;

    @GetMapping("/list")
    @ApiOperation(value = "查询下载中心通过父ID(分页)")
    public Response<PageInfo<DownloadCenterDTO>> listDownloadCenterByParentId(@Valid DownloadCenterQry qry) {
        return Response.of(downloadCenterService.listDownloadCenterByParentId(qry));
    }

}
