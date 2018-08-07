package com.bat.financial.deposit.executor;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.financial.deposit.convertor.DepositConvertor;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.financial.api.deposit.dto.data.DepositDistributorSubsidiaryBookDTO;
import com.bat.financial.dao.deposit.DepositDistributorSubsidiaryBookMapper;
import com.bat.financial.dao.deposit.dataobject.DepositDistributorSubsidiaryBookDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 余额明细
 * @date: 2018/5/27 15:01
 */
@Component
@Slf4j
public class DepositDetailCmdExc {

    @Resource
    private DepositDistributorSubsidiaryBookMapper depositDistributorSubsidiaryBookMapper;

    public void insertBatch(List<DepositDistributorSubsidiaryBookDO> bookDOS) {
        depositDistributorSubsidiaryBookMapper.insertBatch(bookDOS);
    }

    public void createDepositDetail(DepositDistributorSubsidiaryBookDTO aDo) {
        depositDistributorSubsidiaryBookMapper.insert(DepositConvertor.toDepositDistributorSubsidiaryBookDO(aDo));
    }

    public List<DepositDistributorSubsidiaryBookDO> getDepositDetail(DepositDistributorSubsidiaryBookDO aDo) {
        Map<String, Object> map = JSONObject.parseObject(JSON.toJSONString(aDo));
        return depositDistributorSubsidiaryBookMapper.selectByParams(map);
    }

}
