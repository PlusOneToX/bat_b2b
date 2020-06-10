package com.bat.flexible.manager.exchange;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.manager.exchange.executor.ExchangeEntityRuleCmdExe;
import com.bat.flexible.manager.exchange.executor.ExchangeEntityRuleQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.ExchangeEntityRuleServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeBoxCodeRequest;
import com.bat.flexible.api.exchange.dto.ExchangeCardCmd;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeEntityRuleDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeEntityRuleServiceImpl implements ExchangeEntityRuleServiceI {



    @Autowired
    private ExchangeEntityRuleQryExe exchangeEntityRuleQryExe;

    @Autowired
    private ExchangeEntityRuleCmdExe exchangeEntityRuleCmdExe;

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;



    @Transactional
    @Override
    public void syncErp(Integer exchangeId, ExchangeEntityRuleDO exchangeEntityRuleDO) {
        if(exchangeEntityRuleDO ==null){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_ENTITY_RULE_NULL);
        }
        if(exchangeEntityRuleDO.getBoxNum()>1){
            return;
        }
        List<ExchangeCodeDO> codeList = exchangeCodeServiceI.listByExchangeId(exchangeId);
        if(codeList ==null || codeList.size()==0){
            return;
        }
        List<ExchangeBoxCodeRequest> list = new ArrayList<>();
        List<ExchangeCodeDO> updateList = new ArrayList<>();
        for(int x=0;x<codeList.size();x++){
            ExchangeCodeDO exchangeCodeDO = codeList.get(x);

            if(exchangeEntityRuleDO !=null && exchangeEntityRuleDO.getIsSyncFactory() - ExchangeConstant.isSyncFactoryNo==0 && exchangeEntityRuleDO.getBoxNum()-1==0
                    && exchangeCodeDO.getStatus()- ExchangeCodeConstant.StatusInit==0 && StringUtils.isBlank(exchangeCodeDO.getBoxCode())
            ){
                //不同步工厂、且每盒一个、明码作为盒码
                ExchangeBoxCodeRequest codeRequest = new ExchangeBoxCodeRequest();
                codeRequest.setBoxCode(exchangeCodeDO.getPlainCode());
                codeRequest.setPlainCode(exchangeCodeDO.getPlainCode());
                codeRequest.setExchangeFactoryId(exchangeCodeDO.getExchangeFactoryId());
                codeRequest.setExchangeCodeId(exchangeCodeDO.getId());
                list.add(codeRequest);
                exchangeCodeDO.setBoxCode(exchangeCodeDO.getPlainCode());
                updateList.add(exchangeCodeDO);
            }
        }
        if(list ==null || list.size()==0){
            //无符合条件明码同步到ERP
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_SYNC_ERP_FAIL_BY_NONE);
        }
        //批量修改
        exchangeCodeServiceI.batchUpdate(updateList);
        exchangeCodeServiceI.setBoxCode(list,exchangeId);
    }

    @Override
    public ExchangeEntityRuleDO getByExchangeId(Integer exchangeId) {
        return exchangeEntityRuleQryExe.getByExchangeId(exchangeId);
    }

    @Override
    public void create(ExchangeEntityRuleDO rule) {
        exchangeEntityRuleCmdExe.create(rule);
    }

    /**
     * 设置是否生成实物卡
     * @param exchangeCardRequest
     * @param exchangeId
     * @param currentAdmin
     * @param isNew
     */
    @Override
    @Transactional
    public void setEntityRule(ExchangeCardCmd exchangeCardRequest, Integer exchangeId, AdminResponse currentAdmin, boolean isNew) {
        if(exchangeCardRequest.getIsEntity() - ExchangeConstant.IsEntityNo ==0){
            return;
        }
        ExchangeEntityRuleDO rule = null;
        if (!isNew){
            rule = exchangeEntityRuleQryExe.getByExchangeId(exchangeId);
        }
        if(rule ==null){
            rule= new ExchangeEntityRuleDO();
        }
        rule.setCardType(exchangeCardRequest.getCardType());
        rule.setRuleType(exchangeCardRequest.getRuleType());
        rule.setRiseValue(exchangeCardRequest.getRiseValue());
        rule.setFloatValue(exchangeCardRequest.getFloatValue());
        rule.setExchangeId(exchangeId);
        rule.setRandomValue(exchangeCardRequest.getRandomValue());
        rule.setIsSyncFactory(exchangeCardRequest.getIsSyncFactory());
        rule.setBoxNum(exchangeCardRequest.getBoxNum());
        setEntityRuleAdminMsg(rule,currentAdmin);
        if(rule.getId()==null){
            exchangeEntityRuleCmdExe.create(rule);
        }else{
            exchangeEntityRuleCmdExe.update(rule);
        }
    }

    private void setEntityRuleAdminMsg(ExchangeEntityRuleDO rule, AdminResponse currentAdmin) {
        if(rule.getId() ==null){
            rule.setCreateTime(new Date());
            rule.setCreateUserId(currentAdmin.getId());
            rule.setCreateUserName(currentAdmin.getUserName());
        }
        rule.setUpdateTime(new Date());
        rule.setUpdateUserId(currentAdmin.getId());
        rule.setUpdateUserName(currentAdmin.getUserName());
    }
}
