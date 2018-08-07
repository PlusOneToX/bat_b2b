package com.bat.financial.deposit.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.deposit.convertor.FreezingConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.deposit.dto.FreezableDistributorQry;
import com.bat.financial.api.deposit.dto.FreezableQry;
import com.bat.financial.api.deposit.dto.data.DepositDistributorFreezingDTO;
import com.bat.financial.common.CommonServiceImpl;
import com.bat.financial.dao.deposit.DepositDistributorFreezingMapper;
import com.bat.financial.dao.deposit.DepositDistributorMapper;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorFreezingDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/29 14:01
 */
@Component
@Slf4j
public class FreezingQryExc {

    @Resource
    private DepositDistributorMapper distributorMapper;

    @Resource
    private DepositDistributorFreezingMapper freezingMapper;

    @Resource
    private CommonServiceImpl commonService;

    public void listFreezableDistributor(FreezableDistributorQry qry, List<Integer> distributorId) {
        // PageHelper.startPage(qry.getPage(), qry.getSize());
        // BeanMap map = BeanMap.create(qry);
        // map.put("distributorIds", distributorId);
        // List<DepositDistributorDO> dos = distributorMapper.selectByParams(map);
        // PageInfo pageInfo = new PageInfo(dos);
    }

    public PageInfo<DepositDistributorFreezingDTO> listFreezingByDistributorId(FreezableQry qry) {
        List<Integer> distributorIds = commonService.getDistributorIds(qry.getUserId());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("distributorIds", distributorIds);
        List<DepositDistributorFreezingDO> dos = freezingMapper.selectByParams(map);
        PageInfo pageInfo = new PageInfo(dos);
        List<DepositDistributorFreezingDTO> depositDistributorFreezingDTOS =
            FreezingConvertor.toDepositDistributorFreezingDOList(pageInfo.getList());
        pageInfo.setList(depositDistributorFreezingDTOS);
        return pageInfo;
    }
}
