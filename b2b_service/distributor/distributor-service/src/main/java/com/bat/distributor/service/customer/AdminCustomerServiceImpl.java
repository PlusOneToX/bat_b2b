package com.bat.distributor.service.customer;

import javax.annotation.Resource;

import com.bat.distributor.api.customer.AdminCustomerServiceI;
import com.bat.distributor.api.customer.dto.CustomerListQry;
import com.bat.distributor.api.customer.dto.CustomerStatusCmd;
import com.bat.distributor.api.customer.dto.data.CustomerListDTO;
import com.bat.distributor.service.customer.executor.CustomerCmdExe;
import com.bat.distributor.service.customer.executor.CustomerQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/9 19:47
 */
@Service
public class AdminCustomerServiceImpl implements AdminCustomerServiceI {

    @Resource
    private CustomerQryExe qryExe;

    @Resource
    private CustomerCmdExe cmdExe;

    @Override
    public PageInfo<CustomerListDTO> listCustomer(CustomerListQry qry) {
        return qryExe.listCustomer(qry);
    }

    @Override
    public void statusCustomer(CustomerStatusCmd cmd) {
        cmdExe.statusCustomer(cmd);
    }
}
