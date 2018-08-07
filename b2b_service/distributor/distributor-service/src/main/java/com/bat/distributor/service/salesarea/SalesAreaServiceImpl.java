package com.bat.distributor.service.salesarea;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.distributor.api.salesarea.SalesAreaServiceI;
import com.bat.distributor.api.salesarea.dto.SalesAreaCmd;
import com.bat.distributor.api.salesarea.dto.SalesAreaId;
import com.bat.distributor.api.salesarea.dto.SalesAreaListQry;
import com.bat.distributor.api.salesarea.dto.SalesAreaOpenCmd;
import com.bat.distributor.api.salesarea.dto.data.SalesAreaDTO;
import com.bat.distributor.service.salesarea.executor.SalesAreaCmdExe;
import com.bat.distributor.service.salesarea.executor.SalesAreaQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

@Service
public class SalesAreaServiceImpl implements SalesAreaServiceI {

    @Resource
    private SalesAreaCmdExe cmdExe;

    @Resource
    private SalesAreaQryExe qryExe;

    @Override
    public void createSalesArea(SalesAreaCmd cmd) {
        cmdExe.createSalesArea(cmd);
    }

    @Override
    public void updateSalesArea(SalesAreaCmd cmd) {
        cmdExe.updateSalesArea(cmd);
    }

    @Override
    public void openSalesArea(SalesAreaOpenCmd cmd) {
        cmdExe.openSalesArea(cmd);
    }

    @Override
    public PageInfo<SalesAreaDTO> listSalesArea(SalesAreaListQry qry) {
        return qryExe.executeList(qry);
    }

    @Override
    public void deleteSalesArea(SalesAreaId cmd) {
        cmdExe.deleteSalesArea(cmd);
    }

    @Override
    public SalesAreaDTO getSalesArea(SalesAreaId qry) {
        return qryExe.execute(qry);
    }

}