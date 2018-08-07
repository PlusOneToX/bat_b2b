package com.bat.distributor.web.subaccount;

import java.util.List;

import javax.validation.Valid;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.BasePage;
import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.DistributorSubAccountUserConfigServiceI;
import com.bat.distributor.api.subaccount.dto.DistributorSubAccountUserConfigDOQry;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigCmd;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigPageDTO;
import com.bat.distributor.api.subaccount.dto.SubAccountUserUpdateCmd;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商分账分销商端配置后台接口")
@RestController
@RequestMapping(value = "/distributor/v1/web/user/subAccountUserConfig")
public class DistributorSubAccountUserConfigController extends BaseController {

    @Autowired
    private DistributorSubAccountUserConfigServiceI distributorSubAccountUserConfigServiceI;

    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody SubAccountUserConfigCmd subAccountUserConfigCmd) {
        return distributorSubAccountUserConfigServiceI.create(subAccountUserConfigCmd, getUserId(), getUserName());
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Response update(@Valid @RequestBody SubAccountUserUpdateCmd subAccountUserUpdateCmd) {
        return distributorSubAccountUserConfigServiceI.update(subAccountUserUpdateCmd, getUserId(), getUserName());
    }

    @GetMapping
    @ApiOperation(value = "根据分销商id查询详情")
    public Response<SubAccountUserUpdateCmd> detailByDistributorId(@Valid BaseId baseId) {
        SubAccountUserUpdateCmd subAccountUserUpdateCmd =
            distributorSubAccountUserConfigServiceI.detailById(baseId.getId());
        return Response.of(subAccountUserUpdateCmd);
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<SubAccountUserConfigPageDTO>> page(@Valid BasePage basePage) {
        PageInfo<SubAccountUserConfigPageDTO> pageInfo =
            distributorSubAccountUserConfigServiceI.page(getUserId(), basePage.getPage(), basePage.getSize());
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "查询分销商分账配置列表")
    public Response<List<DistributorSubAccountUserConfigDOQry>> listByCondition() {
        List<DistributorSubAccountUserConfigDO> list =
            distributorSubAccountUserConfigServiceI.listByCondition(Integer.parseInt(getUserId()));
        List<DistributorSubAccountUserConfigDOQry> distributorSubAccountUserConfigDOQries =
            BeanUtils.copyList(list, DistributorSubAccountUserConfigDOQry.class);
        return Response.of(distributorSubAccountUserConfigDOQries);
    }

    @DeleteMapping
    @ApiOperation(value = "删除配置")
    public Response delete(@Valid @RequestBody BaseId baseId) {
        distributorSubAccountUserConfigServiceI.delete(baseId.getId(), Integer.parseInt(getUserId()), getUserName());
        return Response.buildSuccess();
    }
}
