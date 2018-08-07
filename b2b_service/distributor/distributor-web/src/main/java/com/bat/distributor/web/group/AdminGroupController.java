package com.bat.distributor.web.group;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.group.GroupServiceI;
import com.bat.distributor.api.group.dto.GroupCmd;
import com.bat.distributor.api.group.dto.GroupId;
import com.bat.distributor.api.group.dto.GroupListQry;
import com.bat.distributor.api.group.dto.GroupOpenCmd;
import com.bat.distributor.api.group.dto.data.GroupDTO;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商分组后台接口", value = "AdminGroupController")
@RestController
@RequestMapping("/distributor/v1/web/admin/group")
public class AdminGroupController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminGroupController.class);

    @Resource
    private GroupServiceI service;

    @ApiOperation(value = "根据搜索条件查找分销商分组列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<GroupDTO>> listGroup(@Valid GroupListQry qry) {
        PageInfo<GroupDTO> pageInfo = service.listGroup(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找分销商分组列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<GroupDTO>> listGroupPo(@Valid GroupListQry qry) {
        PageInfo<GroupDTO> pageInfo = service.listGroup(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据分销商分组id获取分销商分组详情")
    @GetMapping()
    public Response<GroupDTO> getGroup(@Valid GroupId qry) {
        GroupDTO dto = service.getGroup(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminGroup, value = CommonLogTypeConstantDTO.AdminGroupAdd)
    @ApiOperation(value = "添加分销商分组")
    @PostMapping()
    public Response createGroup(@RequestBody @Valid GroupCmd cmd) {
        service.createGroup(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminGroup, value = CommonLogTypeConstantDTO.AdminGroupUpdate)
    @ApiOperation(value = "更新分销商分组")
    @PutMapping()
    public Response updateGroup(@RequestBody @Valid GroupCmd cmd) {
        service.updateGroup(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminGroup, value = CommonLogTypeConstantDTO.AdminGroupUpdateStatus)
    @ApiOperation(value = "更新分销商分组状态")
    @PutMapping(value = "/open")
    public Response openGroup(@RequestBody @Valid GroupOpenCmd cmd) {
        service.openGroup(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminGroup, value = CommonLogTypeConstantDTO.AdminGroupDeleteById)
    @ApiOperation(value = "根据分销商分组id删除分销商分组")
    @DeleteMapping()
    public Response deleteGroup(@RequestBody @Valid GroupId cmd) {
        service.deleteGroup(cmd);
        return Response.buildSuccess();
    }

}
