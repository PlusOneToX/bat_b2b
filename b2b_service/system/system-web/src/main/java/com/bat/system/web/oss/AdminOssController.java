package com.bat.system.web.oss;

import javax.annotation.Resource;

import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bat.system.api.oss.OssService;
import com.bat.system.api.oss.dto.data.AssumeRoleDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/11 21:29
 */
@Api(tags = "系统模块-对象存储后台接口", value = "AdminOssController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/oss")
public class AdminOssController extends BaseController {

    @Resource
    private OssService ossService;

    @GetMapping("/sts")
    @ApiOperation(value = "获取STS授权")
    public Response<AssumeRoleDTO> getSts(@RequestParam(required = false) String userId) {
        return Response.of(ossService.getSts(userId));
    }
}
