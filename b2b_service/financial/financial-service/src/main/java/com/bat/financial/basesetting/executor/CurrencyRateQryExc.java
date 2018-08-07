package com.bat.financial.basesetting.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.financial.basesetting.convertor.CurrencyRateConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.dto.CurrencyRateListQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateQry;
import com.bat.financial.api.basesetting.dto.data.CurrencyRateDTO;
import com.bat.financial.dao.basesetting.CurrencyRateMapper;
import com.bat.financial.dao.basesetting.dataobject.CurrencyRateDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 14:02
 */
@Component
@Slf4j
public class CurrencyRateQryExc {

    @Resource
    private CurrencyRateMapper currencyRateMapper;

    public CurrencyRateDTO getCurrencyRateById(Integer id) {
        CurrencyRateDO aDo = currencyRateMapper.selectByPrimaryKey(id);
        return aDo == null ? null : CurrencyRateConvertor.toCurrencyRateDTO(aDo);
    }

    public PageInfo<CurrencyRateDTO> listCurrencyRate(CurrencyRateListQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CurrencyRateDO> currencyDOS = currencyRateMapper.listAll();
        PageInfo pageInfo = new PageInfo(currencyDOS);
        List<CurrencyRateDTO> toCurrencyRateDTOList = CurrencyRateConvertor.toCurrencyRateDTOList(pageInfo.getList());
        pageInfo.setList(toCurrencyRateDTOList);
        return pageInfo;
    }

    public CurrencyRateDTO getCurrencyRateByForToCurrencyCode(String forCurrencyCode, String toCurrencyCode) {
        CurrencyRateDO aDo = currencyRateMapper.getCurrencyRateByForToCurrencyCode(forCurrencyCode, toCurrencyCode);
        return aDo == null ? null : CurrencyRateConvertor.toCurrencyRateDTO(aDo);
    }

    public CurrencyRateDTO getCurrencyRate(CurrencyRateQry qry) {
        BeanMap map = BeanMap.create(qry);
        List<CurrencyRateDO> currencyRateDOS = currencyRateMapper.selectByParams(map);
        if (!CollectionUtils.isEmpty(currencyRateDOS) && currencyRateDOS.size() == 1) {
            return CurrencyRateConvertor.toCurrencyRateDTO(currencyRateDOS.get(0));
        }
        return null;
    }
}
