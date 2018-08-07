package com.bat.system.web.storesetting;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.storesetting.NoticeService;
import com.bat.system.api.storesetting.dto.NoticeId;
import com.bat.system.api.storesetting.dto.NoticeQry;
import com.bat.system.api.storesetting.dto.data.NoticeDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 首页推广（轮播图）（banner）
 * @date: 2018/4/28 11:08
 */

@Api(tags = "商店配置模块-首页公告前台接口", value = "UserNoticeController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/notice")
public class UserNoticeController extends BaseController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("/list")
    @ApiOperation(value = "查询公告列表(分页)")
    public Response<PageInfo<NoticeDTO>> listNotice(@Valid NoticeQry qry) {
        return Response.of(noticeService.listNotice(qry));
    }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个后台用户")
    public Response<NoticeDTO> getNotice(@Valid NoticeId id) {
        return Response.of(noticeService.getNoticeById(id.getId()));
    }
}
