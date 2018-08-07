package com.bat.distributor.api.customer;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.customer.dto.CustomerListQry;
import com.bat.distributor.api.customer.dto.CustomerStatusCmd;
import com.bat.distributor.api.customer.dto.data.CustomerListDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/9 17:34
 */
public interface AdminCustomerServiceI {
    /**
     * 根据搜索条件查询C端客户列表
     * 
     * @param qry
     * @return
     */
    PageInfo<CustomerListDTO> listCustomer(CustomerListQry qry);

    /**
     * 根据id冻结解冻C端客户
     * 
     * @param cmd
     */
    void statusCustomer(CustomerStatusCmd cmd);
}
