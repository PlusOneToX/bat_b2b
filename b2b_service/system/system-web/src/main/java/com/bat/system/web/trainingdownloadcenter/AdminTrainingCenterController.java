package com.bat.system.web.trainingdownloadcenter;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.trainingdownloadcenter.TrainingCenterService;
import com.bat.system.api.trainingdownloadcenter.dto.*;
import com.bat.system.api.trainingdownloadcenter.dto.data.TrainingCenterDTO;
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

@Api(tags = "商店配置模块-训练中心后台接口", value = "AdminTrainingCenterController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/admin/trainingCenter")
public class AdminTrainingCenterController extends BaseController {

    @Resource
    private TrainingCenterService trainingCenterService;

    @GetMapping("")
    @ApiOperation(value = "查询训练中心通过ID")
    public Response<TrainingCenterDTO> getTrainingCenterById(@Valid TrainingCenterId id) {
        return Response.of(trainingCenterService.getTrainingCenterById(id.getId()));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询训练中心通过父ID(分页)")
    public Response<PageInfo<TrainingCenterDTO>> listTrainingCenterByParentId(@Valid TrainingCenterQry qry) {
        return Response.of(trainingCenterService.listTrainingCenterByParentId(qry));
    }

    @GetMapping("/list/menu")
    @ApiOperation(value = "查询训练中心通过父ID(不分页)")
    public Response<List<TrainingCenterDTO>> listTrainingCenterMenuByParentId(@Valid TrainingCenterMenuQry qry) {
        return Response.of(trainingCenterService.listTrainingCenterMenuByParentId(qry));
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTraining, value = CommonLogTypeConstantDTO.AdminTrainingAdd)
    @PostMapping("")
    @ApiOperation(value = "新增训练中心")
    public Response createTrainingCenter(@RequestBody @Valid TrainingCenterCreateCmd cmd) {
        trainingCenterService.createTrainingCenter(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTraining, value = CommonLogTypeConstantDTO.AdminTrainingUpdate)
    @PutMapping("")
    @ApiOperation(value = "更新训练中心")
    public Response updateTrainingCenter(@RequestBody @Valid TrainingCenterUpdateCmd cmd) {
        trainingCenterService.updateTrainingCenter(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTraining, value = CommonLogTypeConstantDTO.AdminTrainingUp)
    @PutMapping("/sort/up")
    @ApiOperation(value = "上移")
    public Response sortTrainingCenterUp(@RequestBody @Valid TrainingCenterId id) {
        trainingCenterService.sortTrainingCenterUp(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTraining, value = CommonLogTypeConstantDTO.AdminTrainingDown)
    @PutMapping("/sort/down")
    @ApiOperation(value = "下移")
    public Response sortTrainingCenterDown(@RequestBody @Valid TrainingCenterId id) {
        trainingCenterService.sortTrainingCenterDown(id.getId());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTraining, value = CommonLogTypeConstantDTO.AdminTrainingDelete)
    @DeleteMapping("")
    @ApiOperation(value = "删除训练中心")
    public Response deleteTrainingCenter(@RequestBody @Valid TrainingCenterId id) {
        trainingCenterService.deleteTrainingCenter(id.getId());
        return Response.buildSuccess();
    }

}
