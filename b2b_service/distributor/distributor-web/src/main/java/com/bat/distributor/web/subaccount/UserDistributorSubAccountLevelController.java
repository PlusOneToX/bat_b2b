package com.bat.distributor.web.subaccount;

import java.util.List;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.DistributorSubAccountLevelServiceI;
import com.bat.distributor.api.subaccount.dto.DistributorSubAccountLevelDOQry;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bat.distributor.web.base.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商分账等级前台接口")
@RestController
@RequestMapping(value = "/distributor/v1/web/user/subAccountLevel")
public class UserDistributorSubAccountLevelController extends BaseController {

    @Autowired
    private DistributorSubAccountLevelServiceI distributorSubAccountLevelServiceI;

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "条件查询分账等级")
    public Response<List<DistributorSubAccountLevelDOQry>> listByCondition() {
        List<DistributorSubAccountLevelDO> levelDOList =
            distributorSubAccountLevelServiceI.listByDistributorId(Integer.parseInt(getUserId()));
        List<DistributorSubAccountLevelDOQry> levelDOQryList =
            BeanUtils.copyList(levelDOList, DistributorSubAccountLevelDOQry.class);
        return Response.of(levelDOQryList);
    }
}
