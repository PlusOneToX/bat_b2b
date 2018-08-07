package com.bat.system.web.trainingdownloadcenter;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.trainingdownloadcenter.TrainingCenterService;
import com.bat.system.api.trainingdownloadcenter.dto.TrainingCenterQry;
import com.bat.system.api.trainingdownloadcenter.dto.data.TrainingCenterDTO;
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

@Api(tags = "商店配置模块-训练中心前台接口", value = "UserTrainingCenterController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/trainingCenter")
public class UserTrainingCenterController extends BaseController {

    @Resource
    private TrainingCenterService trainingCenterService;

    @GetMapping("/list")
    @ApiOperation(value = "查询训练中心通过父ID(分页)")
    public Response<PageInfo<TrainingCenterDTO>> listTrainingCenterByParentId(@Valid TrainingCenterQry qry) {
        return Response.of(trainingCenterService.listTrainingCenterByParentId(qry));
    }

}
