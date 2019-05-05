package com.bat.thirdparty.distributor.service;

import javax.annotation.Resource;

import com.bat.thirdparty.distributor.executor.SamsungExe;
import org.springframework.stereotype.Service;

import com.bat.thirdparty.distributor.api.SamsungServiceI;
import com.bat.thirdparty.distributor.api.dto.SamsungAccountQry;
import com.bat.thirdparty.distributor.api.dto.SamsungCheckTokenQry;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 11:53
 */
@Service
public class SamsungServiceImpl implements SamsungServiceI {

    @Resource
    private SamsungExe samsungExe;

    @Override
    public Short couponQualified(SamsungAccountQry qry) {
        return samsungExe.couponQualified(qry);
    }

    @Override
    public String checkToken(SamsungCheckTokenQry qry) {
        return samsungExe.checkToken(qry);
    }

    @Override
    public Short couponReceived(SamsungAccountQry qry) {
        return samsungExe.couponReceived(qry);
    }
}
