package com.bat.distributor.web.customer;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.customer.AdminCustomerServiceI;
import com.bat.distributor.api.customer.dto.CustomerListQry;
import com.bat.distributor.api.customer.dto.CustomerStatusCmd;
import com.bat.distributor.api.customer.dto.data.CustomerListDTO;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/7 8:51
 */
@Api(tags = "C端客户后台接口", value = "AdminCustomerController")
@RestController
@RequestMapping("/distributor/v1/web/admin/customer")
public class AdminCustomerController {
    private static Logger logger = LoggerFactory.getLogger(AdminCustomerController.class);

    @Resource
    private AdminCustomerServiceI service;

    @ApiOperation(value = "根据搜索条件查询C端客户列表")
    @GetMapping(value = "/list")
    public Response<PageInfo<CustomerListDTO>> listCustomer(@Valid CustomerListQry qry) {
        PageInfo<CustomerListDTO> pageInfo = service.listCustomer(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminCustomer, value = CommonLogTypeConstantDTO.AdminCustomerStatus)
    @ApiOperation(value = "根据id冻结解冻C端客户")
    @PutMapping(value = "/status")
    public Response statusCustomer(@RequestBody @Valid CustomerStatusCmd cmd) {
        service.statusCustomer(cmd);
        return Response.buildSuccess();
    }
}
