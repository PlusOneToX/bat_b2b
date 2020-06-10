package com.bat.flexible.manager.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeDistributorRelevanceCmdExe;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeDistributorRelevanceQryExe;
import com.bat.flexible.api.exchange.ExchangeDistributorRelevanceServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeDistributorRelevanceDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ExchangeDistributorRelevanceServiceImpl implements ExchangeDistributorRelevanceServiceI {

    @Autowired
    private ExchangeDistributorRelevanceQryExe exchangeDistributorRelevanceQryExe;

    @Autowired
    private ExchangeDistributorRelevanceCmdExe exchangeDistributorRelevanceCmdExe;

    /**
     * 设置关联关系
     * @param isAdd 是否新增
     * @param distributorScope 兑换卡的分销商范围
     * @param distributorList 分销商关联关系
     * @param exchangeId 兑换卡活动id
     */
    @Transactional
    @Override
    public void saveRelevance(boolean isAdd, Short distributorScope, List<DistributorSimpleRelaQry> distributorList, Integer exchangeId, AdminResponse currentAdmin) {
        if(isAdd && ExchangeConstant.EXCHANGE_DISTRIBUTOR_SCOPE_ALL.equals(distributorScope)){
            //新增且分销商全部可用、直接返回
            return;
        }
        //处理修改
        dealWithByUpdate(isAdd,distributorScope,distributorList,exchangeId);
        if(distributorList !=null && distributorList.size()>0){
            distributorList.stream().forEach(distributorSimpleRelaQry -> {
                //新增
                ExchangeDistributorRelevanceDO distributorRelevanceDO = new ExchangeDistributorRelevanceDO();
                distributorRelevanceDO.setCompanyName(distributorSimpleRelaQry.getDistributorCompanyName());
                distributorRelevanceDO.setDistributorId(distributorSimpleRelaQry.getDistributorId());
                distributorRelevanceDO.setDistributorName(distributorSimpleRelaQry.getDistributorName());
                distributorRelevanceDO.setCreateTime(new Date());
                distributorRelevanceDO.setCreateUserId(currentAdmin.getId());
                distributorRelevanceDO.setCreateUserName(currentAdmin.getUserName());
                distributorRelevanceDO.setExchangeId(exchangeId);
                exchangeDistributorRelevanceCmdExe.create(distributorRelevanceDO);
            });
        }
    }

    @Override
    public List<ExchangeDistributorRelevanceDO> listByExchangeIdAndDistributorId(Integer exchangeId,Integer distributorId) {
        return exchangeDistributorRelevanceQryExe.listByExchangeIdAndDistributorId(exchangeId,distributorId);
    }

    private void dealWithByUpdate(boolean isAdd, Short distributorScope, List<DistributorSimpleRelaQry> distributorList, Integer exchangeId) {
        if(isAdd){
            return;
        }
        List<ExchangeDistributorRelevanceDO> list = exchangeDistributorRelevanceQryExe.listByExchangeIdAndDistributorId(exchangeId,null);
        if(list !=null && list.size()>0){
            //之前的指定分销商、判断修改之后分销商是否被删除了
            if(distributorList !=null && distributorList.size()>0){
                //修改后变成了指定
                for(int x=0;x<list.size();x++){
                    for(int y=0;y<distributorList.size();y++){
                        if(list.get(x).getDistributorId() - distributorList.get(y).getDistributorId() ==0){
                            //分销商还在
                            list.remove(x);
                            distributorList.remove(y);
                            x--;
                            y--;
                            break;
                        }
                    }
                }
            }
            if(list !=null && list.size()==0){
                //列表不为空表示修改之后分销商被剔除了关联关系
                list.stream().forEach(exchangeDistributorRelevanceDO -> {
                    exchangeDistributorRelevanceCmdExe.delete(exchangeDistributorRelevanceDO.getId());
                });
            }
        }
    }
}
