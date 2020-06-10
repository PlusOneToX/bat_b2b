package com.bat.flexible.manager.exchange;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.picture.PictureCategoryServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangePictureRelevanceCmdExe;
import com.bat.flexible.manager.exchange.executor.relavance.ExchangePictureRelevanceQryExe;
import com.bat.flexible.api.exchange.ExchangePictureRelevanceServiceI;
import com.bat.flexible.dao.exchange.dataobject.ExchangePictureRelevanceDO;
import com.bat.flexible.dao.picture.dataobject.PictureCategoryDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.manager.common.constant.exchange.ExchangeRelaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExchangePictureRelevanceServiceImpl implements ExchangePictureRelevanceServiceI {

    @Autowired
    private ExchangePictureRelevanceCmdExe exchangePictureRelevanceCmdExe;


    @Autowired
    private ExchangePictureRelevanceQryExe exchangePictureRelevanceQryExe;

    @Autowired
    private PictureServiceI pictureServiceI;

    @Autowired
    private PictureCategoryServiceI pictureCategoryServiceI;

    @Override
    @Transactional
    public void saveRelevance(Boolean isAdd, Integer exchangeId, List<Integer> pictureIdList, AdminResponse adminResponse) {
        dealWithByUpdate(isAdd,exchangeId,pictureIdList,adminResponse);
        if(pictureIdList !=null && pictureIdList.size()>0){
            pictureIdList.stream().forEach(pictureId -> {
                createNew(exchangeId, adminResponse, pictureId);
            });
        }else{
            if(isAdd){
                //新增全部使用
                createNew(exchangeId,adminResponse,null);
            }
        }
    }

    @Override
    public List<PictureRelaSimpleDTO> listDTOByExchangeId(Integer exchangeId) {
        List<ExchangePictureRelevanceDO>  relevanceDOList = exchangePictureRelevanceQryExe.listByExchangeId(exchangeId);
        List<PictureRelaSimpleDTO> list = new ArrayList<>();
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            relevanceDOList.stream().forEach(exchangePictureRelevanceDO -> {
               if(ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_SOME.equals(exchangePictureRelevanceDO.getType())){
                   //部分
                   PictureRelaSimpleDTO simpleDTO = new PictureRelaSimpleDTO();
                   simpleDTO.setId(exchangePictureRelevanceDO.getId());
                   PictureDO pictureDO = pictureServiceI.getById(exchangePictureRelevanceDO.getPictureId());
                   simpleDTO.setName(pictureDO.getName());
                   simpleDTO.setEnglishName(pictureDO.getEnglishName());
                   PictureCategoryDO pictureCategoryDO = pictureCategoryServiceI.getById(pictureDO.getCategoryId());
                   simpleDTO.setCategoryName(pictureCategoryDO.getName());
                   simpleDTO.setCategoryEnglishName(pictureCategoryDO.getEnglishName());
                   simpleDTO.setPictureId(pictureDO.getId());
                   list.add(simpleDTO);
               }
            });
        }
        return list;
    }

    @Override
    public List<ExchangePictureRelevanceDO> listByExchangeId(Integer exchangeId) {
        return exchangePictureRelevanceQryExe.listByExchangeId(exchangeId);
    }

    @Transactional
    @Override
    public void create(ExchangePictureRelevanceDO rela) {
        exchangePictureRelevanceCmdExe.create(rela);
    }

    @Override
    public void deleteById(Integer id) {
        exchangePictureRelevanceCmdExe.deleteById(id);
    }

    @Override
    public ExchangePictureRelevanceDO findOneByExchangeId(Integer exchangeId) {
        return exchangePictureRelevanceQryExe.findOneByExchangeId(exchangeId);
    }

    @Override
    public ExchangePictureRelevanceDO findByExchangeIdAndPictureId(Integer exchangeId, Integer pictureId) {
        return exchangePictureRelevanceQryExe.findByExchangeIdAndPictureId(exchangeId,pictureId);
    }

    private void dealWithByUpdate(Boolean isAdd, Integer exchangeId, List<Integer> pictureIdList, AdminResponse adminResponse) {
        if(isAdd){
            return;
        }
        List<ExchangePictureRelevanceDO> relevanceDOList = exchangePictureRelevanceQryExe.listByExchangeId(exchangeId);
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            //前后都是全部使用
            if((relevanceDOList.get(0).getType()- ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL==0) && (pictureIdList==null || pictureIdList.size()==0)){
                //前后不变
                return;
            }
            //定义标记、修改后部分可用
            Boolean flag = true;
            if(pictureIdList ==null || pictureIdList.size()==0){
                flag = false;
            }
            for(int x=0;x<relevanceDOList.size();x++){
                //前后都是部分可用
                if( relevanceDOList.get(x).getPictureId() !=null && pictureIdList !=null && pictureIdList.size()>0){
                    for(int y=0;y<pictureIdList.size();y++){
                        if(relevanceDOList.get(x).getPictureId() - pictureIdList.get(y)==0){
                            relevanceDOList.remove(x);
                            x--;
                            pictureIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
            }
            if(relevanceDOList !=null && relevanceDOList.size()>0){
                //删除
                relevanceDOList.stream().forEach(exchangeMaterialRelevanceDO -> {
                    exchangePictureRelevanceCmdExe.deleteById(exchangeMaterialRelevanceDO.getId());
                });
            }
            if(!flag &&  (pictureIdList ==null || pictureIdList.size()==0)){
                //之前是部分可用、后续改为全部可用
                createNew(exchangeId,adminResponse,null);
            }
        }
    }

    private void createNew(Integer exchangeId, AdminResponse adminResponse, Integer pictureId) {
        ExchangePictureRelevanceDO relevanceDO = new ExchangePictureRelevanceDO();
        relevanceDO.setExchangeId(exchangeId);
        relevanceDO.setPictureId(pictureId);
        relevanceDO.setType(pictureId==null? ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_ALL: ExchangeRelaConstant.EXCHANGE_SCOPE_COMMON_TYPE_SOME);
        relevanceDO.setCreateTime(new Date());
        relevanceDO.setCreateUserId(adminResponse.getId());
        relevanceDO.setCreateUserName(adminResponse.getUserName());
        exchangePictureRelevanceCmdExe.create(relevanceDO);
    }
}
