package com.bat.financial.api.basesetting;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.dto.CurrencyRateCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyRateListQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateQry;
import com.bat.financial.api.basesetting.dto.CurrencyRateUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyRateDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 11:04
 */
public interface CurrencyRateService {

    /**
     * 根据id获取汇率
     * 
     * @param id
     * @return
     */
    CurrencyRateDTO getCurrencyRateById(Integer id);

    /**
     * 获取汇率列表
     *
     * @param qry
     * @return
     */
    PageInfo<CurrencyRateDTO> listCurrencyRate(CurrencyRateListQry qry);

    /**
     * 创建汇率
     *
     * @param cmd
     */
    boolean createCurrencyRate(CurrencyRateCreateCmd cmd);

    /**
     * 更新汇率
     *
     * @param cmd
     */
    boolean updateCurrencyRate(CurrencyRateUpdateCmd cmd);

    /**
     * 根据id删除汇率
     *
     * @param id
     * @return
     */
    boolean deleteCurrencyRateById(Integer id);

    /**
     * 与ERP同步币别
     *
     * @return
     */
    boolean syncCurrencyRate();

    /**
     * 获取汇率
     * 
     * @param qry
     * @return
     */
    CurrencyRateDTO getCurrencyRate(CurrencyRateQry qry);
}
