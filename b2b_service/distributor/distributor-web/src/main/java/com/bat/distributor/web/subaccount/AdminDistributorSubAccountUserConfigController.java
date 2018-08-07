package com.bat.distributor.web.subaccount;

import java.util.List;

import javax.validation.Valid;

import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.distributor.dto.DistributorId;
import com.bat.distributor.api.subaccount.DistributorSubAccountUserConfigServiceI;
import com.bat.distributor.api.subaccount.dto.DistributorSubAccountUserConfigDOQry;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigPageDTO;
import com.bat.distributor.api.subaccount.dto.SubAccountUserConfigPageQry;
import com.bat.distributor.api.subaccount.dto.SubAccountUserUpdateCmd;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountUserConfigDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商分账运营后台端后台接口")
@RestController
@RequestMapping(value = "/distributor/v1/web/admin/subAccountUserConfig")
public class AdminDistributorSubAccountUserConfigController extends BaseController {

    @Autowired
    private DistributorSubAccountUserConfigServiceI distributorSubAccountUserConfigServiceI;

    @GetMapping
    @ApiOperation(value = "根据分销商id查询详情")
    public Response<SubAccountUserUpdateCmd> detailByDistributorId(@Valid BaseId baseId) {
        SubAccountUserUpdateCmd subAccountUserUpdateCmd =
            distributorSubAccountUserConfigServiceI.detailById(baseId.getId());
        return Response.of(subAccountUserUpdateCmd);
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<SubAccountUserConfigPageDTO>>
        page(@Valid SubAccountUserConfigPageQry subAccountUserConfigPageQry) {
        PageInfo<SubAccountUserConfigPageDTO> pageInfo =
            distributorSubAccountUserConfigServiceI.pageAdmin(subAccountUserConfigPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "查询分销商分账配置列表")
    public Response<List<DistributorSubAccountUserConfigDOQry>> listByCondition(@Valid DistributorId distributorId) {
        List<DistributorSubAccountUserConfigDO> list =
            distributorSubAccountUserConfigServiceI.listByCondition(distributorId.getId());
        List<DistributorSubAccountUserConfigDOQry> distributorSubAccountUserConfigDOQries =
            BeanUtils.copyList(list, DistributorSubAccountUserConfigDOQry.class);
        return Response.of(distributorSubAccountUserConfigDOQries);
    }
}
