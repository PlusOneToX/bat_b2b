package com.bat.thirdparty.quanyi.service.executor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.quanyi.dao.ThirdQuanyiLogMapper;
import com.bat.thirdparty.quanyi.dao.ThirdQuanyiMapper;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiLogDO;

@Component
public class ThirdQuanyiCmdExe {

    @Autowired
    private ThirdQuanyiMapper thirdQuanyiMapper;

    @Autowired
    private ThirdQuanyiLogMapper thirdQuanyiLogMapper;

    public void insertSelective(ThirdQuanyiDO thirdQuanyiDO) {
        thirdQuanyiMapper.insertSelective(thirdQuanyiDO);
    }

    public void insertSelectiveLog(ThirdQuanyiLogDO thirdQuanyiLogDO) {
        thirdQuanyiLogMapper.insertSelective(thirdQuanyiLogDO);
    }

    public void updateByPrimaryKeySelective(ThirdQuanyiDO thirdQuanyiDO) {
        thirdQuanyiDO.setUpdateTime(new Date());
        thirdQuanyiMapper.updateByPrimaryKeySelective(thirdQuanyiDO);
    }

    public ThirdQuanyiDO getThirdQuanyiDOByThirdCode(Integer distributorId, String thirdCode) {
        return thirdQuanyiMapper.findByDistributorIdAndThirdCode(distributorId, thirdCode);
    }

}
