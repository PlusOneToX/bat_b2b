package com.bat.flexible.api.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.exchange.dto.ExchangeCardCmd;
import com.bat.flexible.dao.exchange.dataobject.ExchangeEntityRuleDO;

public interface ExchangeEntityRuleServiceI {

    void syncErp(Integer exchangeId, ExchangeEntityRuleDO exchangeEntityRuleDO);

    ExchangeEntityRuleDO getByExchangeId(Integer exchangeId);

    /**
     * 新增
     * @param rule
     */
    void create(ExchangeEntityRuleDO rule);

    void setEntityRule(ExchangeCardCmd exchangeCardCmd, Integer id, AdminResponse currentAdmin, boolean isAdd);
}
