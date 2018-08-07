package com.bat.financial.basesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.dto.CurrencyQry;
import com.bat.financial.api.basesetting.dto.data.CurrencyDTO;
import com.bat.financial.basesetting.convertor.CurrencyConvertor;
import com.bat.financial.dao.basesetting.CurrencyMapper;
import com.bat.financial.dao.basesetting.dataobject.CurrencyDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class CurrencyQryExc {

    @Resource
    private CurrencyMapper currencyMapper;

    public CurrencyDTO getCurrencyById(Integer id) {
        CurrencyDO aDo = currencyMapper.selectByPrimaryKey(id);
        return aDo == null ? null : CurrencyConvertor.toCurrencyDTO(aDo);
    }

    public PageInfo<CurrencyDTO> listCurrency(CurrencyQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CurrencyDO> currencyDOS = currencyMapper.listAll();
        PageInfo pageInfo = new PageInfo(currencyDOS);
        List<CurrencyDTO> toCurrencyDTOList = CurrencyConvertor.toCurrencyDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyDTOList);
        return pageInfo;
    }

    public CurrencyDTO getCurrencyByCurrencyCode(String currencyCode) {
        CurrencyDO aDo = currencyMapper.getByCurrencyCode(currencyCode);
        return CurrencyConvertor.toCurrencyDTO(aDo);
    }

    public CurrencyDTO getCurrencyByErpNo(String erpNo) {
        CurrencyDO aDo = currencyMapper.getCurrencyByErpNo(erpNo);
        return CurrencyConvertor.toCurrencyDTO(aDo);
    }
}
