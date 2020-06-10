package com.bat.flexible.manager.exchange.executor;

import com.bat.flexible.api.exchange.dto.ExchangeExportCmd;
import com.bat.flexible.dao.exchange.ExchangeExportCodeMapper;
import com.bat.flexible.dao.exchange.ExchangeExportMapper;
import com.bat.flexible.dao.exchange.dataobject.ExchangeExportCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeExportDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ExchangeExportCmdExe {

    @Autowired
    private ExchangeExportMapper exchangeExportMapper;

    @Autowired
    private ExchangeExportCodeMapper exchangeExportCodeMapper;

    public void add(ExchangeExportCmd exchangeExportCmd) {
        ExchangeExportDO exchangeExportDO = new ExchangeExportDO();
        BeanUtils.copyProperties(exchangeExportCmd, exchangeExportDO);
        exchangeExportDO.setExamineFlag(FlexibleCommonConstant.FLAG_NO);
        exchangeExportDO.setCreateTime(new Date());
        exchangeExportMapper.insertSelective(exchangeExportDO);
    }

    public void update(ExchangeExportDO exchangeExportDO) {
        exchangeExportDO.setUpdateTime(new Date());
        exchangeExportMapper.updateByPrimaryKeySelective(exchangeExportDO);
    }

    public void delete(Integer id) {
        exchangeExportMapper.deleteByPrimaryKey(id);
    }

    public void addExportCodes(List<ExchangeExportCodeDO> exchangeExportCodeDOS) {
        exchangeExportCodeMapper.insertList(exchangeExportCodeDOS);
    }
}
