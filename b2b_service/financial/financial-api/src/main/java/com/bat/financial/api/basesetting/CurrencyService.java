package com.bat.financial.api.basesetting;

import com.github.pagehelper.PageInfo;
import com.bat.financial.api.basesetting.dto.CurrencyCreateCmd;
import com.bat.financial.api.basesetting.dto.CurrencyQry;
import com.bat.financial.api.basesetting.dto.CurrencyUpdateCmd;
import com.bat.financial.api.basesetting.dto.data.CurrencyDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/17 11:04
 */
public interface CurrencyService {

    /**
     * 根据id获取币别
     *
     * @param id
     * @return
     */
    CurrencyDTO getCurrencyById(Integer id);

    /**
     * 获取币别列表
     * 
     * @param qry
     * @return
     */
    PageInfo<CurrencyDTO> listCurrency(CurrencyQry qry);

    /**
     * 与ERP同步币别
     * 
     * @return
     */
    boolean syncCurrency();

    /**
     * 创建币别
     * 
     * @param cmd
     */
    boolean createCurrency(CurrencyCreateCmd cmd);

    /**
     * 更新币别
     *
     * @param cmd
     */
    boolean updateCurrency(CurrencyUpdateCmd cmd);

    /**
     * 根据id删除币别
     * 
     * @param id
     * @return
     */
    boolean deleteCurrencyById(Integer id);

}
