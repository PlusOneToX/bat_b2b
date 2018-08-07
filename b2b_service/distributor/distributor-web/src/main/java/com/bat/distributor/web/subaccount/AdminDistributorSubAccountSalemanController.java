package com.bat.distributor.web.subaccount;

import java.util.List;

import javax.validation.Valid;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.DistributorSubAccountSalemanServiceI;
import com.bat.distributor.api.subaccount.dto.DistributorOpenFlagQry;
import com.bat.distributor.api.subaccount.dto.DistributorSubAccountSalemanDOQry;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanPageDTO;
import com.bat.distributor.api.subaccount.dto.SubAccountSalemanPageQry;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商业务员运营后台接口")
@RestController
@RequestMapping(value = "/distributor/v1/web/admin/subAccountSaleman")
public class AdminDistributorSubAccountSalemanController {

    @Autowired
    private DistributorSubAccountSalemanServiceI distributorSubAccountSalemanServiceI;

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询业务员")
    public Response<PageInfo<SubAccountSalemanPageDTO>> page(@Valid SubAccountSalemanPageQry subAccountSalemanPageQry) {
        PageInfo<SubAccountSalemanPageDTO> pageInfo =
            distributorSubAccountSalemanServiceI.pageAdmin(subAccountSalemanPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "条件查询业务员")
    public Response<List<DistributorSubAccountSalemanDOQry>>
        listByCondition(@Valid DistributorOpenFlagQry distributorOpenFlagQry) {
        List<DistributorSubAccountSalemanDO> doList =
            distributorSubAccountSalemanServiceI.listByCondition(distributorOpenFlagQry.getDistributorId(), null,
                distributorOpenFlagQry.getOpenFlag(), distributorOpenFlagQry.getSonLevelId());
        List<DistributorSubAccountSalemanDOQry> distributorSubAccountSalemanDOQries =
            BeanUtils.copyList(doList, DistributorSubAccountSalemanDOQry.class);
        return Response.of(distributorSubAccountSalemanDOQries);
    }
}
