package com.bat.flexible.manager.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeMaterialRelevanceCmdExe;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeMaterialRelevanceQryExe;
import com.bat.flexible.manager.material.convertor.MaterialConvertor;
import com.bat.flexible.api.exchange.ExchangeMaterialRelevanceServiceI;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ExchangeMaterialRelevanceServiceImpl implements ExchangeMaterialRelevanceServiceI {


    @Autowired
    private ExchangeMaterialRelevanceQryExe exchangeMaterialRelevanceQryExe;

    @Autowired
    private ExchangeMaterialRelevanceCmdExe exchangeMaterialRelevanceCmdExe;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private MaterialConvertor materialConvertor;

    @Override
    @Transactional
    public void saveRelevance(Boolean isAdd, Integer exchangeId, List<Integer> materialIdList, AdminResponse adminResponse) {
        dealWithByUpdate(isAdd,exchangeId,materialIdList,adminResponse);
        if(materialIdList !=null && materialIdList.size()>0){
            materialIdList.stream().forEach(materialId -> {
                createNew(exchangeId, adminResponse, materialId);
            });
        }else{
            if(isAdd){
                //新增全部使用
                createNew(exchangeId,adminResponse,null);
            }
        }
    }

    @Override
    public List<MaterialRelaSimpleDTO> listDTOByExchangeId(Integer exchangeId) {
        List<ExchangeMaterialRelevanceDO> relevanceDOList = exchangeMaterialRelevanceQryExe.listByExchangeId(exchangeId);
        return materialConvertor.toMaterialRelaSimpleDTOListFromExchange(relevanceDOList);
    }

    @Override
    public List<ExchangeMaterialRelevanceDO> listByExchangeId(Integer exchangeId) {
        return exchangeMaterialRelevanceQryExe.listByExchangeId(exchangeId);
    }

    @Override
    public void create(ExchangeMaterialRelevanceDO rela) {
        exchangeMaterialRelevanceCmdExe.create(rela);
    }

    @Override
    public void deleteById(Integer id) {
        exchangeMaterialRelevanceCmdExe.deleteById(id);
    }

    @Override
    public ExchangeMaterialRelevanceDO findByExchangeIdAndMaterialId(Integer exchangeId, Integer materialId) {
        return exchangeMaterialRelevanceQryExe.findByExchangeIdAndMaterialId(exchangeId,materialId);
    }

    private void dealWithByUpdate(Boolean isAdd, Integer exchangeId, List<Integer> materialIdList, AdminResponse adminResponse) {
        if(isAdd){
            return;
        }
        List<ExchangeMaterialRelevanceDO> relevanceDOList = exchangeMaterialRelevanceQryExe.listByExchangeId(exchangeId);
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            //前后都是全部使用
            if( materialIdList==null || materialIdList.size()==0){
                //前后不变
                return;
            }
            //修改后变成部分可用
            Boolean flag =true;
            if(materialIdList ==null || materialIdList.size()==0){
                flag = false;
            }
            for(int x=0;x<relevanceDOList.size();x++){
                //前后都是部分可用
                if( relevanceDOList.get(x).getMaterialId() !=null && materialIdList !=null && materialIdList.size()>0){
                    for(int y=0;y<materialIdList.size();y++){
                        if(relevanceDOList.get(x).getMaterialId() - materialIdList.get(y)==0){
                            relevanceDOList.remove(x);
                            x--;
                            materialIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
            }
            if(relevanceDOList !=null && relevanceDOList.size()>0){
                //删除
                relevanceDOList.stream().forEach(exchangeMaterialRelevanceDO -> {
                    exchangeMaterialRelevanceCmdExe.deleteById(exchangeMaterialRelevanceDO.getId());
                });
            }
            if(!flag && (materialIdList ==null || materialIdList.size()==0)){
                //之前是部分可用、后续改为全部可用
                createNew(exchangeId,adminResponse,null);
            }
        }
    }


    private void createNew(Integer exchangeId, AdminResponse adminResponse, Integer materialId) {
        ExchangeMaterialRelevanceDO relevanceDO = new ExchangeMaterialRelevanceDO();
        relevanceDO.setExchangeId(exchangeId);
        relevanceDO.setMaterialId(materialId);
        relevanceDO.setCreateTime(new Date());
        relevanceDO.setCreateUserId(adminResponse.getId());
        relevanceDO.setCreateUserName(adminResponse.getUserName());
        exchangeMaterialRelevanceCmdExe.create(relevanceDO);
    }
}
