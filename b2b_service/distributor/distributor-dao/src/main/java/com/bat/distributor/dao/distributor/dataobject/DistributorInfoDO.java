package com.bat.distributor.dao.distributor.dataobject;

import lombok.Data;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/5/8 8:48
 */
@Data
public class DistributorInfoDO {
    private Integer id;
    private String name;
    private String companyName;
    /**
     * ERP余额是否同步 1.是 0.否
     */
    private Short erpBalanceFlag;
}
