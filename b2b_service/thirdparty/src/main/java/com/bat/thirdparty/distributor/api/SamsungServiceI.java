package com.bat.thirdparty.distributor.api;

import com.bat.thirdparty.distributor.api.dto.SamsungAccountQry;
import com.bat.thirdparty.distributor.api.dto.SamsungCheckTokenQry;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 11:28
 */
public interface SamsungServiceI {
    /**
     * 账号判断是否资质领券
     *
     * @param qry
     * @return
     */
    Short couponQualified(SamsungAccountQry qry);

    /**
     * token验证
     * 
     * @param qry
     * @return
     */
    String checkToken(SamsungCheckTokenQry qry);

    /**
     * 侧判断是否已领券
     * @param qry
     * @return
     */
    Short couponReceived(SamsungAccountQry qry);
}
