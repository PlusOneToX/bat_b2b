package com.bat.goods.service.brand;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.goods.service.brand.executor.BrandCmdExe;
import com.bat.goods.service.brand.executor.BrandQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.brand.BrandServiceI;
import com.bat.goods.api.brand.dto.BrandCmd;
import com.bat.goods.api.brand.dto.BrandId;
import com.bat.goods.api.brand.dto.BrandListQry;
import com.bat.goods.api.brand.dto.BrandOpenCmd;
import com.bat.goods.api.brand.dto.data.BrandDTO;

@Service
public class BrandServiceImpl implements BrandServiceI {

    @Resource
    private BrandCmdExe brandCmdExe;

    @Resource
    private BrandQryExe brandQryExe;

    @Override
    public void createBrand(BrandCmd cmd) {
        brandCmdExe.createBrand(cmd);
    }

    @Override
    public void updateBrand(BrandCmd brandCmd) {
        brandCmdExe.updateBrand(brandCmd);
    }

    @Override
    public void openBrand(BrandOpenCmd cmd) {
        brandCmdExe.openBrand(cmd);
    }

    @Override
    public PageInfo<BrandDTO> listBrand(BrandListQry brandListQry) {
        return brandQryExe.executeList(brandListQry);
    }

    @Override
    public void deleteBrand(BrandId cmd) {
        brandCmdExe.deleteBrand(cmd);
    }

    @Override
    public BrandDTO getBrand(BrandId brandId) {
        return brandQryExe.execute(brandId);
    }

}