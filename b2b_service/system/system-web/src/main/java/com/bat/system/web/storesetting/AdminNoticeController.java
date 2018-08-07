package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.storesetting.NoticeService;
import com.bat.system.api.storesetting.dto.*;
import com.bat.system.api.storesetting.dto.data.NoticeDTO;
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
 * @description: 首页公告
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页公告后台接口", value = "AdminNoticeController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/notice")
public class AdminNoticeController extends BaseController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("/list")
    @ApiOperation(value = "查询公告列表(不分页)")
    public Response<PageInfo<NoticeDTO>> listNotice(@Valid NoticeQry qry) {
        return Response.of(noticeService.listNotice(qry));
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个公告")
    public Response<NoticeDTO> getNotice(@Valid NoticeId id) {
        return Response.of(noticeService.getNoticeById(id.getId()));
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminNotice, value = CommonLogTypeConstantDTO.AdminNoticeAdd)
    @PostMapping()
    @ApiOperation(value = "添加公告")
    public Response createNotice(@RequestBody @Valid NoticeCreateCmd cmd) {
        noticeService.createNotice(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminNotice, value = CommonLogTypeConstantDTO.AdminNoticeUpdate)
    @PutMapping()
    @ApiOperation(value = "更新公告")
    public Response updateNotice(@RequestBody @Valid NoticeUpdateCmd cmd) {
        noticeService.updateNotice(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminNotice, value = CommonLogTypeConstantDTO.AdminNoticeRelease)
    @PutMapping("/release")
    @ApiOperation(value = "发布公告")
    public Response releaseNotice(@RequestBody @Valid NoticeReleaseCmd cmd) {
        noticeService.releaseNotice(cmd);
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminNotice, value = CommonLogTypeConstantDTO.AdminNoticeDelete)
    @DeleteMapping()
    @ApiOperation(value = "通过ID删除公告")
    public Response deleteNotice(@RequestBody @Valid NoticeId id) {
        noticeService.deleteNoticeById(id.getId());
        return Response.buildSuccess();
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminNotice, value = CommonLogTypeConstantDTO.AdminNoticeDeleteByIds)
    @DeleteMapping("/ids")
    @ApiOperation(value = "通过IDS删除公告")
    public Response deleteNoticeByIds(@RequestBody @Valid NoticeIds ids) {
        ids.getIds().forEach(integer -> noticeService.deleteNoticeById(integer));
        return Response.buildSuccess();
    }
}
