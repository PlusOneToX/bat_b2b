package com.bat.flexible.manager.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeModelRelevanceCmdExe;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangeModelRelevanceQryExe;
import com.bat.flexible.api.exchange.ExchangeModelRelevanceServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangeModelRelevanceDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRelaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeModelRelevanceServiceImpl implements ExchangeModelRelevanceServiceI {


    @Autowired
    private ExchangeModelRelevanceQryExe exchangeModelRelevanceQryExe;

    @Autowired
    private ExchangeModelRelevanceCmdExe exchangeModelRelevanceCmdExe;

    @Autowired
    private ModelServiceI modelServiceI;

    @Override
    @Transactional
    public void saveRelevance(Boolean isAdd, Integer exchangeId, List<Integer> modelIdList, AdminResponse adminResponse) {
        dealWithByUpdate(isAdd,exchangeId,modelIdList,adminResponse);
        if(modelIdList !=null && modelIdList.size()>0){
            modelIdList.stream().forEach(modelId -> {
                createNew(exchangeId, adminResponse, modelId);
            });
        }else{
            if(isAdd){
                //新增全部使用
                createNew(exchangeId,adminResponse,null);
            }
        }
    }

    @Override
    public List<ModelRelaSimpleDTO> listDTOByExchangeId(Integer exchangeId) {
        List<ExchangeModelRelevanceDO>  relevanceDOList = exchangeModelRelevanceQryExe.listByExchangeId(exchangeId);
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            return null;
        }
        List<ModelRelaSimpleDTO> list = new ArrayList<>();
        relevanceDOList.stream().forEach(exchangeModelRelevanceDO -> {
           if(ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_SOME.equals(exchangeModelRelevanceDO.getType())){
               ModelRelaSimpleDTO simpleDTO = new ModelRelaSimpleDTO();
               simpleDTO.setId(exchangeModelRelevanceDO.getId());
               ModelDO modelDO = modelServiceI.getById(exchangeModelRelevanceDO.getModelId());
               simpleDTO.setName(modelDO.getName());
               simpleDTO.setEnglishName(modelDO.getEnglishName());
               simpleDTO.setModelId(modelDO.getId());
               simpleDTO.setModelNo(modelDO.getModelNo());
               if(modelDO.getParentId()==0){
                   simpleDTO.setCategoryName(modelDO.getName());
                   simpleDTO.setCategoryEnglishName(modelDO.getEnglishName());
               }else{
                   ModelDO parentDO = modelServiceI.getById(modelDO.getParentId());
                   simpleDTO.setCategoryName(parentDO.getName());
                   simpleDTO.setCategoryEnglishName(parentDO.getEnglishName());
               }
               list.add(simpleDTO);
           }
        });
        return list;
    }

    @Override
    public List<ExchangeModelRelevanceDO> listByExchangeId(Integer exchangeId) {
        return exchangeModelRelevanceQryExe.listByExchangeId(exchangeId);
    }

    @Transactional
    @Override
    public void create(ExchangeModelRelevanceDO rela) {
        exchangeModelRelevanceCmdExe.create(rela);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        exchangeModelRelevanceCmdExe.deleteById(id);
    }

    @Override
    public ExchangeModelRelevanceDO findOneByExchangeIdAndModelId(Integer exchangeId, Integer modelId) {
        return exchangeModelRelevanceQryExe.findOneByExchangeIdAndModelId(exchangeId,modelId);
    }

    private void dealWithByUpdate(Boolean isAdd, Integer exchangeId, List<Integer> modelIdList, AdminResponse adminResponse) {
        if(isAdd){
            return;
        }
        List<ExchangeModelRelevanceDO> relevanceDOList = exchangeModelRelevanceQryExe.listByExchangeId(exchangeId);
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            //前后都是全部使用
            if((relevanceDOList.get(0).getType()- ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL==0) && (modelIdList==null || modelIdList.size()==0)){
                //前后不变
                return;
            }
            //定义标记
            Boolean flag = true;
            if(modelIdList ==null || modelIdList.size()==0){
                flag = false;
            }
            for(int x=0;x<relevanceDOList.size();x++){
                //前后都是部分可用
                if( relevanceDOList.get(x).getModelId() !=null && modelIdList !=null && modelIdList.size()>0){
                    for(int y=0;y<modelIdList.size();y++){
                        if(relevanceDOList.get(x).getModelId() - modelIdList.get(y)==0){
                            relevanceDOList.remove(x);
                            x--;
                            modelIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
            }
            if(relevanceDOList !=null && relevanceDOList.size()>0){
                //删除
                relevanceDOList.stream().forEach(exchangeMaterialRelevanceDO -> {
                    exchangeModelRelevanceCmdExe.deleteById(exchangeMaterialRelevanceDO.getId());
                });
            }
            if(!flag && modelIdList ==null || modelIdList.size()==0){
                //之前是部分可用、后续改为全部可用
                createNew(exchangeId,adminResponse,null);
            }
        }
    }

    private void createNew(Integer exchangeId, AdminResponse adminResponse, Integer modelId) {
        ExchangeModelRelevanceDO relevanceDO = new ExchangeModelRelevanceDO();
        relevanceDO.setExchangeId(exchangeId);
        relevanceDO.setModelId(modelId);
        relevanceDO.setType(modelId==null? ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL: ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_SOME);
        relevanceDO.setCreateTime(new Date());
        relevanceDO.setCreateUserId(adminResponse.getId());
        relevanceDO.setCreateUserName(adminResponse.getUserName());
        exchangeModelRelevanceCmdExe.create(relevanceDO);
    }
}
