package com.bat.goods.service.scaleprice;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.goods.service.scaleprice.executor.ScalePriceCmdExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.scaleprice.ScalePriceServiceI;
import com.bat.goods.api.scaleprice.dto.ScalePriceCmd;
import com.bat.goods.api.scaleprice.dto.ScalePriceId;
import com.bat.goods.api.scaleprice.dto.ScalePriceListQry;
import com.bat.goods.api.scaleprice.dto.ScalePriceOpenCmd;
import com.bat.goods.api.scaleprice.dto.data.ScalePriceDTO;
import com.bat.goods.service.scaleprice.executor.ScalePriceQryExe;

@Service
public class ScalePriceServiceImpl implements ScalePriceServiceI {

    @Resource
    private ScalePriceCmdExe scalePriceCmdExe;

    @Resource
    private ScalePriceQryExe scalePriceQryExe;

    @Override
    public void createScalePrice(ScalePriceCmd cmd) {
        scalePriceCmdExe.createScalePrice(cmd);
    }

    @Override
    public void updateScalePrice(ScalePriceCmd cmd) {
        scalePriceCmdExe.updateScalePrice(cmd);
    }

    @Override
    public void openScalePrice(ScalePriceOpenCmd cmd) {
        scalePriceCmdExe.openScalePrice(cmd);
    }

    @Override
    public PageInfo<ScalePriceDTO> listScalePrice(ScalePriceListQry qry) {
        return scalePriceQryExe.executeList(qry);
    }

    @Override
    public void deleteScalePrice(ScalePriceId cmd) {
        scalePriceCmdExe.deleteScalePrice(cmd);
    }

    @Override
    public ScalePriceDTO getScalePrice(ScalePriceId scalePriceId) {
        return scalePriceQryExe.execute(scalePriceId);
    }

}