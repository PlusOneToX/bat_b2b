package com.bat.financial.web.depositdetail;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.financial.web.annotation.SearchMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.DepositDetailService;
import com.bat.financial.api.deposit.dto.DepositDetailAdminQry;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.web.base.BaseController;
import com.bat.financial.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 代码拆开方便生产权限数据
 * @date: 2018/5/27 14:20
 */
@Api(tags = "预存款明细-明细账后台接口", value = "AdminDepositDetailController")
@Slf4j
@RestController
@RequestMapping("/financial/v1/web/admin/deposit")
public class AdminDepositDetailController extends BaseController {

    @Resource
    private DepositDetailService depositDetailService;

    @GetMapping("/detail/list")
    @ApiOperation(value = "预存款明细-明细账")
    @SearchMethod
    public Response<PageInfo<DepositDistributorSubsidiaryBookDTO>> listDepositDetail(@Valid DepositDetailAdminQry qry) {
        return Response.of(depositDetailService.listDepositDetail(qry));
    }

}
