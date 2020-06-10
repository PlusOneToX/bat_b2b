package com.bat.flexible.manager.exchange.convertor;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.exchange.dto.ExchangeCardCmd;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCardDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeCodeDO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeEntityRuleDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeCodeConstant;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.manager.common.utils.code.AESUtil;
import com.bat.flexible.manager.common.utils.code.NumUtils;

import java.util.Date;

public class ExchangeCardConvertor {

    public static void updateByStatusInit(ExchangeCardDO exchangeCardDO, ExchangeCardCmd exchangeCardCmd){
        if(exchangeCardDO.getStatus() - ExchangeConstant.StatusInit==0){
            exchangeCardDO.setCodeName(exchangeCardCmd.getCodeName());
            exchangeCardDO.setCodeDesc(exchangeCardCmd.getCodeDesc());
            exchangeCardDO.setType(exchangeCardCmd.getType());
            exchangeCardDO.setSource(exchangeCardCmd.getSource());
            exchangeCardDO.setCodeQuantity(exchangeCardCmd.getCodeQuantity());
            exchangeCardDO.setLimitQuantity(exchangeCardCmd.getLimitQuantity());
            exchangeCardDO.setStartTime(exchangeCardCmd.getStartTime());
            exchangeCardDO.setEndTime(exchangeCardCmd.getEndTime());
            exchangeCardDO.setExchangeWay(exchangeCardCmd.getExchangeWay());
            exchangeCardDO.setOrderUseThreshold(exchangeCardCmd.getOrderUseThreshold());
            exchangeCardDO.setGoodsScope(exchangeCardCmd.getGoodsScope());
            exchangeCardDO.setIsEntity(exchangeCardCmd.getIsEntity());
            exchangeCardDO.setIsUseMall(exchangeCardCmd.getIsUseMall());
            exchangeCardDO.setMallType(exchangeCardCmd.getMallType());
            exchangeCardDO.setItemId(exchangeCardCmd.getItemId());
            exchangeCardDO.setModelNo(exchangeCardCmd.getModelNo());
            exchangeCardDO.setHeadImg(exchangeCardCmd.getHeadImg());
        }
    }

    /**
     * 生成兑换码
     * @param exchangeCard
     * @param currentAdmin
     * @param riseNum
     * @param num
     * @param exchangeFactoryId
     * @param exchangeEntityRule
     * @return
     */
    public static ExchangeCodeDO createExchangeCode(ExchangeCardDO exchangeCard, AdminResponse currentAdmin, String riseNum, String num, Integer exchangeFactoryId,
                                                    ExchangeEntityRuleDO exchangeEntityRule) {
        ExchangeCodeDO exchangeCodeDO = new ExchangeCodeDO();
        exchangeCodeDO.setExchangeId(exchangeCard.getId());
        exchangeCodeDO.setPlainCode(riseNum+num);
        //8到12位
        exchangeCodeDO.setSecretCode(AESUtil.encrypt(NumUtils.getRandomNum()));
        exchangeCodeDO.setStatus(ExchangeCodeConstant.StatusInit);
        exchangeCodeDO.setExchangeFactoryId(exchangeFactoryId);
        setExchangeCodeOperateMsg(currentAdmin, exchangeCodeDO);
       /* if(exchangeEntityRule !=null && exchangeEntityRule.getIsSyncFactory() - ExchangeConstant.isSyncFactoryNo==0 && exchangeEntityRule.getBoxNum()-1==0){
            //不同步工厂、且每盒一个、明码作为盒码
            exchangeCode.setBoxCode(exchangeCode.getPlainCode());
        }*/
        return exchangeCodeDO;
        //exchangeCodeCmdExe.create(exchangeCodeDO);
    }

    public static void setExchangeCodeOperateMsg(AdminResponse currentAdmin, ExchangeCodeDO exchangeCodeDO) {
        if(exchangeCodeDO.getId() ==null){
            exchangeCodeDO.setCreateTime(new Date());
            exchangeCodeDO.setCreateUserId(currentAdmin.getId());
            exchangeCodeDO.setCreateUserName(currentAdmin.getUserName());
        }
        exchangeCodeDO.setUpdateTime(new Date());
        exchangeCodeDO.setUpdateUserId(currentAdmin.getId());
        exchangeCodeDO.setUpdateUserName(currentAdmin.getUserName());
    }

}
