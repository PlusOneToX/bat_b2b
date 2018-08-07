package com.bat.distributor.web.distributor;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.BaseIds;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.distributor.DistributorServiceI;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.api.distributor.dto.data.DistributorDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorIdsDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorNextListDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorOneListDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.base.BaseController;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商后台接口", value = "AdminDistributorController")
@RestController
@RequestMapping("/distributor/v1/web/admin/distributor")
public class DistributorController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(DistributorController.class);

    @Resource
    private DistributorServiceI service;

    @ApiOperation(value = "根据搜索条件查找一级分销商列表(分页)")
    @GetMapping(value = "/one/list")
    public Response<PageInfo<DistributorOneListDTO>> listOneDistributor(@Valid DistributorOneListQry qry) {
        PageInfo<DistributorOneListDTO> pageInfo = service.listOneDistributor(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找多级分销商列表(分页)")
    @GetMapping(value = "/next/list")
    public Response<PageInfo<DistributorNextListDTO>> listNextDistributor(@Valid DistributorNextListQry qry) {
        PageInfo<DistributorNextListDTO> pageInfo = service.listNextDistributor(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找一级分销商列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/one/list")
    public Response<PageInfo<DistributorOneListDTO>> listOneDistributorPo(@Valid DistributorOneListQry qry) {
        PageInfo<DistributorOneListDTO> pageInfo = service.listOneDistributor(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商列表(分页，包含所有分销商)")
    @GetMapping(value = "/list")
    public Response<PageInfo<DistributorListDTO>> listDistributor(@Valid DistributorListQry qry) {
        PageInfo<DistributorListDTO> pageInfo = service.listDistributor(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商列表(分页，包含所有分销商)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<DistributorListDTO>> listDistributorPo(@Valid DistributorListQry qry) {
        PageInfo<DistributorListDTO> pageInfo = service.listDistributor(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商id获取分销商详情")
    @GetMapping()
    public Response<DistributorDTO> getGroup(@Valid DistributorId qry) {
        DistributorDTO dto = service.getDistributor(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Distributor,
        value = CommonLogTypeConstantDTO.DistributorAdd)
    @ApiOperation(value = "添加分销商")
    @PostMapping()
    public Response createDistributor(@RequestBody @Valid DistributorCmd cmd) {
        service.createDistributor(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Distributor,
        value = CommonLogTypeConstantDTO.DistributorUpdate)
    @ApiOperation(value = "更新分销商(包括申请分销商同意,支持多级分销修改)")
    @PutMapping()
    public Response updateDistributor(@RequestBody @Valid DistributorUpdateCmd cmd) throws Exception {
        service.updateDistributor(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Distributor,
        value = CommonLogTypeConstantDTO.DistributorRefuse)
    @ApiOperation(value = "根据分销商id拒绝申请中的分销商")
    @PutMapping("/refuse")
    public Response refuse(@RequestBody @Valid DistributorId cmd) {
        service.refuse(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Distributor,
        value = CommonLogTypeConstantDTO.DistributorOpen)
    @ApiOperation(value = "冻结解冻分销商")
    @PutMapping(value = "/open")
    public Response freezeDistributor(@RequestBody @Valid DistributorFreezeStatusCmd cmd) {
        service.freezeDistributor(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Distributor,
        value = CommonLogTypeConstantDTO.DistributorDeleteById)
    @ApiOperation(value = "根据分销商id删除分销商")
    @DeleteMapping()
    public Response deleteDistributor(@RequestBody @Valid DistributorId cmd) {
        service.deleteDistributor(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据ids查找分销商基本数据")
    @PostMapping(value = "/po/ids")
    public Response<List<DistributorIdsDTO>> distributorIds(@RequestBody @Valid BaseIds qry) {
        List<DistributorIdsDTO> dtos = service.distributorIds(qry);
        return Response.of(dtos);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Distributor,
        value = CommonLogTypeConstantDTO.DistributorNextCheck)
    @ApiOperation(value = "下级分销商审核")
    @PutMapping(value = "/next/check")
    public Response checkNextDistributor(@RequestBody @Valid DistributorApplyStatusCmd cmd) {
        service.checkNextDistributor(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "分销商资料审批列表")
    @GetMapping(value = "/check/list")
    public Response<PageInfo<DistributorCheckListDTO>> listDistributorCheck(@Valid DistributorCheckListQry qry) {
        PageInfo<DistributorCheckListDTO> pageInfo = service.listDistributorCheck(qry, getUserId());
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "分销商资料审批详情")
    @GetMapping(value = "/check")
    public Response<DistributorCheckDTO> getDistributorCheck(@Valid BaseId qry) {
        DistributorCheckDTO checkDTO = service.getDistributorCheck(qry);
        return Response.of(checkDTO);
    }

    @ApiOperation(value = "分销商资料审批")
    @PutMapping(value = "/check")
    public Response checkDistributor(@RequestBody @Valid DistributorCheckCmd cmd) {
        service.checkDistributor(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

}
