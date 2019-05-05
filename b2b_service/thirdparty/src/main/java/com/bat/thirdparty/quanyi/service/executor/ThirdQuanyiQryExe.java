package com.bat.thirdparty.quanyi.service.executor;

import com.bat.thirdparty.quanyi.dao.ThirdQuanyiLogMapper;
import com.bat.thirdparty.quanyi.dao.ThirdQuanyiMapper;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThirdQuanyiQryExe {


    @Autowired
    private ThirdQuanyiMapper thirdQuanyiMapper;

    @Autowired
    private ThirdQuanyiLogMapper thirdQuanyiLogMapper;

    /**
     * 根据id获取权益
     * @param thirdQuanyiId
     * @return
     */
    public ThirdQuanyiDO findById(Integer thirdQuanyiId) {
       return thirdQuanyiMapper.selectByPrimaryKey(thirdQuanyiId);
    }

    public ThirdQuanyiDO findByDistributorIdAndThirdCode(Integer distributorId, String thirdCode) {
        return thirdQuanyiMapper.findByDistributorIdAndThirdCode(distributorId, thirdCode);
    }

    public List<ThirdQuanyiDO> listByCondition(Integer distributorId, String distributorName, String secretCode, String thirdCode, String thirdPhone,Short status) {
        return thirdQuanyiMapper.listByCondition(distributorId, distributorName,secretCode,thirdCode,thirdPhone,status);
    }

    public List<ThirdQuanyiLogDO> logByThirdQuanyiId(Integer thirdQuanyiId) {
        return thirdQuanyiLogMapper.listByThirdQuanyiId(thirdQuanyiId);
    }

    public ThirdQuanyiDO findByExchangeCodeId(Integer exchangeCodeId) {
        return thirdQuanyiMapper.findByExchangeCodeId(exchangeCodeId);
    }

    public List<ThirdQuanyiDO> findByExchangeCodeIds(List<Integer> exchangeCodeIds) {
        return thirdQuanyiMapper.findByExchangeCodeIds(exchangeCodeIds);
    }
}
